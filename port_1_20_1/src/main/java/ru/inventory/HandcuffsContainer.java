package ru.stalcraft.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import ru.stalcraft.player.IPlayerServerInfo;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class HandcuffsContainer extends uy implements ICustomContainer {
   public boolean isLocalWorld = false;
   public final uf inventoryOwner;
   public final PlayerInfo inventoryOwnerInfo;
   public final uf containerUser;
   private boolean hasBackpackSlots = false;
   public ArrayList backpackSlots = new ArrayList();
   public ArrayList armorSlots = new ArrayList();
   private ArrayList leftSlots = new ArrayList();
   private ArrayList hotbarSlots = new ArrayList();
   private SlotBackpack backpackSlot;

   public HandcuffsContainer(uf inventoryOwner, uf containerUser, boolean localWorld) {
      ud par1InventoryPlayer = inventoryOwner.bn;
      this.isLocalWorld = localWorld;
      this.inventoryOwner = inventoryOwner;
      this.inventoryOwnerInfo = PlayerUtils.getInfo(inventoryOwner);
      this.containerUser = containerUser;
      this.hotbarSlots.add(new StalkerSlot(this, this, par1InventoryPlayer, 0, 26, 26));
      this.hotbarSlots.add(new StalkerSlot(this, this, par1InventoryPlayer, 1, 26, 44));
      this.hotbarSlots.add(new StalkerSlot(this, this, par1InventoryPlayer, 2, 62, 26));
      this.hotbarSlots.add(new StalkerSlot(this, this, par1InventoryPlayer, 3, 62, 44));

      for (we slot1 : this.hotbarSlots) {
         this.a(slot1);
      }

      for (int i = 0; i < 4; i++) {
         for (int j = 0; j < 8; j++) {
            this.a(new StalkerSlot(this, this, par1InventoryPlayer, i * 8 + j + 4, 125 + i * 18, 16 + j * 18));
         }
      }

      for (int var9 = 0; var9 < 4; var9++) {
         SlotArmor var9x = new SlotArmor(this, this, par1InventoryPlayer, par1InventoryPlayer.j_() - 1 - var9, 44, 8 + var9 * 18, var9);
         this.a(var9x);
         this.armorSlots.add(var9x);
         this.leftSlots.add(var9x);
      }

      for (int var10 = 0; var10 < 5; var10++) {
         ArtefaktSlot var10x = new ArtefaktSlot(this, this, this.inventoryOwnerInfo.stInv, var10, 8 + var10 * 18, 108);
         this.a(var10x);
         this.leftSlots.add(var10x);
      }

      for (int var11 = 0; var11 < 3; var11++) {
         SlotDetector var11x = new SlotDetector(this, this, this.inventoryOwnerInfo.stInv, var11 + 5, 26 + var11 * 18, 131);
         this.a(var11x);
         this.leftSlots.add(var11x);
      }

      for (int var12 = 0; var12 < 4; var12++) {
         SlotMedicine var12x = new SlotMedicine(this, this, this.inventoryOwnerInfo.stInv, var12 + 8, 17 + var12 * 18, 85);
         this.a(var12x);
         this.leftSlots.add(var12x);
      }

      this.backpackSlot = new SlotBackpack(inventoryOwner, this, this, this.inventoryOwnerInfo.stInv, 12, 44, 155);
      this.a(this.backpackSlot);
      this.leftSlots.add(this.backpackSlot);

      for (int var131 = 0; var131 < 8; var131++) {
         StalkerSlot var13x = new StalkerSlot(this, this, this.inventoryOwnerInfo.stInv, var131 + 13, 201, 16 + var131 * 18);
         this.backpackSlots.add(var13x);
         this.a(var13x);
      }

      this.hasBackpackSlots = this.hasBackpack();
   }

   public boolean a(uf par1EntityPlayer) {
      return par1EntityPlayer == this.containerUser;
   }

   public ye b(uf par1EntityPlayer, int par2) {
      return null;
   }

   public ye a(int par1, int par2, int par3, uf par4EntityPlayer) {
      if (par1 >= 0 && this.a(par1) != null && !this.isSlotActive(this.a(par1))) {
         return null;
      } else if (par2 == 1 && par1 >= 0 && this.a(par1) != null && this.a(par1).e()) {
         PlayerUtils.addItem(par4EntityPlayer, this.a(par1).d());
         this.a(par1).c((ye)null);
         return null;
      } else {
         return super.a(par1, par2, par3, par4EntityPlayer);
      }
   }

   @Override
   public boolean hasBackpack() {
      return this.backpackSlot.e();
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
               PlayerUtils.addItem(this.inventoryOwner, stack);
            }

            this.b();
         }
      }
   }

   public void b() {
      boolean hasItemsChanged = false;

      for (int i = 0; i < super.c.size(); i++) {
         ye itemstack = ((we)super.c.get(i)).d();
         ye itemstack1 = (ye)super.b.get(i);
         if (!ye.b(itemstack1, itemstack)) {
            hasItemsChanged = true;
            itemstack1 = itemstack == null ? null : itemstack.m();
            super.b.set(i, itemstack1);

            for (int j = 0; j < super.e.size(); j++) {
               ((vi)super.e.get(j)).a(this, i, itemstack1);
            }
         }
      }

      if (hasItemsChanged && this.inventoryOwnerInfo instanceof IPlayerServerInfo) {
         IPlayerServerInfo par2 = (IPlayerServerInfo)this.inventoryOwnerInfo;
         par2.updateWeightSpeed();
      }
   }

   @Override
   public ArrayList getBackpackSlots() {
      return this.backpackSlots;
   }

   @Override
   public ArrayList getArmorSlots() {
      return this.armorSlots;
   }

   @Override
   public of getOwner() {
      return this.inventoryOwner;
   }

   @Override
   public boolean isSlotActive(we slot) {
      return this.hasBackpack() || !this.backpackSlots.contains(slot);
   }
}
