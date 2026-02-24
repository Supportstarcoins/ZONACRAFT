package ru.stalcraft.client.particles;

import ru.stalcraft.tile.IParticleEmmiter;

public class BlockParticleEmitter implements IParticleEmmiter {
   public abw world;
   public double posX;
   public double posY;
   public double posZ;

   public BlockParticleEmitter(abw world, double posX, double posY, double posZ) {
      this.world = world;
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
   }

   @Override
   public double getPosX() {
      return this.posX;
   }

   @Override
   public double getPosY() {
      return this.posY;
   }

   @Override
   public double getPosZ() {
      return this.posZ;
   }

   @Override
   public abw getWorld() {
      return this.world;
   }
}
