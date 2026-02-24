package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class ShotSmokeParticle extends Particle {
   private ShotParticleEmitter parent;

   public ShotSmokeParticle(ShotParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.15F, 0.15F, icon);
      this.parent = parent;
      this.setPosition(parent.centerX, parent.centerY, parent.centerZ);
      this.speedFactor = 0.4F;
      this.rotation = this.prevRotation = parent.world.s.nextFloat() * 360.0F;
      this.alpha = 0.0F;
   }

   @Override
   public void tick() {
      super.tick();
      if (((nn)this.parent.emmiter).ac <= 2) {
         this.alpha += 0.1F;
      } else {
         this.alpha *= 0.5F;
      }

      this.textureSize *= 1.3F;
   }
}
