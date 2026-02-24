package ru.stalcraft.inventory;

import ru.stalcraft.items.ItemMedicine;

public class SlotMedicine extends StalkerSlot {
   public SlotMedicine(uy parent, ICustomContainer customContainer, mo par1iInventory, int par2, int par3, int par4) {
      super(parent, customContainer, par1iInventory, par2, par3, par4);
   }

   public boolean a(ye par1ItemStack) {
      return par1ItemStack.b() instanceof ItemMedicine;
   }
}
