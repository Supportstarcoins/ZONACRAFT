package ru.stalcraft.client.gui.clans;

public class GuiElementScrollButton extends GuiElement {
   private final GuiClans parent;
   private final GuiElementSlider scroll;
   private final GuiElementScrollButton.ScrollButtonDirection direction;
   private final int x;
   private final int y;
   private final int xSize;
   private final int ySize;
   private int ticksUsed = -1;

   public GuiElementScrollButton(
      GuiClans parent, GuiElementSlider scroll, GuiElementScrollButton.ScrollButtonDirection direction, int x, int y, int xSize, int ySize
   ) {
      this.parent = parent;
      this.scroll = scroll;
      this.direction = direction;
      this.x = x;
      this.y = y;
      this.xSize = xSize;
      this.ySize = ySize;
   }

   void draw(int x, int y) {
      atv.w().N.a(GuiClans.buttonsTexture);
      int yPosOnTexture = this.direction == GuiElementScrollButton.ScrollButtonDirection.TOP ? 270 : 290;
      if (this.ticksUsed >= 0) {
         this.parent.drawTexturedModalRect(this.x, this.y, 36, yPosOnTexture, this.xSize, this.ySize, 512);
      } else if (x >= this.x && x < this.x + this.xSize && y >= this.y && y < this.y + this.ySize) {
         this.parent.drawTexturedModalRect(this.x, this.y, 18, yPosOnTexture, this.xSize, this.ySize, 512);
      } else {
         this.parent.drawTexturedModalRect(this.x, this.y, 0, yPosOnTexture, this.xSize, this.ySize, 512);
      }
   }

   void mouseClicked(int x, int y, int button) {
      if (button == 0 && x >= this.x && x < this.x + this.xSize && y >= this.y && y < this.y + this.ySize) {
         this.scroll();
         this.ticksUsed = 0;
      }
   }

   void mouseUp(int x, int y) {
      this.ticksUsed = -1;
   }

   void updateScreen() {
      if (this.ticksUsed >= 0) {
         this.ticksUsed++;
      }

      if (this.ticksUsed > 5) {
         this.scroll();
      }
   }

   private void scroll() {
      if (this.direction == GuiElementScrollButton.ScrollButtonDirection.TOP) {
         this.scroll.scrollUp(1);
      } else if (this.direction == GuiElementScrollButton.ScrollButtonDirection.BOTTOM) {
         this.scroll.scrollDown(1);
      }
   }

   static enum ScrollButtonDirection {
      TOP("TOP", 0),
      BOTTOM("BOTTOM", 1);

      private static final GuiElementScrollButton.ScrollButtonDirection[] $VALUES = new GuiElementScrollButton.ScrollButtonDirection[]{TOP, BOTTOM};

      private ScrollButtonDirection(String var1, int var2) {
      }
   }
}
