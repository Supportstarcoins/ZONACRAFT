package ru.stalcraft.tile;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.server.ServerTicker;

public abstract class TileEntityExtendedAnomaly extends TileEntityAnomaly {
   public void w_() {
      super.w_();
      if (!super.k.I) {
         for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
               for (int z = -1; z < 2; z++) {
                  super.k.a(super.l + x, super.m + y, super.n + z);
                  if (super.k.a(super.l + x, super.m + y, super.n + z) == StalkerMain.anomalyNeighbor.cF) {
                     StalkerMain.anomalyNeighbor.checkIsValid(super.k, super.l + x, super.m + y, super.n + z);
                  }
               }
            }
         }
      }
   }

   public void s() {
      super.s();
      if (!super.k.I) {
         ServerTicker.anomaliesToCheck.add(this);
      }
   }

   public void addNeighborBlocks() {
      for (int x = -1; x < 2; x++) {
         for (int y = -1; y < 2; y++) {
            for (int z = -1; z < 2; z++) {
               if (x != 0 || y != 0 || z != 0) {
                  super.k.a(super.l + x, super.m + y, super.n + z);
                  if (super.k.a(super.l + x, super.m + y, super.n + z) == 0) {
                     super.k.c(super.l + x, super.m + y, super.n + z, StalkerMain.anomalyNeighbor.cF);
                  }
               }
            }
         }
      }
   }
}
