package ru.zonecraft.npcglbcompat;

import java.lang.reflect.Field;

import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/**
 * Damage fallback for CustomNPCs using a StalkerMod ItemWeapon.
 *
 * stalcraftglb 0.5.19 replaces the normal CustomNPC projectile with a reflected
 * Stalker EntityBullet. In the affected 1.6.4 pack the replacement bullet plays
 * the shot sound but does not apply server damage. The original CustomNPC
 * projectile was already cancelled at that point.
 *
 * This LOWEST-priority handler runs after the replacement bridge. When the
 * cancelled entity is the original CustomNPC projectile and its owner holds a
 * real StalkerMod weapon, it revives and uncancels that projectile. CustomNPCs
 * then performs its normal server collision and damage logic. StalkerMod and
 * CustomNPCs jars are not modified.
 */
public final class NpcStalkerProjectileFallbackEvents {
    private static final String CUSTOM_NPC_PROJECTILE =
            "noppes.npcs.entity.EntityProjectile";
    private static int restoredShots;

    @ForgeSubscribe(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event == null || event.entity == null || event.world == null) {
            return;
        }
        if (CompatReflection.booleanField(event.world,
                new String[] {"isRemote", "field_72995_K"}, false)) {
            return;
        }
        if (!isClassOrSubclass(event.entity, CUSTOM_NPC_PROJECTILE)) {
            return;
        }

        Object shooter = findNpcShooter(event.entity);
        if (shooter == null || CompatReflection.findHeldWeapon(shooter) == null) {
            return;
        }

        if (!event.isCanceled()) {
            return;
        }

        // Some replacement handlers also call setDead() before cancelling.
        CompatReflection.setBoolean(event.entity,
                new String[] {"isDead", "field_70128_L"}, false);
        event.setCanceled(false);

        restoredShots++;
        if (restoredShots <= 20 || restoredShots % 100 == 0) {
            System.out.println(
                    "[Zonecraft NPC Stalker Compat] Restored CustomNPC projectile #"
                    + restoredShots + " for " + shooter.getClass().getName());
        }
    }

    private static Object findNpcShooter(Object projectile) {
        Object shooter = CompatReflection.field(projectile, new String[] {
            "thrower", "shooter", "owner", "shootingEntity", "npc",
            "field_70192_c", "field_70250_c"
        });
        if (isCustomNpc(shooter)) {
            return shooter;
        }

        Class<?> type = projectile.getClass();
        while (type != null && type != Object.class) {
            Field[] fields = type.getDeclaredFields();
            int index;
            for (index = 0; index < fields.length; index++) {
                try {
                    Field field = fields[index];
                    if (field.getType().isPrimitive()) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object candidate = field.get(projectile);
                    if (isCustomNpc(candidate)) {
                        return candidate;
                    }
                } catch (Throwable ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private static boolean isCustomNpc(Object value) {
        if (value == null) {
            return false;
        }
        Class<?> type = value.getClass();
        while (type != null && type != Object.class) {
            String name = type.getName();
            if (name.startsWith("noppes.npcs.entity.EntityNPC")
                    || "noppes.npcs.entity.EntityNPCInterface".equals(name)) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    private static boolean isClassOrSubclass(Object value, String className) {
        if (value == null) {
            return false;
        }
        Class<?> type = value.getClass();
        while (type != null && type != Object.class) {
            if (className.equals(type.getName())) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }
}
