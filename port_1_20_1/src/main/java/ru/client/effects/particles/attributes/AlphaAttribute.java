package ru.stalcraft.client.effects.particles.attributes;

import ru.stalcraft.client.effects.particles.Particle;

public class AlphaAttribute extends Attribute {
   public AlphaAttribute() {
      super("alpha", 1);
   }

   @Override
   public void writeToBuffer(Particle particle, float frame) {
      super.buffer.put(particle.prevAlpha + (particle.alpha - particle.prevAlpha) * frame);
   }
}
