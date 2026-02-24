package ru.stalcraft.inventory;

import ru.stalcraft.items.ItemWeaponSightAcog;
import ru.stalcraft.items.ItemWeaponSightPO;
import ru.stalcraft.items.ItemWeaponSightPSO;

public class SlotWeaponSightUpgreade extends StalkerSlot {
   public WeaponInventory f;
   public int sightIndex;

   public SlotWeaponSightUpgreade(uy parent, ICustomContainer customContainer, mo inventory, int index, int x, int y, int sightIndex) {
      super(parent, customContainer, inventory, index, x, y);
      this.f = (WeaponInventory)inventory;
      this.sightIndex = sightIndex;
   }

   public boolean a(ye stack) {
      return this.sightIndex > 0
         ? (this.sightIndex == 1 ? stack.b() instanceof ItemWeaponSightAcog : (this.sightIndex == 2 ? stack.b() instanceof ItemWeaponSightPO : false))
         : stack.b() instanceof ItemWeaponSightPSO;
   }
}
