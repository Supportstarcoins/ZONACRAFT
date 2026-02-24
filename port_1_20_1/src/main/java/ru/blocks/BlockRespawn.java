package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TilEntityRespawn;

public class BlockRespawn extends aqz implements aoe {
   public BlockRespawn(int par1) {
      super(par1, StalkerMain.fakeAir);
      this.c("block_respawn");
      this.a(StalkerMain.tab);
      this.r();
      this.a(1.0F);
      LanguageRegistry.addName(this, "Точка сохранения");
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.001F, 1.0F);
      this.k(0);
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

   public void a(mt par1IconRegister) {
      super.cW = par1IconRegister.a("stalker:respawn");
   }

   public asp b(abw world) {
      return new TilEntityRespawn();
   }
}
