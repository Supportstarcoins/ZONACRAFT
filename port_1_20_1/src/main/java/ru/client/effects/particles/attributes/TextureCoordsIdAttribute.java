package ru.stalcraft.client.effects.particles.attributes;

import ru.stalcraft.client.effects.particles.Particle;

public class TextureCoordsIdAttribute extends Attribute {
   public TextureCoordsIdAttribute() {
      super("textureCoordsIdFloat", 1);
   }

   @Override
   public void writeToBuffer(Particle particle, float frame) {
      super.buffer.put(particle.icon.index);
   }
}
