package ru.stalcraft.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.IPlayerServerInfo;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class WeaponContainer extends uy implements ICustomContainer {
   public vk craftMatrix = new vk(this, 2, 2);
   public mo craftResult = new wc();
   public boolean isLocalWorld = false;
   protected final uf player;
   public final PlayerInfo info;
   private boolean hasBackpackSlots = false;
   public ArrayList backpackSlots = new ArrayList();
   private ArrayList hotbarSlots = new ArrayList();
   public SlotWeaponStatUpgreade upgrade;
   public SlotWeaponSilencerUpgreade silencer;
   public SlotWeaponGrenadeLaunchUpgreade grenadeLaunch;
   public SlotWeaponSightUpgreade sight;
   private WeaponInventory weaponInventory;
   public ye updatedWeapon;

   public WeaponContainer(ud par1InventoryPlayer, boolean par2, uf par3EntityPlayer, int weaponSlotId) {
      this.player = par3EntityPlayer;
      this.info = PlayerUtils.getInfo(par3EntityPlayer);
      ye weapon;
      if (weaponSlotId > par1InventoryPlayer.j_()) {
         weapon = this.info.stInv.a(weaponSlotId - par1InventoryPlayer.j_());
      } else {
         weapon = par1InventoryPlayer.a(weaponSlotId);
      }

      this.isLocalWorld = par2;

      for (int i = 0; i < 4; i++) {
         this.a(new StalkerSlot(this, this, par1InventoryPlayer, i, 137 + i * 18, 153));
      }

      for (int var8 = 0; var8 < 4; var8++) {
         for (int j = 0; j < 8; j++) {
            this.a(new StalkerSlot(this, this, par1InventoryPlayer, var8 * 8 + j + 4, 137 + var8 * 18, 9 + j * 18));
         }
      }

      ItemWeapon itemWeapon = (ItemWeapon)weapon.b();
      this.weaponInventory = new WeaponInventory();
      this.upgrade = new SlotWeaponStatUpgreade(this, this, this.weaponInventory, 0, 12, 73);
      this.silencer = new SlotWeaponSilencerUpgreade(this, this, this.weaponInventory, 1, 12, 53, itemWeapon.isPistol);
      this.grenadeLaunch = new SlotWeaponGrenadeLaunchUpgreade(this, this, this.weaponInventory, 2, 32, 53, itemWeapon.indexGrenadeLauncher);
      this.sight = new SlotWeaponSightUpgreade(this, this, this.weaponInventory, 3, 52, 53, itemWeapon.indexSight);
      this.a(this.upgrade);
      if (itemWeapon.silencerEnabled) {
         this.a(this.silencer);
      }

      if (itemWeapon.grenadeEnabled && !itemWeapon.integrateGrenade) {
         this.a(this.grenadeLaunch);
      }

      if (itemWeapon.sightEnabled && !itemWeapon.integrateSight) {
         this.a(this.sight);
      }

      this.a(this.craftMatrix);
      this.hasBackpackSlots = this.hasBackpack();
      this.switchToWeaponUpgrade(weapon);
   }

   public void switchToWeaponUpgrade(ye stack) {
      this.updatedWeapon = stack;
      by tag = PlayerUtils.getTag(this.updatedWeapon);
      ItemWeapon weapon = (ItemWeapon)stack.b();
      if (tag.n("silencer")) {
         if (!weapon.isPistol) {
            this.weaponInventory.contents[1] = new ye(StalkerMain.silencer, 1, 0);
         } else {
            this.weaponInventory.contents[1] = new ye(StalkerMain.pistolsilencer, 1, 0);
         }
      }

      if (tag.n("grenade_launcher")) {
         if (weapon.indexGrenadeLauncher == 1) {
            this.weaponInventory.contents[2] = new ye(StalkerMain.grenadeLauncherM203, 1, 0);
         } else {
            this.weaponInventory.contents[2] = new ye(StalkerMain.grenadeLauncherKoster, 1, 0);
         }
      }

      if (tag.n("sight")) {
         if (weapon.indexSight == 1) {
            this.weaponInventory.contents[3] = new ye(StalkerMain.weaponSightAcog, 1, 0);
         } else if (weapon.indexSight == 2) {
            this.weaponInventory.contents[3] = new ye(StalkerMain.weaponSightPO, 1, 0);
         } else {
            this.weaponInventory.contents[3] = new ye(StalkerMain.weaponSightPSO, 1, 0);
         }
      }
   }

   public void a(mo par1IInventory) {
      if (this.player != null) {
         this.craftResult.a(0, aaf.a().a(this.craftMatrix, this.player.q));
      }
   }

   public boolean a(uf par1EntityPlayer) {
      return true;
   }

   public ye b(uf par1EntityPlayer, int par2) {
      return null;
   }

   public ye a(int par1, int par2, int par3, uf par4EntityPlayer) {
      return par1 >= 0 && this.a(par1) != null && !this.isSlotActive(this.a(par1)) ? null : super.a(par1, par2, par3, par4EntityPlayer);
   }

   public boolean a(ye par1ItemStack, we par2Slot) {
      return par2Slot.f != this.craftResult && super.a(par1ItemStack, par2Slot);
   }

   @Override
   public boolean hasBackpack() {
      return this.info.stInv.mainInventory[12] != null;
   }

   public we a(int par1) {
      return par1 < super.c.size() ? (we)super.c.get(par1) : null;
   }

   public void a(int par1, ye par2ItemStack) {
      we slot = this.a(par1);
      if (slot != null) {
         slot.c(par2ItemStack);
      }
   }

   @Override
   public void handleBackpackChanged(boolean hasBackpack) {
      if (this.hasBackpackSlots != hasBackpack) {
         this.hasBackpackSlots = hasBackpack;
         if (!hasBackpack && !this.isLocalWorld) {
            ArrayList inBackpack = new ArrayList();

            for (int it = 53; it < 61; it++) {
               if (this.a(it).e()) {
                  inBackpack.add(this.a(it).d());
                  this.a(it, (ye)null);
               }
            }

            Iterator var7 = super.b.iterator();

            while (var7.hasNext()) {
               ye i$ = (ye)var7.next();
               if (i$ != null && inBackpack.contains(i$)) {
                  var7.remove();
               }
            }

            for (ye stack : inBackpack) {
               PlayerUtils.addItem(this.player, stack);
            }

            this.b();
         }
      }
   }

   @Override
   public ArrayList getBackpackSlots() {
      return this.backpackSlots;
   }

   @Override
   public of getOwner() {
      return this.player;
   }

   public void b() {
      boolean hasItemsChanged = false;

      for (int tag = 0; tag < super.c.size(); tag++) {
         ye itemstack = ((we)super.c.get(tag)).d();
         ye itemstack1 = (ye)super.b.get(tag);
         if (!ye.b(itemstack1, itemstack)) {
            hasItemsChanged = true;
            itemstack1 = itemstack == null ? null : itemstack.m();
            super.b.set(tag, itemstack1);

            for (int j = 0; j < super.e.size(); j++) {
               ((vi)super.e.get(j)).a(this, tag, itemstack1);
            }
         }
      }

      if (hasItemsChanged) {
         if (this.info instanceof IPlayerServerInfo) {
            IPlayerServerInfo par2 = (IPlayerServerInfo)this.info;
            par2.updateWeightSpeed();
         }

         by var7 = PlayerUtils.getTag(this.updatedWeapon);
      }
   }

   @Override
   public boolean isSlotActive(we slot) {
      return this.hasBackpack() || !this.backpackSlots.contains(slot);
   }

   public void tryExtractAmmo() {
      if (this.updatedWeapon != null) {
         by tag = PlayerUtils.getTag(this.updatedWeapon);
         if (tag.e("cage") > 0) {
            ItemWeapon item = (ItemWeapon)this.updatedWeapon.b();
            PlayerUtils.addItem(this.player, new ye(item.bulletsID[tag.e("ammoType")], tag.e("cage"), 0));
            PlayerUtils.getTag(this.updatedWeapon).a("cage", 0);
            ((jv)this.player).a(this);
         }
      }
   }

   public void b(uf player) {
      super.b(player);
      if (!player.q.I) {
         IPlayerServerInfo par2 = (IPlayerServerInfo)PlayerUtils.getInfo(player);
         par2.addItemSafe(player, this.updatedWeapon);
      }

      PlayerUtils.addItem(player, this.upgrade.d());
   }

   @Override
   public ArrayList getArmorSlots() {
      return new ArrayList();
   }
}
