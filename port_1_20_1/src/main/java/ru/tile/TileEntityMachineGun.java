package ru.stalcraft.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.entity.EntityShot;
import ru.stalcraft.entity.EntitySleeve;
import ru.stalcraft.player.IPlayerServerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.network.ServerPacketSender;

public class TileEntityMachineGun extends asp {
   private static final int CAGE_SIZE = 300;
   private static final int MAX_COOLDOWN = 2;
   private static final int MAX_RELOAD_TIME = 100;
   private static final int BULLET_ID = 14955;
   private static final String RELOAD_SOUND = "stalker:machinegun_reload";
   private static final String SHOOT_SOUND = "stalker:machinegun_shoot";
   private static final String HIT_SOUND = "stalker:machinegun_hit";
   public int bulletsInCage = 0;
   private uf shooter = null;
   public int cooldown = 0;
   public int reloadTime = -1;
   public float yaw = 0.0F;
   public float pitch = 0.0F;
   public float prevYaw = 0.0F;
   public float prevPitch = 0.0F;

   public void onRightClick(uf par1) {
      if (PlayerUtils.getInfo(par1) != null && PlayerUtils.getInfo(par1) instanceof IPlayerServerInfo) {
         ((IPlayerServerInfo)PlayerUtils.getInfo(par1)).shooterMachineGun(this);
      }
   }

   public void shootRequest() {
      if (this.shooter != null && PlayerUtils.getInfo(this.shooter) != null && PlayerUtils.getInfo(this.shooter) instanceof IPlayerServerInfo) {
         ((IPlayerServerInfo)PlayerUtils.getInfo(this.shooter)).shootMachineGun(this);
      }
   }

   public void reloadRequest() {
      if (PlayerUtils.getInfo(this.shooter) != null && PlayerUtils.getInfo(this.shooter) instanceof IPlayerServerInfo) {
         ((IPlayerServerInfo)PlayerUtils.getInfo(this.shooter)).reloadRequestMachineGun(this);
      }
   }

   public void removeShooter() {
      if (this.shooter != null) {
         ServerPacketSender.sendMachinegunState(this.shooter, this, false);
         PlayerUtils.getInfo(this.shooter).weaponInfo.currentGun = null;
      }

      this.shooter = null;
      this.yaw = 0.0F;
      this.pitch = 0.0F;
   }

   public void h() {
      if (this.cooldown > 0) {
         this.cooldown--;
      }

      if (this.reloadTime > 0) {
         this.reloadTime--;
      }

      if (this.shooter != null && !super.k.I && this.shooter.bn.h() != null) {
         this.removeShooter();
      }

      if (this.shooter != null && this.reloadTime == 0) {
         if (!this.az().I) {
            this.reloadFinish();
         }

         this.reloadTime = -1;
      } else if (this.shooter == null && this.reloadTime == 0) {
         this.reloadTime = -1;
      }

      this.updatePlayerPos();
      this.updateRotation();
   }

   @SideOnly(Side.CLIENT)
   private uf getThePlayer() {
      return atv.w().h;
   }

   private void reloadFinish() {
      if (this.shooter != null && PlayerUtils.getInfo(this.shooter) != null && PlayerUtils.getInfo(this.shooter) instanceof IPlayerServerInfo) {
         ((IPlayerServerInfo)PlayerUtils.getInfo(this.shooter)).reloadFinishMachineGun(this);
      }
   }

   public boolean isBlocked(float yaw, float pitch) {
      float length = 1.5F;
      yaw += this.p() * 90;
      double lookX = -ls.a(yaw / 180.0F * (float) Math.PI) * ls.b(pitch / 180.0F * (float) Math.PI) * length;
      double lookZ = ls.b(yaw / 180.0F * (float) Math.PI) * ls.b(pitch / 180.0F * (float) Math.PI) * length;
      double lookY = -ls.a(pitch / 180.0F * (float) Math.PI) * length;
      atc posVec = super.k.V().a(super.l + 0.5 + lookX * 0.5, super.m + 0.4 + lookY * 0.5, super.n + 0.5 + lookZ * 0.5);
      atc lookVec = super.k.V().a(posVec.c + lookX, posVec.d + lookY, posVec.e + lookZ);
      ata obj = super.k.a(posVec, lookVec, false, true);
      return obj != null;
   }

   public boolean canUpdate() {
      return true;
   }

   public void updateRotation() {
      this.prevYaw = this.yaw;
      this.prevPitch = this.pitch;
      uf shooter = this.getShooter();
      if (shooter != null) {
         float tempPitch = Math.max(-60.0F, Math.min(shooter.B, 60.0F));
         float tempYaw = (shooter.A - this.p() * 90) % 360.0F;
         if (tempYaw < -180.0F) {
            tempYaw += 360.0F;
         }

         if (tempYaw > 180.0F) {
            tempYaw -= 360.0F;
         }

         if (tempYaw > 90.0F) {
            tempYaw = 90.0F;
         }

         if (tempYaw < -90.0F) {
            tempYaw = -90.0F;
         }

         if (this.yaw == -90.0F && tempYaw == 90.0F || this.yaw == 90.0F && tempYaw == -90.0F) {
            return;
         }

         if (tempYaw != this.yaw || tempPitch != this.pitch) {
            if (this.isBlocked(this.yaw, this.pitch) || !this.isBlocked(tempYaw, tempPitch)) {
               this.pitch = tempPitch;
               this.yaw = tempYaw;
            } else if (!this.isBlocked(tempYaw, this.pitch)) {
               this.yaw = tempYaw;
            } else if (!this.isBlocked(this.yaw, tempPitch)) {
               this.pitch = tempPitch;
            }
         }
      }
   }

   public void updatePlayerPos() {
      uf shooter = this.getShooter();
      if (shooter != null) {
         float realYaw = this.yaw + this.p() * 90;
         double newX = -(-ls.a(realYaw / 180.0F * (float) Math.PI)) * 1.2 + this.l + 0.5;
         double newZ = -ls.b(realYaw / 180.0F * (float) Math.PI) * 1.2 + this.n + 0.5;
         if (Math.abs(shooter.u - newX) > 0.01 || Math.abs(shooter.w - newZ) > 0.01) {
            shooter.d(newX - shooter.u, -0.5, newZ - shooter.w);
         }
      }
   }

   public uf getShooter() {
      if (super.k.I && this.shooter != null && PlayerUtils.getInfo(this.shooter).weaponInfo.currentGun == null) {
         this.shooter = null;
      }

      return this.shooter;
   }

   public void setShooter(uf shooter) {
      this.shooter = shooter;
   }

   public void a(by tag) {
      super.a(tag);
      this.bulletsInCage = tag.e("bullets_in_cage");
      this.yaw = this.prevYaw = tag.g("rot_yaw");
      this.pitch = this.prevPitch = tag.g("rot_pitch");
   }

   public void b(by tag) {
      super.b(tag);
      tag.a("bullets_in_cage", this.bulletsInCage);
      tag.a("rot_yaw", this.yaw);
      tag.a("rot_pitch", this.pitch);
   }

   @SideOnly(Side.CLIENT)
   public boolean b(int par1, int par2) {
      if (par1 == 1) {
         this.bulletsInCage = par2;
         return true;
      } else if (par1 == 2) {
         atv mc = atv.w();
         this.prevPitch = Math.max(-45.0F, this.prevPitch - 3.0F);
         if (GuiSettingsStalker.renderSleeves) {
            mc.f.d(new EntitySleeve(mc.f, super.l + 0.5, super.m + 0.5, super.n + 0.5, this.yaw + this.p() * 90, this.pitch, 0.0F, "machinegun_sleeve"));
         }

         mc.f.d(new EntityShot(mc.f, super.l + 0.5, super.m + 0.5, super.n + 0.5, this.yaw + this.p() * 90, this.pitch, 1.0F, 1.5F));
         return true;
      } else if (par1 == 3) {
         this.reloadTime = par2;
         return true;
      } else {
         return par1 == 4 ? true : super.b(par1, par2);
      }
   }
}
