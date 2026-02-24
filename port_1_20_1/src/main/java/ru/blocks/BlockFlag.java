package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.proxy.IClientProxy;
import ru.stalcraft.tile.TileEntityFlag;

public class BlockFlag extends aqz implements aoe {
   public BlockFlag(int par1) {
      super(par1, new MaterialFlag(ake.e));
      this.c("block_flag");
      this.a(StalkerMain.tab);
      this.b(100000.0F);
      this.c(5.0F);
      LanguageRegistry.addName(this, "Флаг");
      this.d("stalker:transparent");
   }

   public int a(Random par1Random) {
      return 0;
   }

   public boolean c() {
      return false;
   }

   public asx b(abw par1World, int par2, int par3, int par4) {
      return null;
   }

   public void a(abw world, int x, int y, int z, of entity, ye par6ItemStack) {
      if (world.I) {
         world.c(x, y, z, 0);
      }
   }

   public boolean a(abw par1World, int par2, int par3, int par4, int par5, ye par6ItemStack) {
      if (par1World.I && !ClientProxy.clanData.thePlayerClan.equals("")) {
         IClientProxy proxy = (IClientProxy)StalkerMain.getProxy();
         proxy.onFlagGui(par2, par3, par4);
      }

      return super.a(par1World, par2, par3, par4, par5, par6ItemStack);
   }

   public void a(abw par1World, int par2, int par3, int par4, int par5, int par6) {
      super.a(par1World, par2, par3, par4, par5, par6);
      if (!par1World.I) {
         StalkerMain.flagManager.onBlockFlagRemoved(par1World.t.i, par2, par3, par4);
      }
   }

   public boolean a(abw par1World, int x, int y, int z, uf player, int par6, float par7, float par8, float par9) {
      if (!par1World.I) {
         StalkerMain.flagManager.tryJoinClanLand(player, x, y, z);
      }

      return true;
   }

   public asp b(abw world) {
      return new TileEntityFlag();
   }
}
