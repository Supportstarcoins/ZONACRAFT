package ru.stalcraft.client.shop;

import java.util.List;

public class ShopContentItems {
   public List<ShopItem> itemsShop;
   public String name;

   public ShopContentItems(String name, List<ShopItem> itemsShop) {
      this.name = name;
      this.itemsShop = itemsShop;
   }
}
