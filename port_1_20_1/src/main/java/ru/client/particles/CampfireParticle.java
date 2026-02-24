package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class CampfireParticle extends Particle {
   public boolean vectorXSide;
   public boolean vectorZSide;

   public CampfireParticle(ParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.1F, 0.1F, icon);
      float x = parent.world.s.nextBoolean() ? 0.275F - parent.world.s.nextFloat() * 0.1F : -0.275F + parent.world.s.nextFloat() * 0.1F;
      float z = parent.world.s.nextBoolean() ? 0.275F - parent.world.s.nextFloat() * 0.1F : -0.275F + parent.world.s.nextFloat() * 0.1F;
      super.setPosition(parent.centerX + 0.05F + x, parent.centerY - 4.15, parent.centerZ + 0.075F + z);
      super.blendFunc = 0;
      super.burn = 1.0F;
      super.alpha = 0.9F;
      super.textureSize = 0.2F;
      super.rotationSpeed = 0.5F;
      if (x < 0.0F) {
         this.vectorXSide = false;
      } else {
         this.vectorXSide = true;
      }

      if (z < 0.0F) {
         this.vectorZSide = false;
      } else {
         this.vectorZSide = true;
      }
   }

   @Override
   public void tick() {
      super.tick();
      super.alpha -= 0.045F;
      super.textureSize += 0.03F;
      super.motionY += 0.0065F;
      if (this.vectorXSide) {
         this.motionX -= 0.0015F;
      } else {
         this.motionX += 0.0015F;
      }

      if (this.vectorZSide) {
         this.motionZ -= 0.0015F;
      } else {
         this.motionZ += 0.0015F;
      }

      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }
}
