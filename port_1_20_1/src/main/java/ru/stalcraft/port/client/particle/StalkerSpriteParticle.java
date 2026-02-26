package ru.stalcraft.port.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class StalkerSpriteParticle extends TextureSheetParticle {
    protected StalkerSpriteParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ,
        SpriteSet spriteSet) {
        super(level, x, y, z, speedX, speedY, speedZ);
        this.friction = 0.92F;
        this.gravity = 0.02F;
        this.quadSize *= 0.9F + level.random.nextFloat() * 0.4F;
        this.lifetime = 14 + level.random.nextInt(12);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
            double speedX, double speedY, double speedZ) {
            return new StalkerSpriteParticle(level, x, y, z, speedX, speedY, speedZ, this.spriteSet);
        }
    }
}
