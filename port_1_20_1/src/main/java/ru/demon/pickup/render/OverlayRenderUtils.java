package ru.demon.pickup.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverlayRenderUtils extends avk {
   atv mc = atv.w();

   public void renderToolTip(ye is, int y, awf resolution) {
      List list = is.a(this.mc.h, this.mc.u.x);

      for (int k = 0; k < list.size(); k++) {
         if (k == 0) {
            list.set(k, "§" + Integer.toHexString(is.w().e) + (String)list.get(k));
         } else {
            list.set(k, a.h + (String)list.get(k));
         }
      }

      avi font = is.b().getFontRenderer(is);
      int center = this.calculateCenter(list, resolution.a() / 2, font == null ? this.mc.l : font);
      this.drawHoveringText(list, center, y, font == null ? this.mc.l : font);
   }

   int calculateCenter(List list, int par1, avi font) {
      if (!list.isEmpty()) {
         int k = 0;

         for (String s : list) {
            int l = font.a(s);
            if (l > k) {
               k = l;
            }
         }

         return par1 - (4 + k + 4) / 2;
      } else {
         return 0;
      }
   }

   void drawHoveringText(List list, int par1, int par2, avi font) {
      if (!list.isEmpty()) {
         GL11.glDisable(32826);
         att.a();
         GL11.glDisable(2896);
         GL11.glDisable(2929);
         int k = 0;

         for (String s : list) {
            int l = font.a(s);
            if (l > k) {
               k = l;
            }
         }

         int j2 = par1 + 4;
         int k2 = par2 + 4;
         int i1 = 8;
         if (list.size() > 1) {
            i1 += 2 + (list.size() - 1) * 10;
         }

         this.n = 300.0F;
         bgw itemRender = new bgw();
         itemRender.f = 300.0F;
         int j1 = -267386864;
         this.a(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
         this.a(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
         this.a(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
         this.a(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
         this.a(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
         int k1 = 1347420415;
         int l1 = (k1 & 16711422) >> 1 | k1 & 0xFF000000;
         this.a(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
         this.a(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
         this.a(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
         this.a(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

         for (int i2 = 0; i2 < list.size(); i2++) {
            String s1 = (String)list.get(i2);
            font.a(s1, j2, k2, -1);
            if (i2 == 0) {
               k2 += 2;
            }

            k2 += 10;
         }

         this.n = 0.0F;
         itemRender.f = 0.0F;
         GL11.glEnable(2896);
         GL11.glEnable(2929);
         att.b();
         GL11.glEnable(32826);
      }
   }

   public void drawItemStack(ye stack, int x, int y, String str, bgw itemRender, avi fontRendererObj) {
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glTranslatef(0.0F, 0.0F, 32.0F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      att.c();
      GL11.glEnable(32826);
      GL11.glEnable(2929);
      itemRender.f = 200.0F;
      avi font = null;
      if (stack != null) {
         font = stack.b().getFontRenderer(stack);
      }

      if (font == null) {
         font = fontRendererObj;
      }

      itemRender.b(font, atv.w().J(), stack, x, y);
      itemRender.a(font, atv.w().J(), stack, x, y, str);
      itemRender.f = 0.0F;
      GL11.glPopMatrix();
   }
}
