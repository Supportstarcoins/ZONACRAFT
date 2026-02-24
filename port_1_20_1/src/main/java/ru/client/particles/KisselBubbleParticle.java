package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class KisselBubbleParticle extends Particle {
   public KisselBubbleParticle(ParticleEmitter parent, float size, ParticleIcon icon) {
      super(parent, size, size, icon);
      Random rand = parent.world.s;
      this.setPosition(parent.centerX + rand.nextDouble() - 0.5, parent.centerY, parent.centerZ + rand.nextDouble() - 0.5);
      this.motionY = 0.01F + rand.nextFloat() * 0.005F;
      this.burn = 0.2F + rand.nextFloat() * 0.1F;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.textureSize == 0.0F) {
         this.isDead = true;
      } else if (this.parent.world.s.nextInt(20) == 0) {
         this.textureSize = 0.0F;
      }
   }
}
