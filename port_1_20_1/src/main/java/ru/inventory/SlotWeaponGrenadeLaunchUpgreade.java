package ru.stalcraft.inventory;

import ru.stalcraft.items.ItemGrenadeLauncherKoster;
import ru.stalcraft.items.ItemGrenadeLauncherM203;

public class SlotWeaponGrenadeLaunchUpgreade extends StalkerSlot {
   public WeaponInventory f;
   public int indexGrenadeLauncher;

   public SlotWeaponGrenadeLaunchUpgreade(uy parent, ICustomContainer customContainer, mo inventory, int index, int x, int y, int indexGrenadeLauncher) {
      super(parent, customContainer, inventory, index, x, y);
      this.f = (WeaponInventory)inventory;
      this.indexGrenadeLauncher = indexGrenadeLauncher;
   }

   public boolean a(ye stack) {
      return this.indexGrenadeLauncher > 0 ? stack.b() instanceof ItemGrenadeLauncherM203 : stack.b() instanceof ItemGrenadeLauncherKoster;
   }
}
