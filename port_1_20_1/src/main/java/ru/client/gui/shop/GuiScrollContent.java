package ru.stalcraft.client.gui.shop;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.gui.GuiCustomScreen;

public class GuiScrollContent extends avk {
   private int x;
   private int y;
   private int scrollPosY;
   private int prevScrollPosY;
   private GuiCustomScreen parent;
   private GuiButtonSlider slider;
   private IGuiScrollable guiScrollable;

   public GuiScrollContent(GuiCustomScreen parent, IGuiScrollable guiScrollable, int x, int y) {
      guiScrollable.initScroll(x, y);
      this.slider = new GuiButtonSlider(
         parent,
         0,
         x + guiScrollable.getScrollWidth() / 2,
         y,
         15,
         guiScrollable.getScrollHeight() / 2 - 15,
         guiScrollable.getScrollTotalHeight(),
         guiScrollable.getScrollHeightPerPage()
      );
      this.parent = parent;
      this.guiScrollable = guiScrollable;
      this.x = x;
      this.y = y;
   }

   public void updateScroll() {
      this.slider.updateSlider();
   }

   public void mouseClicked(atv mc, int mouseX, int mouseY) {
      this.slider.mouseClicked(mc, mouseX, mouseY, 0);
   }

   public void mouseMovedOrUp(int x, int y, int action) {
      if (action == 0) {
         this.slider.a(x, y);
      }
   }

   public void drawScroll(atv mc, int mouseWidth, int mouseHeight) {
      awf scaled = new awf(mc.u, mc.d, mc.e);
      int width = this.guiScrollable.getScrollWidth();
      int height = this.guiScrollable.getScrollHeight();
      int scaledFactor = scaled.e();
      int scissorX = this.x;
      int scissorY = this.y;
      int guiScale = mc.u.al;
      if (scaledFactor != guiScale) {
         scissorX = this.x;
         scissorY = this.y;
         if (guiScale > 0) {
            width /= 2;
            height /= 2;
         } else if (scaledFactor != 1) {
            scissorX = this.x * scaledFactor;
            scissorY = this.y * scaledFactor;
            width *= 2;
            height *= 2;
         }
      } else {
         scissorX = this.x * scaledFactor;
         scissorY = this.y * scaledFactor;
         int scaledSize = scaledFactor - 2;
         if (scaledSize > 0) {
            width = (int)(width * 1.5F);
            height = (int)(height * 1.5F);
         } else if (scaledFactor == 1) {
            width /= 2;
            height /= 2;
         }
      }

      GL11.glPushMatrix();
      this.scrollPosY = -((int)(this.guiScrollable.getScrollSize() * this.slider.getPos()));
      GL11.glEnable(3089);
      GL11.glScissor(scissorX, mc.e - scissorY - height, width, height);
      this.guiScrollable.drawScrollable(mc, mouseWidth, mouseHeight, this.scrollPosY);
      GL11.glDisable(3089);
      GL11.glPopMatrix();
      this.slider.a(mc, mouseWidth, mouseHeight);
      this.guiScrollable.drawScreen(mc, mouseWidth, mouseHeight);
   }

   public void actionPerformed(aut button) {
      this.guiScrollable.actionPerformed(button);
   }

   public void drawPlayer() {
      ((GuiScrollSlotItem)this.guiScrollable).drawPlayer();
   }
}
