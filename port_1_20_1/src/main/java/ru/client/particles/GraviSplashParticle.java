package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class GraviSplashParticle extends Particle {
   public GraviSplashParticle(ParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.0F, 0.0F, icon);
      this.clip = false;
      this.setPosition(parent.centerX, parent.centerY, parent.centerZ);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.ticksExisted < 6) {
         this.textureSize = (float)(this.textureSize + 0.75);
      }

      if (this.ticksExisted > 10) {
         this.alpha *= 0.75F;
      }

      if (this.ticksExisted > 20) {
         this.isDead = true;
      }
   }
}
