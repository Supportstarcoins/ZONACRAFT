package ru.demon.money.server;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import java.util.Iterator;
import ru.demon.money.player.PlayerInfo;
import ru.demon.money.utils.PlayerMoneyUtils;

public class ServerTick implements ITickHandler {
   public void tickStart(EnumSet<TickType> type, Object... tickData) {
      Iterator it = PlayerMoneyUtils.players.iterator();

      while (it.hasNext()) {
         uf player = (uf)it.next();
         PlayerInfo playerInfo = PlayerMoneyUtils.getInfo(player);
         if (player.M) {
            PlayerMoneyUtils.playersInfo.remove(playerInfo);
            it.remove();
         }
      }
   }

   public void tickEnd(EnumSet<TickType> type, Object... tickData) {
   }

   public EnumSet<TickType> ticks() {
      return EnumSet.of(TickType.SERVER);
   }

   public String getLabel() {
      return "ServerTick";
   }
}
