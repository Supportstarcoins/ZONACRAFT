package ru.stalcraft.port.anomaly;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ru.stalcraft.port.registry.ModParticles;

public final class AnomalyFxDispatcher {
    private static final String CLIENT_HOOK_CLASS = "ru.stalcraft.port.client.AnomalyClientFxHooks";

    private AnomalyFxDispatcher() {
    }

    public static void spawnAmbient(Level level, BlockPos pos, AnomalyType type, int animationTick, long fxSeed, boolean active) {
        if (!level.isClientSide) {
            return;
        }

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
            () -> () -> invokeClientHook("spawnAmbient", level, pos, type, animationTick, fxSeed, active));
    }

    public static void spawnActivate(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        if (level.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> invokeClientHook("spawnActivate", level, pos, type, fxSeed));
            return;
        }

        spawnServerBurst(level, pos, type, true);
    }

    public static void spawnHit(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        if (level.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> invokeClientHook("spawnHit", level, pos, type, fxSeed));
            return;
        }

        spawnServerBurst(level, pos, type, false);
    }

    public static void spawnSplash(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        if (level.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> invokeClientHook("spawnSplash", level, pos, type, fxSeed));
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            ParticleOptions particle = ModParticles.get(type == AnomalyType.LIGHTER ? "lighter/distortion" : "bubble").orElse(null);
            if (particle != null) {
                serverLevel.sendParticles(particle,
                    pos.getX() + 0.5D, pos.getY() + 0.45D, pos.getZ() + 0.5D,
                    16, 0.8D, 0.6D, 0.8D, 0.02D);
            }
        }
    }

    private static void spawnServerBurst(Level level, BlockPos pos, AnomalyType type, boolean activate) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ParticleOptions particle = ModParticles.get(switch (type) {
            case ELECTRA -> "electra/active";
            case BLACK_HOLE -> activate ? "funnel/funnel_eye" : "funnel/distortion";
            case TRAMPOLINE -> activate ? "trampoline/distortion" : "trampoline/idle";
            case LIGHTER -> "lighter/distortion";
            case CAROUSEL -> activate ? "carousel/distortion" : "carousel/idle";
        }).orElse(null);

        if (particle == null) {
            return;
        }

        int count = activate ? 18 : 8;
        double spread = activate ? 0.55D : 0.35D;
        double speed = activate ? 0.06D : 0.03D;
        serverLevel.sendParticles(particle,
            pos.getX() + 0.5D, pos.getY() + 0.4D, pos.getZ() + 0.5D,
            count, spread, 0.45D, spread, speed);
    }

    private static void invokeClientHook(String methodName, Object... args) {
        try {
            Class<?> hookClass = Class.forName(CLIENT_HOOK_CLASS);
            Class<?>[] signature = switch (methodName) {
                case "spawnAmbient" -> new Class<?>[]{Level.class, BlockPos.class, AnomalyType.class, int.class, long.class, boolean.class};
                case "spawnActivate", "spawnHit", "spawnSplash" -> new Class<?>[]{Level.class, BlockPos.class, AnomalyType.class, long.class};
                default -> throw new IllegalArgumentException("Unknown method: " + methodName);
            };

            Method method = hookClass.getDeclaredMethod(methodName, signature);
            method.invoke(null, args);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            // Keep FX failures isolated from gameplay.
        }
    }
}
