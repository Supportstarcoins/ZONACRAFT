package ru.stalcraft.client.gui.shop;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.client.shop.ShopItem;
import ru.stalcraft.player.PlayerUtils;

public class GuiSlotButton extends aut {
   public bjo caseBackground = new bjo("stalker:textures/case_background.png");
   public bjo shop_widgets = new bjo("stalker:textures/shop_widgets.png");

   public GuiSlotButton(int par1, int par2, int par3, int par4, int par5, String par6Str, ShopItem stackSlot) {
      super(par1, par2, par3, par4, par5, par6Str);
   }

   public void a(atv par1Minecraft, int par2, int par3) {
      if (this.i) {
         avi fontrenderer = par1Minecraft.l;
         par1Minecraft.J().a(this.shop_widgets);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.j = par2 >= this.d && par3 >= this.e && par2 < this.d + this.b && par3 < this.e + this.c;
         int k = this.a(this.j);
         if (this.h) {
            this.drawTexturedModalRect(super.d, super.e, 12, 2, 234, 78, 256, 0.72);
         } else {
            this.drawTexturedModalRect(super.d, super.e, 12, 82, 234, 78, 256, 0.72);
         }

         par1Minecraft.J().a(this.caseBackground);
         this.drawTexturedModalRect(super.d - 6, super.e + 3, 960, 448, 64, 64, 1024, 0.79);
         this.b(par1Minecraft, par2, par3);
         this.a(fontrenderer, this.f, this.d + this.b / 2 - 89, this.e + (this.c - 8) / 2 - 48, -1);
         PlayerClientInfo player = (PlayerClientInfo)PlayerUtils.getInfo(par1Minecraft.h);
         if (super.g == 0) {
            this.a(fontrenderer, "В наличии: " + player.getNewCaseValue(), this.d + this.b / 2 - 91, this.e + (this.c - 8) / 2 - 7, -1);
         }
      }
   }

   public void drawTexturedModalRect(double width, double weight, int minU, int minV, int maxU, int maxV, int textureSize, double scale) {
      double d = 1.0 / textureSize;
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(width + 0.0, weight + maxV * scale, super.n, (minU + 0) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + maxV * scale, super.n, (minU + maxU) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + 0.0, super.n, (minU + maxU) * d, (minV + 0) * d);
      tessellator.a(width + 0.0, weight + 0.0, super.n, (minU + 0) * d, (minV + 0) * d);
      tessellator.a();
   }
}
