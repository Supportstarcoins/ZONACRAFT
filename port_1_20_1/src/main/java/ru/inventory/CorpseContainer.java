package ru.stalcraft.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import ru.stalcraft.entity.EntityCorpse;
import ru.stalcraft.player.PlayerUtils;

public class CorpseContainer extends uy implements ICustomContainer {
   public boolean isLocalWorld = false;
   private boolean hasBackpackSlots = false;
   public ArrayList backpackSlots = new ArrayList();
   public ArrayList armorSlots = new ArrayList();
   private SlotBackpack backpackSlot;
   private EntityCorpse corpse;
   private CorpseInventory corpseInventory;

   public CorpseContainer(EntityCorpse corpse, boolean par2) {
      this.isLocalWorld = par2;
      this.corpse = corpse;
      this.corpseInventory = corpse.inventory;
      this.a(new StalkerSlot(this, this, this.corpseInventory, 0, 26, 26));
      this.a(new StalkerSlot(this, this, this.corpseInventory, 1, 26, 44));
      this.a(new StalkerSlot(this, this, this.corpseInventory, 2, 62, 26));
      this.a(new StalkerSlot(this, this, this.corpseInventory, 3, 62, 44));

      for (int i = 0; i < 4; i++) {
         for (int j = 0; j < 8; j++) {
            this.a(new StalkerSlot(this, this, this.corpseInventory, i * 8 + j + 4, 125 + i * 18, 16 + j * 18));
         }
      }

      for (int var5 = 0; var5 < 4; var5++) {
         SlotArmor slot = new SlotArmor(this, this, this.corpseInventory, 39 - var5, 44, 8 + var5 * 18, var5);
         this.a(slot);
         this.armorSlots.add(slot);
      }

      for (int var61 = 0; var61 < 5; var61++) {
         this.a(new ArtefaktSlot(this, this, this.corpseInventory, 40 + var61, 8 + var61 * 18, 108));
      }

      for (int var7 = 0; var7 < 3; var7++) {
         this.a(new SlotDetector(this, this, this.corpseInventory, var7 + 45, 26 + var7 * 18, 131));
      }

      for (int var8 = 0; var8 < 4; var8++) {
         this.a(new SlotMedicine(this, this, this.corpseInventory, var8 + 48, 17 + var8 * 18, 85));
      }

      this.backpackSlot = new SlotBackpack(null, this, this, this.corpseInventory, 52, 44, 155);
      this.a(this.backpackSlot);

      for (int var9 = 0; var9 < 8; var9++) {
         StalkerSlot var6 = new StalkerSlot(this, this, this.corpseInventory, var9 + 53, 201, 16 + var9 * 18);
         this.backpackSlots.add(var6);
         this.a(var6);
      }

      this.hasBackpackSlots = this.hasBackpack();
      corpse.openedContainers.add(this);
   }

   public boolean a(uf par1EntityPlayer) {
      return true;
   }

   public ye b(uf par1EntityPlayer, int par2) {
      return null;
   }

   public ye a(int par1, int par2, int par3, uf par4EntityPlayer) {
      if (par1 >= 0 && !this.hasBackpack() && this.a(par1) != null && this.backpackSlots.contains(this.a(par1))) {
         return null;
      } else if (par2 == 1 && par1 >= 0 && this.a(par1) != null && this.a(par1).e()) {
         PlayerUtils.addItem(par4EntityPlayer, this.a(par1).d());
         this.a(par1).c((ye)null);
         return null;
      } else {
         return super.a(par1, par2, par3, par4EntityPlayer);
      }
   }

   public boolean a(ye par1ItemStack, we par2Slot) {
      return super.a(par1ItemStack, par2Slot);
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

            Iterator var15 = super.b.iterator();

            while (var15.hasNext()) {
               ye i$ = (ye)var15.next();
               if (i$ != null && inBackpack.contains(i$)) {
                  var15.remove();
               }
            }

            for (ye stack : inBackpack) {
               if (!this.corpseInventory.addItemStackToInventory(stack)) {
                  float f = 0.7F;
                  double d0 = this.corpse.q.s.nextFloat() * f + (1.0F - f) * 0.5;
                  double d1 = this.corpse.q.s.nextFloat() * f + (1.0F - f) * 0.5;
                  double d2 = this.corpse.q.s.nextFloat() * f + (1.0F - f) * 0.5;
                  ss entityitem = new ss(this.corpse.q, this.corpse.u + d0, this.corpse.v + d1, this.corpse.w + d2, stack);
                  entityitem.b = 10;
                  this.corpse.q.d(entityitem);
               }
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
   public ArrayList getArmorSlots() {
      return this.armorSlots;
   }

   @Override
   public of getOwner() {
      return this.corpse;
   }

   @Override
   public boolean isSlotActive(we slot) {
      return this.hasBackpack() || !this.getBackpackSlots().contains(slot);
   }

   public void b(uf par1EntityPlayer) {
      super.b(par1EntityPlayer);
      this.corpse.openedContainers.remove(this);
   }
}
