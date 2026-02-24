package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class ElectraParticleSplash extends Particle {
   public ElectraParticleSplash(ParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.1F, 0.1F, icon);
      Random rand = parent.world.s;
      float x = rand.nextBoolean() ? rand.nextFloat() * 1.95F : -rand.nextFloat() * 1.95F;
      float y = rand.nextBoolean() ? rand.nextFloat() * 1.75F : -rand.nextFloat() * 1.35F;
      float z = rand.nextBoolean() ? rand.nextFloat() * 1.95F : -rand.nextFloat() * 1.95F;
      super.setPosition(parent.centerX + x, parent.centerY - 3.0 + y, parent.centerZ + z);
      super.setColorRGBA(0.614F, 0.733F, 1.0F, 1.0F);
      super.blendFunc = 1;
      super.textureSize = 1.15F;
   }

   @Override
   public void tick() {
      super.tick();
      super.textureSize += 0.1F;
      super.alpha -= 0.15F;
      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }
}
