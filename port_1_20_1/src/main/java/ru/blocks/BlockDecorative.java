package ru.stalcraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.Random;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityDecorative;

public class BlockDecorative extends aqz implements aoe {
   public String model;
   public boolean isSpectater;
   public boolean isCollided;

   public BlockDecorative(int id, String name, String model, float lightValue, boolean isSpectater, boolean isCollided) {
      super(id, akc.f);
      this.model = model;
      this.isSpectater = isSpectater;
      this.isCollided = isCollided;
      super.a(StalkerMain.tab);
      super.c(model + id);
      super.d("stalker:decor");
      super.a(lightValue);
      LanguageRegistry.addName(this, name);
      GameRegistry.registerBlock(this, name + id);
      GameRegistry.registerTileEntity(TileEntityDecorative.class, "TileEntityDecorative" + id);
   }

   public asx c_(abw par1World, int par2, int par3, int par4) {
      return atv.w().c.h() ? asx.a().a(par2 + this.cM, par3 + this.cN, par4 + this.cO, par2 + this.cP, par3 + this.cQ, par4 + this.cR) : null;
   }

   public asx b(abw par1World, int par2, int par3, int par4) {
      return this.isCollided ? asx.a().a(par2 + this.cM, par3 + this.cN, par4 + this.cO, par2 + this.cP, par3 + this.cQ, par4 + this.cR) : null;
   }

   public int a(Random par1Random) {
      return 0;
   }

   public boolean c() {
      return false;
   }

   public asp b(abw world) {
      return new TileEntityDecorative(1);
   }
}
