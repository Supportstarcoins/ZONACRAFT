package ru.stalcraft.entity;

public class EntityRail extends EntityBullet {
   public EntityRail(abw par1World) {
      super(par1World);
   }

   public EntityRail(of shooter, int damage, boolean aim, float speedFactor, String hitSound, float yaw, float pitch) {
      super(shooter, damage, aim, 0.0F, speedFactor, hitSound, 1.0, yaw, pitch);
   }

   public boolean a(double par1) {
      double d1 = super.E.b() * 64.0;
      d1 *= 64.0;
      return par1 < d1 * d1;
   }
}
