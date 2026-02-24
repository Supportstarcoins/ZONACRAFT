package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.proxy.IClientProxy;
import ru.stalcraft.tile.TileEntityBaseWarehouse;

public class BlockBaseWarehouse extends aqz implements aoe {
   public BlockBaseWarehouse(int id) {
      super(id, akc.a);
      super.c("BlockBaseWarehouse");
      super.a(StalkerMain.tab);
      super.d("stalker:decor");
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
      GameRegistry.registerBlock(this);
      GameRegistry.registerTileEntity(TileEntityBaseWarehouse.class, "TileEntityBaseWarehouse");
      LanguageRegistry.addName(this, "Блок склад базы(Геям не трогать)");
   }

   public void isClickF(abw world, int x, int y, int z) {
      if (world.I) {
         ClientPacketSender.sendOpenGuiBaseWarehouse(x, y, z);
      }
   }

   public boolean a(abw world, int par2, int par3, int par4, uf player, int par6, float par7, float par8, float par9) {
      if (world.I && player.bG.d) {
         IClientProxy proxy = (IClientProxy)StalkerMain.getProxy();
         proxy.onBaseWarehouseGui();
      }

      return true;
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

   public void a(abw world, int x, int y, int z, of entityLivingBase, ye itemStack) {
      super.a(world, x, y, z, entityLivingBase, itemStack);
      int rotation = ls.c(entityLivingBase.A * 4.0F / 360.0F + 0.5) & 3;
      world.b(x, y, z, rotation, 2);
   }

   public asp b(abw world) {
      return new TileEntityBaseWarehouse();
   }
}
