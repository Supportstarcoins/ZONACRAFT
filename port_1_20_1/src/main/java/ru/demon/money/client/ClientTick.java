package ru.demon.money.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;

public class ClientTick implements ITickHandler {
   public atv mc = atv.w();

   public void tickStart(EnumSet<TickType> type, Object... tickData) {
   }

   public void tickEnd(EnumSet<TickType> type, Object... tickData) {
   }

   public EnumSet<TickType> ticks() {
      return EnumSet.of(TickType.CLIENT);
   }

   public String getLabel() {
      return "ClientTick";
   }
}
