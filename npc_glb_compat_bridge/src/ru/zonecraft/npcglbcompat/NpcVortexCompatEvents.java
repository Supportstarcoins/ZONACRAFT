package ru.zonecraft.npcglbcompat;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

/**
 * Restores the physical pull of StalkerMod vortex-like anomalies for every
 * non-player living entity, including friendly, neutral and hostile CustomNPCs.
 *
 * CustomNPCs movement code and armor knockback attributes can overwrite the
 * anomaly impulse in the same tick. This handler detects the actual nearby
 * Stalker anomaly block/tile/entity and reapplies its pull after living AI.
 * StalkerMod itself is not modified.
 */
public final class NpcVortexCompatEvents {
    private static final String[] VORTEX_TOKENS = new String[] {
        "vortex", "voronka", "blackhole", "black_hole",
        "carousel", "whirl", "funnel", "gravitywell"
    };
    private static final Map<Object, PullState> ACTIVE =
            new WeakHashMap<Object, PullState>();

    @ForgeSubscribe(priority = EventPriority.LOWEST)
    public void onLivingUpdate(LivingUpdateEvent event) {
        Object living = event == null ? null : event.entityLiving;
        if (living == null || isPlayer(living)) {
            return;
        }

        Object world = CompatReflection.field(living,
                new String[] {"worldObj", "field_70170_p"});
        if (world == null || CompatReflection.booleanField(world,
                new String[] {"isRemote", "field_72995_K"}, false)) {
            return;
        }

        VortexSource source = findNearestVortex(living, world);
        PullState state = ACTIVE.get(living);
        if (source != null) {
            state = new PullState(source.x, source.y, source.z, 8);
            ACTIVE.put(living, state);
        } else if (state != null) {
            state.ticks--;
            if (state.ticks <= 0) {
                ACTIVE.remove(living);
                return;
            }
        } else {
            return;
        }

        applyPull(living, state);
    }

    private static VortexSource findNearestVortex(Object living, Object world) {
        double entityX = CompatReflection.doubleField(living,
                new String[] {"posX", "field_70165_t"}, 0.0D);
        double entityY = CompatReflection.doubleField(living,
                new String[] {"posY", "field_70163_u"}, 0.0D);
        double entityZ = CompatReflection.doubleField(living,
                new String[] {"posZ", "field_70161_v"}, 0.0D);

        int baseX = floor(entityX);
        int baseY = floor(entityY);
        int baseZ = floor(entityZ);
        double bestDistance = Double.MAX_VALUE;
        VortexSource best = null;

        int x;
        int y;
        int z;
        for (x = baseX - 5; x <= baseX + 5; x++) {
            for (y = baseY - 2; y <= baseY + 4; y++) {
                for (z = baseZ - 5; z <= baseZ + 5; z++) {
                    Object idValue = CompatReflection.invoke(world,
                            new String[] {"getBlockId", "func_72798_a"},
                            new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE},
                            new Object[] {Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
                    int blockId = idValue instanceof Number
                            ? ((Number) idValue).intValue() : 0;
                    if (blockId <= 0) {
                        continue;
                    }

                    Object block = getBlock(blockId);
                    Object tile = CompatReflection.invoke(world,
                            new String[] {"getBlockTileEntity", "func_72796_p"},
                            new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE},
                            new Object[] {Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
                    if (!isVortexObject(block) && !isVortexObject(tile)) {
                        continue;
                    }

                    double centerX = x + 0.5D;
                    double centerY = y + 0.5D;
                    double centerZ = z + 0.5D;
                    double distance = distanceSquared(entityX, entityY, entityZ,
                            centerX, centerY, centerZ);
                    if (distance < bestDistance && distance <= 42.25D) {
                        bestDistance = distance;
                        best = new VortexSource(centerX, centerY, centerZ);
                    }
                }
            }
        }

        Object loaded = CompatReflection.field(world,
                new String[] {"loadedEntityList", "field_72996_f"});
        if (loaded instanceof List) {
            @SuppressWarnings("rawtypes")
            List entities = (List) loaded;
            int index;
            for (index = 0; index < entities.size(); index++) {
                Object candidate = entities.get(index);
                if (candidate == living || !isVortexObject(candidate)) {
                    continue;
                }
                double xPos = CompatReflection.doubleField(candidate,
                        new String[] {"posX", "field_70165_t"}, 0.0D);
                double yPos = CompatReflection.doubleField(candidate,
                        new String[] {"posY", "field_70163_u"}, 0.0D);
                double zPos = CompatReflection.doubleField(candidate,
                        new String[] {"posZ", "field_70161_v"}, 0.0D);
                double distance = distanceSquared(entityX, entityY, entityZ,
                        xPos, yPos, zPos);
                if (distance < bestDistance && distance <= 42.25D) {
                    bestDistance = distance;
                    best = new VortexSource(xPos, yPos, zPos);
                }
            }
        }
        return best;
    }

    private static Object getBlock(int blockId) {
        try {
            Class<?> blockClass = Class.forName("net.minecraft.block.Block");
            Object blocks = CompatReflection.staticField(blockClass,
                    new String[] {"blocksList", "field_71973_m"});
            if (blocks != null && blocks.getClass().isArray()
                    && blockId >= 0 && blockId < Array.getLength(blocks)) {
                return Array.get(blocks, blockId);
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static boolean isVortexObject(Object value) {
        if (value == null) {
            return false;
        }
        StringBuilder identity = new StringBuilder();
        identity.append(value.getClass().getName()).append(' ');
        Object name = CompatReflection.invoke(value,
                new String[] {"getUnlocalizedName", "func_71917_a"},
                new Class[0], new Object[0]);
        if (name != null) {
            identity.append(String.valueOf(name));
        }
        String lower = identity.toString().toLowerCase(Locale.ROOT);
        int index;
        for (index = 0; index < VORTEX_TOKENS.length; index++) {
            if (lower.indexOf(VORTEX_TOKENS[index]) >= 0) {
                return true;
            }
        }
        return false;
    }

    private static void applyPull(Object living, PullState state) {
        double x = CompatReflection.doubleField(living,
                new String[] {"posX", "field_70165_t"}, 0.0D);
        double y = CompatReflection.doubleField(living,
                new String[] {"posY", "field_70163_u"}, 0.0D);
        double z = CompatReflection.doubleField(living,
                new String[] {"posZ", "field_70161_v"}, 0.0D);
        double dx = state.x - x;
        double dz = state.z - z;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        double safeDistance = Math.max(0.18D, horizontal);
        double closeness = Math.max(0.0D, 5.5D - horizontal);
        double attraction = Math.min(0.22D, 0.065D + closeness * 0.022D);

        double motionX = CompatReflection.doubleField(living,
                new String[] {"motionX", "field_70159_w"}, 0.0D);
        double motionY = CompatReflection.doubleField(living,
                new String[] {"motionY", "field_70181_x"}, 0.0D);
        double motionZ = CompatReflection.doubleField(living,
                new String[] {"motionZ", "field_70179_y"}, 0.0D);

        motionX += dx / safeDistance * attraction;
        motionZ += dz / safeDistance * attraction;
        double minimumLift = Math.min(0.68D, 0.30D + closeness * 0.065D);
        if (motionY < minimumLift) {
            motionY = minimumLift;
        }

        CompatReflection.setDouble(living,
                new String[] {"motionX", "field_70159_w"}, clamp(motionX, -1.15D, 1.15D));
        CompatReflection.setDouble(living,
                new String[] {"motionY", "field_70181_x"}, clamp(motionY, 0.22D, 0.78D));
        CompatReflection.setDouble(living,
                new String[] {"motionZ", "field_70179_y"}, clamp(motionZ, -1.15D, 1.15D));
        CompatReflection.setFloat(living,
                new String[] {"fallDistance", "field_70143_R"}, 0.0F);
        CompatReflection.setBoolean(living,
                new String[] {"onGround", "field_70122_E"}, false);
        CompatReflection.setBoolean(living,
                new String[] {"isAirBorne", "field_70160_al"}, true);
        CompatReflection.setBoolean(living,
                new String[] {"velocityChanged", "field_70133_I"}, true);

        Object navigator = CompatReflection.invoke(living,
                new String[] {"getNavigator", "func_70661_as"},
                new Class[0], new Object[0]);
        CompatReflection.invoke(navigator,
                new String[] {"clearPathEntity", "func_75515_a"},
                new Class[0], new Object[0]);
    }

    private static boolean isPlayer(Object living) {
        Class<?> type = living.getClass();
        while (type != null && type != Object.class) {
            if (type.getName().indexOf("EntityPlayer") >= 0) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    private static int floor(double value) {
        int integer = (int) value;
        return value < integer ? integer - 1 : integer;
    }

    private static double distanceSquared(double x1, double y1, double z1,
                                          double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }

    private static final class VortexSource {
        final double x;
        final double y;
        final double z;

        VortexSource(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static final class PullState {
        final double x;
        final double y;
        final double z;
        int ticks;

        PullState(double x, double y, double z, int ticks) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.ticks = ticks;
        }
    }
}
