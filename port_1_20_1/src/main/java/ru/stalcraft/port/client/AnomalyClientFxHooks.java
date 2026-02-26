package ru.stalcraft.port.client;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import ru.stalcraft.port.StalkerPortMod;
import ru.stalcraft.port.anomaly.AnomalyType;
import ru.stalcraft.port.registry.ModSounds;

public final class AnomalyClientFxHooks {
    private static final Map<String, FxRuntimeState> FX_STATE = new HashMap<>();

    private AnomalyClientFxHooks() {
    }

    public static void spawnAmbient(Level level, BlockPos pos, AnomalyType type, int animationTick, long fxSeed, boolean active) {
        RandomSource random = randomForTick(fxSeed, animationTick);
        FxRuntimeState state = state(level.dimension(), pos);

        if (type == AnomalyType.TRAMPOLINE) {
            spawnTrampolineLeaf(level, pos, random);
        }
        if (type == AnomalyType.BLACK_HOLE) {
            if (animationTick % 40 == 0 && !active) {
                spawnFunnelEye(level, pos, random);
            }
            spawnFunnelLeaf(level, pos, random);
            spawnFunnelLeaf(level, pos, random);
            if (active && state.activeProgressTick < 15) {
                spawnFunnelActivationCloud(level, pos, random, state);
            }
        }
        if (type == AnomalyType.ELECTRA && !active && animationTick % 4 == 0) {
            double x = signedOffset(random);
            double z = signedOffset(random);
            add(level, ParticleTypes.ELECTRIC_SPARK, pos, 0.5D + x, 0.7D, 0.5D + z, 0.0D, 0.02D, 0.0D);
            add(level, ParticleTypes.END_ROD, pos, 0.5D + x, 0.7D, 0.5D + z, 0.0D, 0.01D, 0.0D);
            logFx("ambient", type, 2, level, pos);
        }
        if (type == AnomalyType.LIGHTER) {
            if (active) {
                for (int i = 0; i < 12; i++) {
                    add(level, ParticleTypes.FLAME, pos, 0.5D + (random.nextDouble() - 0.5D) * 0.4D,
                        0.25D + random.nextDouble() * 0.2D, 0.5D + (random.nextDouble() - 0.5D) * 0.4D,
                        0.0D, 0.02D + random.nextDouble() * 0.03D, 0.0D);
                }
                logFx("ambient", type, 12, level, pos);
            } else if (animationTick % 3 == 0) {
                add(level, ParticleTypes.SMALL_FLAME, pos, 0.5D + (random.nextDouble() - 0.5D) * 0.25D,
                    0.28D, 0.5D + (random.nextDouble() - 0.5D) * 0.25D,
                    0.0D, 0.01D, 0.0D);
                logFx("ambient", type, 1, level, pos);
            }
        }

        if (shouldPlayAmbient(type, random)) {
            level.playLocalSound(pos, ambientSound(type), SoundSource.BLOCKS, ambientVolume(type), ambientPitch(random), false);
            StalkerPortMod.LOGGER.info("PLAY sound={} tick={}", ambientSound(type).getLocation(), level.getGameTime());
        }

        if (!active) {
            state.activeProgressTick = 0;
        }
    }

    public static void spawnActivate(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        RandomSource random = randomForTick(fxSeed, (int) level.getGameTime());
        FxRuntimeState state = state(level.dimension(), pos);
        state.activeProgressTick = 0;

        int particles = switch (type) {
            case ELECTRA -> 8;
            case TRAMPOLINE -> 250;
            case BLACK_HOLE -> 50;
            case LIGHTER -> 0;
            case CAROUSEL -> 18;
        };

        for (int i = 0; i < particles; i++) {
            switch (type) {
                case ELECTRA -> add(level, ParticleTypes.ELECTRIC_SPARK, pos, 0.5D + (random.nextDouble() - 0.5D),
                    0.2D + random.nextDouble() * 0.9D, 0.5D + (random.nextDouble() - 0.5D), 0.0D, 0.03D, 0.0D);
                case TRAMPOLINE -> add(level, ParticleTypes.POOF, pos, 0.5D + (random.nextDouble() - 0.5D) * 1.4D,
                    0.05D + random.nextDouble() * 0.6D, 0.5D + (random.nextDouble() - 0.5D) * 1.4D,
                    (random.nextDouble() - 0.5D) * 0.03D, 0.06D + random.nextDouble() * 0.05D, (random.nextDouble() - 0.5D) * 0.03D);
                case BLACK_HOLE -> add(level, ParticleTypes.SMOKE, pos, 0.5D + (random.nextDouble() - 0.5D) * 0.7D,
                    0.1D + random.nextDouble() * 0.8D, 0.5D + (random.nextDouble() - 0.5D) * 0.7D, 0.0D, 0.03D, 0.0D);
                case LIGHTER, CAROUSEL -> {
                }
            }
        }

        if (Minecraft.getInstance().player != null) {
            level.playLocalSound(pos, activateSound(type), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            StalkerPortMod.LOGGER.info("PLAY sound={} tick={}", activateSound(type).getLocation(), level.getGameTime());
        }
        logFx("activate", type, particles, level, pos);
    }

    public static void spawnHit(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        RandomSource random = randomForTick(fxSeed, (int) level.getGameTime() + 31);
        int count = type == AnomalyType.ELECTRA ? 8 : 8;
        for (int i = 0; i < count; i++) {
            add(level, switch (type) {
                case ELECTRA -> ParticleTypes.CRIT;
                case BLACK_HOLE -> ParticleTypes.SMOKE;
                case TRAMPOLINE -> ParticleTypes.SPLASH;
                case LIGHTER -> ParticleTypes.SMALL_FLAME;
                case CAROUSEL -> ParticleTypes.ENCHANT;
            }, pos,
                0.5D + (random.nextDouble() - 0.5D) * 0.6D,
                0.15D + random.nextDouble() * 0.4D,
                0.5D + (random.nextDouble() - 0.5D) * 0.6D,
                0.0D, 0.02D, 0.0D);
        }

        level.playLocalSound(pos, hitSound(type), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        StalkerPortMod.LOGGER.info("PLAY sound={} tick={}", hitSound(type).getLocation(), level.getGameTime());
        logFx("hit", type, count, level, pos);
    }

    public static void spawnSplash(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        RandomSource random = randomForTick(fxSeed, (int) level.getGameTime() + 79);
        for (int i = 0; i < 12; i++) {
            add(level, type == AnomalyType.LIGHTER ? ParticleTypes.FLAME : ParticleTypes.BUBBLE, pos,
                0.5D + (random.nextDouble() - 0.5D),
                0.1D + random.nextDouble() * 0.9D,
                0.5D + (random.nextDouble() - 0.5D),
                0.0D, 0.05D, 0.0D);
        }
        logFx("splash", type, 12, level, pos);
    }

    private static boolean shouldPlayAmbient(AnomalyType type, RandomSource random) {
        return random.nextFloat() < switch (type) {
            case ELECTRA -> 0.07F;
            case BLACK_HOLE -> 0.01F;
            case TRAMPOLINE -> 0.05F;
            case LIGHTER -> 0.0F;
            case CAROUSEL -> 0.03F;
        };
    }

    private static float ambientVolume(AnomalyType type) {
        return 0.5F;
    }

    private static float ambientPitch(RandomSource random) {
        return 0.9F + random.nextFloat() * 0.15F;
    }

    private static void spawnTrampolineLeaf(Level level, BlockPos pos, RandomSource random) {
        add(level, ParticleTypes.CHERRY_LEAVES, pos, 0.5D + (random.nextDouble() - 0.5D) * 1.7D,
            0.35D + random.nextDouble() * 0.2D, 0.5D + (random.nextDouble() - 0.5D) * 1.7D,
            0.0D, 0.01D, 0.0D);
    }

    private static void spawnFunnelLeaf(Level level, BlockPos pos, RandomSource random) {
        add(level, ParticleTypes.CHERRY_LEAVES, pos, 0.5D + (random.nextDouble() - 0.5D) * 2.3D,
            0.2D + random.nextDouble() * 0.3D, 0.5D + (random.nextDouble() - 0.5D) * 2.3D,
            0.0D, 0.02D, 0.0D);
    }

    private static void spawnFunnelEye(Level level, BlockPos pos, RandomSource random) {
        add(level, ParticleTypes.PORTAL, pos, 0.5D + (random.nextDouble() - 0.5D) * 0.25D,
            0.85D, 0.5D + (random.nextDouble() - 0.5D) * 0.25D,
            0.0D, 0.01D, 0.0D);
        logFx("ambient", AnomalyType.BLACK_HOLE, 1, level, pos);
    }

    private static void spawnFunnelActivationCloud(Level level, BlockPos pos, RandomSource random, FxRuntimeState state) {
        int dustToSpawn = state.notSpawnedDust / 15 + state.notSpawnedDust % 15;
        for (int i = 0; i < dustToSpawn; i++) {
            add(level, ParticleTypes.ASH, pos, 0.5D + (random.nextDouble() - 0.5D) * 2.0D,
                0.2D + random.nextDouble() * 1.0D, 0.5D + (random.nextDouble() - 0.5D) * 2.0D,
                0.0D, 0.03D, 0.0D);
        }

        int leavesToSpawn = state.notSpawnedLeaf / 15 + state.notSpawnedLeaf % 15;
        for (int i = 0; i < leavesToSpawn; i++) {
            spawnFunnelLeaf(level, pos, random);
        }

        state.notSpawnedDust -= dustToSpawn;
        state.notSpawnedLeaf -= leavesToSpawn;
        state.activeProgressTick++;
        logFx("activate", AnomalyType.BLACK_HOLE, dustToSpawn + leavesToSpawn, level, pos);
    }

    private static void add(Level level, net.minecraft.core.particles.ParticleOptions type, BlockPos pos,
        double xOff, double yOff, double zOff, double speedX, double speedY, double speedZ) {
        level.addParticle(type, pos.getX() + xOff, pos.getY() + yOff, pos.getZ() + zOff, speedX, speedY, speedZ);
    }

    private static double signedOffset(RandomSource random) {
        return random.nextBoolean() ? random.nextFloat() : -random.nextFloat();
    }

    private static RandomSource randomForTick(long fxSeed, int tick) {
        return RandomSource.create(fxSeed ^ (long) tick * 341873128712L);
    }

    private static FxRuntimeState state(ResourceKey<Level> dimension, BlockPos pos) {
        String key = dimension.location() + ":" + pos.asLong();
        return FX_STATE.computeIfAbsent(key, ignored -> new FxRuntimeState());
    }

    private static void logFx(String event, AnomalyType type, int count, Level level, BlockPos pos) {
        StalkerPortMod.LOGGER.info("FX event={} anomaly={} count={} tick={} pos={}", event, type.name().toLowerCase(), count,
            level.getGameTime(), pos);
    }

    private static SoundEvent ambientSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> ModSounds.ELECTRA_AMBIENT.get();
            case BLACK_HOLE -> ModSounds.FUNNEL_AMBIENT.get();
            case TRAMPOLINE -> ModSounds.TRAMPOLINE_AMBIENT.get();
            case LIGHTER -> ModSounds.LIGHTER_AMBIENT.get();
            case CAROUSEL -> ModSounds.CAROUSEL_AMBIENT.get();
        };
    }

    private static SoundEvent activateSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> ModSounds.ELECTRA_ACTIVATE.get();
            case BLACK_HOLE -> ModSounds.FUNNEL_ACTIVATE.get();
            case TRAMPOLINE -> ModSounds.TRAMPOLINE_ACTIVATE.get();
            case LIGHTER -> ModSounds.LIGHTER_ACTIVATE.get();
            case CAROUSEL -> ModSounds.CAROUSEL_ACTIVATE.get();
        };
    }

    private static SoundEvent hitSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> ModSounds.ELECTRA_HIT.get();
            case BLACK_HOLE -> ModSounds.FUNNEL_HIT.get();
            case TRAMPOLINE -> ModSounds.TRAMPOLINE_HIT.get();
            case LIGHTER -> ModSounds.LIGHTER_HIT.get();
            case CAROUSEL -> ModSounds.CAROUSEL_HIT.get();
        };
    }

    private static final class FxRuntimeState {
        private int notSpawnedDust = 750;
        private int notSpawnedLeaf = 100;
        private int activeProgressTick;
    }
}
