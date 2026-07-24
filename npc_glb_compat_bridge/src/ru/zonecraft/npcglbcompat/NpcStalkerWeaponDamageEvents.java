package ru.zonecraft.npcglbcompat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/**
 * Converts broken StalkerMod bullets spawned by CustomNPCs into a server-side
 * projectile hit. The original StalkerMod and CustomNPCs jars remain untouched.
 *
 * The armor mod's legacy weapon hook can create the muzzle sound and an
 * EntityBullet, but that bullet was designed around player-only Stalker network
 * data. For NPC shooters it can reach the client without a valid player context,
 * producing sound with no damage and repeated packet-side null pointers.
 *
 * This bridge intercepts only StalkerMod EntityBullet instances that can be
 * positively associated with a CustomNPC holding ItemWeapon. Player bullets and
 * bullets from other mods are never changed. The intercepted NPC bullet becomes
 * one projectile DamageSource hit against the NPC's current attack target.
 */
public final class NpcStalkerWeaponDamageEvents {
    private static final String STALKER_BULLET = "ru.stalcraft.entity.EntityBullet";
    private static final float DEFAULT_DAMAGE = 10.0F;
    private static int handledShots;

    @ForgeSubscribe(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (event == null || event.entity == null || event.world == null) {
            return;
        }
        if (CompatReflection.booleanField(event.world,
                new String[] {"isRemote", "field_72995_K"}, false)) {
            return;
        }
        if (!isClassOrSubclass(event.entity, STALKER_BULLET)) {
            return;
        }

        ShotContext shot = findShotContext(event.entity, event.world);
        if (shot == null || shot.shooter == null || shot.target == null) {
            return;
        }

        // Cancel before StalkerMod tracks/synchronizes the player-oriented
        // bullet. Sound/animation has already been triggered by the firing hook.
        event.setCanceled(true);
        CompatReflection.invoke(event.entity,
                new String[] {"setDead", "func_70106_y"},
                new Class[0], new Object[0]);

        float damage = resolveDamage(event.entity, shot.weaponStack);
        boolean visible = canSee(shot.shooter, shot.target);
        boolean inRange = distanceSquared(shot.shooter, shot.target) <= 9216.0D;
        boolean alive = isAlive(shot.target);
        boolean applied = false;

        if (visible && inRange && alive) {
            applied = applyProjectileDamage(shot.shooter, shot.target, damage);
        }

        handledShots++;
        if (handledShots <= 25 || handledShots % 100 == 0 || !applied) {
            System.out.println("[Zonecraft NPC GLB Compat][Weapon] shot="
                    + handledShots + " shooter=" + identity(shot.shooter)
                    + " target=" + identity(shot.target)
                    + " damage=" + damage + " visible=" + visible
                    + " inRange=" + inRange + " applied=" + applied);
        }
    }

    private static ShotContext findShotContext(Object bullet, Object world) {
        Object owner = CompatReflection.field(bullet, new String[] {
                "shootingEntity", "shooter", "owner", "thrower",
                "entityOwner", "field_70250_c", "field_70235_a"
        });
        if (owner == null) {
            owner = CompatReflection.invoke(bullet,
                    new String[] {"getThrower", "getOwner", "func_85052_h"},
                    new Class[0], new Object[0]);
        }

        if (owner != null) {
            if (!isCustomNpc(owner)) {
                // A known non-NPC owner means this is a player/other mob bullet.
                return null;
            }
            Object weaponStack = CompatReflection.findHeldWeapon(owner);
            Object target = getAttackTarget(owner);
            if (weaponStack != null && target != null) {
                return new ShotContext(owner, target, weaponStack);
            }
            return null;
        }

        Object loaded = CompatReflection.field(world,
                new String[] {"loadedEntityList", "field_72996_f"});
        if (!(loaded instanceof List)) {
            return null;
        }

        @SuppressWarnings("rawtypes")
        List entities = (List) loaded;
        Object bestShooter = null;
        Object bestTarget = null;
        Object bestWeapon = null;
        double bestScore = Double.MAX_VALUE;

        double bulletX = coordinate(bullet, "posX", "field_70165_t");
        double bulletY = coordinate(bullet, "posY", "field_70163_u");
        double bulletZ = coordinate(bullet, "posZ", "field_70161_v");
        double motionX = coordinate(bullet, "motionX", "field_70159_w");
        double motionY = coordinate(bullet, "motionY", "field_70181_x");
        double motionZ = coordinate(bullet, "motionZ", "field_70179_y");
        double motionLength = Math.sqrt(motionX * motionX + motionY * motionY
                + motionZ * motionZ);

        int index;
        for (index = 0; index < entities.size(); index++) {
            Object candidate = entities.get(index);
            if (!isCustomNpc(candidate) || !isAlive(candidate)) {
                continue;
            }
            Object weaponStack = CompatReflection.findHeldWeapon(candidate);
            if (weaponStack == null) {
                continue;
            }
            Object target = getAttackTarget(candidate);
            if (target == null || !isAlive(target)) {
                continue;
            }

            double shooterDistance = distanceSquared(bulletX, bulletY, bulletZ,
                    coordinate(candidate, "posX", "field_70165_t"),
                    coordinate(candidate, "posY", "field_70163_u") + 1.2D,
                    coordinate(candidate, "posZ", "field_70161_v"));
            if (shooterDistance > 36.0D) {
                continue;
            }

            double score = shooterDistance;
            if (motionLength > 0.01D) {
                double tx = coordinate(target, "posX", "field_70165_t") - bulletX;
                double ty = coordinate(target, "posY", "field_70163_u") + 0.8D - bulletY;
                double tz = coordinate(target, "posZ", "field_70161_v") - bulletZ;
                double targetLength = Math.sqrt(tx * tx + ty * ty + tz * tz);
                if (targetLength > 0.01D) {
                    double dot = (motionX * tx + motionY * ty + motionZ * tz)
                            / (motionLength * targetLength);
                    if (dot < -0.15D) {
                        continue;
                    }
                    score += (1.0D - dot) * 10.0D;
                }
            }

            if (score < bestScore) {
                bestScore = score;
                bestShooter = candidate;
                bestTarget = target;
                bestWeapon = weaponStack;
            }
        }

        return bestShooter == null ? null
                : new ShotContext(bestShooter, bestTarget, bestWeapon);
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

    private static boolean applyProjectileDamage(Object shooter, Object target,
                                                  float damage) {
        Object source = createDamageSource(shooter);
        if (source == null) {
            return false;
        }

        // Stalker automatic weapons are not meant to be swallowed by vanilla's
        // long hurt-resistance window. Every actual NPC bullet may deal damage.
        CompatReflection.setField(target,
                new String[] {"hurtResistantTime", "field_70172_ad"},
                Integer.valueOf(0));
        CompatReflection.setField(target,
                new String[] {"hurtTime", "field_70737_aN"},
                Integer.valueOf(0));

        Object result = CompatReflection.invokeCompatible(target,
                new String[] {"attackEntityFrom", "func_70097_a"},
                new Object[] {source, Float.valueOf(damage)});
        return result instanceof Boolean && ((Boolean) result).booleanValue();
    }

    private static Object createDamageSource(Object shooter) {
        try {
            Class<?> damageSourceClass = Class.forName("net.minecraft.util.DamageSource");
            Method factory = CompatReflection.findCompatibleMethod(
                    damageSourceClass,
                    new String[] {"causeMobDamage", "func_76358_a"},
                    new Object[] {shooter}, true);
            Object source = factory == null ? null
                    : factory.invoke(null, new Object[] {shooter});

            if (source == null) {
                Class<?> entitySourceClass = Class.forName(
                        "net.minecraft.util.EntityDamageSource");
                Constructor<?>[] constructors = entitySourceClass.getDeclaredConstructors();
                int index;
                for (index = 0; index < constructors.length; index++) {
                    Constructor<?> constructor = constructors[index];
                    Class<?>[] parameters = constructor.getParameterTypes();
                    if (parameters.length == 2
                            && parameters[0] == String.class
                            && parameters[1].isAssignableFrom(shooter.getClass())) {
                        constructor.setAccessible(true);
                        source = constructor.newInstance(new Object[] {"bullet", shooter});
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
            System.out.println("[Zonecraft NPC GLB Compat][Weapon] DamageSource error: "
                    + error.getClass().getName() + ": " + error.getMessage());
            return null;
        }
    }

    private static float resolveDamage(Object bullet, Object weaponStack) {
        Float value = namedNumber(bullet, new String[] {
                "damage", "dmg", "bulletDamage", "weaponDamage",
                "shotDamage", "field_70255_ao"
        });
        if (validDamage(value)) {
            return value.floatValue();
        }

        Object item = CompatReflection.getItem(weaponStack);
        value = namedNumber(item, new String[] {
                "damage", "dmg", "bulletDamage", "weaponDamage", "shotDamage"
        });
        if (validDamage(value)) {
            return value.floatValue();
        }

        Object nested = CompatReflection.field(item,
                new String[] {"weaponInfo", "info", "config", "stats"});
        value = namedNumber(nested, new String[] {
                "damage", "dmg", "bulletDamage", "weaponDamage", "shotDamage"
        });
        if (validDamage(value)) {
            return value.floatValue();
        }

        value = methodNumber(item, new String[] {
                "getBulletDamage", "getWeaponDamage", "getShotDamage", "getDamage"
        });
        if (validDamage(value)) {
            return value.floatValue();
        }
        return DEFAULT_DAMAGE;
    }

    private static Float namedNumber(Object value, String[] names) {
        Number number = CompatReflection.numberField(value, names);
        return number == null ? null : Float.valueOf(number.floatValue());
    }

    private static Float methodNumber(Object value, String[] names) {
        if (value == null) {
            return null;
        }
        Object result = CompatReflection.invoke(value, names,
                new Class[0], new Object[0]);
        return result instanceof Number
                ? Float.valueOf(((Number) result).floatValue()) : null;
    }

    private static boolean validDamage(Float value) {
        return value != null && !Float.isNaN(value.floatValue())
                && value.floatValue() >= 0.1F && value.floatValue() <= 1000.0F;
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

    private static double coordinate(Object value, String deobf, String srg) {
        return CompatReflection.doubleField(value,
                new String[] {deobf, srg}, 0.0D);
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
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }

    private static String identity(Object value) {
        if (value == null) {
            return "null";
        }
        Object name = CompatReflection.invoke(value,
                new String[] {"getCommandSenderName", "func_70023_ak"},
                new Class[0], new Object[0]);
        return name == null ? value.getClass().getSimpleName()
                : String.valueOf(name);
    }

    private static final class ShotContext {
        final Object shooter;
        final Object target;
        final Object weaponStack;

        ShotContext(Object shooter, Object target, Object weaponStack) {
            this.shooter = shooter;
            this.target = target;
            this.weaponStack = weaponStack;
        }
    }
}
