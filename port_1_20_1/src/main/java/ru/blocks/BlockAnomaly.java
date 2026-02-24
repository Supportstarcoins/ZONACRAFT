package ru.stalcraft.blocks;

import cpw.mods.fml.common.FMLCommonHandler;
import java.util.ArrayList;
import java.util.Random;
import ru.stalcraft.AnomalyDrop;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityAnomaly;

public abstract class BlockAnomaly extends aqz implements aoe {
   private AnomalyDrop drop;
   public String sound;
   private float soundChance;
   public static ArrayList anomalies = new ArrayList();

   public BlockAnomaly(int par1, akc par2Material, AnomalyDrop drop, String soundName, float soundChance) {
      super(par1, par2Material);
      this.a(StalkerMain.tab);
      this.r();
      super.cK = true;
      this.drop = drop;
      this.sound = soundName;
      this.soundChance = soundChance;
      anomalies.add(par1);
      if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
         this.a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      }
   }

   public void b(abw par1World, int par2, int par3, int par4, Random par5Random) {
      if (par1World.s.nextFloat() < this.soundChance) {
         par1World.a(par2, par3, par4, this.sound, 0.5F + par1World.s.nextFloat() * 0.5F, 0.9F + par5Random.nextFloat() * 0.15F, false);
      }
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

   public asx b(abw par1World, int par2, int par3, int par4) {
      return null;
   }

   public void a(mt par1IconRegister) {
      super.cW = par1IconRegister.a("stalker:anomaly");
   }

   public void a(abw par1World, int par2, int par3, int par4, Random par5Random) {
      if (!par1World.I) {
         TileEntityAnomaly tile = (TileEntityAnomaly)par1World.r(par2, par3, par4);
         if (tile.lastEjectionId < StalkerMain.getProxy().getEjectionManager().getLastEjectionId()) {
            this.dropItem(par1World, par2, par3, par4, this.drop);
            tile.lastEjectionId = StalkerMain.getProxy().getEjectionManager().getLastEjectionId();
         }
      }
   }

   public void dropItem(abw par1World, int par2, int par3, int par4, AnomalyDrop drop) {
      int droppedId = drop.getDrop(par1World.s);
      if (droppedId != 0 && (droppedId < 32000 && yc.g[droppedId] != null || droppedId < 4096 && aqz.s[droppedId] != null)) {
         float f = 0.5F;
         ye stack = new ye(droppedId, 1, 0);
         Random rand = par1World.s;
         ss entityitem = new ss(par1World, par2 + rand.nextFloat(), par3 + rand.nextFloat(), par4 + rand.nextFloat(), stack);
         entityitem.g((rand.nextFloat() - 0.5F) * f, rand.nextFloat() * f / 2.0F, (rand.nextFloat() - 0.5F) * f);
         entityitem.b = 10;
         par1World.d(entityitem);
      }
   }

   public boolean addBlockDestroyEffects(abw world, int x, int y, int z, int meta, beh effectRenderer) {
      return true;
   }

   public boolean addBlockHitEffects(abw worldObj, ata target, beh effectRenderer) {
      return true;
   }

   public boolean isAirBlock(abw world, int x, int y, int z) {
      return true;
   }
}
