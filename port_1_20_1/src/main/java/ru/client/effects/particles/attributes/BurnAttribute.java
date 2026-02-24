package ru.stalcraft.client.effects.particles.attributes;

import ru.stalcraft.client.effects.particles.Particle;

public class BurnAttribute extends Attribute {
   public BurnAttribute() {
      super("burn", 1);
   }

   @Override
   public void writeToBuffer(Particle particle, float frame) {
      super.buffer.put(particle.prevBurn + (particle.burn - particle.prevBurn) * frame);
   }
}
