package ru.stalcraft.inventory;

public class SlotWeaponUpgrade extends StalkerSlot {
   public final int itemID;

   public SlotWeaponUpgrade(uy parent, ICustomContainer customContainer, mo par1iInventory, int index, int x, int y, int itemID) {
      super(parent, customContainer, par1iInventory, index, x, y);
      this.itemID = itemID;
   }

   public boolean a(ye par1ItemStack) {
      return par1ItemStack == null || par1ItemStack.d == this.itemID;
   }
}
