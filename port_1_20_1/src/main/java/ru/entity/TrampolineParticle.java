package ru.stalcraft.entity;

import ru.stalcraft.tile.TileEntityTrampoline;

public class TrampolineParticle extends beg {
   private final double startY;
   private TileEntityTrampoline tileEntity;
   private final int xTile;
   private final int yTile;
   private final int zTile;

   public TrampolineParticle(abw par1World, int x, int y, int z) {
      super(par1World, x + Math.random(), y, z + Math.random());
      super.x = super.y = super.z = 0.0;
      this.startY = super.v;
      this.xTile = x;
      this.yTile = y;
      this.zTile = z;
      this.tileEntity = (TileEntityTrampoline)par1World.r(x, y, z);
      super.aw = 0.7F;
      super.i = 0.0F;
      super.j = 0.87058824F;
      super.au = 0.72156864F;
      super.av = 0.5294118F;
      super.Z = true;
      this.i((int)(Math.random() * 8.0));
      super.h = 0.5F;
   }

   public void l_() {
      super.r = super.u;
      super.s = super.v;
      super.t = super.w;
      if (this.tileEntity == super.q.r(this.xTile, this.yTile, this.zTile) && this.tileEntity != null) {
         if (super.y == 0.0 && Math.random() > 0.99) {
            super.y = Math.random() / 4.0;
         }

         super.y -= 0.05;
         super.v = Math.max(this.startY, super.v + super.y);
         if (super.v <= this.startY) {
            super.y = 0.0;
            super.v = this.startY;
         }
      } else {
         this.x();
      }
   }
}
