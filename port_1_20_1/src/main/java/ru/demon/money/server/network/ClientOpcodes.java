package ru.demon.money.server.network;

import ru.demon.money.network.DebugGroup;
import ru.demon.money.network.DebugPriority;
import ru.demon.money.network.IOpcode;

public enum ClientOpcodes implements IOpcode {
   SEND_MONEY_INFO("SEND_CLAN_INFO", DebugPriority.LOW, DebugGroup.OTHER) {};

   private final DebugPriority priority;
   private final DebugGroup group;

   private ClientOpcodes(String var1, DebugPriority priority, DebugGroup group) {
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
