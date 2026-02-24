package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityEjectionSave;

public class BlockEjectionSave extends aqz implements aoe {
   public static boolean isBoxSave;

   public BlockEjectionSave(int par1) {
      super(par1, StalkerMain.fakeAir);
      this.a(StalkerMain.tab);
      this.c("ejectionsave");
      this.c(10000.0F);
      this.b(20000.0F);
      this.d("stalker:decor");
      GameRegistry.registerBlock(this, "Ejection Save");
   }

   public int a(Random par1) {
      return 0;
   }

   public boolean c() {
      return false;
   }

   public boolean isAirBlock(abw world, int x, int y, int z) {
      return true;
   }

   public asx c_(abw par1, int par2, int par3, int par4) {
      TileEntityEjectionSave par5 = (TileEntityEjectionSave)par1.r(par2, par3, par4);
      return atv.w().c.h() ? super.c_(par1, par2, par3, par4) : null;
   }

   public asx b(abw par1, int par2, int par3, int par4) {
      return null;
   }

   public asp b(abw par1) {
      return new TileEntityEjectionSave();
   }
}
