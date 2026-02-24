package ru.stalcraft.inventory;

import ru.stalcraft.items.ItemPoint;

public class SlotArmorStatUpgreade extends StalkerSlot {
   public SlotArmorStatUpgreade(uy parent, ICustomContainer customContainer, mo inventory, int index, int x, int y) {
      super(parent, customContainer, inventory, index, x, y);
   }

   public boolean a(ye stack) {
      return stack.b() instanceof ItemPoint && !((ItemPoint)stack.b()).isWeapon;
   }
}
