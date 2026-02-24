package ru.stalcraft.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityBaseWarehouse extends asp {
   @SideOnly(Side.CLIENT)
   public asx getRenderBoundingBox() {
      return asx.a().a(this.l - 2, this.m - 2, this.n - 2, this.l + 2, this.m + 2, this.n + 2);
   }
}
