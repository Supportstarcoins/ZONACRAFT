package ru.stalcraft.client.effects.particles.attributes;

import ru.stalcraft.client.effects.particles.Particle;

public class PositionAttribute extends Attribute {
   public PositionAttribute() {
      super("modelPosition", 3);
   }

   @Override
   public void writeToBuffer(Particle particle, float frame) {
      super.buffer.put((float)(particle.prevPosX + (particle.posX - particle.prevPosX) * frame - bgl.b));
      super.buffer.put((float)(particle.prevPosY + (particle.posY - particle.prevPosY) * frame - bgl.c));
      super.buffer.put((float)(particle.prevPosZ + (particle.posZ - particle.prevPosZ) * frame - bgl.d));
   }
}
