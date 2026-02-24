package ru.stalcraft.client.gui.shop;

import org.lwjgl.input.Mouse;
import ru.stalcraft.client.gui.GuiCustomScreen;

public class GuiButtonSlider extends aut {
   public bjo buttons = new bjo("stalker", "textures/buttons.png");
   private final GuiCustomScreen parent;
   private float pos = 0.0F;
   private boolean mouseDown;
   private int pressY;
   private int totalHeight;
   private int heightPerPage;
   private boolean dragging = false;

   public GuiButtonSlider(GuiCustomScreen parent, int id, int x, int y, int width, int height, int totalHeight, int heightPerPage) {
      super(id, x, y, width, height, "");
      this.parent = parent;
      this.totalHeight = totalHeight;
      this.heightPerPage = heightPerPage;
   }

   public void updateSlider() {
      super.h = this.heightPerPage < this.totalHeight;
      if (!super.h) {
         this.pos = 0.0F;
      }
   }

   public void a(atv mc, int mouseWidth, int mouseHeight) {
      if (super.i && super.h) {
         if (this.dragging) {
            this.pos = (mouseHeight - super.e - 5.0F) / ((float)(super.e + super.c + 16) - super.e - 10.0F);
         } else {
            int i = Mouse.getDWheel();
            int j = 4 + this.totalHeight;
            if (i != 0 && this.c(mc, mouseWidth, mouseHeight)) {
               if (i > 0) {
                  i = 1;
               }

               if (i < 0) {
                  i = -1;
               }

               this.pos = (float)(this.pos - (double)i / j);
            }
         }

         if (this.pos < 0.0F) {
            this.pos = 0.0F;
         }

         if (this.pos > 1.0F) {
            this.pos = 1.0F;
         }

         mc.N.a(this.buttons);
         int currentY = super.e + (int)(super.c * this.pos);
         if (this.mouseDown) {
            this.parent.drawTexturedModalRect((int)(super.d - 0.8), currentY, 36, 240, 16, 30, 512, 0.5);
         } else if (mouseWidth >= super.d && mouseWidth < super.d + super.b && mouseHeight >= currentY && mouseHeight < currentY + 16) {
            this.parent.drawTexturedModalRect((int)(super.d - 0.8), currentY, 18, 240, 16, 30, 512, 0.5);
         } else {
            this.parent.drawTexturedModalRect((int)(super.d - 0.8), currentY, 0, 240, 16, 30, 512, 0.5);
         }
      }
   }

   public void mouseClicked(atv mc, int mouseWidth, int mouseHeight, int button) {
      if (this.c(mc, mouseWidth, mouseHeight)) {
         this.dragging = true;
      }
   }

   public void keyTyped(char par1, int par2) {
   }

   public void a(int par1, int par2) {
      this.dragging = false;
   }

   public boolean c(atv par1Minecraft, int par2, int par3) {
      return this.h && this.i && par2 >= this.d && par3 >= this.e && par2 < this.d + this.b && par3 < super.e + super.c + 16;
   }

   public float getPos() {
      return this.pos;
   }
}
