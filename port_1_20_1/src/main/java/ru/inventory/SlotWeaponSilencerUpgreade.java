package ru.stalcraft.inventory;

import ru.stalcraft.items.ItemSilencer;

public class SlotWeaponSilencerUpgreade extends StalkerSlot {
   public WeaponInventory f;
   public boolean isPistol;

   public SlotWeaponSilencerUpgreade(uy parent, ICustomContainer customContainer, mo inventory, int index, int x, int y, boolean isPistol) {
      super(parent, customContainer, inventory, index, x, y);
      this.f = (WeaponInventory)inventory;
      this.isPistol = isPistol;
   }

   public boolean a(ye stack) {
      ItemSilencer itemSilencer = (ItemSilencer)stack.b();
      return stack.b() instanceof ItemSilencer && itemSilencer.index > 0 ? this.isPistol : !this.isPistol;
   }
}
