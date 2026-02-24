package ru.stalcraft.inventory.shop;

import java.util.List;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;

public class ShopContainer extends uy {
   public ShopInventory shopInventory;
   public uf player;
   public List<Integer> contents;
   public List<Integer> stackSize;

   public ShopContainer(uf player, List<Integer> contents, List<Integer> stackSize) {
      this.player = player;
      this.contents = contents;
      this.stackSize = stackSize;
      this.shopInventory = new ShopInventory();
   }

   public ye a(int par1, int par2, int par3, uf par4EntityPlayer) {
      return null;
   }

   public boolean a(uf entityplayer) {
      return true;
   }

   public void setSlotDrop(yc item) {
      super.b.clear();
      super.c.clear();
      SlotShop slot = new SlotShop(this.shopInventory, 0, 40, -40, 43, 43);
      super.a(slot);
      ye stack = new ye(item);
      if (item instanceof ItemWeapon || item instanceof ItemArmorArtefakt) {
         PlayerUtils.getTag(stack).a("no_drop", 1);
      }

      this.shopInventory.contents[0] = stack;
   }

   public void setSlotsNewContainer() {
      super.b.clear();
      super.c.clear();
      this.shopInventory.setContentSize(this.contents.size());

      for (int i = 0; i < this.contents.size(); i++) {
         yc item = yc.g[this.contents.get(i)];
         int height = 0;
         int width = 0;
         if (i > 5) {
            height = 50;
            width = -294;
         }

         if (i > 11) {
            height = 100;
            width = -588;
         }

         if (i > 17) {
            height = 150;
            width = -882;
         }

         if (i > 22) {
            height = 200;
            width = -1176;
         }

         SlotShop slot = new SlotShop(this.shopInventory, i, 40 + i * 49 + width, -40 + height, 43, 43);
         super.a(slot);
         ye stack = new ye(item, this.stackSize.get(i), 0);
         if (item instanceof ItemWeapon || item instanceof ItemArmorArtefakt) {
            PlayerUtils.getTag(stack).a("no_drop", 1);
         }

         this.shopInventory.contents[i] = stack;
      }
   }

   public void a(int par1, ye par2ItemStack) {
   }
}
