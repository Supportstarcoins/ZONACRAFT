package ru.stalcraft.client.gui.clans;

import org.lwjgl.input.Mouse;

public class GuiElementSlider extends GuiElement {
   private final GuiClans parent;
   private final IScrollable content;
   private final int x;
   private final int topY;
   private final int bottomY;
   private final int xSize;
   private final int ySize;
   float pos = 0.0F;
   private boolean mouseDown;
   private int pressY;

   public GuiElementSlider(GuiClans parent, IScrollable content, int x, int topY, int bottomY, int xSize, int ySize) {
      this.parent = parent;
      this.content = content;
      this.x = x;
      this.topY = topY;
      this.bottomY = bottomY;
      this.xSize = xSize;
      this.ySize = ySize;
   }

   void draw(int x, int y) {
      if (this.enabled()) {
         if (this.mouseDown) {
            this.mouseMove(Mouse.getX(), atv.w().e - Mouse.getY());
         }

         atv.w().N.a(GuiClans.buttonsTexture);
         int curY = this.getCurrentY();
         if (this.mouseDown) {
            this.parent.drawTexturedModalRect(this.x, curY, 36, 240, this.xSize, this.ySize, 512);
         } else if (x >= this.x && x < this.x + this.xSize && y >= curY && y < curY + this.ySize) {
            this.parent.drawTexturedModalRect(this.x, curY, 18, 240, this.xSize, this.ySize, 512);
         } else {
            this.parent.drawTexturedModalRect(this.x, curY, 0, 240, this.xSize, this.ySize, 512);
         }
      }
   }

   void mouseClicked(int x, int y, int button) {
      if (button == 0 && this.enabled() && x >= this.x && x < this.x + this.xSize && y >= this.topY && y <= this.bottomY + this.ySize) {
         int currentY = this.getCurrentY();
         if (y >= currentY && y < currentY + this.ySize) {
            this.mouseDown = true;
            this.pressY = atv.w().e - Mouse.getY() - currentY;
         } else if (y < currentY) {
            this.scrollUp(10);
         } else {
            this.scrollDown(10);
         }
      }
   }

   void mouseUp(int x, int y) {
      this.mouseDown = false;
      this.pressY = 0;
   }

   void mouseMove(int x, int y) {
      if (this.enabled() && this.mouseDown) {
         this.pos = (float)(y - this.pressY - this.topY) / (this.bottomY - this.topY);
         if (this.pos < 0.0F) {
            this.pos = 0.0F;
         }

         if (this.pos > 1.0F) {
            this.pos = 1.0F;
         }
      }
   }

   void keyTyped(char par1, int par2) {
      if (this.enabled()) {
         if (par2 == 201) {
            this.scrollUp(15);
         } else if (par2 == 209) {
            this.scrollDown(15);
         }
      }
   }

   private boolean enabled() {
      return this.content.getHeightPerPage() < this.content.getTotalHeight();
   }

   private int getCurrentY() {
      return !this.enabled() ? 0 : Math.min(this.topY + Math.round((this.bottomY - this.topY) * this.pos), this.bottomY);
   }

   void updateScreen() {
      if (!this.enabled()) {
         this.pos = 0.0F;
      }
   }

   void scrollUp(int weight) {
      if (this.enabled() && this.content.getTotalHeight() > this.content.getHeightPerPage()) {
         this.pos = Math.max(0.0F, this.pos - (float)(this.content.getMinScroll() * weight) / (this.content.getTotalHeight() - this.content.getHeightPerPage()));
      }
   }

   void scrollDown(int weight) {
      if (this.enabled() && this.content.getTotalHeight() > this.content.getHeightPerPage()) {
         this.pos = Math.min(1.0F, this.pos + (float)(this.content.getMinScroll() * weight) / (this.content.getTotalHeight() - this.content.getHeightPerPage()));
      }
   }
}
