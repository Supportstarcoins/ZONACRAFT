package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityCampfire;

public class BlockCampfire extends aqz implements aoe {
   public BlockCampfire(int id) {
      super(id, akc.a);
      super.d("stalker:anomaly");
      this.a(StalkerMain.tab);
      this.r();
      LanguageRegistry.addName(this, "Костер");
      GameRegistry.registerBlock(this, "blockCampfire");
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

   public asp b(abw world) {
      return new TileEntityCampfire();
   }
}
