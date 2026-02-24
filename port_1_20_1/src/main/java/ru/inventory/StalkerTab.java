package ru.stalcraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StalkerTab extends ww {
   public StalkerTab(String tabName) {
      super(tabName);
   }

   public String c() {
      return this.b();
   }

   @SideOnly(Side.CLIENT)
   public yc d() {
      return yc.n;
   }
}
