package ru.demon.money.server.player;

import ru.demon.money.player.PlayerInfo;
import ru.demon.money.server.network.ServerPacketMoneySender;

public class PlayerServerInfo extends PlayerInfo {
   public PlayerServerInfo(uf player) {
      super(player);
   }

   @Override
   public void tick() {
   }

   @Override
   public void addMoneyValue(int value) {
      by tag = this.getPersistedTag();
      int moneyValue = tag.e("moneyValue");
      int newMoneyValue = moneyValue + value;
      tag.a("moneyValue", Integer.valueOf(newMoneyValue));
      ServerPacketMoneySender.sendMoney(this.player, tag.e("moneyValue"));
   }

   @Override
   public by getPersistedTag() {
      by data = this.player.getEntityData();
      if (!data.b("PlayerPersisted")) {
         data.a("PlayerPersisted", new by());
      }

      return data.l("PlayerPersisted");
   }
}
