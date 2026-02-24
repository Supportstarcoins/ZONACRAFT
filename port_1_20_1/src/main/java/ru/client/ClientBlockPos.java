package ru.stalcraft.client;

public class ClientBlockPos {
   public final int x;
   public final int y;
   public final int z;
   public final int w;

   public ClientBlockPos(int w, int x, int y, int z) {
      this.w = w;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public boolean posEquals(ClientBlockPos pos) {
      return pos != null && pos.w == this.w && pos.x == this.x && pos.y == this.y && pos.z == this.z;
   }

   public abw getWorld() {
      return this.w == atv.w().f.t.i ? atv.w().f : null;
   }
}
