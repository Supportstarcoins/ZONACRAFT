package ru.stalcraft.inventory;

import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemPNV;
import ru.stalcraft.items.ItemSkin;

public class SlotCustomArmor extends SlotArmor {
   public nn player;

   public SlotCustomArmor(nn player, uy parent, ICustomContainer customContainer, mo par2iInventory, int par3, int par4, int par5, int par6) {
      super(parent, customContainer, par2iInventory, par3, par4, par5, par6);
      this.player = player;
   }

   @Override
   public boolean a(ye stack) {
      if (this.player instanceof uf && stack.b() instanceof ItemArmorArtefakt && this.getSlotIndex() == 38) {
         uf player = (uf)this.player;
         ItemArmorArtefakt info = (ItemArmorArtefakt)stack.b();
         return ((StalkerContainer)this.customContainer).backpackSlot.d() != null && info.backpack
            || ((StalkerContainer)this.customContainer).backpackSlot.d() == null;
      } else if (this.getSlotIndex() == 39) {
         return !(stack.b() instanceof ItemArmorArtefakt) && stack.b() instanceof ItemPNV;
      } else {
         return this.getSlotIndex() != 37 ? false : !(stack.b() instanceof ItemArmorArtefakt) && stack.b() instanceof ItemSkin;
      }
   }
}
