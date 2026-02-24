package ru.stalcraft.inventory.shop;

import ru.stalcraft.player.PlayerUtils;

public class SlotPersonal extends we {
   public SlotPersonal(mo par1iInventory, int par2, int par3, int par4) {
      super(par1iInventory, par2, par3, par4);
   }

   public boolean a(ye stack) {
      by tag = PlayerUtils.getTag(stack);
      return tag != null && tag.e("no_drop") > 0;
   }
}
