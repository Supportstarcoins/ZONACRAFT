package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class FunnelParticleLeaf extends Particle {
   private ParticleEmitter parent;

   public FunnelParticleLeaf(ParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.15F, 1.0F, icon);
      this.parent = parent;
      float x = parent.world.s.nextBoolean() ? (float)(Math.random() * 0.7F) : (float)(-Math.random() * 0.7F);
      float y = parent.world.s.nextBoolean() ? (float)(Math.random() * 0.3F) : (float)(-Math.random() * 0.3F);
      float z = parent.world.s.nextBoolean() ? (float)(Math.random() * 0.7F) : (float)(-Math.random() * 0.7F);
      this.setPosition(parent.centerX + x, parent.centerY - 4.0 + y, parent.centerZ + z);
      super.alpha = 1.0F;
      super.textureSize = 0.1F;
   }

   @Override
   public void tick() {
      super.tick();
      super.motionX = this.parent.world.s.nextBoolean() ? (float)(Math.random() * 0.12F) : (float)(-Math.random() * 0.12F);
      super.motionY = this.parent.world.s.nextBoolean() ? (float)(Math.random() * 0.35F) : (float)(-Math.random() * 0.2F);
      super.motionZ = this.parent.world.s.nextBoolean() ? (float)(Math.random() * 0.12F) : (float)(-Math.random() * 0.12F);
      super.alpha -= 0.05F;
      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }
}
