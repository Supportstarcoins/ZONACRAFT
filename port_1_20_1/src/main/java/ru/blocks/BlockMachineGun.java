package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.ArrayList;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.tile.TileEntityMachineGun;

public class BlockMachineGun extends aqz implements aoe {
   public BlockMachineGun(int id) {
      super(id, new MaterialMachineGun(ake.h));
      this.a(StalkerMain.tab);
      this.c("StalkerMachineGun");
      LanguageRegistry.addName(this, "Пулемет");
      this.c(3.0F);
      this.a(0.25F, 0.0F, 0.25F, 0.75F, 0.6F, 0.75F);
   }

   public boolean c() {
      return false;
   }

   public boolean b() {
      return false;
   }

   public boolean a_(acf par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   public asx b(abw par1World, int par2, int par3, int par4) {
      return null;
   }

   public void a(mt par1IconRegister) {
      super.cW = par1IconRegister.a("stalker:transparent");
   }

   public asp b(abw world) {
      return new TileEntityMachineGun();
   }

   public void g(abw par1World, int par2, int par3, int par4, int par5) {
      if (!par1World.I) {
         float f = 0.7F;
         ye stack = new ye(this);
         double d0 = par1World.s.nextFloat() * f + (1.0F - f) * 0.5;
         double d1 = par1World.s.nextFloat() * f + (1.0F - f) * 0.5;
         double d2 = par1World.s.nextFloat() * f + (1.0F - f) * 0.5;
         ss entityitem = new ss(par1World, par2 + d0, par3 + d1, par4 + d2, stack);
         entityitem.b = 10;
         par1World.d(entityitem);
      }
   }

   public ArrayList getBlockDropped(abw world, int x, int y, int z, int metadata, int fortune) {
      return new ArrayList();
   }

   public void a(abw world, int x, int y, int z, of entity, ye stack) {
      if (entity != null) {
         int direction = ls.c(entity.A * 4.0F / 360.0F + 0.5) & 3;
         world.b(x, y, z, direction, 3);
      }
   }

   public boolean a(abw par1World, int par2, int par3, int par4, uf par5EntityPlayer, int par6, float par7, float par8, float par9) {
      if (par1World.I) {
         ClientPacketSender.sendMachineGunShooter(par2, par3, par4);
      }

      return true;
   }
}
