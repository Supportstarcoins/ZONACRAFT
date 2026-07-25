package ru.zonecraft.npcglbcompat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

/**
 * Replaces the item-shaped projectile used by CustomNPCs with server-side
 * Stalker weapon fire. Fire cadence, magazine size, reload time, pellet count
 * and the selected FireMode are read directly from ItemWeapon by reflection.
 *
 * The Projectile slot still acts only as the CustomNPC ranged-AI trigger. Its
 * ItemStack is never launched into the world, so ammo boxes no longer fly from
 * the muzzle. StalkerMod and CustomNPCs jars remain untouched.
 */
public final class NpcStalkerWeaponControllerEvents {
    private static final String CUSTOM_NPC_PROJECTILE =
            "noppes.npcs.entity.EntityProjectile";
    private static final String STALKER_BULLET =
            "ru.stalcraft.entity.EntityBullet";

    private static final Map<Object, WeaponState> STATES =
            new WeakHashMap<Object, WeaponState>();

    private static int cancelledBoxes;
    private static int firedShots;
    private static int reloads;

    @ForgeSubscribe(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event == null || event.entity == null || event.world == null) {
            return;
        }
        if (CompatReflection.booleanField(event.world,
                new String[] {"isRemote", "field_72995_K"}, false)) {
            return;
        }

        if (isClassOrSubclass(event.entity, CUSTOM_NPC_PROJECTILE)) {
            Object shooter = findNpcShooter(event.entity);
            Object weaponStack = CompatReflection.findHeldWeapon(shooter);
            if (shooter == null || weaponStack == null) {
                return;
            }

            WeaponState state = stateFor(shooter);
            state.triggerTicks = Math.max(state.triggerTicks, 100);

            // Stop the ItemStack from the CustomNPC Projectile slot from being
            // rendered as a flying ammunition box.
            event.setCanceled(true);
            killEntity(event.entity);

            cancelledBoxes++;
            if (cancelledBoxes <= 20 || cancelledBoxes % 100 == 0) {
                System.out.println("[Zonecraft NPC Stalker Compat][Weapon] "
                        + "cancelled item projectile #" + cancelledBoxes
                        + " for " + identity(shooter));
            }
            return;
        }

        // The legacy stalkerarmor hook may still create a player-oriented
        // EntityBullet after the CustomNPC event. It is not allowed to enter the
        // Stalker packet path; this controller performs the authoritative shot.
        if (isClassOrSubclass(event.entity, STALKER_BULLET)) {
            Object shooter = findNpcShooter(event.entity);
            if (shooter == null) {
                shooter = findNearestActiveNpc(event.entity, event.world);
            }
            if (shooter != null && CompatReflection.findHeldWeapon(shooter) != null) {
                event.setCanceled(true);
                killEntity(event.entity);
            }
        }
    }

    @ForgeSubscribe(priority = EventPriority.LOWEST)
    public void onLivingUpdate(LivingUpdateEvent event) {
        Object shooter = event == null ? null : event.entityLiving;
        if (!isCustomNpc(shooter)) {
            return;
        }

        Object world = CompatReflection.field(shooter,
                new String[] {"worldObj", "field_70170_p"});
        if (world == null || CompatReflection.booleanField(world,
                new String[] {"isRemote", "field_72995_K"}, false)) {
            return;
        }

        Object weaponStack = CompatReflection.findHeldWeapon(shooter);
        if (weaponStack == null) {
            STATES.remove(shooter);
            return;
        }

        WeaponState state = stateFor(shooter);
        Object weaponItem = CompatReflection.getItem(weaponStack);
        if (state.weaponItem != weaponItem || state.profile == null) {
            state.resetForWeapon(weaponItem, WeaponProfile.read(weaponStack));
            logProfile(shooter, state.profile);
        }

        if (state.triggerTicks > 0) {
            state.triggerTicks--;
        }
        if (state.cooldownTicks > 0) {
            state.cooldownTicks--;
        }
        if (state.burstPauseTicks > 0) {
            state.burstPauseTicks--;
        }

        if (state.reloadTicks > 0) {
            state.reloadTicks--;
            if (state.reloadTicks == 0) {
                state.rounds = state.profile.magazineSize;
            }
            return;
        }

        Object target = getAttackTarget(shooter);
        if (target == null || !isAlive(target)) {
            state.burstRemaining = 0;
            return;
        }

        // Require at least one real ranged-AI trigger. Once the NPC starts an
        // attack, its own weapon profile controls the following cadence.
        if (state.triggerTicks <= 0) {
            return;
        }

        if (state.rounds <= 0) {
            startReload(world, shooter, state);
            return;
        }
        if (state.cooldownTicks > 0 || state.burstPauseTicks > 0) {
            return;
        }
        if (!canSee(shooter, target)
                || distanceSquared(shooter, target) > state.profile.rangeSquared) {
            return;
        }

        if (state.profile.mode == WeaponProfile.MODE_BURST
                && state.burstRemaining <= 0) {
            state.burstRemaining = state.profile.burstCount;
        }

        boolean applied = fire(world, shooter, target, weaponStack, state.profile);
        if (!applied) {
            state.cooldownTicks = Math.max(2, state.profile.cooldownTicks);
            return;
        }

        state.rounds--;
        state.cooldownTicks = state.profile.cooldownTicks;

        if (state.profile.mode == WeaponProfile.MODE_BURST) {
            state.burstRemaining--;
            if (state.burstRemaining <= 0) {
                state.burstPauseTicks = state.profile.burstPauseTicks;
            }
        }

        if (state.rounds <= 0) {
            startReload(world, shooter, state);
        }
    }

    private static void startReload(Object world, Object shooter, WeaponState state) {
        if (state.reloadTicks > 0) {
            return;
        }
        state.reloadTicks = state.profile.reloadTicks;
        state.burstRemaining = 0;
        state.burstPauseTicks = 0;
        playSound(world, shooter, state.profile.reloadSound, 1.0F, 1.0F);
        reloads++;
        if (reloads <= 20 || reloads % 100 == 0) {
            System.out.println("[Zonecraft NPC Stalker Compat][Weapon] reload #"
                    + reloads + " shooter=" + identity(shooter)
                    + " ticks=" + state.profile.reloadTicks
                    + " magazine=" + state.profile.magazineSize);
        }
    }

    private static boolean fire(Object world, Object shooter, Object target,
                                Object weaponStack, WeaponProfile profile) {
        float totalDamage = profile.damage * profile.projectilesPerShot;
        totalDamage = Math.max(0.1F, Math.min(1000.0F, totalDamage));

        Object source = createProjectileDamageSource(shooter);
        if (source == null) {
            return false;
        }

        // Each scheduled shot must be able to damage automatic-fire targets;
        // vanilla's long hurt-resistance window would otherwise swallow bursts.
        CompatReflection.setField(target,
                new String[] {"hurtResistantTime", "field_70172_ad"},
                Integer.valueOf(0));
        CompatReflection.setField(target,
                new String[] {"hurtTime", "field_70737_aN"},
                Integer.valueOf(0));

        Object result = CompatReflection.invokeCompatible(target,
                new String[] {"attackEntityFrom", "func_70097_a"},
                new Object[] {source, Float.valueOf(totalDamage)});
        boolean applied = result instanceof Boolean
                && ((Boolean) result).booleanValue();

        playSound(world, shooter, profile.shootSound, 1.0F,
                0.96F + (float) (Math.random() * 0.08D));

        firedShots++;
        if (firedShots <= 30 || firedShots % 100 == 0 || !applied) {
            System.out.println("[Zonecraft NPC Stalker Compat][Weapon] shot="
                    + firedShots + " shooter=" + identity(shooter)
                    + " target=" + identity(target)
                    + " mode=" + profile.modeName
                    + " damage=" + totalDamage
                    + " cooldown=" + profile.cooldownTicks
                    + " applied=" + applied);
        }
        return applied;
    }

    private static Object createProjectileDamageSource(Object shooter) {
        try {
            Class<?> damageSourceClass = Class.forName(
                    "net.minecraft.util.DamageSource");
            Method factory = CompatReflection.findCompatibleMethod(
                    damageSourceClass,
                    new String[] {"causeMobDamage", "func_76358_a"},
                    new Object[] {shooter}, true);
            Object source = factory == null ? null
                    : factory.invoke(null, new Object[] {shooter});

            if (source == null) {
                Class<?> entitySourceClass = Class.forName(
                        "net.minecraft.util.EntityDamageSource");
                Constructor<?>[] constructors =
                        entitySourceClass.getDeclaredConstructors();
                int index;
                for (index = 0; index < constructors.length; index++) {
                    Constructor<?> constructor = constructors[index];
                    Class<?>[] parameters = constructor.getParameterTypes();
                    if (parameters.length == 2
                            && parameters[0] == String.class
                            && parameters[1].isAssignableFrom(shooter.getClass())) {
                        constructor.setAccessible(true);
                        source = constructor.newInstance(
                                new Object[] {"bullet", shooter});
                        break;
                    }
                }
            }

            if (source != null) {
                Object projectile = CompatReflection.invoke(source,
                        new String[] {"setProjectile", "func_76349_b"},
                        new Class[0], new Object[0]);
                if (projectile != null) {
                    source = projectile;
                }
            }
            return source;
        } catch (Throwable error) {
            System.out.println("[Zonecraft NPC Stalker Compat][Weapon] "
                    + "DamageSource error: " + error);
            return null;
        }
    }

    private static void playSound(Object world, Object shooter, String sound,
                                  float volume, float pitch) {
        if (world == null || shooter == null || sound == null
                || sound.trim().length() == 0) {
            return;
        }
        CompatReflection.invokeCompatible(world,
                new String[] {"playSoundAtEntity", "func_72956_a"},
                new Object[] {shooter, sound, Float.valueOf(volume),
                        Float.valueOf(pitch)});
    }

    private static WeaponState stateFor(Object shooter) {
        WeaponState state = STATES.get(shooter);
        if (state == null) {
            state = new WeaponState();
            STATES.put(shooter, state);
        }
        return state;
    }

    private static Object findNearestActiveNpc(Object entity, Object world) {
        Object loaded = CompatReflection.field(world,
                new String[] {"loadedEntityList", "field_72996_f"});
        if (!(loaded instanceof java.util.List)) {
            return null;
        }
        @SuppressWarnings("rawtypes")
        java.util.List entities = (java.util.List) loaded;
        Object nearest = null;
        double best = 25.0D;
        int index;
        for (index = 0; index < entities.size(); index++) {
            Object candidate = entities.get(index);
            WeaponState state = STATES.get(candidate);
            if (!isCustomNpc(candidate) || state == null
                    || state.triggerTicks <= 0
                    || CompatReflection.findHeldWeapon(candidate) == null) {
                continue;
            }
            double distance = distanceSquared(entity, candidate);
            if (distance < best) {
                best = distance;
                nearest = candidate;
            }
        }
        return nearest;
    }

    private static Object findNpcShooter(Object projectile) {
        if (projectile == null) {
            return null;
        }
        Object shooter = CompatReflection.field(projectile, new String[] {
            "thrower", "shooter", "owner", "shootingEntity", "npc",
            "entityOwner", "field_70192_c", "field_70250_c", "field_70235_a"
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

    private static Object getAttackTarget(Object shooter) {
        Object target = CompatReflection.invoke(shooter,
                new String[] {"getAttackTarget", "func_70638_az"},
                new Class[0], new Object[0]);
        if (target == null) {
            target = CompatReflection.field(shooter,
                    new String[] {"attackTarget", "field_70696_bz"});
        }
        return target;
    }

    private static boolean canSee(Object shooter, Object target) {
        Object senses = CompatReflection.invoke(shooter,
                new String[] {"getEntitySenses", "func_70635_at"},
                new Class[0], new Object[0]);
        Object visible = CompatReflection.invokeCompatible(senses,
                new String[] {"canSee", "func_75522_a"},
                new Object[] {target});
        if (visible instanceof Boolean) {
            return ((Boolean) visible).booleanValue();
        }
        visible = CompatReflection.invokeCompatible(shooter,
                new String[] {"canEntityBeSeen", "func_70685_l"},
                new Object[] {target});
        return !(visible instanceof Boolean)
                || ((Boolean) visible).booleanValue();
    }

    private static boolean isAlive(Object value) {
        if (value == null || CompatReflection.booleanField(value,
                new String[] {"isDead", "field_70128_L"}, false)) {
            return false;
        }
        Object health = CompatReflection.invoke(value,
                new String[] {"getHealth", "func_110143_aJ"},
                new Class[0], new Object[0]);
        return !(health instanceof Number)
                || ((Number) health).floatValue() > 0.0F;
    }

    private static boolean isCustomNpc(Object value) {
        if (value == null) {
            return false;
        }
        Class<?> type = value.getClass();
        while (type != null && type != Object.class) {
            String name = type.getName().toLowerCase(Locale.ROOT);
            if (name.equals("noppes.npcs.entity.entitynpcinterface")
                    || name.equals("noppes.npcs.entitynpcinterface")
                    || (name.indexOf("noppes.npcs") >= 0
                    && name.indexOf("entitynpc") >= 0)) {
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

    private static void killEntity(Object entity) {
        CompatReflection.invoke(entity,
                new String[] {"setDead", "func_70106_y"},
                new Class[0], new Object[0]);
        CompatReflection.setBoolean(entity,
                new String[] {"isDead", "field_70128_L"}, true);
    }

    private static double distanceSquared(Object first, Object second) {
        return distanceSquared(
                coordinate(first, "posX", "field_70165_t"),
                coordinate(first, "posY", "field_70163_u"),
                coordinate(first, "posZ", "field_70161_v"),
                coordinate(second, "posX", "field_70165_t"),
                coordinate(second, "posY", "field_70163_u"),
                coordinate(second, "posZ", "field_70161_v"));
    }

    private static double distanceSquared(double x1, double y1, double z1,
                                          double x2, double y2, double z2) {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;
        return x * x + y * y + z * z;
    }

    private static double coordinate(Object value, String name, String obfName) {
        return CompatReflection.doubleField(value,
                new String[] {name, obfName}, 0.0D);
    }

    private static String identity(Object value) {
        if (value == null) {
            return "null";
        }
        Object name = CompatReflection.invoke(value,
                new String[] {"getCommandSenderName", "func_70005_c_"},
                new Class[0], new Object[0]);
        return name == null ? value.getClass().getName() : String.valueOf(name);
    }

    private static void logProfile(Object shooter, WeaponProfile profile) {
        System.out.println("[Zonecraft NPC Stalker Compat][Weapon] profile shooter="
                + identity(shooter) + " mode=" + profile.modeName
                + " cooldown=" + profile.cooldownTicks
                + " reload=" + profile.reloadTicks
                + " magazine=" + profile.magazineSize
                + " burst=" + profile.burstCount
                + " pellets=" + profile.projectilesPerShot
                + " damage=" + profile.damage);
    }

    private static final class WeaponState {
        Object weaponItem;
        WeaponProfile profile;
        int rounds;
        int triggerTicks;
        int cooldownTicks;
        int reloadTicks;
        int burstRemaining;
        int burstPauseTicks;

        void resetForWeapon(Object item, WeaponProfile newProfile) {
            weaponItem = item;
            profile = newProfile;
            rounds = newProfile.magazineSize;
            cooldownTicks = 0;
            reloadTicks = 0;
            burstRemaining = 0;
            burstPauseTicks = 0;
        }
    }

    private static final class WeaponProfile {
        static final int MODE_SINGLE = 0;
        static final int MODE_BURST = 1;
        static final int MODE_AUTO = 2;

        final float damage;
        final int cooldownTicks;
        final int reloadTicks;
        final int magazineSize;
        final int projectilesPerShot;
        final int mode;
        final String modeName;
        final int burstCount;
        final int burstPauseTicks;
        final double rangeSquared;
        final String shootSound;
        final String reloadSound;

        WeaponProfile(float damage, int cooldownTicks, int reloadTicks,
                      int magazineSize, int projectilesPerShot, int mode,
                      String modeName, int burstCount, int burstPauseTicks,
                      double rangeSquared, String shootSound,
                      String reloadSound) {
            this.damage = damage;
            this.cooldownTicks = cooldownTicks;
            this.reloadTicks = reloadTicks;
            this.magazineSize = magazineSize;
            this.projectilesPerShot = projectilesPerShot;
            this.mode = mode;
            this.modeName = modeName;
            this.burstCount = burstCount;
            this.burstPauseTicks = burstPauseTicks;
            this.rangeSquared = rangeSquared;
            this.shootSound = shootSound;
            this.reloadSound = reloadSound;
        }

        static WeaponProfile read(Object stack) {
            Object item = CompatReflection.getItem(stack);
            Object nested = CompatReflection.field(item,
                    new String[] {"weaponInfo", "info", "config", "stats"});

            float damage = number(item, nested,
                    new String[] {"damage", "dmg", "bulletDamage", "weaponDamage"},
                    10.0F);
            int cooldown = integer(item, nested,
                    new String[] {"cooldown", "shotCooldown", "shootCooldown",
                            "fireCooldown", "cooldownTicks"}, 4, 1, 200);
            int reload = integer(item, nested,
                    new String[] {"reload_time", "reloadTime", "reloadTicks",
                            "reloadCooldown"}, 80, 1, 1200);
            int magazine = integer(item, nested,
                    new String[] {"cage_size", "cageSize", "clipSize",
                            "magazineSize", "ammoCapacity"}, 30, 1, 1000);
            int projectiles = integer(item, nested,
                    new String[] {"bullets_count", "bulletsCount", "pelletCount",
                            "projectilesPerShot", "shotCount"}, 1, 1, 64);
            float range = number(item, nested,
                    new String[] {"distance", "range", "maxDistance"},
                    96.0F);
            if (range < 8.0F) {
                range = 96.0F;
            }

            String shootSound = string(item, nested,
                    new String[] {"shoot_sound", "shootSound", "shotSound"},
                    "");
            String reloadSound = string(item, nested,
                    new String[] {"reload_sound", "reloadSound"}, "");

            Object fireMode = CompatReflection.invokeCompatible(item,
                    new String[] {"getFireMod", "getFireMode"},
                    new Object[] {stack});
            String modeText = fireModeText(fireMode);
            boolean automatic = bool(item, nested,
                    new String[] {"autoShooting", "automatic", "isAutomatic"},
                    false);

            int mode = MODE_SINGLE;
            String modeName = "single";
            if (containsMode(modeText, new String[] {
                    "BURST", "QUEUE", "SERIES", "ОЧЕРЕД"})) {
                mode = MODE_BURST;
                modeName = "burst";
            } else if (containsMode(modeText, new String[] {
                    "AUTO", "FULL", "AUTOMATIC", "АВТО"}) || automatic) {
                mode = MODE_AUTO;
                modeName = "auto";
            }

            int burstCount = numberFromMode(fireMode,
                    new String[] {"burstCount", "burst", "queueSize",
                            "shots", "count"}, 3);
            burstCount = Math.max(2, Math.min(10, burstCount));
            int burstPause = Math.max(8, cooldown * 3);

            return new WeaponProfile(
                    Math.max(0.1F, Math.min(500.0F, damage)),
                    cooldown,
                    reload,
                    magazine,
                    projectiles,
                    mode,
                    modeName,
                    burstCount,
                    burstPause,
                    (double) range * (double) range,
                    shootSound,
                    reloadSound);
        }

        private static float number(Object primary, Object secondary,
                                    String[] names, float fallback) {
            Number value = CompatReflection.numberField(primary, names);
            if (value == null) {
                value = CompatReflection.numberField(secondary, names);
            }
            return value == null ? fallback : value.floatValue();
        }

        private static int integer(Object primary, Object secondary,
                                   String[] names, int fallback,
                                   int minimum, int maximum) {
            int value = Math.round(number(primary, secondary, names, fallback));
            return Math.max(minimum, Math.min(maximum, value));
        }

        private static boolean bool(Object primary, Object secondary,
                                    String[] names, boolean fallback) {
            Object value = CompatReflection.field(primary, names);
            if (!(value instanceof Boolean)) {
                value = CompatReflection.field(secondary, names);
            }
            return value instanceof Boolean
                    ? ((Boolean) value).booleanValue() : fallback;
        }

        private static String string(Object primary, Object secondary,
                                     String[] names, String fallback) {
            Object value = CompatReflection.field(primary, names);
            if (!(value instanceof String)) {
                value = CompatReflection.field(secondary, names);
            }
            return value instanceof String ? (String) value : fallback;
        }

        private static String fireModeText(Object fireMode) {
            if (fireMode == null) {
                return "";
            }
            StringBuilder text = new StringBuilder();
            text.append(fireMode.getClass().getName()).append(' ');
            text.append(String.valueOf(fireMode));
            Object name = CompatReflection.invoke(fireMode,
                    new String[] {"name", "getName"},
                    new Class[0], new Object[0]);
            if (name != null) {
                text.append(' ').append(String.valueOf(name));
            }
            return text.toString().toUpperCase(Locale.ROOT);
        }

        private static boolean containsMode(String text, String[] tokens) {
            int index;
            for (index = 0; index < tokens.length; index++) {
                if (text.indexOf(tokens[index]) >= 0) {
                    return true;
                }
            }
            return false;
        }

        private static int numberFromMode(Object fireMode, String[] names,
                                          int fallback) {
            Number number = CompatReflection.numberField(fireMode, names);
            if (number != null) {
                return number.intValue();
            }
            String text = fireModeText(fireMode);
            int index;
            for (index = 0; index < text.length(); index++) {
                char current = text.charAt(index);
                if (current >= '2' && current <= '9') {
                    return current - '0';
                }
            }
            return fallback;
        }
    }
}
