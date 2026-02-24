package ru.stalcraft.tile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Constructor;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.particles.KisselParticleEmitter;

public abstract class TileEntityAnomaly extends asp implements IParticleEmmiter {
   public int lastEjectionId = -1;
   public Object particleEmitter;
   private boolean firstTick = true;
   public int reloadTime;
   public int tickTile;

   public boolean canUpdate() {
      return FMLCommonHandler.instance().getEffectiveSide().isClient();
   }

   public void h() {
      if (this.firstTick) {
         if (super.k.I) {
            this.addParticleEmitter();
         }

         this.firstTick = false;
      }
   }

   public static void damageEntityForce(of entity, nb source, float damage, boolean hurtEffect) {
      int savedResistantTime = entity.af;
      entity.af = 0;
      entity.a(source, damage);
      entity.af = savedResistantTime;
   }

   @SideOnly(Side.CLIENT)
   private boolean addParticleEmitter() {
      Class clazz = this.getEmitterClass();
      if (EffectsEngine.instance != null && clazz != null) {
         try {
            Constructor e = clazz.getConstructor(this.getClass());
            this.particleEmitter = e.newInstance(this);
            EffectsEngine.instance.addParticleEmitter(this.getParticleEmitter());
         } catch (Exception var31) {
            var31.printStackTrace();
         }

         return true;
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public ParticleEmitter getParticleEmitter() {
      if (this.particleEmitter == null) {
         this.addParticleEmitter();
         this.firstTick = false;
      }

      return (ParticleEmitter)this.particleEmitter;
   }

   @SideOnly(Side.CLIENT)
   protected abstract Class getEmitterClass();

   @SideOnly(Side.CLIENT)
   public void spawnActiveParticles() {
      if (this.particleEmitter != null) {
         ((KisselParticleEmitter)this.particleEmitter).spawnActiveParticles();
      }
   }

   public void addTarget(of entity) {
   }

   public void removeTarget() {
   }

   public of getTarget() {
      return null;
   }

   public boolean b(int par1, int par2) {
      if (par1 == 1) {
         nn entity = super.k.a(par2);
         if (entity instanceof of) {
            this.addTarget((of)entity);
         }

         return true;
      } else if (par1 == 2) {
         this.removeTarget();
         return true;
      } else {
         return super.b(par1, par2);
      }
   }

   @Override
   public double getPosX() {
      return super.l;
   }

   @Override
   public double getPosY() {
      return super.m;
   }

   @Override
   public double getPosZ() {
      return super.n;
   }

   @Override
   public abw getWorld() {
      return super.k;
   }
}
