package ru.stalcraft.blocks;

import java.util.Random;

public class BlockAnomalyNeighbor extends aqz {
   public BlockAnomalyNeighbor(int par1) {
      super(par1, akc.a);
      this.r();
      this.a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   public void a(abw par1World, int par2, int par3, int par4, nn par5Entity) {
      for (int x = -1; x < 2; x++) {
         for (int y = -1; y < 2; y++) {
            for (int z = -1; z < 2; z++) {
               int blockId = par1World.a(par2 + x, par3 + y, par4 + z);
               if (BlockExtendedAnomaly.isExtendedAnomaly(blockId)) {
                  aqz.s[blockId].a(par1World, par2 + x, par3 + y, par4 + z, par5Entity);
               }
            }
         }
      }
   }

   public int d() {
      return -1;
   }

   public int a(Random par1Random) {
      return 0;
   }

   public boolean c() {
      return false;
   }

   public void checkIsValid(abw w, int blockX, int blockY, int blockZ) {
      for (int x = -1; x < 2; x++) {
         for (int y = -1; y < 2; y++) {
            for (int z = -1; z < 2; z++) {
               int blockId = w.a(blockX + x, blockY + y, blockZ + z);
               if (BlockExtendedAnomaly.isExtendedAnomaly(blockId)) {
                  return;
               }
            }
         }
      }

      w.c(blockX, blockY, blockZ, 0);
   }

   public asx b(abw par1World, int par2, int par3, int par4) {
      return null;
   }

   public void a(mt par1IconRegister) {
      super.cW = par1IconRegister.a("stalker:transparent");
   }

   public boolean isAirBlock(abw world, int x, int y, int z) {
      return true;
   }
}
