package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class ShotLightParticle extends Particle {
   private ShotParticleEmitter parent;

   public ShotLightParticle(ShotParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.15F, 0.15F, icon);
      this.parent = parent;
      this.setPosition(parent.centerX, parent.centerY, parent.centerZ);
      this.burn = this.prevBurn = 1.0F;
      this.speedFactor = 0.3F;
      this.rotation = this.prevRotation = parent.world.s.nextFloat() * 360.0F;
      this.alpha = 0.0F;
   }

   @Override
   public void tick() {
      super.tick();
      if (((nn)this.parent.emmiter).ac <= 2) {
         this.alpha = 1.0F;
      }

      if (((nn)this.parent.emmiter).ac > 2) {
         this.alpha = 0.0F;
      }
   }
}
