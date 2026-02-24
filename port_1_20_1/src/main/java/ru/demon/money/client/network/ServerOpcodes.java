package ru.demon.money.client.network;

import ru.demon.money.network.DebugGroup;
import ru.demon.money.network.DebugPriority;
import ru.demon.money.network.IOpcode;

public enum ServerOpcodes implements IOpcode {
   SEND_ADDING_MONEY("SEND_ADDING_MONEY", DebugPriority.HIGH, DebugGroup.WEAPONS) {};

   private final DebugPriority priority;
   private final DebugGroup group;

   private ServerOpcodes(String name, DebugPriority priority, DebugGroup group) {
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
