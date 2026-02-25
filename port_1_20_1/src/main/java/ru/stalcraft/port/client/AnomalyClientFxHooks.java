package ru.stalcraft.port.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import ru.stalcraft.port.anomaly.AnomalyType;

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

    public static void spawnTrigger(Level level, BlockPos pos, AnomalyType type, long fxSeed) {
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

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            level.playLocalSound(pos, triggerSound(type), SoundSource.BLOCKS, 0.8F, 0.95F + random.nextFloat() * 0.1F, false);
        }
    }

    private static net.minecraft.sounds.SoundEvent ambientSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> SoundEvents.BEACON_AMBIENT;
            case BLACK_HOLE -> SoundEvents.PORTAL_AMBIENT;
            case TRAMPOLINE -> SoundEvents.SLIME_BLOCK_PLACE;
            case LIGHTER -> SoundEvents.CAMPFIRE_CRACKLE;
            case CAROUSEL -> SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT;
        };
    }

    private static net.minecraft.sounds.SoundEvent triggerSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> SoundEvents.LIGHTNING_BOLT_IMPACT;
            case BLACK_HOLE -> SoundEvents.ENDERMAN_STARE;
            case TRAMPOLINE -> SoundEvents.SLIME_JUMP;
            case LIGHTER -> SoundEvents.BLAZE_SHOOT;
            case CAROUSEL -> SoundEvents.PLAYER_ATTACK_SWEEP;
        };
    }
}
