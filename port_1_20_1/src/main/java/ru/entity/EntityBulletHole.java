package ru.stalcraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EntityBulletHole extends beg {
   private static final bjo texHole = new bjo("stalker", "textures/bullet_hole.png");
   private int side;
   private Random ab = new Random();

   public EntityBulletHole(abw world, double x, double y, double z, int sideHit) {
      super(world, x, y, z);
      this.x = this.y = this.z = 0.0;
      this.side = sideHit;
      this.g = 2400;
      this.f = 1;
   }

   public int c(float par1) {
      return 61680;
   }

   public void a(bfq tessellator, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
      float f6 = (this.f + p_70539_2_) / this.g;
      f6 *= f6;
      float f7 = 2.0F - f6 * 2.0F;
      if (f7 > 1.0F) {
         f7 = 1.0F;
      }

      nn player = atv.w().h;
      ay = player.U + (player.u - player.U) * p_70539_2_;
      az = player.V + (player.v - player.V) * p_70539_2_;
      aA = player.W + (player.w - player.W) * p_70539_2_;
      GL11.glPushMatrix();
      float f8 = 0.05F;
      float f9 = (float)(this.u - ay);
      float f10 = (float)(this.v - az);
      float f11 = (float)(this.w - aA);
      float f12 = this.q
         .q(
            ls.c(this.u + (this.side == 4 ? -0.5 : (this.side == 5 ? 0.5 : 0.0))),
            ls.c(this.v + (this.side == 0 ? -0.5 : (this.side == 1 ? 0.5 : 0.0))),
            ls.c(this.w + (this.side == 2 ? -0.5 : (this.side == 3 ? 0.5 : 0.0)))
         );
      att.a();
      GL11.glDisable(2896);
      atv.w().J().a(texHole);
      GL11.glEnable(3042);
      GL11.glBlendFunc(768, 775);
      GL11.glDepthMask(false);
      tessellator.b();
      tessellator.a(0.8F, 0.8F, 0.9F, 0.45F);
      switch (this.side) {
         case 0:
            tessellator.a(f9 - f8, f10 - 0.01F, f11 - f8, 0.0, 1.0);
            tessellator.a(f9 + f8, f10 - 0.01F, f11 - f8, 1.0, 1.0);
            tessellator.a(f9 + f8, f10 - 0.01F, f11 + f8, 1.0, 0.0);
            tessellator.a(f9 - f8, f10 - 0.01F, f11 + f8, 0.0, 0.0);
            break;
         case 1:
            tessellator.a(f9 - f8, f10 + 0.01F, f11 + f8, 0.0, 0.0);
            tessellator.a(f9 + f8, f10 + 0.01F, f11 + f8, 1.0, 0.0);
            tessellator.a(f9 + f8, f10 + 0.01F, f11 - f8, 1.0, 1.0);
            tessellator.a(f9 - f8, f10 + 0.01F, f11 - f8, 0.0, 1.0);
            break;
         case 2:
            tessellator.a(f9 - f8, f10 + f8, f11 - 0.01F, 0.0, 1.0);
            tessellator.a(f9 + f8, f10 + f8, f11 - 0.01F, 1.0, 1.0);
            tessellator.a(f9 + f8, f10 - f8, f11 - 0.01F, 1.0, 0.0);
            tessellator.a(f9 - f8, f10 - f8, f11 - 0.01F, 0.0, 0.0);
            break;
         case 3:
            tessellator.a(f9 - f8, f10 - f8, f11 + 0.01F, 0.0, 0.0);
            tessellator.a(f9 + f8, f10 - f8, f11 + 0.01F, 1.0, 0.0);
            tessellator.a(f9 + f8, f10 + f8, f11 + 0.01F, 1.0, 1.0);
            tessellator.a(f9 - f8, f10 + f8, f11 + 0.01F, 0.0, 1.0);
            break;
         case 4:
            tessellator.a(f9 - 0.01F, f10 + f8, f11 + f8, 1.0, 1.0);
            tessellator.a(f9 - 0.01F, f10 + f8, f11 - f8, 1.0, 0.0);
            tessellator.a(f9 - 0.01F, f10 - f8, f11 - f8, 0.0, 0.0);
            tessellator.a(f9 - 0.01F, f10 - f8, f11 + f8, 0.0, 1.0);
            break;
         case 5:
            tessellator.a(f9 + 0.01F, f10 - f8, f11 + f8, 0.0, 1.0);
            tessellator.a(f9 + 0.01F, f10 - f8, f11 - f8, 0.0, 0.0);
            tessellator.a(f9 + 0.01F, f10 + f8, f11 - f8, 1.0, 0.0);
            tessellator.a(f9 + 0.01F, f10 + f8, f11 + f8, 1.0, 1.0);
      }

      tessellator.a();
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2896);
      att.b();
      GL11.glPopMatrix();
   }

   public void l_() {
      this.r = this.u;
      this.s = this.v;
      this.t = this.w;
      this.f++;
      if (this.f++ >= this.g) {
         this.x();
      }
   }

   public int b() {
      return 3;
   }
}
