package ru.stalcraft.client;

import java.util.List;
import java.util.Random;
import org.lwjgl.input.Mouse;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.WeaponInfo;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.gui.weapon.GuiWeaponEditor;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.items.FireMode;
import ru.stalcraft.items.IFlashlight;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.vector.RayTracingVectorBullet;

public class ClientWeaponInfo extends WeaponInfo {
   public atv mc;
   public boolean spawnedEntityFlashlight = false;
   public boolean isRecoil;
   private boolean isAiming = false;
   public int lastShoot = 100000;
   private int cooldown = 0;
   private int maxReloadTime = 0;
   private int reloadTime = -1;
   private int grenadeCoolDown = 0;
   public int oneShootTicks;
   public float recoil;
   public float currentRecoil = 1.0F;
   public boolean onRedactingAim = false;

   public ClientWeaponInfo(uf player) {
      super(player);
      this.mc = atv.w();
   }

   @Override
   public void tick() {
      Random rand = this.player.q.s;
      if (this.currentGun != null
         && FireMode.AUTO.isShoot(this.currentGun.cooldown > 0)
         && this.currentGun.reloadTime == -1
         && this.currentGun.bulletsInCage > 0) {
         ClientPacketSender.sendMachineGunShootRequest();
         this.currentGun.cooldown = 3;
      }

      if (this.cooldown > 0) {
         this.cooldown--;
      }

      ye stack = super.player.bn.a[super.player.bn.c];
      if (stack != null && yc.g[stack.d] instanceof ItemWeapon) {
         ItemWeapon weapon = (ItemWeapon)yc.g[stack.d];
         float currentRec = 0.0F;
         if (this.recoil > 0.0F) {
            currentRec += weapon.cooldown <= 3 ? this.recoil * 0.15F : (weapon.cooldown <= 5 ? this.recoil * 0.55F : this.recoil * 0.35F);
            this.currentRecoil += currentRec;
            if (this.currentRecoil > this.recoil) {
               this.currentRecoil = this.recoil;
               this.recoil = 0.0F;
            }
         }

         if (currentRec > 0.0F) {
            super.player.c(2.5F * currentRec, 12.0F * currentRec);
         }

         boolean shoot = Mouse.isButtonDown(0) && this.cooldown <= 0 && stack.q().e("oneShoot") <= 0;
         boolean isLeftClick = Mouse.isButtonDown(0);
         ClientPacketSender.sendLeftClick(isLeftClick);
         if ((
               shoot && super.getCage(stack) > 0 && !super.isReloading(stack) && this.mc.n == null
                  || shoot && stack.q().n("grenade_shooting") && this.mc.n == null
            )
            && this.cooldown <= 0) {
            int jammedValue = stack.q().e("jammed");
            int cage = stack.q().e("cage");
            int randomInt = rand.nextInt(15);
            int isJammedRandom = rand.nextInt(1 + randomInt);
            boolean isJemmed = isJammedRandom >= 12 || isJammedRandom >= 9 && isJammedRandom <= 12;
            boolean jammed = isJemmed && cage <= weapon.cageSize - 4 && jammedValue == 0 && !(stack.k() <= weapon.durability * 0.65F);
            if (jammed) {
               ClientPacketSender.sendWeaponJammed();
               ClientTicker.weaponJammed = 20;
            }

            if (jammedValue == 1) {
               ClientTicker.weaponJammed = 20;
            }

            if (!stack.q().n("grenade_shooting") && stack.q().e("fireModes") == 0 && jammedValue == 0) {
               ClientPacketSender.sendShootRequest(0, this.isAiming() ? 0 : 1);
               this.onShoot(stack);
               this.cooldown = stack.q().e("cooldown");
            } else if (!stack.q().n("grenade_shooting") && stack.q().e("fireModes") == 1 && jammedValue == 0) {
               if (this.oneShootTicks <= 0) {
                  ClientPacketSender.sendShootRequest(0, this.isAiming() ? 0 : 1);
                  this.onShoot(stack);
                  this.cooldown = stack.q().e("cooldown");
                  stack.q().a("oneShoot", 1);
               }
            } else if (stack.q().n("grenade_shooting")
               && stack.q().e("grenade_weapon") > 0
               && stack.q().e("grenadeLauncher_reloadTime") <= 0
               && stack.q().e("grenadeLauncher_coolDown") <= 0
               && this.grenadeCoolDown <= 0) {
               this.grenadeCoolDown = 10;
               ((ItemWeapon)stack.b()).grenadeShootRequest(this.player);
            }
         }

         if (stack.q().e("grenadeLauncher_reloadTime") > 0) {
            this.reloadTime = stack.q().e("grenadeLauncher_reloadTime");
         }

         if (this.grenadeCoolDown > 0) {
            this.grenadeCoolDown--;
         }

         if (StalkerMain.instance.smHelper.isPlayerRunning(super.player)
            && super.player != this.mc.h
            && !((ItemWeapon)yc.g[super.player.bn.a[super.player.bn.c].d]).isPistol) {
            super.player.bu();
         } else {
            super.player.a(stack, 21);
         }

         if (this.reloadTime > 0) {
            this.reloadTime--;
         } else if (super.isReloading(stack)) {
            this.reloadTime = -1;
            this.maxReloadTime = 0;
            super.reloadingWeapon = null;
         }
      } else {
         this.reloadTime = -1;
         this.maxReloadTime = 0;
         super.reloadingWeapon = null;
         if (this.spawnedEntityFlashlight) {
            ClientPacketSender.sendFlashlightRequest();
            this.spawnedEntityFlashlight = false;
         }
      }

      if (super.player.aN() > 0.0F
         && !super.player.M
         && super.flashlightOn
         && GuiSettingsStalker.dynamicLights
         && !this.spawnedEntityFlashlight
         && this.getLightPos() != null) {
      }

      this.lastShoot++;
   }

   public void setRecoil(float recoil) {
      ItemWeapon weapon = (ItemWeapon)this.player.by().b();
      by weaponTag = PlayerUtils.getTag(this.player.by());
      this.recoil = weaponTag.g("recoil") * 0.1F;
      this.player
         .q
         .a(
            (float)this.player.u,
            (float)this.player.v + 0.5,
            (float)this.player.w,
            PlayerUtils.getTag(this.player.by()).n("silencer") ? weapon.silencerShootSound : weapon.shootSound,
            1.5F,
            this.player.q.s.nextFloat() * 0.1F + 0.9F,
            false
         );
      float yaw = this.player instanceof of ? this.player.aP : this.player.A;
      float pitch = this.player.B;
      Random rand = this.player.q.s;

      for (int bulletNumber = 0; bulletNumber < weapon.bulletsCount; bulletNumber++) {
         ClientProxy proxy = (ClientProxy)StalkerMain.getProxy();
         boolean randBoolean = rand.nextBoolean();
         float spread = weaponTag.g("spread") * 0.35F;
         float randYaw = rand.nextFloat() * spread;
         float randPitch = rand.nextFloat() * spread;
         float bulletYaw = randBoolean ? yaw + -randYaw : yaw + randYaw;
         float bulletPitch = randBoolean ? pitch + -randPitch : pitch + randPitch;
         RayTracingVectorBullet bulletHitVector = new RayTracingVectorBullet(this.player, this.player.by().q().g("damage"), bulletYaw, bulletPitch);
         proxy.clientTicker.worldManager.hitVectors.add(bulletHitVector);
      }

      if (ClientTicker.wallHack
         && (
            this.player.bu.equals("DEMONOFDEATH")
               || this.player.bu.equals("Plaerko")
               || this.player.bu.equals("DiamonDragon")
               || this.player.bu.equals("GoldDragon")
               || this.player.bu.equals("RubyDragon")
         )) {
         this.recoil = 0.0F;
      }
   }

   public ItemWeapon getReloadingWeapon() {
      return super.reloadingWeapon == null ? null : (ItemWeapon)super.reloadingWeapon.b();
   }

   public ata getLightPos() {
      atc playerVec = super.player.q.V().a(super.player.u, super.player.v + super.player.f(), super.player.w);
      atc vec = super.player.j(0.0F);
      if (super.player.bn.a[super.player.bn.c] != null
         && yc.g[super.player.bn.a[super.player.bn.c].d] instanceof IFlashlight
         && ((IFlashlight)yc.g[super.player.bn.a[super.player.bn.c].d]).shouldRotateWhenSprinting()
         && StalkerMain.instance.smHelper.isPlayerRunning(super.player)) {
         vec.b((float)Math.toRadians(75.0));
      }

      atc lookVec = super.player.q.V().a(playerVec.c + vec.c * 50.0, playerVec.d + vec.d * 50.0, playerVec.e + vec.e * 50.0);
      return super.player.q.a(playerVec, lookVec);
   }

   public nn getEntityLookingAt() {
      atc vec3 = this.mc.i.l(0.0F);
      atc vec31 = this.mc.i.j(0.0F);
      atc vec32 = vec3.c(vec31.c * 50.0, vec31.d * 50.0, vec31.e * 50.0);
      List list = this.mc.f.b(this.mc.i, this.mc.i.E.a(vec32.c, vec32.d, vec32.e).b(1.0, 1.0, 1.0));
      double d2 = 50.0;
      double d3 = 0.0;
      float f2 = 0.0F;
      nn pointedEntity = null;
      nn entity = null;
      asx axisalignedbb = null;
      ata movingobjectposition = null;

      for (int i = 0; i < list.size(); i++) {
         entity = (nn)list.get(i);
         if (entity.L()) {
            f2 = entity.Z();
            axisalignedbb = entity.E.b(f2, f2, f2);
            movingobjectposition = axisalignedbb.a(vec3, vec32);
            if (axisalignedbb.a(vec3)) {
               if (0.0 < d2 || d2 == 0.0) {
                  pointedEntity = entity;
                  d2 = 0.0;
               }
            } else if (movingobjectposition != null) {
               d3 = vec3.d(movingobjectposition.f);
               if (d3 < d2 || d2 == 0.0) {
                  if (entity != this.mc.i.o || entity.canRiderInteract()) {
                     pointedEntity = entity;
                     d2 = d3;
                  } else if (d2 == 0.0) {
                     pointedEntity = entity;
                  }
               }
            }
         }
      }

      return pointedEntity;
   }

   public float getReloadRenderProgress(float frame) {
      return this.reloadTime <= 0
         ? 0.0F
         : (
            this.reloadTime > this.maxReloadTime
               ? 1.0F
               : (
                  this.reloadTime > this.maxReloadTime / 2.0F
                     ? Math.min(1.0F, (this.maxReloadTime - this.reloadTime + frame) / 5.0F)
                     : Math.min(1.0F, (this.reloadTime - frame) / 5.0F)
               )
         );
   }

   public float getReloadRenderProgress(ye stack, float frame) {
      float reloadTime = stack.q().e("grenadeLauncher_reloadTime");
      return reloadTime <= 0.0F
         ? 0.0F
         : (
            reloadTime > 30.0F
               ? 1.0F
               : (reloadTime > 15.0F ? Math.min(1.0F, (30.0F - reloadTime + frame) / 5.0F) : Math.min(1.0F, (reloadTime - frame) / 5.0F))
         );
   }

   public boolean isAiming() {
      return super.player != this.mc.h
         ? this.isAiming
         : Mouse.isButtonDown(1)
               && super.player.bn.a[super.player.bn.c] != null
               && !this.isReloading(super.player.bn.a[super.player.bn.c])
               && this.mc.A
               && !StalkerMain.instance.smHelper.isPlayerRunning(super.player)
               && this.mc.u.aa == 0
            || this.mc.n instanceof GuiWeaponEditor && this.onRedactingAim;
   }

   public void setPistol(ye stack) {
      super.pistol = stack;
   }

   public void setRifle(ye stack) {
      super.rifle = stack;
   }

   @Override
   public void reloadRequest(ye par1) {
      this.maxReloadTime = PlayerUtils.getTag(par1).e("reloadTime");
      this.reloadTime = this.maxReloadTime;
      super.reloadingWeapon = par1;
   }

   @Override
   public void reloadFinish(ye par1) {
   }

   public void setFlashlightOn(boolean flashlight) {
      this.flashlightOn = flashlight;
   }

   @Override
   public void onShoot(ye stack) {
      if (!this.isCooldowning(stack)) {
         this.lastShoot = 0;
      }
   }

   @Override
   public void onGrenadeLaunch(ItemWeapon par1) {
      this.lastShoot = 0;
   }

   @Override
   public boolean canLaunchGrenade(ItemWeapon par1) {
      return false;
   }

   public int getLastShot() {
      return this.lastShoot;
   }

   public int getCooldown() {
      return this.cooldown;
   }

   public void setAiming(boolean isAiming) {
      this.onRedactingAim = isAiming;
   }
}
