package ru.stalcraft.client.particles;

import ru.stalcraft.tile.IParticleEmmiter;

public class EntityParticleEmitter implements IParticleEmmiter {
   public nn entity;
   public abw worldObj;

   public EntityParticleEmitter(nn entity) {
      this.worldObj = entity.q;
      this.entity = entity;
   }

   @Override
   public double getPosX() {
      return this.entity.u;
   }

   @Override
   public double getPosY() {
      return this.entity.v;
   }

   @Override
   public double getPosZ() {
      return this.entity.w;
   }

   @Override
   public abw getWorld() {
      return this.worldObj;
   }
}
