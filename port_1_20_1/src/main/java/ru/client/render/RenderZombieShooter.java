package ru.stalcraft.client.render;

import ru.stalcraft.entity.EntityZombieShooter;

public class RenderZombieShooter extends bgu {
   bbj modelBiped = this.a;
   private static final bjo zombieTexture = new bjo("textures/entity/zombie/zombie.png");

   public RenderZombieShooter() {
      super(new bbj(0.0F, 0.0F, 64, 64), 0.5F);
   }

   protected bjo a(nn par1Entity) {
      return zombieTexture;
   }

   public void renderZombieShooter(EntityZombieShooter zombie, double par2, double par4, double par6, float par8, float par9) {
      this.modelBiped.o = true;
      atv.w().N.a(zombieTexture);
      super.a(zombie, par2, par4, par6, par8, par9);
   }

   public void a(og par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderZombieShooter((EntityZombieShooter)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   public void a(nn par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderZombieShooter((EntityZombieShooter)par1Entity, par2, par4, par6, par8, par9);
   }
}
