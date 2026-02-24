package ru.stalcraft.client.shop;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import ru.stalcraft.client.gui.shop.GuiShop;

public class ClientShopManager {
   public List<Integer> armorIds = new ArrayList<>();
   public List<Integer> armorPrices = new ArrayList<>();
   public List<Integer> weaponIds = new ArrayList<>();
   public List<Integer> weaponPrices = new ArrayList<>();
   public List<Integer> miscIds = new ArrayList<>();
   public List<Integer> miscPrices = new ArrayList<>();

   public void addArmor(int id, int price) {
      if (!this.armorIds.contains(id)) {
         this.armorIds.add(id);
         this.armorPrices.add(price);
      }
   }

   public void addWeapon(int id, int price) {
      if (!this.weaponIds.contains(id)) {
         this.weaponIds.add(id);
         this.weaponPrices.add(price);
      }
   }

   public void addMisc(int id, int price) {
      if (!this.miscIds.contains(id)) {
         this.miscIds.add(id);
         this.miscPrices.add(price);
      }
   }

   public void OpenGui(atv mc) {
      List<ShopItem> armors = new ArrayList<>();
      List<ShopItem> weapons = new ArrayList<>();
      List<ShopItem> miscs = new ArrayList<>();

      for (int i = 0; i < this.armorIds.size(); i++) {
         armors.add(new ShopItem(yc.g[this.armorIds.get(i)], this.armorPrices.get(i)));
      }

      for (int i = 0; i < this.weaponIds.size(); i++) {
         weapons.add(new ShopItem(yc.g[this.weaponIds.get(i)], this.weaponPrices.get(i)));
      }

      for (int i = 0; i < this.miscIds.size(); i++) {
         miscs.add(new ShopItem(yc.g[this.miscIds.get(i)], this.miscPrices.get(i)));
      }

      mc.a(
         new GuiShop(
            mc.h,
            Lists.newArrayList(
               new ShopContentItems[]{new ShopContentItems("Броня", armors), new ShopContentItems("Оружие", weapons), new ShopContentItems("Прочее", miscs)}
            )
         )
      );
   }
}
