package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class ElectraParticleGlow extends Particle {
   public ElectraParticleGlow(ParticleEmitter parent, ParticleIcon icon, float x, float z) {
      super(parent, 0.1F, 0.1F, icon);
      super.rotation = (float)(Math.random() * 360.0);
      super.setPosition(parent.centerX + x, parent.centerY - 4.0, parent.centerZ + z);
      super.setColorRGBA(0.714F, 0.833F, 1.0F, 1.0F);
      super.blendFunc = 1;
      super.textureSize = 0.65F;
   }

   @Override
   public void tick() {
      super.tick();
      super.alpha -= 0.13F;
      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }
}
