package ru.stalcraft.client.gui.shop;

public class GuiButtonTab extends aut {
   public boolean select = false;

   public GuiButtonTab(int par1, int par2, int par3, int par4, int par5, String par6Str) {
      super(par1, par2, par3, par4, par5, par6Str);
   }

   protected int a(boolean par1) {
      byte b0 = 1;
      if (!this.h) {
         b0 = 0;
      } else if (this.select) {
         b0 = 2;
      } else if (par1) {
         b0 = 2;
      }

      return b0;
   }
}
