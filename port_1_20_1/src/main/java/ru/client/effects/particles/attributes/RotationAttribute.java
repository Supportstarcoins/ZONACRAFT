package ru.stalcraft.client.effects.particles.attributes;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticlesRenderer;

public class RotationAttribute extends Attribute {
   public RotationAttribute() {
      super("rotation", 1);
   }

   @Override
   public void writeToBuffer(Particle particle, float frame) {
      super.buffer.put(ParticlesRenderer.interpolateRotation(particle.prevRotation, particle.rotation, frame));
   }
}
