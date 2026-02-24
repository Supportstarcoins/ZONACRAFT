package ru.stalcraft.client.render;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.tile.TileEntityEjectionSave;

public class RenderBoxEjectionSave extends bje {
   public void a(asp par1, double d0, double d1, double d2, float f) {
      TileEntityEjectionSave par6 = (TileEntityEjectionSave)par1;
      if (atv.w().c.h()) {
         GL11.glPushMatrix();
         GL11.glDisable(2896);
         GL11.glDisable(3008);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glLineWidth(2.0F);
         GL11.glDisable(3553);
         GL11.glDepthMask(false);
         GL11.glTranslated(d0 + 0.5, d1, d2 + 0.5);
         float f1 = -0.05F;
         GL11.glColor4d(0.0, 0.8, 0.0, 1.0);
         this.drawOutlinedBoundingBox(asx.a().a(-5.0, 0.0, -5.0, 5.0, 2.5, 5.0).b(f1, f1, f1));
         GL11.glDepthMask(true);
         GL11.glEnable(3553);
         GL11.glDisable(3042);
         GL11.glEnable(3008);
         GL11.glEnable(2896);
         GL11.glPopMatrix();
      }
   }

   private void drawOutlinedBoundingBox(asx par1AxisAlignedBB) {
      bfq tessellator = bfq.a;
      tessellator.b(3);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.b, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.b, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
      tessellator.a();
      tessellator.b(3);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.e, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.e, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.e, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.e, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.e, par1AxisAlignedBB.c);
      tessellator.a();
      tessellator.b(1);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.e, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.e, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.b, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.e, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.b, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.e, par1AxisAlignedBB.f);
      tessellator.a();
   }
}
