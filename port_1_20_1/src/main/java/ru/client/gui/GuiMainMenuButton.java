package ru.stalcraft.client.gui;

public class GuiMainMenuButton extends avk {
   public final int id;
   public GuiStalkerMenu parent;
   public int x;
   public int y;
   public int width;
   public int height;
   public int textureX;
   public int textureY;
   public bjo texture;
   public bjo highlightedTexture;

   public GuiMainMenuButton(int id, GuiStalkerMenu parent, int x, int y, int width, int height, int textureX, int textureY, bjo texture, bjo highlightedTexture) {
      this.id = id;
      this.parent = parent;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.textureX = textureX;
      this.textureY = textureY;
      this.texture = texture;
      this.highlightedTexture = highlightedTexture;
   }

   public void mouseClick(int mouseX, int mouseY, int button) {
      if (button == 0 && this.isMouseOver(mouseX, mouseY)) {
         this.parent.buttonClick(this);
      }
   }

   public void drawButton(int mouseX, int mouseY) {
      if (this.isMouseOver(mouseX, mouseY)) {
         atv.w().N.a(this.highlightedTexture);
      } else {
         atv.w().N.a(this.texture);
      }

      this.drawButtonScaled();
   }

   public void drawButtonScaled() {
      float f = 0.00390625F;
      float f1 = 0.00390625F;
      double scale = Math.min(this.parent.h / 500.0F, 1.0F);
      double x = this.parent.g - (20 + this.width) * scale;
      double y = this.y * scale;
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(x, y + this.height * scale, super.n, (this.textureX + 0) * f, (this.textureY + this.height) * f1);
      tessellator.a(x + this.width * scale, y + this.height * scale, super.n, (this.textureX + this.width) * f, (this.textureY + this.height) * f1);
      tessellator.a(x + this.width * scale, y, super.n, (this.textureX + this.width) * f, this.textureY * f1);
      tessellator.a(x, y, super.n, this.textureX * f, this.textureY * f1);
      tessellator.a();
   }

   private boolean isMouseOver(int mouseX, int mouseY) {
      double scale = Math.min(this.parent.h / 500.0F, 1.0F);
      double x = this.parent.g - (20 + this.width) * scale;
      double y = this.y * scale;
      return mouseX >= x && mouseX < x + this.width * scale && mouseY >= y && mouseY < y + this.height * scale;
   }
}
