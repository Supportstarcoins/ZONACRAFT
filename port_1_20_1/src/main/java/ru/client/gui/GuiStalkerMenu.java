package ru.stalcraft.client.gui;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class GuiStalkerMenu extends blt {
   private static bjo background = new bjo("stalker", "textures/background.png");
   private static bjo buttonTexture1 = new bjo("stalker", "textures/menu_buttons1.png");
   private static bjo buttonTexture2 = new bjo("stalker", "textures/menu_buttons2.png");
   private static bjo buttonTexture3 = new bjo("stalker", "textures/menu_buttons3.png");
   private static bjo buttonTexture4 = new bjo("stalker", "textures/menu_buttons4.png");
   private List customButtons = new ArrayList();

   public void A_() {
      super.A_();
      super.f.u.al = 2;
      super.i.clear();
      this.customButtons.clear();
      this.customButtons.add(new GuiMainMenuButton(0, this, super.g - 20 - 197, 25, 197, 69, 0, 0, buttonTexture1, buttonTexture3));
      this.customButtons.add(new GuiMainMenuButton(1, this, super.g - 20 - 156, 119, 156, 69, 0, 128, buttonTexture1, buttonTexture3));
      this.customButtons.add(new GuiMainMenuButton(2, this, super.g - 20 - 71, 213, 71, 69, 0, 0, buttonTexture2, buttonTexture4));
      this.customButtons.add(new GuiMainMenuButton(3, this, super.g - 20 - 94, 307, 94, 69, 0, 128, buttonTexture2, buttonTexture4));
   }

   public void a(int par1, int par2, float par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.f.N.a(background);
      bfq tessellator = bfq.a;
      tessellator.b();
      double minX = 0.0;
      double minY = 0.0;
      double maxX = 1.0;
      double maxY = 1.0;
      double screenRatio = (double)super.g / super.h;
      double textureRatio = 1.7777777777777777;
      if (screenRatio > textureRatio) {
         minY = (1.0 - textureRatio / screenRatio) / 2.0;
         maxY = 1.0 - (1.0 - textureRatio / screenRatio) / 2.0;
      } else if (textureRatio > screenRatio) {
         minX = (1.0 - screenRatio / textureRatio) / 2.0;
         maxX = 1.0 - (1.0 - screenRatio / textureRatio) / 2.0;
      }

      tessellator.a(0.0, super.h, super.n, minX, maxY);
      tessellator.a(super.g, super.h, super.n, maxX, maxY);
      tessellator.a(super.g, 0.0, super.n, maxX, minY);
      tessellator.a(0.0, 0.0, super.n, minX, minY);
      tessellator.a();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);

      for (GuiMainMenuButton button : this.customButtons) {
         button.drawButton(par1, par2);
      }

      GL11.glDisable(3042);
   }

   public void a(char par1, int par2) {
      super.a(par1, par2);
   }

   public void a(int x, int y, int buttonId) {
      for (GuiMainMenuButton button : this.customButtons) {
         button.mouseClick(x, y, buttonId);
      }
   }

   public void buttonClick(GuiMainMenuButton button) {
      if (button.id == 0) {
         super.f.a(new awh(this));
      } else if (button.id == 1) {
         super.f.a(new avw(this, super.f.u));
      } else if (button.id == 2) {
         try {
            Class throwable = Class.forName("java.awt.Desktop");
            Object object = throwable.getMethod("getDesktop").invoke(null);
            throwable.getMethod("browse", URI.class).invoke(object, new URI("https://vk.com/umpscw"));
         } catch (Throwable var41) {
            var41.printStackTrace();
         }
      } else if (button.id == 3) {
         super.f.f();
      }
   }
}
