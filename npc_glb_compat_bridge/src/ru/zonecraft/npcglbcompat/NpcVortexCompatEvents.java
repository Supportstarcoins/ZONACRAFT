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
 * Reapplies StalkerMod vortex physics after CustomNPC movement AI.
 *
 * The bridge deliberately ignores faction and GLB armor state. Friendly,
 * neutral and hostile CustomNPCs receive the same final pull. Players and
 * non-CustomNPC mobs are left to the original StalkerMod implementation.
 */
public final class NpcVortexCompatEvents {
    private static final String[] VORTEX_TOKENS = new String[] {
        "vortex", "voronka", "voronca", "blackhole", "black_hole",
        "carousel", "karusel", "whirl", "funnel", "gravitywell"
    };
    private static final Map<Object, PullState> ACTIVE =
            new WeakHashMap<Object, PullState>();
    private static int appliedPulls;

    @ForgeSubscribe(priority = EventPriority.LOWEST)
    public void onLivingUpdate(LivingUpdateEvent event) {
        Object living = event == null ? null : event.entityLiving;
        if (living == null || !isCustomNpc(living)) {
            return;
        }
        if (CompatReflection.booleanField(living,
                new String[] {"isDead", "field_70128_L"}, false)) {
            ACTIVE.remove(living);
            return;
        }

        Object world = CompatReflection.field(living,
                new String[] {"worldObj", "field_70170_p"});
        if (world == null || CompatReflection.booleanField(world,
                new String[] {"isRemote", "field_72995_K"}, false)) {
            return;
        }

        PullState state = ACTIVE.get(living);
        int ticks = (int) CompatReflection.doubleField(living,
                new String[] {"ticksExisted", "field_70173_aa"}, 0.0D);
        int entityId = entityId(living);
        boolean scanNow = state == null || ((ticks + entityId) % 3 == 0);

        if (scanNow) {
            VortexSource source = findNearestVortex(living, world);
            if (source != null) {
                state = new PullState(source.x, source.y, source.z, 10);
                ACTIVE.put(living, state);
            } else if (state != null) {
                state.ticks -= 3;
                if (state.ticks <= 0) {
                    ACTIVE.remove(living);
                    return;
                }
            } else {
                return;
            }
        }

        if (state != null) {
            applyEqualizedPull(living, state);
        }
    }

    private static VortexSource findNearestVortex(Object living, Object world) {
        double entityX = coordinate(living, "posX", "field_70165_t");
        double entityY = coordinate(living, "posY", "field_70163_u");
        double entityZ = coordinate(living, "posZ", "field_70161_v");

        int baseX = floor(entityX);
        int baseY = floor(entityY);
        int baseZ = floor(entityZ);
        double bestDistance = Double.MAX_VALUE;
        VortexSource best = null;

        int x;
        int y;
        int z;
        for (x = baseX - 6; x <= baseX + 6; x++) {
            for (y = baseY - 3; y <= baseY + 5; y++) {
                for (z = baseZ - 6; z <= baseZ + 6; z++) {
                    Object idValue = CompatReflection.invoke(world,
                            new String[] {"getBlockId", "func_72798_a"},
                            new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE},
                            new Object[] {Integer.valueOf(x), Integer.valueOf(y),
                                    Integer.valueOf(z)});
                    int blockId = idValue instanceof Number
                            ? ((Number) idValue).intValue() : 0;
                    if (blockId <= 0) {
                        continue;
                    }

                    Object block = getBlock(blockId);
                    Object tile = CompatReflection.invoke(world,
                            new String[] {"getBlockTileEntity", "func_72796_p"},
                            new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE},
                            new Object[] {Integer.valueOf(x), Integer.valueOf(y),
                                    Integer.valueOf(z)});
                    if (!isVortexObject(block) && !isVortexObject(tile)) {
                        continue;
                    }

                    double centerX = x + 0.5D;
                    double centerY = y + 0.5D;
                    double centerZ = z + 0.5D;
                    double distance = distanceSquared(entityX, entityY, entityZ,
                            centerX, centerY, centerZ);
                    if (distance < bestDistance && distance <= 56.25D) {
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
                double xPos = coordinate(candidate, "posX", "field_70165_t");
                double yPos = coordinate(candidate, "posY", "field_70163_u");
                double zPos = coordinate(candidate, "posZ", "field_70161_v");
                double distance = distanceSquared(entityX, entityY, entityZ,
                        xPos, yPos, zPos);
                if (distance < bestDistance && distance <= 56.25D) {
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

    private static void applyEqualizedPull(Object living, PullState state) {
        double x = coordinate(living, "posX", "field_70165_t");
        double y = coordinate(living, "posY", "field_70163_u");
        double z = coordinate(living, "posZ", "field_70161_v");
        double dx = state.x - x;
        double dz = state.z - z;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        double safeDistance = Math.max(0.18D, horizontal);
        double closeness = Math.max(0.0D, 6.3D - horizontal);
        double attraction = Math.min(0.28D, 0.075D + closeness * 0.026D);

        // Use deterministic final horizontal motion instead of adding to the
        // original anomaly result. That removes faction-dependent double pull.
        double motionX = dx / safeDistance * attraction;
        double motionZ = dz / safeDistance * attraction;
        double currentY = coordinate(living, "motionY", "field_70181_x");
        double minimumLift = Math.min(0.82D, 0.32D + closeness * 0.075D);
        double motionY = Math.max(currentY, minimumLift);

        CompatReflection.setDouble(living,
                new String[] {"motionX", "field_70159_w"},
                clamp(motionX, -1.2D, 1.2D));
        CompatReflection.setDouble(living,
                new String[] {"motionY", "field_70181_x"},
                clamp(motionY, 0.24D, 0.86D));
        CompatReflection.setDouble(living,
                new String[] {"motionZ", "field_70179_y"},
                clamp(motionZ, -1.2D, 1.2D));
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

        appliedPulls++;
        if (appliedPulls <= 10 || appliedPulls % 500 == 0) {
            System.out.println("[Zonecraft NPC GLB Compat][Vortex] pull="
                    + appliedPulls + " npc=" + identity(living)
                    + " distance=" + horizontal);
        }
    }

    private static boolean isCustomNpc(Object living) {
        Class<?> type = living == null ? null : living.getClass();
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

    private static String identity(Object value) {
        Object name = CompatReflection.invoke(value,
                new String[] {"getCommandSenderName", "func_70023_ak"},
                new Class[0], new Object[0]);
        return name == null ? value.getClass().getSimpleName()
                : String.valueOf(name);
    }

    private static int entityId(Object value) {
        Object id = CompatReflection.invoke(value,
                new String[] {"getEntityId", "func_145782_y"},
                new Class[0], new Object[0]);
        if (id instanceof Number) {
            return ((Number) id).intValue();
        }
        Number field = CompatReflection.numberField(value,
                new String[] {"entityId", "field_70157_k"});
        return field == null ? System.identityHashCode(value) : field.intValue();
    }

    private static double coordinate(Object value, String deobf, String srg) {
        return CompatReflection.doubleField(value,
                new String[] {deobf, srg}, 0.0D);
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
