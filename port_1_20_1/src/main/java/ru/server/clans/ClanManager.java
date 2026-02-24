package ru.stalcraft.server.clans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.server.MinecraftServer;
import ru.stalcraft.Logger;
import ru.stalcraft.WarningType;
import ru.stalcraft.clans.ClanMember;
import ru.stalcraft.clans.ClanRank;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.clans.IClanManager;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.CommonProxy;
import ru.stalcraft.server.network.ServerPacketSender;
import ru.stalcraft.server.player.PlayerServerInfo;

public class ClanManager implements IClanManager {
   private ArrayList clans = new ArrayList();
   public FlagManager flagManager = new FlagManager();
   private int clanTickTimer = 0;
   public SpecialClansConfig config = new SpecialClansConfig();

   public void onUpdate(abw world) {
      if (++this.clanTickTimer >= 20) {
         ArrayList clansToRemove = new ArrayList();
         Iterator i$ = this.clans.iterator();
         Clan clan = null;

         for (Clan var6 : clansToRemove) {
            this.removeClan(var6);
         }

         this.clanTickTimer = 0;
      }

      this.flagManager.onUpdate(world);
   }

   public ArrayList getClanFlags(Clan clan) {
      return FlagManager.instance().getClanFlags(clan);
   }

   public void trySyncReputation(uf player) {
      Clan clan = this.getPlayerClan(player);
      if (clan == null) {
         Logger.warning(player, WarningType.INVALID_USER, "update reputation");
      } else if (clan.getClanMember(player).rank == ClanRank.MEMBER) {
         Logger.warning(player, WarningType.ABUSE_OF_AUTHORITY, "update reputation");
      } else {
         int oldReputation = clan.getReputation();
         clan.syncReputation(false);
         player.a("Репутация группировки синхронизирована (изменена с " + oldReputation + " на " + clan.getReputation() + ")");
      }
   }

   public void tryAddPlayerToFlag(uf player) {
      PlayerInfo info = PlayerUtils.getInfo(player);
      if (info.getClan() != null && this.flagManager.getFlagByPlayer(player) == null) {
         ArrayList clanFlags = this.flagManager.getClanFlags(info.getClan());
         if (clanFlags.size() > 0) {
            ((Flag)clanFlags.get(player.q.s.nextInt(clanFlags.size()))).addMember(info.getClan().getClanMember(player));
         }
      }
   }

   public void tryGetMoney(uf player) {
      PlayerServerInfo info = (PlayerServerInfo)PlayerUtils.getInfo(player);
      IClan clan = info.getClan();
      if (clan.getSpecialClan() != null) {
         if (info.canGetSalary()) {
            info.onWithdrawSalary();
            PlayerUtils.addMoney(player, clan.getSpecialClan().getSalary(info.getReputation()));
         }
      } else if (clan.getClanMember(player).rank == ClanRank.LEADER) {
         clan.withdraw(player);
      }
   }

   public void trySetRank(uf officer, String username, ClanRank newRank) {
      Clan clan = (Clan)PlayerUtils.getInfo(officer).getClan();
      ClanMember officerClanMember = clan.getClanMember(officer.bu);
      if (officerClanMember.rank == ClanRank.MEMBER) {
         Logger.warning(officer, WarningType.ABUSE_OF_AUTHORITY, "set rank", username);
      } else {
         ClanMember playerClanMember = clan.getClanMember(username);
         if (playerClanMember != null && playerClanMember.rank != ClanRank.LEADER) {
            playerClanMember.rank = newRank;
            jv player = MinecraftServer.F().af().f(username);
            if (player != null) {
               ServerPacketSender.sendClanData(player);
            }

            ServerPacketSender.sendClanMembers(officer);
         } else {
            Logger.warning(officer, WarningType.INVALID_TARGET, "set rank", username);
         }
      }
   }

   public void trySetLeader(uf oldLeader, String username) {
      Clan clan = (Clan)PlayerUtils.getInfo(oldLeader).getClan();
      ClanMember oldLeaderClanMember = clan.getClanMember(oldLeader.bu);
      if (oldLeaderClanMember.rank != ClanRank.LEADER) {
         Logger.warning(oldLeader, WarningType.ABUSE_OF_AUTHORITY, "set clan leader", username);
      } else {
         ClanMember newLeaderClanMember = clan.getClanMember(username);
         if (newLeaderClanMember == null) {
            Logger.warning(oldLeader, WarningType.INVALID_TARGET, "set clan leader", username);
         } else {
            oldLeaderClanMember.rank = ClanRank.OFFICER;
            newLeaderClanMember.rank = ClanRank.LEADER;
            jv player = MinecraftServer.F().af().f(username);
            if (player != null) {
               ServerPacketSender.sendClanData(player);
            }

            ServerPacketSender.sendClanData(oldLeader);
            ServerPacketSender.sendClanMembers(oldLeader);
         }
      }
   }

   public void tryJoinClan(uf player, String clanName) {
      Clan clan = this.getClan(clanName);
      if (clan == null) {
         Logger.warning(player, WarningType.INVALID_TARGET, "join clan", clanName);
      } else if (PlayerUtils.getInfo(player).getClan() != null) {
         Logger.warning(player, WarningType.INVALID_USER, "join clan", clanName);
      } else if (!clan.hasInvited(player)) {
         Logger.warning(player, WarningType.ABUSE_OF_AUTHORITY, "join clan", clanName);
      } else {
         clan.joinClan(player);
         PlayerUtils.getInfo(player).setClan(clan);
         this.tryAddPlayerToFlag(player);
         ServerPacketSender.sendAllEnemyClans(player);
         ServerPacketSender.sendPlayerTag(player);
         ServerPacketSender.sendClanData(player);
         ServerPacketSender.sendClanGuiUpdate(player);
      }
   }

   public void tryAddRules(uf player, String newRules) {
      Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (clan == null) {
         Logger.warning(player, WarningType.INVALID_USER, "add clan rules");
      } else {
         ClanMember clanMember = clan.getClanMember(player);
         if (clanMember.rank == ClanRank.MEMBER) {
            Logger.warning(player, WarningType.ABUSE_OF_AUTHORITY, "add clan rules");
         } else {
            clan.rules = clan.rules + newRules;
         }
      }
   }

   public void tryClearRules(uf player) {
      Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (clan == null) {
         Logger.warning(player, WarningType.INVALID_USER, "clear clan rules");
      } else {
         ClanMember clanMember = clan.getClanMember(player);
         if (clanMember.rank == ClanRank.MEMBER) {
            Logger.warning(player, WarningType.ABUSE_OF_AUTHORITY, "clear clan rules");
         } else {
            clan.rules = "";
         }
      }
   }

   public void tryKickPlayer(uf kicker, String username) {
      Clan clan = (Clan)PlayerUtils.getInfo(kicker).getClan();
      if (clan == null) {
         Logger.warning(kicker, WarningType.INVALID_USER, "kick player", username);
      } else {
         ClanMember clanMember = clan.getClanMember(kicker);
         if (clanMember.rank == ClanRank.MEMBER) {
            Logger.warning(kicker, WarningType.ABUSE_OF_AUTHORITY, "kick player", username);
         } else if (this.getPlayerClan(username) != clan) {
            Logger.warning(kicker, WarningType.ABUSE_OF_AUTHORITY, "kick player", username);
         } else if (clan.getClanMember(username).rank != ClanRank.LEADER) {
            jv player = MinecraftServer.F().af().f(username);
            if (player != null) {
               player.a("Вас исключил из клана игрок " + kicker.bu + ".");
            }

            this.leaveClan(username);
            ServerPacketSender.sendClanMembers(kicker);
         }
      }
   }

   public void tryLeaveClan(uf player) {
      Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (clan == null) {
         Logger.warning(player, WarningType.INVALID_USER, "leave clan");
      } else if (clan.getClanMember(player).rank == ClanRank.LEADER) {
         Logger.warning(player, WarningType.INVALID_USER, "leave clan");
      } else {
         this.leaveClan(player.bu);
         ServerPacketSender.sendClanGuiUpdate(player);
      }
   }

   public void tryCancelPeaceOffer(uf player, String enemy) {
      Clan playerClan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (playerClan == null) {
         Logger.warning(player, WarningType.INVALID_USER, "cancel peace offer", enemy);
      } else {
         ClanMember clanMember = playerClan.getClanMember(player);
         if (clanMember.rank == ClanRank.MEMBER) {
            Logger.warning(player, WarningType.ABUSE_OF_AUTHORITY, "cancel peace offer", enemy);
         } else {
            Clan enemyClan = this.getClan(enemy);
            if (enemyClan == null) {
               Logger.warning(player, WarningType.INVALID_TARGET, "cancel peace offer", enemy);
            } else if (!playerClan.isClanEnemy(enemyClan)) {
               Logger.warning(player, WarningType.INVALID_TARGET, "cancel peace offer", enemy);
            } else {
               playerClan.removePeaceOffer(enemyClan);
               ServerPacketSender.sendClansList(player);
            }
         }
      }
   }

   public void tryEndWar(uf player, String enemy) {
      Clan playerClan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (playerClan == null) {
         Logger.warning(player, WarningType.INVALID_USER, "end war", enemy);
      } else {
         ClanMember clanMember = playerClan.getClanMember(player);
         if (clanMember.rank == ClanRank.MEMBER) {
            Logger.warning(player, WarningType.ABUSE_OF_AUTHORITY, "end war", enemy);
         } else {
            Clan enemyClan = this.getClan(enemy);
            if (enemyClan == null) {
               Logger.warning(player, WarningType.INVALID_TARGET, "end war", enemy);
            } else if (!playerClan.isClanEnemy(enemyClan)) {
               Logger.warning(player, WarningType.INVALID_TARGET, "end war", enemy);
            } else {
               playerClan.addPeaceOffer(enemyClan);
               if (enemyClan.isPeaceOffered(playerClan)) {
                  playerClan.removeEnemy(enemyClan);
                  enemyClan.removeEnemy(playerClan);
                  ServerPacketSender.sendEnemiesForClan(playerClan);
                  ServerPacketSender.sendEnemiesForClan(enemyClan);
               }

               ServerPacketSender.sendClansList(player);
            }
         }
      }
   }

   public void tryStartWar(uf player, String agressived) {
      Clan playerClan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (playerClan != null) {
         ClanMember clanMember = playerClan.getClanMember(player);
         if (clanMember.rank == ClanRank.MEMBER) {
            Logger.warning(player, WarningType.ABUSE_OF_AUTHORITY, "start war", agressived);
         } else if (this.getClanFlags(playerClan).size() == 0) {
            Logger.warning(player, WarningType.INVALID_USER, "start war", agressived);
         } else {
            Clan agressivedClan = this.getClan(agressived);
            if (agressivedClan == null || this.getClanFlags(agressivedClan).size() == 0) {
               Logger.warning(player, WarningType.INVALID_TARGET, "start war", agressived);
            } else if (playerClan.isClanEnemy(agressivedClan)) {
               Logger.warning(player, WarningType.INVALID_TARGET, "start war", agressived);
            } else {
               playerClan.addEnemy(agressivedClan);
               agressivedClan.addEnemy(playerClan);
               ServerPacketSender.sendEnemiesForClan(playerClan);
               ServerPacketSender.sendEnemiesForClan(agressivedClan);
               ServerPacketSender.sendClansList(player);
            }
         }
      }
   }

   public void tryRemoveClan(uf requester) {
      Clan clan = (Clan)PlayerUtils.getInfo(requester).getClan();
      if (clan != null && clan.getClanMember(requester.bu).rank == ClanRank.LEADER) {
         Logger.warning(requester, WarningType.ABUSE_OF_AUTHORITY, "remove clan");
         this.removeClan(clan);
      }
   }

   public void tryAddInvite(uf inviter, String username) {
      Clan clan = (Clan)PlayerUtils.getInfo(inviter).getClan();
      jv invited = MinecraftServer.F().af().f(username);
      if (invited == null) {
         inviter.a("Такого игрока не существует, либо он не в сети!");
      } else {
         Clan invitedClan = (Clan)PlayerUtils.getInfo(invited).getClan();
         if (invitedClan != null) {
            inviter.a("Этот игрок уже состоит в клане!");
         } else if (clan != null && clan.getClanMember(inviter.bu).rank != ClanRank.MEMBER) {
            clan.addInvite(username);
            ServerPacketSender.sendClanInvite(clan, invited, inviter);
            inviter.a("Приглашение выслано.");
         }
      }
   }

   void removeClan(Clan clan) {
      Iterator i$ = ((List)clan.getMembers().clone()).iterator();
      ClanMember flag = null;

      while (i$.hasNext()) {
         flag = (ClanMember)i$.next();
         this.leaveClan(flag.username);
      }

      i$ = this.flagManager.getClanFlags(clan).iterator();
      Flag flag1 = null;

      while (i$.hasNext()) {
         flag1 = (Flag)i$.next();
         MinecraftServer.F().a(flag1.dimension).c(flag1.x, flag1.y, flag1.z, 0);
      }

      this.clans.remove(clan);
   }

   private void leaveClan(String username) {
      Clan clan = this.getPlayerClan(username);
      if (clan != null) {
         ClanMember member = clan.getClanMember(username);
         this.flagManager.onClanLeave(clan, member);
         clan.leaveClan(member);
         jv player = MinecraftServer.F().af().f(username);
         if (player != null) {
            PlayerUtils.getInfo(player).setClan(null);
            ServerPacketSender.sendPlayerTag(player);
            ServerPacketSender.sendClanData(player);
            ServerPacketSender.sendClanGuiUpdate(player);
         }
      }
   }

   public Clan getPlayerClan(uf player) {
      return this.getPlayerClan(player.bu);
   }

   public Clan getPlayerClan(String username) {
      for (Clan clan : this.clans) {
         if (clan.getClanMember(username) != null) {
            return clan;
         }
      }

      return null;
   }

   public Clan getClan(String name) {
      for (Clan clan : this.clans) {
         if (clan.name.equals(name)) {
            return clan;
         }
      }

      return null;
   }

   public void tryRegisterClan(String name, uf leader) {
      name = name.trim();
      boolean isNameCorrect = isNameCorrect(name);
      if (!isNameCorrect) {
         leader.a("Название дожно быть длиной от 4 до 16 символов, состоять целиком из русских либо английских букв, также допускаются пробелы.");
      } else if (this.getClan(name) != null) {
         leader.a("Группировка с таким названием уже существует!");
      } else if (name.length() > 3 && PlayerUtils.getInfo(leader).getClan() == null) {
         Clan clan = new Clan(name, leader);
         this.clans.add(clan);
         PlayerUtils.getInfo(leader).setClan(clan);
         ServerPacketSender.sendClanData(leader);
         ServerPacketSender.sendPlayerTag(leader);
         ServerPacketSender.sendClanGuiUpdate(leader);
         leader.a("Группировка создана.");
         if (clan.dissolutionTimer >= 0) {
            leader.a(" У вас есть час, чтобы набрать в нее 5 игроков, иначе она будет распущена.");
         }
      }
   }

   public void readClans(by tag) {
      cg tagList = tag.m("clans");
      HashMap enemiesTag = new HashMap();
      Clan clan = null;
      by i$ = null;

      for (int it = 0; it < tagList.c(); it++) {
         i$ = (by)tagList.b(it);
         clan = new Clan(i$);
         this.clans.add(clan);
         enemiesTag.put(clan, i$.m("enemies"));
      }

      for (Entry var10 : enemiesTag.entrySet()) {
         HashMap var12 = new HashMap();
         Clan enemyClan = null;

         for (int i = 0; i < ((cg)var10.getValue()).c(); i++) {
            enemyClan = this.getClan(((by)((cg)var10.getValue()).b(i)).i("name"));
            if (enemyClan != null) {
               var12.put(enemyClan, ((by)((cg)var10.getValue()).b(i)).n("peace_offered"));
            }

            ((Clan)var10.getKey()).setEnemiesList(var12);
         }
      }

      this.flagManager.readNBT(tag.l("lands"));

      for (Clan var121 : this.clans) {
         var121.syncReputation(false);
      }
   }

   public boolean doesClanExist(Clan clan) {
      return this.clans.contains(clan);
   }

   public void writeClans(by tag) {
      cg tagList = new cg();
      Iterator i$ = this.clans.iterator();
      Clan clan = null;

      while (i$.hasNext()) {
         clan = (Clan)i$.next();
         tagList.a(clan.writeNBT());
      }

      tag.a("clans", tagList);
      tag.a("lands", this.flagManager.writeNBT());
   }

   public ArrayList getClans() {
      return this.clans;
   }

   public static boolean isNameCorrect(String name) {
      name = name.trim();
      return name.length() >= 4 && name.length() <= 16 && (name.matches("[a-zA-Z\\s]*") || name.matches("[а-яА-Я\\s]*"));
   }

   public static ClanManager instance() {
      return CommonProxy.clanManager;
   }
}
