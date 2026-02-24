package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class CampfireParticleSmoke extends Particle {
   public Random rand;
   public int vector;

   public CampfireParticleSmoke(ParticleEmitter parent, ParticleIcon icon, int vector) {
      super(parent, 0.1F, 0.1F, icon);
      this.rand = parent.world.s;
      float x = parent.world.s.nextBoolean() ? parent.world.s.nextFloat() * 0.15F : -parent.world.s.nextFloat() * 0.15F;
      float z = parent.world.s.nextBoolean() ? parent.world.s.nextFloat() * 0.15F : -parent.world.s.nextFloat() * 0.15F;
      super.setPosition(parent.centerX + x, parent.centerY - 4.15, parent.centerZ + z);
      super.alpha = 0.4F;
      super.blendFunc = 0;
      this.vector = vector;
   }

   @Override
   public void tick() {
      super.tick();
      super.motionY = 0.12F;
      super.textureSize += 0.05F;
      super.alpha -= 0.0125F;
      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }
}
