package ru.stalcraft.client.render;

import org.lwjgl.opengl.GL11;

public class Render2d extends bgm {
   private double halfSize;
   private boolean decrementAlpha = false;
   private int lifetime = 0;
   private bjo texture;

   public Render2d(double halfSize, String textureName) {
      this.halfSize = halfSize;
      this.texture = new bjo("stalker", "textures/" + textureName + ".png");
   }

   public Render2d(double halfSize, String textureName, boolean decrementAlpha, int lifetime) {
      this(halfSize, textureName);
      this.decrementAlpha = decrementAlpha;
      this.lifetime = lifetime;
   }

   public void doRenderEntity(nn acid, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      GL11.glEnable(32826);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      atv.w().N.a(this.texture);
      if (this.decrementAlpha) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, Math.max(0.0F, 1.0F - (float)acid.ac / this.lifetime));
      }

      bfq tessellator = bfq.a;
      GL11.glRotatef(180.0F - super.b.j, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-super.b.k, 1.0F, 0.0F, 0.0F);
      tessellator.b();
      tessellator.b(0.0F, 1.0F, 0.0F);
      tessellator.a(-this.halfSize, -this.halfSize, 0.0, 0.0, 1.0);
      tessellator.a(this.halfSize, -this.halfSize, 0.0, 1.0, 1.0);
      tessellator.a(this.halfSize, this.halfSize, 0.0, 1.0, 0.0);
      tessellator.a(-this.halfSize, this.halfSize, 0.0, 0.0, 0.0);
      tessellator.a();
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   public void a(nn par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderEntity(par1Entity, par2, par4, par6, par8, par9);
   }

   protected bjo a(nn entity) {
      return null;
   }
}
