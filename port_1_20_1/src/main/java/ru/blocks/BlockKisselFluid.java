package ru.stalcraft.blocks;

import java.util.Random;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import ru.stalcraft.AnomalyDrop;
import ru.stalcraft.Config;
import ru.stalcraft.StalkerDamage;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.tile.TileEntityAnomaly;
import ru.stalcraft.tile.TileEntityKissel;

public class BlockKisselFluid extends BlockFluidClassic implements aoe {
   protected ms stillIcon;
   private AnomalyDrop drop;

   public BlockKisselFluid(int id, Fluid fluid) {
      super(id, fluid, new MaterialKissel());
      this.c("kisselFluid");
      this.a(StalkerMain.tab);
      super.quantaPerBlock = 0;
      super.cK = true;
      this.drop = new AnomalyDrop(Config.kisselDrop);
   }

   public ms a(int side, int meta) {
      return this.stillIcon;
   }

   public void a(mt register) {
      this.stillIcon = register.a("stalker:kissel_still");
   }

   public boolean canDisplace(acf world, int x, int y, int z) {
      return world.g(x, y, z).d() ? false : super.canDisplace(world, x, y, z);
   }

   public boolean displaceIfPossible(abw world, int x, int y, int z) {
      return world.g(x, y, z).d() ? false : super.displaceIfPossible(world, x, y, z);
   }

   public int getQuantaValue(acf world, int x, int y, int z) {
      return 1;
   }

   public int d() {
      return StalkerMain.kisselRenderId;
   }

   public void a(abw par1World, int par2, int par3, int par4, nn par5Entity) {
      if (!par1World.I && par1World.s.nextFloat() > 0.95F && par5Entity instanceof of && !par5Entity.ar()) {
         TileEntityAnomaly.damageEntityForce((of)par5Entity, StalkerDamage.kissel, Config.kisselDamage, true);
         par1World.a(par5Entity, "stalker:kissel_hit", 1.0F, 1.0F);
      }

      if (par1World.I && !par5Entity.ar() && par5Entity instanceof of) {
         ((TileEntityKissel)par1World.r(par2, par3, par4)).spawnActiveParticles();
      }
   }

   public asp b(abw world) {
      return new TileEntityKissel();
   }

   public void b(abw par1World, int par2, int par3, int par4, Random par5Random) {
      if (par1World.s.nextFloat() < 0.005F) {
         par1World.a(par2, par3, par4, "stalker:kissel", 0.5F + par1World.s.nextFloat() * 0.5F, 0.9F + par5Random.nextFloat() * 0.15F, false);
      }
   }

   public int getLightValue(acf world, int x, int y, int z) {
      return 11;
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
}
