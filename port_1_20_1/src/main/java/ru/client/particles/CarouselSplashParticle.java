package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class CarouselSplashParticle extends Particle {
   public CarouselSplashParticle(ParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.1F, 0.1F, icon);
      super.alpha = 1.0F;
      super.textureSize = 0.0F;
   }

   @Override
   public void tick() {
      super.tick();
      super.alpha -= 0.1F;
      super.textureSize += 0.08F;
      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }
}
