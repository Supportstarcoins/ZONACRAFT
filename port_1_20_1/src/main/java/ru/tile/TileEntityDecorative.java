package ru.stalcraft.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityDecorative extends asp {
   public int collidedRendererDistance;

   public TileEntityDecorative(int collidedRendererDistance) {
      this.collidedRendererDistance = collidedRendererDistance;
   }

   @SideOnly(Side.CLIENT)
   public asx getRenderBoundingBox() {
      return asx.a()
         .a(
            this.l - this.collidedRendererDistance,
            this.m - this.collidedRendererDistance,
            this.n - this.collidedRendererDistance,
            this.l + this.collidedRendererDistance,
            this.m + this.collidedRendererDistance,
            this.n + this.collidedRendererDistance
         );
   }
}
