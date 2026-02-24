package ru.demon.money.client.player;

import ru.demon.money.player.PlayerInfo;

public class PlayerClientInfo extends PlayerInfo {
   public PlayerClientInfo(uf player) {
      super(player);
   }

   @Override
   public void tick() {
   }

   public int getMoney() {
      return this.money;
   }

   @Override
   public by getPersistedTag() {
      by data = this.player.getEntityData();
      if (!data.b("PlayerPersisted")) {
         data.a("PlayerPersisted", new by());
      }

      return data.l("PlayerPersisted");
   }

   @Override
   public void addMoneyValue(int value) {
      by tag = this.getPersistedTag();
      int moneyValue = tag.e("moneyValue");
      int newMoneyValue = moneyValue + value;
      tag.a("moneyValue", Integer.valueOf(newMoneyValue));
   }
}
