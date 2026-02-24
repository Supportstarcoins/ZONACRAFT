package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.player.PlayerUtils;

public class BlockDebuff extends aqz implements aoe {
   public int effectID;
   public int level;
   public int tickTimeInc;

   public BlockDebuff(int blockID, int effectID, int effectLevel, int tickTimeIncrease) {
      super(blockID, StalkerMain.fakeAir);
      this.effectID = effectID;
      this.level = effectLevel;
      this.tickTimeInc = tickTimeIncrease;
      this.a(StalkerMain.tab);
      this.b(false);
      this.c(10000.0F);
      this.b(20000.0F);
      GameRegistry.registerTileEntity(TileEntityDebuff.class, "TileEntityDebuff" + blockID);
   }

   public void a(abw par1World, int par2, int par3, int par4, nn par5Entity) {
      if (par5Entity instanceof uf) {
         uf player = (uf)par5Entity;
         if (!player.q.I) {
            PlayerUtils.getInfo(player).cont.addEffect(this.effectID, this.tickTimeInc, this.level);
         }
      }
   }

   public int a(Random par1Random) {
      return 0;
   }

   public boolean c() {
      return false;
   }

   public asx c_(abw par1World, int par2, int par3, int par4) {
      return atv.w().c.h() ? asx.a().a(par2 + this.cM, par3 + this.cN, par4 + this.cO, par2 + this.cP, par3 + this.cQ, par4 + this.cR) : null;
   }

   public asx b(abw par1World, int par2, int par3, int par4) {
      return null;
   }

   public boolean isAirBlock(abw world, int x, int y, int z) {
      return true;
   }

   public void a(mt par1IconRegister) {
      super.cW = par1IconRegister.a("stalker:transparent");
   }

   public asp b(abw world) {
      return new TileEntityDebuff();
   }
}
