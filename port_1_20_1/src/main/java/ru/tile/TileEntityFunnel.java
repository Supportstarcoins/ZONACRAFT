package ru.stalcraft.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeHooks;
import ru.stalcraft.StalkerDamage;
import ru.stalcraft.client.particles.FunnelParticleEmitter;
import ru.stalcraft.items.EntityBolt;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.network.ServerPacketSender;

public class TileEntityFunnel extends TileEntityAnomaly implements IPlayerQuitListener {
   public of target;
   private boolean isSoundActive;
   private String activeSound = "stalker:blackhole_active";
   private static Random rand = new Random();
   public int activeTimer = -1;

   @Override
   public void h() {
      super.h();

      for (nn entity : this.az().a(nn.class, asx.a(this.l - 2.5, this.m - 2.5, this.n - 2.5, this.l + 2.5, this.m + 2.5, this.n + 2.5))) {
         if (entity instanceof EntityBolt) {
            if (this.k.I) {
               ((FunnelParticleEmitter)super.particleEmitter).spawnSplashBoltParticle((EntityBolt)entity);
            } else {
               entity.x();
            }
         }
      }

      for (of par2 : this.k.a(of.class, asx.a().a(this.l - 2.5, this.m - 2.5, this.n - 2.5, this.l + 2.5, this.m + 2.5, this.n + 2.5))) {
         if (!this.k.I) {
            if (par2 instanceof uf && !((uf)par2).bG.d) {
               this.addTarget(par2);
            } else if (par2 instanceof of && !(par2 instanceof uf)) {
               this.addTarget(par2);
            }
         }
      }

      if (this.activeTimer != -1) {
         this.activeTimer++;
         if (this.activeTimer == 119 && super.k.I) {
            this.activeTimer--;
         }

         if (this.target != null && !super.k.I && this.target.q != super.k) {
            this.removeTarget();
         } else if (this.target != null && this.target instanceof uf && ((uf)this.target).bG.d) {
            this.removeTarget();
         } else if (this.target != null && !super.k.I && this.target.e(super.l + 0.5, super.m + 0.5, super.n + 0.5) > 25.0) {
            this.removeTarget();
         } else if (this.target != null) {
            atc motionVec = atc.a(super.l + 0.5 - this.target.u, super.m + 5 - this.target.v, super.n + 0.5 - this.target.w).a();
            float velocity = 0.05F + this.activeTimer / 2400.0F;
            double x = Math.abs(motionVec.c * velocity) > Math.abs(super.l + 0.5 - this.target.u) ? super.l + 0.5 - this.target.u : motionVec.c * velocity;
            double y = Math.min(motionVec.d * velocity, super.m + 3.38 - this.target.v + this.target.N);
            double z = Math.abs(motionVec.e * velocity) > Math.abs(super.n + 0.5 - this.target.w) ? super.n + 0.5 - this.target.w : motionVec.e * velocity;
            this.target.g(x, y - this.target.y, z);
            if (this.activeTimer >= 120 && this.target != null && !super.k.I) {
               this.doActivation(false);
            }
         }

         if (this.activeTimer == 200) {
            if (super.particleEmitter != null) {
               this.getParticleEmitter().reset();
            }

            this.activeTimer = -1;
         }
      }
   }

   public void doActivation(boolean exit) {
      if (!super.k.I) {
         ServerPacketSender.sendTileEntityEvent(this, 3, 0);
         if (this.target instanceof uf) {
            jv playerMP = (jv)this.target;
            if (!exit) {
               PlayerUtils.getInfo(playerMP).quitListeners.remove(this);
            }

            if (!playerMP.c.d() && ForgeHooks.onLivingAttack(this.target, StalkerDamage.blackhole, 1000000.0F)) {
               ArrayList stacks = new ArrayList();

               for (ye item : playerMP.bn.a) {
                  if (item != null) {
                     stacks.add(item);
                  }
               }

               for (ye itemx : playerMP.bn.b) {
                  if (itemx != null) {
                     stacks.add(itemx);
                  }
               }

               for (ye itemxx : PlayerUtils.getInfo(playerMP).stInv.mainInventory) {
                  if (itemxx != null) {
                     stacks.add(itemxx);
                  }
               }

               playerMP.bn.b(-1, -1);
               PlayerUtils.getInfo(playerMP).stInv.clearInventory(-1, -1);
               hn var10 = MinecraftServer.F().af();

               for (ye var12 : stacks) {
                  ss var13 = new ss(this.target.q, this.target.u, this.target.v + 1.0, this.target.w, var12);
                  var13.b = 10;
                  atc itemMotionVec = atc.a(rand.nextDouble() - 0.5, rand.nextDouble() / 2.0, rand.nextDouble() - 0.5).a();
                  float speedFactor = 0.4F + rand.nextFloat() / 10.0F;
                  var13.x = itemMotionVec.c * speedFactor;
                  var13.y = itemMotionVec.d * speedFactor;
                  var13.z = itemMotionVec.e * speedFactor;
                  this.target.q.d(var13);
               }
            }
         }

         TileEntityAnomaly.damageEntityForce(this.target, StalkerDamage.blackhole, 1000000.0F, true);
      }

      this.target = null;
      this.activeTimer = 120;
   }

   @Override
   public void addTarget(of entity) {
      if (this.target == null && this.activeTimer == -1 && !entity.M && entity.aN() > 0.0F && !entity.ar() || super.k.I) {
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
      } else if (super.particleEmitter != null) {
         this.getParticleEmitter().reset();
      }

      this.activeTimer = -1;
      this.target = null;
   }

   @Override
   public boolean canUpdate() {
      return true;
   }

   @Override
   public void onPlayerExit() {
      if (this.target != null) {
         this.doActivation(true);
      }
   }

   @Override
   protected Class getEmitterClass() {
      return FunnelParticleEmitter.class;
   }

   @Override
   public of getTarget() {
      return this.target;
   }

   @Override
   public boolean b(int par1, int par2) {
      if (par1 == 3) {
         this.doActivation(false);
         return true;
      } else {
         return super.b(par1, par2);
      }
   }
}
