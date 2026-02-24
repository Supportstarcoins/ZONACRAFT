package ru.stalcraft.inventory.shop;

public class ContainerPersonalWarehouse extends uy {
   private mo lowerChestInventory;
   private int numRows;

   public ContainerPersonalWarehouse(mo par1IInventory, mo par2IInventory) {
      this.lowerChestInventory = par2IInventory;
      this.numRows = par2IInventory.j_() / 9;
      par2IInventory.k_();
      int i = (this.numRows - 4) * 18;

      for (int j = 0; j < this.numRows; j++) {
         for (int k = 0; k < 9; k++) {
            this.a(new SlotPersonal(par2IInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
         }
      }

      for (int var6 = 0; var6 < 3; var6++) {
         for (int k = 0; k < 9; k++) {
            this.a(new we(par1IInventory, k + var6 * 9 + 9, 8 + k * 18, 103 + var6 * 18 + i));
         }
      }

      for (int var7 = 0; var7 < 9; var7++) {
         this.a(new we(par1IInventory, var7, 8 + var7 * 18, 161 + i));
      }
   }

   public boolean a(uf par1EntityPlayer) {
      return this.lowerChestInventory.a(par1EntityPlayer);
   }

   public ye b(uf par1EntityPlayer, int par2) {
      ye itemstack = null;
      we slot = (we)this.c.get(par2);
      if (slot != null && slot.e()) {
         ye itemstack1 = slot.d();
         itemstack = itemstack1.m();
         if (par2 < this.numRows * 9) {
            if (!this.a(itemstack1, this.numRows * 9, this.c.size(), true)) {
               return null;
            }
         } else if (!this.a(itemstack1, 0, this.numRows * 9, false)) {
            return null;
         }

         if (itemstack1.b == 0) {
            slot.c((ye)null);
         } else {
            slot.f();
         }
      }

      return itemstack;
   }

   public void b(uf par1EntityPlayer) {
      super.b(par1EntityPlayer);
      this.lowerChestInventory.g();
   }

   public mo getLowerChestInventory() {
      return this.lowerChestInventory;
   }
}
