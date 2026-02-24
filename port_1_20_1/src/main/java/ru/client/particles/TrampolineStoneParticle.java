package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class TrampolineStoneParticle extends Particle {
   public TrampolineStoneParticle(TrampolineParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.03F, 0.05F, icon);
      this.setPosition(
         parent.centerX + parent.world.s.nextDouble() - 0.5, parent.centerY + this.halfCollisionSize, parent.centerZ + parent.world.s.nextDouble() - 0.5
      );
   }

   @Override
   public void tick() {
      super.tick();
      if (this.motionY == 0.0F && Math.random() > 0.99) {
         this.jump();
      }

      this.motionY = (float)(this.motionY - 0.05);
   }

   public void jump() {
      this.motionY = this.motionY + this.parent.world.s.nextFloat() / 4.0F;
   }
}
