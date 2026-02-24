package ru.demon.money.server.player;

import cpw.mods.fml.common.IPlayerTracker;
import ru.demon.money.MoneyMod;
import ru.demon.money.client.player.PlayerClientInfo;
import ru.demon.money.player.PlayerInfo;
import ru.demon.money.server.network.ServerPacketMoneySender;
import ru.demon.money.utils.PlayerMoneyUtils;

public class PlayerServerTracker implements IPlayerTracker {
   public void onPlayerLogin(uf player) {
      PlayerInfo playerInfo = PlayerMoneyUtils.getInfo(player);
      ServerPacketMoneySender.sendMoney(player, playerInfo.getPersistedTag().e("moneyValue"));
   }

   public void onPlayerLogout(uf player) {
   }

   public void onPlayerChangedDimension(uf player) {
   }

   public void onPlayerRespawn(uf player) {
      abw worldObj = player.q;
      MoneyMod.instance.playerUtils.setPlayerInfo(new PlayerClientInfo(player));
      MoneyMod.instance.playerUtils.setPlayerInfo(new PlayerServerInfo(player));
      PlayerInfo playerInfo = PlayerMoneyUtils.getInfo(player);
      if (playerInfo != null) {
         ServerPacketMoneySender.sendMoney(player, playerInfo.getPersistedTag().e("moneyValue"));
      }
   }
}
