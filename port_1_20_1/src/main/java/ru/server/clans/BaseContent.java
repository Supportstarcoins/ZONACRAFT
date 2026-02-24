package ru.stalcraft.server.clans;

public class BaseContent {
   public int itemID;
   public int price;
   public int cooldown;
   public boolean isCoolDown = false;

   public BaseContent(int itemID, int price, int cooldown) {
      this.itemID = itemID;
      this.price = price;
      this.cooldown = cooldown;
   }
}
