package ru.stalcraft.client.gui.clans;

import org.lwjgl.opengl.GL11;

public class GuiElement extends avk {
   public static void drawRectHD(int par0, int par1, int par2, int par3, int par4) {
      if (par0 < par2) {
         int j1 = par0;
         par0 = par2;
         par2 = j1;
      }

      if (par1 < par3) {
         int j1 = par1;
         par1 = par3;
         par3 = j1;
      }

      float f = (par4 >> 24 & 0xFF) / 255.0F;
      float f1 = (par4 >> 16 & 0xFF) / 255.0F;
      float f2 = (par4 >> 8 & 0xFF) / 255.0F;
      float f3 = (par4 & 0xFF) / 255.0F;
      bfq tessellator = bfq.a;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(f1, f2, f3, f);
      tessellator.b();
      tessellator.a(par0 / 2.0, par3 / 2.0, 0.0);
      tessellator.a(par2 / 2.0, par3 / 2.0, 0.0);
      tessellator.a(par2 / 2.0, par1 / 2.0, 0.0);
      tessellator.a(par0 / 2.0, par1 / 2.0, 0.0);
      tessellator.a();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static int getStringWidthHD(String str) {
      return atv.w().l.a(str) * 2;
   }
}
