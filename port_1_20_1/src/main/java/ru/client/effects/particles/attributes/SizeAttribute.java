package ru.stalcraft.client.effects.particles.attributes;

import ru.stalcraft.client.effects.particles.Particle;

public class SizeAttribute extends Attribute {
   public SizeAttribute() {
      super("size", 1);
   }

   @Override
   public void writeToBuffer(Particle particle, float frame) {
      super.buffer.put(particle.prevTextureSize + (particle.textureSize - particle.prevTextureSize) * frame);
   }
}
