package ru.stalcraft.client.gui.shop;

import java.util.ArrayList;
import java.util.List;
import ru.stalcraft.client.shop.ShopItem;

public class GuiScrollSlotItem implements IGuiScrollable {
   public bgw renderItem = new bgw();
   private List<GuiSlotItem> itemsSlot = new ArrayList<>();
   private List<ShopItem> itemsShop;

   public GuiScrollSlotItem(List<ShopItem> itemsShop) {
      this.itemsShop = itemsShop;
   }

   @Override
   public void initScroll(int x, int y) {
      int l = this.itemsShop.size();
      this.itemsSlot.clear();
      int k = 0;
      int h = 0;

      for (int i = 0; i < l; i++) {
         ShopItem shopItem = this.itemsShop.get(i);
         k = i % 2;
         if (i > 0 && i % 2 == 0) {
            h++;
         }

         GuiSlotItem slotItem = new GuiSlotItem(shopItem, this.renderItem, x + 150 * k, y + 49 * h);
         slotItem.initSlot();
         this.itemsSlot.add(slotItem);
      }
   }

   @Override
   public int getScrollSize() {
      int size = 0;
      if (this.itemsShop.size() > 8) {
         size = ls.f(this.itemsShop.size() / 2.0F) - 4;
      }

      return 49 * size;
   }

   @Override
   public int getScrollTotalHeight() {
      return this.itemsShop.size();
   }

   @Override
   public int getScrollHeightPerPage() {
      return 1;
   }

   @Override
   public int getScrollWidth() {
      return 602;
   }

   @Override
   public int getScrollHeight() {
      return 385;
   }

   @Override
   public void drawScrollable(atv mc, int mouseWidth, int mouseHeight, int scrollPosY) {
      int l = this.itemsSlot.size();

      for (int i = 0; i < l; i++) {
         this.itemsSlot.get(i).drawSlot(mc, mouseWidth, mouseHeight, scrollPosY, this);
      }
   }

   @Override
   public void drawScreen(atv mc, int mouseWidth, int mouseHeight) {
      int l = this.itemsSlot.size();

      for (int i = 0; i < l; i++) {
         this.itemsSlot.get(i).drawScreen(mc, mouseWidth, mouseHeight);
      }
   }

   @Override
   public void actionPerformed(aut button) {
      int l = this.itemsSlot.size();
   }

   public void drawPlayer() {
      int l = this.itemsSlot.size();

      for (int i = 0; i < l; i++) {
         this.itemsSlot.get(i).drawPlayer();
      }
   }

   public void disableDrawArmor(GuiSlotItem slot) {
      int l = this.itemsSlot.size();

      for (int i = 0; i < l; i++) {
         if (this.itemsSlot.get(i) != slot) {
            this.itemsSlot.get(i).renderArmor = false;
         }
      }
   }
}
