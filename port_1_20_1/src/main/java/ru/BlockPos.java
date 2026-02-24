package ru.stalcraft;

public class BlockPos {
   public final int x;
   public final int y;
   public final int z;
   public final int dimension;

   public BlockPos(abw w, int x, int y, int z) {
      this.dimension = w.t.i;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public BlockPos(int dimension, int x, int y, int z) {
      this.dimension = dimension;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public boolean equals(BlockPos pos) {
      return pos != null && pos.x == this.x && pos.y == this.y && pos.z == this.z && pos.dimension == this.dimension;
   }
}
