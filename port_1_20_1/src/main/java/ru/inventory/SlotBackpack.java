package ru.stalcraft.inventory;

import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemBackpack;

public class SlotBackpack extends StalkerSlot {
   public nn player;

   public SlotBackpack(nn player, uy parent, ICustomContainer customContainer, mo par1iInventory, int par2, int par3, int par4) {
      super(parent, customContainer, par1iInventory, par2, par3, par4);
      this.player = (uf)player;
   }

   public boolean a(ye stack) {
      return this.player instanceof uf
            && ((uf)this.player).o(2) != null
            && ((ItemArmorArtefakt)((uf)this.player).o(2).b()).backpack
            && stack.b() instanceof ItemBackpack
         || ((uf)this.player).o(2) == null && stack.b() instanceof ItemBackpack;
   }

   public int a() {
      return 1;
   }
}
