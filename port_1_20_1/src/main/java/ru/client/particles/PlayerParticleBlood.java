package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class PlayerParticleBlood extends Particle {
   public boolean alphaBoost;

   public PlayerParticleBlood(ParticleEmitter parent, ParticleIcon icon, double posX, double posY, double posZ) {
      super(parent, 1.1F, 1.0F, icon);
      this.setPosition(posX, posY, posZ);
      super.prevRotation = super.rotation = super.parent.world.s.nextFloat() * 360.0F;
      super.textureSize = 0.1F;
      super.alpha = 1.0F;
   }

   @Override
   public void tick() {
      super.tick();
      super.alpha -= 0.1F;
      super.textureSize += 0.1F;
      super.motionY -= 0.01F;
      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }
}
