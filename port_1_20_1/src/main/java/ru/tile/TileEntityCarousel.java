package ru.stalcraft.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import ru.stalcraft.Config;
import ru.stalcraft.StalkerDamage;
import ru.stalcraft.client.particles.CarouselParticleEmitter;
import ru.stalcraft.items.EntityBolt;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.network.ServerPacketSender;

public class TileEntityCarousel extends TileEntityExtendedAnomaly implements IPlayerQuitListener {
   public of target;
   private boolean isSoundActive;
   private String activeSound = "stalker:carousel_active";
   private static Random rand = new Random();
   public int activeTimer = -1;
   public int ticksSinceActive;

   @Override
   public void h() {
      super.h();
      if (super.k.I) {
         this.ticksSinceActive++;
      } else if (this.activeTimer == -1) {
         return;
      }

      for (nn entity : this.az().a(nn.class, asx.a(this.l - 1.5, this.m - 1.5, this.n - 1.5, this.l + 1.5, this.m + 1.5, this.n + 1.5))) {
         if (entity instanceof EntityBolt) {
            if (this.k.I) {
               ((CarouselParticleEmitter)super.particleEmitter).spawnSplashBoltParticle((EntityBolt)entity);
            } else {
               entity.x();
            }
         }
      }

      for (nn entityx : this.az().a(nn.class, asx.a(this.l - 1.5, this.m - 1.5, this.n - 1.5, this.l + 1.5, this.m + 1.5, this.n + 1.5))) {
         if (entityx instanceof EntityBolt && !this.k.I) {
            entityx.x();
         }
      }

      if (this.activeTimer != -1) {
         this.activeTimer++;
         if (this.target != null && !super.k.I && this.target.q != super.k && this.target.M) {
            this.removeTarget();
         } else if (this.target == null || super.k.I || !(this.target.aN() <= 0.0F) && !(this.target.e(super.l + 0.5, super.m + 0.5, super.n + 0.5) > 25.0)) {
            if (this.target != null && this.target instanceof uf && ((uf)this.target).bG.d) {
               this.removeTarget();
            } else if (this.target != null) {
               atc motionVec = atc.a(super.l + 0.5 - this.target.u, super.m + 5 - this.target.v, super.n + 0.5 - this.target.w).a();
               float velocity = 0.012F + this.activeTimer / 9000.0F;
               this.target.A = this.target.A + ((int)(this.activeTimer / 40.0F) + 3);
               double x = Math.abs(motionVec.c * velocity) > Math.abs(super.l + 0.5 - this.target.u) ? super.l + 0.5 - this.target.u : motionVec.c * velocity;
               double y = Math.min(motionVec.d * velocity, super.m + 3.38 - this.target.v + this.target.N);
               double z = Math.abs(motionVec.e * velocity) > Math.abs(super.n + 0.5 - this.target.w) ? super.n + 0.5 - this.target.w : motionVec.e * velocity;
               this.target.g(x, y - this.target.y, z);
               if (this.activeTimer >= 120 && !this.az().I) {
                  TileEntityAnomaly.damageEntityForce(this.target, StalkerDamage.carousel, Config.carouselDamage, true);
                  this.removeTarget();
                  this.reloadTime = 80;
               }
            }
         } else {
            this.removeTarget();
         }

         if (this.activeTimer >= 300) {
            this.removeTarget();
         }
      }
   }

   @SideOnly(Side.CLIENT)
   private void spawnSplashParticle() {
      ((CarouselParticleEmitter)super.particleEmitter).spawnSplashParticle(this.target);
   }

   @Override
   public void addTarget(of entity) {
      if (this.target == null && this.reloadTime == 0 && this.activeTimer == -1 && !entity.M && entity.aN() > 0.0F && !entity.ar() || super.k.I) {
         if (!super.k.I) {
            ServerPacketSender.sendTileEntityEvent(this, 1, entity.k);
            if (entity instanceof uf) {
               PlayerUtils.getInfo((uf)entity).quitListeners.add(this);
            }
         } else {
            if (super.particleEmitter != null) {
               this.getParticleEmitter().reset();
            }

            this.playSound();
         }

         this.target = entity;
         this.activeTimer = 0;
      }
   }

   @SideOnly(Side.CLIENT)
   private void playSound() {
      float var10002 = super.l + 0.5F;
      float var10003 = super.m + 0.5F;
      atv.w().v.a(this.activeSound, var10002, var10003, super.n + 0.5F, 1.0F, 1.0F);
   }

   @Override
   public void removeTarget() {
      if (!super.k.I) {
         ServerPacketSender.sendTileEntityEvent(this, 2, 0);
      } else if (this.target != null && this.target.aN() <= 0.0F && super.k.I) {
         this.spawnSplashParticle();
      }

      this.activeTimer = -1;
      this.ticksSinceActive = 0;
      this.target = null;
   }

   @Override
   public boolean canUpdate() {
      return true;
   }

   @Override
   public void onPlayerExit() {
      if (this.target != null) {
         TileEntityAnomaly.damageEntityForce(this.target, StalkerDamage.carousel, 1.0E7F, true);
      }
   }

   @Override
   protected Class getEmitterClass() {
      return CarouselParticleEmitter.class;
   }

   @Override
   public of getTarget() {
      return this.target;
   }
}
