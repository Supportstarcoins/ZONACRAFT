package ru.demon.money.utils;

import java.util.ArrayList;
import java.util.List;
import ru.demon.money.player.PlayerInfo;

public class PlayerMoneyUtils {
   public static List<PlayerInfo> playersInfo = new ArrayList<>();
   public static List<uf> players = new ArrayList<>();

   public void setPlayerInfo(PlayerInfo player) {
      if (!players.contains(player.player)) {
         playersInfo.add(player);
         players.add(player.player);
      }
   }

   public static PlayerInfo getInfo(uf player) {
      PlayerInfo playerInfo = null;

      for (PlayerInfo getPlayerInfo : playersInfo) {
         if (getPlayerInfo.player == player) {
            playerInfo = getPlayerInfo;
         }
      }

      return playerInfo;
   }
}
