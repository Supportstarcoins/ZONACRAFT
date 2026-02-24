package ru.stalcraft.inventory.shop;

public class ShopInventory implements mo {
   public ye[] contents;

   public void setContentSize(int contentSize) {
      this.contents = new ye[contentSize];
   }

   public int j_() {
      return 4;
   }

   public ye a(int i) {
      return this.contents[i];
   }

   public ye a(int slot, int count) {
      if (this.contents[slot] != null) {
         if (this.contents[slot].b <= count) {
            ye itemstack = this.contents[slot];
            this.contents[slot] = null;
            return itemstack;
         } else {
            ye itemstack = this.contents[slot].a(count);
            if (this.contents[slot].b == 0) {
               this.contents[slot] = null;
            }

            return itemstack;
         }
      } else {
         return null;
      }
   }

   public ye a_(int slot) {
      if (this.contents[slot] != null) {
         ye itemstack = this.contents[slot];
         this.contents[slot] = null;
         return itemstack;
      } else {
         return null;
      }
   }

   public void a(int i, ye itemstack) {
      this.contents[i] = itemstack;
   }

   public String b() {
      return "StalkerShop";
   }

   public boolean c() {
      return false;
   }

   public void consumeInventoryItem(int index) {
      this.contents[index] = null;
   }

   public int d() {
      return 1;
   }

   public boolean a(uf entityplayer) {
      return true;
   }

   public boolean b(int i, ye itemstack) {
      return true;
   }

   public void e() {
   }

   public void k_() {
   }

   public void g() {
   }
}
