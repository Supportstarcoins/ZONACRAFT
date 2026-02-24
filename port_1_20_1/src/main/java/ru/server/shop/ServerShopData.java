package ru.stalcraft.server.shop;

import java.util.ArrayList;
import java.util.List;
import ru.stalcraft.server.network.ServerPacketSender;

public class ServerShopData {
   public List<Integer> contentsNewCaseID = new ArrayList<>();
   public List<Integer> stackSizeNewCaseID = new ArrayList<>();
   public List<Integer> armorIds = new ArrayList<>();
   public List<Integer> armorPrices = new ArrayList<>();
   public List<Integer> weaponIds = new ArrayList<>();
   public List<Integer> weaponPrices = new ArrayList<>();
   public List<Integer> miscIds = new ArrayList<>();
   public List<Integer> miscPrices = new ArrayList<>();

   public void tick() {
      for (int i = 0; i < this.contentsNewCaseID.size(); i++) {
         int content = this.contentsNewCaseID.get(i);
         int stackSize = this.stackSizeNewCaseID.get(i);
         ServerPacketSender.sendAddingContent(content, stackSize);
         if (i >= this.contentsNewCaseID.size()) {
            break;
         }
      }

      for (int ix = 0; ix < this.armorIds.size(); ix++) {
         int id = this.armorIds.get(ix);
         int price = this.armorPrices.get(ix);
         ServerPacketSender.sendAddItemShop(id, price, 0);
         if (ix >= this.armorIds.size()) {
            break;
         }
      }

      for (int ixx = 0; ixx < this.weaponIds.size(); ixx++) {
         int id = this.weaponIds.get(ixx);
         int price = this.weaponPrices.get(ixx);
         ServerPacketSender.sendAddItemShop(id, price, 1);
         if (ixx >= this.weaponIds.size()) {
            break;
         }
      }

      for (int ixxx = 0; ixxx < this.miscIds.size(); ixxx++) {
         int id = this.miscIds.get(ixxx);
         int price = this.miscPrices.get(ixxx);
         ServerPacketSender.sendAddItemShop(id, price, 2);
         if (ixxx >= this.miscIds.size()) {
            break;
         }
      }
   }

   public void addNewCaseContent(int id, int stackSize) {
      this.contentsNewCaseID.add(id);
      this.stackSizeNewCaseID.add(stackSize);
   }

   public void addArmor(int id, int price) {
      this.armorIds.add(id);
      this.armorPrices.add(price);
   }

   public void addWeapon(int id, int price) {
      this.weaponIds.add(id);
      this.weaponPrices.add(price);
   }

   public void addMisc(int id, int price) {
      this.miscIds.add(id);
      this.miscPrices.add(price);
   }
}
