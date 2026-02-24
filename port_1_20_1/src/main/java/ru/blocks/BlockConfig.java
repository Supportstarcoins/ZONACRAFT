package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityDecorative;

public class BlockConfig extends aqz implements aoe {
   public String model;
   public String icon;
   public boolean isCollided;
   public boolean isModelBlock;
   public int collidedRendererDistance;

   public BlockConfig(Class tileEntity, int id, String name, String model, String icon, boolean isModelBlock, boolean isCollided, int collidedRendererDistance) {
      super(id, akc.a);
      this.model = model;
      this.icon = icon;
      this.isCollided = isCollided;
      this.isModelBlock = isModelBlock;
      this.collidedRendererDistance = collidedRendererDistance;
      super.d("stalker:decor");
      super.r();
      super.c("UnLoc" + name);
      super.a(StalkerMain.tabDecor);
      GameRegistry.registerBlock(this, name + id);
      LanguageRegistry.addName(this, name);
      GameRegistry.registerTileEntity(tileEntity, "decor" + id + model + icon);
   }

   public asx c_(abw par1World, int par2, int par3, int par4) {
      return atv.w().c.h() ? asx.a().a(par2 + this.cM, par3 + this.cN, par4 + this.cO, par2 + this.cP, par3 + this.cQ, par4 + this.cR) : null;
   }

   public asx b(abw par1World, int par2, int par3, int par4) {
      return this.isCollided ? asx.a().a(par2 + this.cM, par3 + this.cN, par4 + this.cO, par2 + this.cP, par3 + this.cQ, par4 + this.cR) : null;
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
      return new TileEntityDecorative(this.collidedRendererDistance);
   }

   public void a(mt iconRegister) {
      super.cW = iconRegister.a("stalker:" + this.icon);
   }

   public void a(abw par1World, int par2, int par3, int par4, of par5EntityLivingBase, ye par6ItemStack) {
      super.a(par1World, par2, par3, par4, par5EntityLivingBase, par6ItemStack);
      int l1 = ls.c(par5EntityLivingBase.A * 4.0F / 360.0F + 0.5) & 3;
      par1World.b(par2, par3, par4, l1, 2);
   }
}
