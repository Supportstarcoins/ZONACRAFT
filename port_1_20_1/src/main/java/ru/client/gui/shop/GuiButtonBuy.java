package ru.stalcraft.client.gui.shop;

import org.lwjgl.input.Mouse;

public class GuiButtonBuy extends aut {
   public GuiSlotItem guiSlotItem;
   public int click;

   public GuiButtonBuy(int par1, int par2, int par3, int par4, int par5, String par6Str, GuiSlotItem guiSlotItem) {
      super(par1, par2, par3, par4, par5, par6Str);
      this.guiSlotItem = guiSlotItem;
   }

   public void update(int mouseX, int mouseY) {
      boolean isMouse = mouseX >= this.d && mouseY >= this.e && mouseX < this.d + this.b && mouseY < this.e + this.c && Mouse.isButtonDown(0);
      if (!Mouse.isButtonDown(0)) {
         this.click = 0;
      }

      if (isMouse && this.click <= 0) {
         this.guiSlotItem.setBuy();
         this.click = 1;
      }
   }
}
