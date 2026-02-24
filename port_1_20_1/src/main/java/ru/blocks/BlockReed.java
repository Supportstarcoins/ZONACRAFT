package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityReed;

public class BlockReed extends aqz implements aoe {
   public BlockReed(int id) {
      super(id, akc.F);
      super.a(StalkerMain.tab);
      super.d("stalker:decor");
      super.c("reedBlock");
      LanguageRegistry.addName(this, "Камыш");
      GameRegistry.registerBlock(this, "ReedBlock");
   }

   public int a(Random random) {
      return 0;
   }

   public boolean c() {
      return false;
   }

   public asx b(abw par1World, int par2, int par3, int par4) {
      return null;
   }

   public asp b(abw world) {
      return new TileEntityReed();
   }
}
