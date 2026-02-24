package ru.demon.money.client.network;

import ru.demon.money.network.DebugGroup;
import ru.demon.money.network.DebugPriority;
import ru.demon.money.network.IOpcodeClient;
import ru.demon.money.utils.PlayerMoneyUtils;

public enum ServerOpcode implements IOpcodeClient {
   SEND_MONEY_INFO("SEND_CLAN_INFO", DebugPriority.LOW, DebugGroup.OTHER) {
      @Override
      public void handle(String... data) {
         int moneyValue = Integer.parseInt(data[0]);
         PlayerMoneyUtils.getInfo(atv.w().h).setMoney(moneyValue);
      }
   };

   private final DebugPriority priority;
   private final DebugGroup group;
   private static final ServerOpcode[] $VALUES = new ServerOpcode[]{SEND_MONEY_INFO};

   private ServerOpcode(String var1, DebugPriority priority, DebugGroup group) {
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
