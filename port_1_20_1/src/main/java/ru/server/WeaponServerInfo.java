package ru.stalcraft.server;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.WeaponInfo;
import ru.stalcraft.items.IFlashlight;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.network.ServerPacketSender;

public class WeaponServerInfo extends WeaponInfo {
   private int cooldown = 0;
   private int reloadTime = -1;
   private int oneShootTick;
   private boolean isLeftClick;

   public WeaponServerInfo(uf player) {
      super(player);
   }

   @Override
   public void tick() {
      ye stack = super.player.bn.a[super.player.bn.c];
      yc item = null;
      if (stack != null && yc.g[stack.d] instanceof ItemWeapon) {
         item = yc.g[stack.d];
         if (super.currentWeapon != stack) {
            super.currentWeapon = stack;
         }

         if (!PlayerUtils.getTag(stack).n("weaponInfo")) {
            PlayerUtils.getTag(stack).a("reloadTime", ((ItemWeapon)item).reloadTime);
            PlayerUtils.getTag(stack).a("cooldown", ((ItemWeapon)item).cooldown);
            PlayerUtils.getTag(stack).a("fireModeLength", 0);
            PlayerUtils.getTag(stack).a("fireMode", ((ItemWeapon)item).fireMods[0].ordinal());
            PlayerUtils.getTag(stack).a("damage", ((ItemWeapon)item).damage);
            PlayerUtils.getTag(stack).a("weaponInfo", true);
         }

         if (((ItemWeapon)item).integrateGrenade) {
            PlayerUtils.getTag(stack).a("grenade_launcher", true);
         }

         if (!PlayerUtils.getTag(stack).n("upgrade")) {
            if (PlayerUtils.getTag(stack).g("damage") != ((ItemWeapon)item).damage) {
               PlayerUtils.getTag(stack).a("damage", ((ItemWeapon)item).damage);
            }

            if (PlayerUtils.getTag(stack).g("recoil") != ((ItemWeapon)item).recoil) {
               PlayerUtils.getTag(stack).a("recoil", ((ItemWeapon)item).recoil);
            }

            if (PlayerUtils.getTag(stack).g("spread") != ((ItemWeapon)item).spread) {
               PlayerUtils.getTag(stack).a("spread", ((ItemWeapon)item).spread);
            }
         }

         by tag = PlayerUtils.getTag(stack);
         if (tag.e("grenadeLauncher_reloadTime") > 0) {
            tag.a("grenadeLauncher_reloadTime", tag.e("grenadeLauncher_reloadTime") - 1);
         }

         if (tag.e("grenadeLauncher_coolDown") > 0) {
            tag.a("grenadeLauncher_coolDown", tag.e("grenadeLauncher_coolDown") - 1);
         }

         if (!this.isLeftClick && tag.e("oneShoot") > 0) {
            tag.a("oneShoot", 0);
         }

         if (!tag.n("grenade_launcher")) {
            tag.a("grenade_shooting", false);
         }

         if (tag.e("fireModes") == 0 && !((ItemWeapon)stack.b()).autoShooting) {
            tag.a("fireModes", 1);
         }

         if (this.cooldown > 0) {
            this.cooldown--;
         }

         if (this.reloadTime > 0) {
            this.reloadTime--;
         } else if (this.reloadTime == 0) {
            this.reloadFinish(stack);
            this.reloadTime = -1;
         } else if (super.isReloading(stack)) {
            PlayerUtils.getTag(stack).a("reloading", false);
         }
      } else {
         if (super.flashlightOn) {
            this.flashlightToggleRequest(null);
         }

         this.reloadTime = -1;
         super.reloadingWeapon = null;
         this.cooldown = 0;
      }

      this.updateEquippedWeapons();
   }

   public boolean canShoot(ye stack) {
      return yc.g[stack.d] instanceof ItemWeapon
         && !PlayerUtils.getInfo(this.player).getHandcuffs()
         && this.cooldown <= 0
         && super.getCage(stack) > 0
         && this.reloadTime <= 0
         && (!StalkerMain.instance.smHelper.isPlayerRunning(super.player) || ((ItemWeapon)yc.g[stack.d]).isPistol);
   }

   @Override
   public boolean canLaunchGrenade(ItemWeapon weapon) {
      return !PlayerUtils.getInfo(super.player).getHandcuffs()
         && this.cooldown <= 0
         && PlayerUtils.hasItem(super.player, weapon.grenadeId)
         && this.reloadTime <= 0;
   }

   @Override
   public void onGrenadeLaunch(ItemWeapon weapon) {
      this.cooldown = weapon.grenadeLaunchCooldown;
      PlayerUtils.consumeItems(super.player, weapon.grenadeId, 1, true);
   }

   @Override
   public void onShoot(ye stack) {
      if (PlayerUtils.getTag(stack).e("oneShoot") <= 0 && this.cooldown <= 0) {
         PlayerUtils.getTag(stack).a("cage", PlayerUtils.getTag(stack).e("cage") - 1);
         this.cooldown = ((ItemWeapon)yc.g[stack.d]).cooldown / 2;
         if (PlayerUtils.getTag(stack).e("fireModes") == 1) {
            PlayerUtils.getTag(stack).a("oneShoot", 1);
         }

         ServerPacketSender.sendToRecoil(this.player);
         ServerPacketSender.sendToFlash(this.player.k);
      }
   }

   public void setLeftClick(boolean click) {
      this.isLeftClick = click;
   }

   public void flashlightToggleRequest(ye stack) {
      if (super.flashlightOn) {
         super.flashlightOn = false;
      } else if (stack != null && yc.g[stack.d] instanceof IFlashlight && ((IFlashlight)yc.g[stack.d]).canShine(stack)) {
         super.flashlightOn = true;
      }

      ServerPacketSender.sendFlashlight(super.player, super.flashlightOn);
   }

   @Override
   public void reloadRequest(ye stack) {
      if (!super.isReloading(stack)
         && this.reloadTime == -1
         && super.getCage(stack) < ((ItemWeapon)yc.g[stack.d]).cageSize
         && !PlayerUtils.getInfo(super.player).getHandcuffs()) {
         PlayerUtils.hasItem(super.player, ((ItemWeapon)yc.g[stack.d]).bulletsID[0]);
         ItemWeapon itemWeapon = (ItemWeapon)yc.g[stack.d];
         by stackWeaponNBT = PlayerUtils.getTag(stack);
         int ammoType = stackWeaponNBT.e("ammoType");
         if (ammoType == 0 && PlayerUtils.hasItem(super.player, itemWeapon.bulletsID[0])
            || ammoType == 1 && PlayerUtils.hasItem(super.player, itemWeapon.bulletsID[1])) {
            this.reloadTime = itemWeapon.reloadTime;
            super.reloadingWeapon = stack;
            PlayerUtils.getTag(stack).a("reloading", true);
            ServerPacketSender.sendReloadStart(super.player);
         }

         if (ammoType == 1 && !PlayerUtils.hasItem(this.player, itemWeapon.bulletsID[1]) && PlayerUtils.hasItem(this.player, itemWeapon.bulletsID[0])) {
            this.reloadTime = itemWeapon.reloadTime;
            super.reloadingWeapon = stack;
            PlayerUtils.getTag(stack).a("reloading", true);
            ServerPacketSender.sendReloadStart(super.player);
            stackWeaponNBT.a("ammoType", 0);
            int ammoCage = super.getCage(stack);
            PlayerUtils.addItem(this.player, new ye(yc.g[itemWeapon.bulletsID[1]], ammoCage));
            stackWeaponNBT.a("cage", 0);
         }

         if (ammoType == 0 && !PlayerUtils.hasItem(this.player, itemWeapon.bulletsID[0]) && PlayerUtils.hasItem(this.player, itemWeapon.bulletsID[1])) {
            this.reloadTime = itemWeapon.reloadTime;
            super.reloadingWeapon = stack;
            PlayerUtils.getTag(stack).a("reloading", true);
            ServerPacketSender.sendReloadStart(super.player);
            stackWeaponNBT.a("ammoType", 1);
            int ammoCage = super.getCage(stack);
            PlayerUtils.addItem(this.player, new ye(yc.g[itemWeapon.bulletsID[0]], ammoCage));
            stackWeaponNBT.a("cage", 0);
         }
      }
   }

   @Override
   public void reloadFinish(ye stack) {
      by stackWeaponNBT = PlayerUtils.getTag(stack);
      int bulletsInCage = stackWeaponNBT.e("cage");
      int ammoType = stackWeaponNBT.e("ammoType");
      int bulletCount = PlayerUtils.consumeItems(
         super.player, ((ItemWeapon)yc.g[stack.d]).bulletsID[ammoType], ((ItemWeapon)yc.g[stack.d]).cageSize - bulletsInCage, true
      );
      stackWeaponNBT.a("cage", bulletCount + bulletsInCage);
      stackWeaponNBT.a("reloading", false);
      stackWeaponNBT.a("jammed", 0);
   }

   public void updateEquippedWeapons() {
      ye newPistol = null;
      ye newRifle = null;
      ye stack = null;
      ItemWeapon itemWeapon = null;

      for (int item = 0; item < 4; item++) {
         stack = super.player.bn.a[item];
         if (item != this.player.bn.c && stack != null && yc.g[stack.d] instanceof ItemWeapon) {
            itemWeapon = (ItemWeapon)yc.g[stack.d];
            if (itemWeapon.renderEquipped) {
               if (itemWeapon.isPistol) {
                  newPistol = stack;
               } else {
                  newRifle = stack;
               }
            }
         }
      }

      if (super.pistol != newPistol || super.rifle != newRifle) {
         ServerPacketSender.sendEquippedWeapons(super.player, newRifle, newPistol);
      }

      super.pistol = newPistol;
      super.rifle = newRifle;
   }

   public void readNBT(by tag) {
   }

   public void writeNBT(by tag) {
   }

   public void updateFireMode(ye stack) {
      int fireModeLength = PlayerUtils.getTag(stack).e("fireModeLength");
      int preFireModeLength = fireModeLength;
      ItemWeapon itemWeapon = (ItemWeapon)yc.g[stack.d];
      if (fireModeLength < itemWeapon.fireMods.length - 1) {
         fireModeLength++;
      } else if (fireModeLength == itemWeapon.fireMods.length - 1) {
         fireModeLength = 0;
      }

      PlayerUtils.getTag(stack).a("fireModeLength", fireModeLength);
      PlayerUtils.getTag(stack).a("fireMode", itemWeapon.fireMods[fireModeLength].ordinal());
      if (preFireModeLength != fireModeLength) {
         super.player.q.a(super.player, "stalker:firemode", 1.0F, 0.9F + super.player.q.s.nextFloat() * 0.1F);
      }
   }
}
