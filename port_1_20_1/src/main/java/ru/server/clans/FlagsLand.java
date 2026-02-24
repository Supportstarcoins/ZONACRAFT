package ru.stalcraft.server.clans;

import ru.stalcraft.clans.IFlagsLand;

public class FlagsLand implements IFlagsLand {
   public final int x1;
   public final int x2;
   public final int z1;
   public final int z2;
   public final int dimension;
   public final int rent;
   public final int maxRentTimer;

   public FlagsLand(int dimension, int x1, int x2, int z1, int z2, int rent, int maxRentTimer) {
      this.x1 = Math.min(x1, x2);
      this.x2 = Math.max(x1, x2);
      this.z1 = Math.min(z1, z2);
      this.z2 = Math.max(z1, z2);
      this.dimension = dimension;
      this.rent = rent;
      this.maxRentTimer = maxRentTimer;
   }

   public boolean isBlockInLand(int dimension, int x, int z) {
      return dimension == this.dimension && x >= this.x1 && x <= this.x2 && z >= this.z1 && z <= this.z2;
   }
}
