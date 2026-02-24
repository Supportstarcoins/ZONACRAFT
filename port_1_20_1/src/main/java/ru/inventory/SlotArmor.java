package ru.stalcraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.items.IStalkerArmor;

class SlotArmor extends StalkerSlot {
   final int armorType;

   SlotArmor(uy parent, ICustomContainer customContainer, mo par2IInventory, int par3, int par4, int par5, int par6) {
      super(parent, customContainer, par2IInventory, par3, par4, par5);
      this.armorType = par6;
   }

   public int a() {
      return 1;
   }

   public boolean a(ye par1ItemStack) {
      yc item = par1ItemStack == null ? null : par1ItemStack.b();
      boolean isValidArmor = item != null && item.isValidArmor(par1ItemStack, this.armorType, super.customContainer.getOwner());
      boolean isSuitable = true;
      if (item instanceof IStalkerArmor && ((IStalkerArmor)item).getSetID() != null) {
         IStalkerArmor newArmor = (IStalkerArmor)item;

         for (we slot : super.customContainer.getArmorSlots()) {
            if (slot.e() && slot.d().b() instanceof IStalkerArmor) {
               IStalkerArmor armor = (IStalkerArmor)slot.d().b();
               if (armor.getSetID() != null && !armor.getSetID().equals(newArmor.getSetID()) && newArmor.getArmorType() != armor.getArmorType()) {
                  isSuitable = false;
                  break;
               }
            }
         }
      }

      return isValidArmor && isSuitable;
   }

   @SideOnly(Side.CLIENT)
   public ms c() {
      return wh.b(this.armorType);
   }
}
