package ru.stalcraft.blocks;

import ru.stalcraft.AnomalyDrop;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityCarousel;

public class BlockCarousel extends BlockExtendedAnomaly {
   public BlockCarousel(int par1, AnomalyDrop drop) {
      super(par1, StalkerMain.fakeAir, drop, "stalker:carousel", 0.05F);
   }

   public void a(abw world, int par2, int par3, int par4, nn par5Entity) {
      if (!world.I && par5Entity instanceof of && !par5Entity.ar()) {
         if (par5Entity instanceof uf && ((uf)par5Entity).bG.d) {
            return;
         }

         ((TileEntityCarousel)world.r(par2, par3, par4)).addTarget((of)par5Entity);
      }
   }

   public asp b(abw world) {
      return new TileEntityCarousel();
   }
}
