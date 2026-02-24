package ru.stalcraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StalkerInventory implements mo {
   public ye[] mainInventory = new ye[21];
   public uf player;
   public boolean inventoryChanged;

   public StalkerInventory(uf par1EntityPlayer) {
      this.player = par1EntityPlayer;
   }

   private int getInventorySlotContainItem(int par1) {
      for (int j = 0; j < this.mainInventory.length; j++) {
         if (this.mainInventory[j] != null && this.mainInventory[j].d == par1) {
            return j;
         }
      }

      return -1;
   }

   @SideOnly(Side.CLIENT)
   private int getInventorySlotContainItemAndDamage(int par1, int par2) {
      for (int k = 0; k < this.mainInventory.length; k++) {
         if (this.mainInventory[k] != null && this.mainInventory[k].d == par1 && this.mainInventory[k].k() == par2) {
            return k;
         }
      }

      return -1;
   }

   private int storeItemStack(ye par1ItemStack) {
      if (this.mainInventory[12] != null) {
         for (int i = 13; i < 21; i++) {
            if (this.mainInventory[i] != null
               && this.mainInventory[i].d == par1ItemStack.d
               && this.mainInventory[i].f()
               && this.mainInventory[i].b < this.mainInventory[i].e()
               && this.mainInventory[i].b < this.d()
               && (!this.mainInventory[i].h() || this.mainInventory[i].k() == par1ItemStack.k())
               && ye.a(this.mainInventory[i], par1ItemStack)) {
               return i;
            }
         }
      }

      return -1;
   }

   public int getFirstEmptyStack() {
      if (this.mainInventory[12] != null) {
         for (int i = 13; i < 21; i++) {
            if (this.mainInventory[i] == null) {
               return i;
            }
         }
      }

      return -1;
   }

   public int clearInventory(int par1, int par2) {
      int k = 0;

      for (int l = 0; l < this.mainInventory.length; l++) {
         ye itemstack = this.mainInventory[l];
         if (itemstack != null && (par1 <= -1 || itemstack.d == par1) && (par2 <= -1 || itemstack.k() == par2)) {
            k += itemstack.b;
            this.mainInventory[l] = null;
         }
      }

      return k;
   }

   private int storePartialItemStack(ye par1ItemStack) {
      int i = par1ItemStack.d;
      int j = par1ItemStack.b;
      if (par1ItemStack.e() == 1) {
         int k = this.getFirstEmptyStack();
         if (k < 0) {
            return j;
         } else {
            if (this.mainInventory[k] == null) {
               this.mainInventory[k] = ye.b(par1ItemStack);
            }

            return 0;
         }
      } else {
         int k = this.storeItemStack(par1ItemStack);
         if (k < 0) {
            k = this.getFirstEmptyStack();
         }

         if (k < 0) {
            return j;
         } else {
            if (this.mainInventory[k] == null) {
               this.mainInventory[k] = new ye(i, 0, par1ItemStack.k());
               if (par1ItemStack.p()) {
                  this.mainInventory[k].d((by)par1ItemStack.q().b());
               }
            }

            int l = j;
            if (j > this.mainInventory[k].e() - this.mainInventory[k].b) {
               l = this.mainInventory[k].e() - this.mainInventory[k].b;
            }

            if (l > this.d() - this.mainInventory[k].b) {
               l = this.d() - this.mainInventory[k].b;
            }

            if (l == 0) {
               return j;
            } else {
               j -= l;
               this.mainInventory[k].b += l;
               this.mainInventory[k].c = 5;
               return j;
            }
         }
      }
   }

   public void decrementAnimations() {
      for (int i = 0; i < this.mainInventory.length; i++) {
         if (this.mainInventory[i] != null) {
            this.mainInventory[i].a(this.player.q, this.player, i, false);
         }
      }
   }

   public boolean consumeInventoryItem(int par1) {
      int j = this.getInventorySlotContainItem(par1);
      if (j < 0) {
         return false;
      } else {
         if (--this.mainInventory[j].b <= 0) {
            this.mainInventory[j] = null;
         }

         return true;
      }
   }

   public boolean hasItem(int par1) {
      int j = this.getInventorySlotContainItem(par1);
      return j >= 0;
   }

   public boolean addItemStackToInventory(ye par1ItemStack) {
      if (par1ItemStack == null) {
         return false;
      } else if (par1ItemStack.b == 0) {
         return false;
      } else {
         try {
            if (par1ItemStack.i()) {
               int throwable = this.getFirstEmptyStack();
               if (throwable >= 0) {
                  this.mainInventory[throwable] = ye.b(par1ItemStack);
                  this.mainInventory[throwable].c = 5;
                  par1ItemStack.b = 0;
                  return true;
               } else if (this.player.bG.d) {
                  par1ItemStack.b = 0;
                  return true;
               } else {
                  return false;
               }
            } else {
               int throwable;
               do {
                  throwable = par1ItemStack.b;
                  par1ItemStack.b = this.storePartialItemStack(par1ItemStack);
               } while (par1ItemStack.b > 0 && par1ItemStack.b < throwable);

               if (par1ItemStack.b == throwable && this.player.bG.d) {
                  par1ItemStack.b = 0;
                  return true;
               } else {
                  return par1ItemStack.b < throwable;
               }
            }
         } catch (Throwable var51) {
            b crashreport = b.a(var51, "Adding item to inventory");
            m crashreportcategory = crashreport.a("Item being added");
            crashreportcategory.a("Item ID", par1ItemStack.d);
            crashreportcategory.a("Item data", par1ItemStack.k());
            throw new u(crashreport);
         }
      }
   }

   public boolean isItemValidForSlot(uy par1, int par2, ye par3) {
      return par1.a(par2).a(par3);
   }

   public ye a(int par1, int par2) {
      ye[] aitemstack = this.mainInventory;
      if (aitemstack[par1] != null) {
         if (aitemstack[par1].b <= par2) {
            ye itemstack = aitemstack[par1];
            aitemstack[par1] = null;
            return itemstack;
         } else {
            ye itemstack = aitemstack[par1].a(par2);
            if (aitemstack[par1].b == 0) {
               aitemstack[par1] = null;
            }

            return itemstack;
         }
      } else {
         return null;
      }
   }

   public ye a_(int par1) {
      ye[] aitemstack = this.mainInventory;
      if (aitemstack[par1] != null) {
         ye itemstack = aitemstack[par1];
         aitemstack[par1] = null;
         return itemstack;
      } else {
         return null;
      }
   }

   public void a(int par1, ye par2ItemStack) {
      this.mainInventory[par1] = par2ItemStack;
   }

   public cg writeToNBT(cg par1NBTTagList) {
      for (int i = 0; i < this.mainInventory.length; i++) {
         if (this.mainInventory[i] != null) {
            by nbttagcompound = new by();
            nbttagcompound.a("Slot", (byte)i);
            this.mainInventory[i].b(nbttagcompound);
            par1NBTTagList.a(nbttagcompound);
         }
      }

      return par1NBTTagList;
   }

   public void readFromNBT(cg par1NBTTagList) {
      this.mainInventory = new ye[21];
      by nbttagcompound = null;
      int j = 0;
      ye itemstack = null;

      for (int i = 0; i < par1NBTTagList.c(); i++) {
         nbttagcompound = (by)par1NBTTagList.b(i);
         j = nbttagcompound.c("Slot") & 255;
         itemstack = ye.a(nbttagcompound);
         if (itemstack != null && j >= 0 && j < this.mainInventory.length) {
            this.mainInventory[j] = itemstack;
         }
      }
   }

   public int j_() {
      return this.mainInventory.length;
   }

   public ye a(int par1) {
      return this.mainInventory[par1];
   }

   public String b() {
      return "container.stalkerinventory";
   }

   public boolean c() {
      return false;
   }

   public int d() {
      return 64;
   }

   public void dropAllItems() {
      for (int i = 0; i < 12; i++) {
         if (this.mainInventory[i] != null) {
            this.player.a(this.mainInventory[i], true);
            this.mainInventory[i] = null;
         }
      }
   }

   public void e() {
      this.inventoryChanged = true;
   }

   public boolean a(uf par1EntityPlayer) {
      return this.player.M ? false : par1EntityPlayer.e(this.player) <= 64.0;
   }

   public boolean hasItemStack(ye par1ItemStack) {
      for (int i = 0; i < this.mainInventory.length; i++) {
         if (this.mainInventory[i] != null && this.mainInventory[i].a(par1ItemStack)) {
            return true;
         }
      }

      return false;
   }

   public void k_() {
   }

   public void g() {
   }

   public void copyInventory(StalkerInventory par1InventoryPlayer) {
      for (int i = 0; i < this.mainInventory.length; i++) {
         this.mainInventory[i] = ye.b(par1InventoryPlayer.mainInventory[i]);
      }
   }

   public int getBackpack() {
      return this.mainInventory[12] == null ? 0 : this.mainInventory[12].d;
   }

   public boolean b(int i, ye itemstack) {
      return false;
   }
}
