package ru.stalcraft.server.player;

import cpw.mods.fml.common.IPlayerTracker;
import java.util.Iterator;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.CommonProxy;
import ru.stalcraft.server.WeaponServerInfo;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.network.ServerPacketSender;
import ru.stalcraft.tile.IPlayerQuitListener;

public class PlayerTracker implements IPlayerTracker {
   public void onPlayerLogin(uf player) {
      CommonProxy commonProxy = (CommonProxy)StalkerMain.getProxy();
      PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
      if (playerInfo.getHandcuffs()) {
         ServerPacketSender.sendHandcuffs(player, true);
      }

      playerInfo.sendUpdateStalkerContainer();
      by tag = playerInfo.getPersistedTag();
      ServerPacketSender.sendReputation(player);
      ServerPacketSender.sendDeathScore(player);
      ServerPacketSender.sendAllEnemyClans(player);
      ServerPacketSender.sendClanData(player);
      ServerPacketSender.sendAllTags(player);
      ServerPacketSender.sendPlayerTag(player);
      ServerPacketSender.sendUpdateDonateValue(player, tag.e("donateValue"));
      ServerPacketSender.sendUpdateCaseDonateValue(player, tag.e("caseValue"));
      ServerPacketSender.sendUpdateMoneyValue(player, tag.e("moneyValue"));
      ServerPacketSender.sendClanLands(player);
      ServerPacketSender.sendClansList(player);
      ServerPacketSender.sendClanInformation(player);
      ServerPacketSender.sendClanMembers(player);
      ServerPacketSender.sendClanRules(player);
      ServerPacketSender.sendClanInformation(player);
      if (player.aN() <= 0.0F) {
         ServerPacketSender.sendForceCooldown(player);
      }

      ClanManager.instance().tryAddPlayerToFlag(player);
   }

   public void onPlayerLogout(uf player) {
      PlayerServerInfo info = (PlayerServerInfo)PlayerUtils.getInfo(player);
      CommonProxy commonProxy = (CommonProxy)StalkerMain.getProxy();
      if (info.getLeashingPlayer() != null) {
         info.getLeashingPlayer().bn.a(new ye(StalkerMain.rope.cv, 1, 0));
      }

      Iterator i$ = info.quitListeners.iterator();
      IPlayerQuitListener listener = null;

      while (i$.hasNext()) {
         listener = (IPlayerQuitListener)i$.next();
         listener.onPlayerExit();
      }

      CommonProxy.antiRelog.addReloggingPlayer((jv)player);
      info.setPositionToPrevious();
   }

   public void onPlayerChangedDimension(uf player) {
   }

   public void onPlayerRespawn(uf player) {
      PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
      PlayerSavedDrop.retrieveDrops(player);
      playerInfo.sendUpdateStalkerContainer();
      playerInfo.resetInfo(new WeaponServerInfo(player), player);
      playerInfo.teleportToSpawnPoint();
      by tag = playerInfo.getPersistedTag();
      ServerPacketSender.sendUpdateDonateValue(player, tag.e("donateValue"));
      ServerPacketSender.sendUpdateCaseDonateValue(player, tag.e("caseValue"));
      ServerPacketSender.sendUpdateMoneyValue(player, tag.e("moneyValue"));
   }
}
