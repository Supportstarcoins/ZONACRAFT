package ru.stalcraft.blocks;

import ru.stalcraft.AnomalyDrop;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityElectra;

public class BlockElectra extends BlockExtendedAnomaly {
   public int w = 10;

   public BlockElectra(int par1, AnomalyDrop drop) {
      super(par1, StalkerMain.fakeAir, drop, "stalker:electra", 0.07F);
   }

   public asp b(abw world) {
      return new TileEntityElectra();
   }

   public int getLightValue(acf world, int x, int y, int z) {
      return this.w;
   }
}
