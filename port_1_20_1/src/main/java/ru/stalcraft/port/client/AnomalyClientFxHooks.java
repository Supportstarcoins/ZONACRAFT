package ru.stalcraft.port.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import ru.stalcraft.port.anomaly.AnomalyType;
import ru.stalcraft.port.registry.ModSounds;

public final class AnomalyClientFxHooks {
    private AnomalyClientFxHooks() {
    }

    public static void spawnAmbient(Level level, BlockPos pos, AnomalyType type, int animationTick, long fxSeed, boolean active) {
        RandomSource random = level.random;
        if (animationTick % 6 != 0 && !active) {
            return;
        }

        double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.7D;
        double y = pos.getY() + 0.25D + random.nextDouble() * 0.5D;
        double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.7D;

        switch (type) {
            case ELECTRA -> level.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0.0D, 0.03D, 0.0D);
            case BLACK_HOLE -> level.addParticle(ParticleTypes.PORTAL, x, y, z, 0.0D, 0.02D, 0.0D);
            case TRAMPOLINE -> level.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0D, 0.05D, 0.0D);
            case LIGHTER -> level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.01D, 0.0D);
            case CAROUSEL -> level.addParticle(ParticleTypes.SWEEP_ATTACK, x, y, z, 0.0D, 0.01D, 0.0D);
        }

        if (active && animationTick % 12 == 0) {
            level.playLocalSound(pos, ambientSound(type), SoundSource.BLOCKS, 0.35F, 1.0F, false);
        }
    }

    public static void spawnActivate(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        RandomSource random = level.random;
        for (int i = 0; i < 18; i++) {
            double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.9D;
            double y = pos.getY() + 0.35D + random.nextDouble() * 0.8D;
            double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.9D;
            double speed = 0.04D + random.nextDouble() * 0.04D;

            switch (type) {
                case ELECTRA -> level.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0.0D, speed, 0.0D);
                case BLACK_HOLE -> level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, 0.0D, speed * 0.5D, 0.0D);
                case TRAMPOLINE -> level.addParticle(ParticleTypes.POOF, x, y, z, 0.0D, speed, 0.0D);
                case LIGHTER -> level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, speed, 0.0D);
                case CAROUSEL -> level.addParticle(ParticleTypes.CRIT, x, y, z, 0.0D, speed, 0.0D);
            }
        }

        if (Minecraft.getInstance().player != null) {
            level.playLocalSound(pos, activateSound(type), SoundSource.BLOCKS, 0.8F, 0.95F + random.nextFloat() * 0.1F, false);
        }
    }

    public static void spawnHit(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        RandomSource random = level.random;
        for (int i = 0; i < 8; i++) {
            double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.6D;
            double y = pos.getY() + 0.15D + random.nextDouble() * 0.4D;
            double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.6D;
            switch (type) {
                case ELECTRA -> level.addParticle(ParticleTypes.CRIT, x, y, z, 0.0D, 0.02D, 0.0D);
                case BLACK_HOLE -> level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.01D, 0.0D);
                case TRAMPOLINE -> level.addParticle(ParticleTypes.SPLASH, x, y, z, 0.0D, 0.06D, 0.0D);
                case LIGHTER -> level.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0.0D, 0.02D, 0.0D);
                case CAROUSEL -> level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0.0D, 0.04D, 0.0D);
            }
        }

        level.playLocalSound(pos, hitSound(type), SoundSource.BLOCKS, 0.55F, 1.0F + random.nextFloat() * 0.08F, false);
    }

    public static void spawnSplash(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
        RandomSource random = level.random;
        for (int i = 0; i < 12; i++) {
            double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D);
            double y = pos.getY() + 0.1D + random.nextDouble() * 0.9D;
            double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D);
            level.addParticle(type == AnomalyType.LIGHTER ? ParticleTypes.FLAME : ParticleTypes.BUBBLE, x, y, z, 0.0D, 0.05D, 0.0D);
        }
    }

    private static SoundEvent ambientSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> ModSounds.ELECTRA_AMBIENT.get();
            case BLACK_HOLE -> ModSounds.BLACK_HOLE_AMBIENT.get();
            case TRAMPOLINE -> ModSounds.TRAMPOLINE_AMBIENT.get();
            case LIGHTER -> ModSounds.LIGHTER_AMBIENT.get();
            case CAROUSEL -> ModSounds.CAROUSEL_AMBIENT.get();
        };
    }

    private static SoundEvent activateSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> ModSounds.ELECTRA_ACTIVATE.get();
            case BLACK_HOLE -> ModSounds.BLACK_HOLE_ACTIVATE.get();
            case TRAMPOLINE -> ModSounds.TRAMPOLINE_ACTIVATE.get();
            case LIGHTER -> ModSounds.LIGHTER_ACTIVATE.get();
            case CAROUSEL -> ModSounds.CAROUSEL_ACTIVATE.get();
        };
    }

    private static SoundEvent hitSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> ModSounds.ELECTRA_HIT.get();
            case BLACK_HOLE -> ModSounds.BLACK_HOLE_HIT.get();
            case TRAMPOLINE -> ModSounds.TRAMPOLINE_HIT.get();
            case LIGHTER -> ModSounds.LIGHTER_HIT.get();
            case CAROUSEL -> ModSounds.CAROUSEL_HIT.get();
        };
    }
}
