package ru.stalcraft.server.capture;

import java.util.Iterator;
import java.util.List;
import ru.stalcraft.clans.ClanMember;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.CommonProxy;
import ru.stalcraft.server.clans.Clan;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.clans.Flag;
import ru.stalcraft.server.clans.FlagManager;
import ru.stalcraft.server.network.ServerPacketSender;
import ru.stalcraft.server.player.PlayerServerInfo;
import ru.stalcraft.tile.TileEntityFlag;

public class CaptureManager {
   public CommonProxy proxy;

   public CaptureManager(CommonProxy proxy) {
      this.proxy = proxy;
   }

   public void onFlagCapture(TileEntityFlag tileFlag) {
      Flag flag = FlagManager.instance().getFlagByPos(tileFlag.k.t.i, tileFlag.l, tileFlag.m, tileFlag.n);
      if (flag != null) {
         List<jv> players = tileFlag.k
            .a(
               jv.class,
               asx.a(
                  (float)flag.x - flag.captureSize,
                  (float)flag.y - flag.captureSize,
                  (float)flag.z - flag.captureSize,
                  flag.x + 1.0F + flag.captureSize,
                  flag.y + 1.0F + flag.captureSize,
                  flag.z + 1.0F + flag.captureSize
               )
            );
         int l = players.size();
         boolean defending = false;
         if (!flag.isCapture) {
            flag.invader = null;
            flag.capture = 0;
         }

         if (flag.isCapture) {
            if (flag.capture == 0) {
               flag.owner = null;
            }

            for (uf entityPlayer : players) {
               ServerPacketSender.sendFlagCapture(entityPlayer, flag.capture, flag.captureTime);
               Clan clan = ClanManager.instance().getPlayerClan(entityPlayer);
               ClanMember var10000 = clan.getClanMember(entityPlayer);
               var10000.loyalePoint = var10000.loyalePoint + clan.loyalePoint;
            }
         }

         if (l > 0) {
            for (jv player : players) {
               PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
               IClan clan = playerInfo.getClan();
               if (clan != null) {
                  if (flag.isCapture && flag.owner == null && flag.owner == null) {
                     flag.invader = (Clan)clan;
                  } else if (flag.isCapture && flag.owner != null && flag.owner != clan && flag.owner.isClanEnemy(clan) && flag.invader == null) {
                     flag.invader = (Clan)clan;
                  } else if (flag.owner != null && flag.owner == clan && flag.owner != null) {
                     defending = true;
                     ServerPacketSender.sendCaptureData(player, clan.getName(), "non");
                  }

                  String clanName = clan.getName();
                  if (flag.invader != null && clanName.equals(flag.invader.getName()) && !flag.invaders.contains(player)) {
                     flag.invaders.add(player);
                  } else {
                     ServerPacketSender.sendFlagCapture(player, -1, 0);
                  }
               }
            }
         }

         Iterator it = flag.invaders.iterator();

         while (it.hasNext()) {
            jv playerx = (jv)it.next();
            if (!players.contains(playerx) || !flag.isCapture) {
               ServerPacketSender.sendFlagCapture(playerx, -1, 0);
               it.remove();
            }
         }

         if (flag != null && flag.isCapture && players != null) {
            ServerPacketSender.sendInfoAllCapturePlayer(players, flag.capture, flag.captureTime);
         }

         if (l > 0) {
            l = flag.invaders.size();
            flag.capture = flag.capture + flag.captureRate * l;
            if (flag.capture >= flag.captureTime && flag.invader != null) {
               flag.owner = flag.invader;

               for (jv playerx : flag.invaders) {
                  if (players.contains(playerx)) {
                     ServerPacketSender.sendFlagCapture(playerx, flag.capture, flag.captureTime);
                     ServerPacketSender.sendCaptureSize(playerx, flag.captureSize);
                  }
               }

               flag.invader = null;
               flag.capture = 0;
               flag.isCapture = false;
               return;
            }
         }

         for (jv playerxx : flag.invaders) {
            Clan clanInvader = (Clan)PlayerUtils.getInfo(playerxx).getClan();
            if (players.contains(playerxx)) {
               ServerPacketSender.sendFlagCapture(playerxx, flag.capture, flag.captureTime);
               ServerPacketSender.sendCaptureSize(playerxx, flag.captureSize);
               ServerPacketSender.sendCaptureData(playerxx, "null", clanInvader.getName());
            }
         }
      }
   }
}
