package ru.demon.money.server.network;

import ru.demon.money.network.DebugGroup;
import ru.demon.money.network.DebugPriority;
import ru.demon.money.network.IOpcodeServer;
import ru.demon.money.utils.PlayerMoneyUtils;

public enum ClientOpcode implements IOpcodeServer {
   SEND_ADDING_MONEY("SEND_ADDING_MONEY", DebugPriority.HIGH, DebugGroup.WEAPONS) {
      @Override
      public void handle(jv player, String... data) {
         int value = Integer.parseInt(data[0]);
         by tag = PlayerMoneyUtils.getInfo(player).getPersistedTag();
         int money = tag.e("moneyValue");
         int newMoney = money + value;
         tag.a("moneyValue", newMoney);
         ServerPacketMoneySender.sendMoney(player, tag.e("moneyValue"));
      }
   };

   private final DebugPriority priority;
   private final DebugGroup group;
   private static final ClientOpcode[] $VALUES = new ClientOpcode[0];

   private ClientOpcode(String var1, DebugPriority priority, DebugGroup group) {
      this.priority = priority;
      this.group = group;
   }

   @Override
   public DebugPriority getPriority() {
      return this.priority;
   }

   @Override
   public DebugGroup getGroup() {
      return this.group;
   }

   @Override
   public int getOrdinal() {
      return this.ordinal();
   }

   @Override
   public String getName() {
      return this.name();
   }
}
