package ru.stalcraft.port.anomaly;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

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

    public static void spawnTrigger(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        if (!level.isClientSide) {
            return;
        }

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
            () -> () -> invokeClientHook("spawnTrigger", level, pos, type, fxSeed));
    }

    private static void invokeClientHook(String methodName, Object... args) {
        try {
            Class<?> hookClass = Class.forName(CLIENT_HOOK_CLASS);
            Class<?>[] signature = switch (methodName) {
                case "spawnAmbient" -> new Class<?>[]{Level.class, BlockPos.class, AnomalyType.class, int.class, long.class, boolean.class};
                case "spawnTrigger" -> new Class<?>[]{Level.class, BlockPos.class, AnomalyType.class, long.class};
                default -> throw new IllegalArgumentException("Unknown method: " + methodName);
            };

            Method method = hookClass.getDeclaredMethod(methodName, signature);
            method.invoke(null, args);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            // Silently ignore FX errors to keep gameplay logic server-safe.
        }
    }
}
