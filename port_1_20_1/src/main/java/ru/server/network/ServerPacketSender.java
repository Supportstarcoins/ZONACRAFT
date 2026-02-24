package ru.stalcraft.server.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.server.MinecraftServer;
import ru.stalcraft.Logger;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.clans.ClanMember;
import ru.stalcraft.entity.EntityTurrel;
import ru.stalcraft.network.IOpcode;
import ru.stalcraft.network.PacketHandler;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.proxy.IServerProxy;
import ru.stalcraft.server.clans.Clan;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.clans.ClanWarState;
import ru.stalcraft.server.clans.Flag;
import ru.stalcraft.server.clans.FlagManager;
import ru.stalcraft.server.player.PlayerServerInfo;
import ru.stalcraft.tile.TileEntityMachineGun;

public class ServerPacketSender {
   public static void sendUpdateHitmarker(uf hitEntity, int damageLevel) {
      sendToPlayer(hitEntity, ClientOpcodes.HIT_MARKER_UPDATE, damageLevel);
   }

   public static void sendHasQuitted(uf player) {
      sendToTrackingPlayers(player, ClientOpcodes.PLAYER_QUIT, player.k);
   }

   public static void sendTileEntityEvent(asp tile, int arg1, int arg2) {
      sendToTrackingPlayers(tile, ClientOpcodes.TILE_ENTITY_EVENT, tile.l, tile.m, tile.n, arg1, arg2);
   }

   public static void sendWindowId(uf player, int windowId) {
      sendToPlayer(player, ClientOpcodes.WINDOW_ID, windowId);
   }

   public static void sendAddVelocity(uf entity, float x, float y, float z) {
      sendToPlayer(entity, ClientOpcodes.ADD_VELOCITY, entity.k, x, y, z);
   }

   public static void sendForceCooldown(uf player) {
      sendToPlayer(player, ClientOpcodes.FORCE_COOLDOWN, PlayerUtils.getInfo(player).getForceCooldown());
   }

   public static void sendClanData(uf player) {
      Clan clan = (Clan)((PlayerServerInfo)PlayerUtils.getInfo(player)).getClan();
      if (clan != null) {
         sendToPlayer(player, ClientOpcodes.CLAN_COMMON_DATA, clan.name, clan.getClanMember(player).rank.ordinal());
      } else {
         sendToPlayer(player, ClientOpcodes.CLAN_COMMON_DATA, "", -1);
      }
   }

   public static void sendAllEnemyClans(uf player) {
      Clan clan = (Clan)((PlayerServerInfo)PlayerUtils.getInfo(player)).getClan();
      sendToPlayer(player, ClientOpcodes.CLAN_CLEAR_ENEMIES);
      if (clan != null) {
         HashMap wars = clan.getEnemies();
         Iterator it = wars.entrySet().iterator();
         Object[] data = null;
         int i = 0;

         while (it.hasNext()) {
            if (data == null) {
               data = new Object[Math.min(50, wars.size() - i)];
            }

            data[i % 50] = ((Clan)((Entry)it.next()).getKey()).name;
            if (++i % 50 == 0 || !it.hasNext()) {
               sendToPlayer(player, ClientOpcodes.CLAN_ADD_ENEMIES, data);
               data = null;
            }
         }
      }
   }

   public static void sendEnemiesForClan(Clan clan) {
      if (clan != null) {
         ArrayList players = new ArrayList();
         Iterator wars = MinecraftServer.F().af().a.iterator();
         uf it = null;

         while (wars.hasNext()) {
            it = (uf)wars.next();
            if (PlayerUtils.getInfo(it).getClan() == clan) {
               sendToPlayer(it, ClientOpcodes.CLAN_CLEAR_ENEMIES);
               players.add(it);
            }
         }

         HashMap var8 = clan.getEnemies();
         Iterator var9 = var8.entrySet().iterator();
         Object[] data = null;
         int i = 0;
         uf player = null;

         while (var9.hasNext()) {
            if (data == null) {
               data = new Object[Math.min(50, var8.size() - i)];
            }

            data[i % 50] = ((Clan)((Entry)var9.next()).getKey()).name;
            if (++i % 50 == 0 || !var9.hasNext()) {
               for (uf var10 : players) {
                  sendToPlayer(var10, ClientOpcodes.CLAN_ADD_ENEMIES, data);
               }

               data = null;
            }
         }
      }
   }

   public static void sendClanLands(uf player) {
      Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
      hn scf = MinecraftServer.F().af();
      if (clan == null) {
         sendClanGuiUpdate(player);
      } else {
         sendToPlayer(player, ClientOpcodes.CLAN_CLEAR_LANDS);
         ArrayList flags = FlagManager.instance().getClanFlags(clan);
         ClanMember member = clan.getClanMember(player);
         Object[] data = null;
         Flag flag = null;

         for (int i = 0; i < (flags.size() - 1) / 25 + 1; i++) {
            data = new Object[Math.min(25, flags.size() - i * 25) * 13];

            for (int j = 0; j < 25 && j < flags.size() - i * 25; j++) {
               flag = (Flag)flags.get(i * 25 + j);
               data[j * 13] = flag.getName();
               data[j * 13 + 1] = flag.id;
               data[j * 13 + 2] = flag.x;
               data[j * 13 + 3] = flag.z;
               data[j * 13 + 4] = flag.getMembersCount();
               data[j * 13 + 5] = flag.isMember(member);
               data[j * 13 + 6] = flag.owner != null ? flag.owner.getName() : "null";
               data[j * 13 + 7] = flag.invader != null ? flag.invader.getName() : "null";
               data[j * 13 + 8] = flag.y;
               data[j * 13 + 9] = String.valueOf(flag.loc);
               data[j * 13 + 10] = String.valueOf(flag.captureDay);
               data[j * 13 + 11] = String.valueOf(flag.captureTimes);
               data[j * 13 + 12] = String.valueOf(flag.captureTimeMunute);
            }

            sendToPlayer(player, ClientOpcodes.CLAN_LANDS, data);
         }
      }
   }

   public static void sendClanMembers(uf player) {
      Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
      hn scf = MinecraftServer.F().af();
      if (clan == null) {
         sendClanGuiUpdate(player);
      } else {
         sendToPlayer(player, ClientOpcodes.CLAN_CLEAR_MEMBERS);
         ArrayList members = clan.getMembers();
         Object[] data = null;
         ClanMember member = null;

         for (int i = 0; i < (clan.getMembers().size() - 1) / 50 + 1; i++) {
            data = new Object[Math.min(50, clan.getMembers().size() - i * 50) * 4];

            for (int j = 0; j < 50 && j < clan.getMembers().size() - i * 50; j++) {
               member = (ClanMember)members.get(i * 50 + j);
               data[j * 4] = member.username;
               data[j * 4 + 1] = member.rank.ordinal();
               data[j * 4 + 2] = scf.f(member.username) == null ? 0 : 1;
               data[j * 4 + 3] = member.loyalePoint;
            }

            sendToPlayer(player, ClientOpcodes.CLAN_MEMBERS, data);
         }
      }
   }

   public static void sendClansList(uf player) {
      Clan playerClan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (playerClan != null) {
         HashMap wars = playerClan.getEnemies();
         ArrayList clans = ClanManager.instance().getClans();
         Collections.sort(clans);
         sendToPlayer(player, ClientOpcodes.CLAN_CLEAR_LIST);
         Object[] data = null;
         Clan clan = null;

         for (int i = 0; i < (clans.size() - 1) / 25 + 1; i++) {
            data = new Object[Math.min(25, clans.size() - i * 25) * 5];

            for (int j = 0; j < 25 && j < clans.size() - i * 25; j++) {
               clan = (Clan)clans.get(i * 25 + j);
               data[j * 5] = clan.name;
               data[j * 5 + 1] = clan.getLeader().username;
               if (wars.containsKey(clan)) {
                  data[j * 5 + 2] = ((ClanWarState)wars.get(clan)).ordinal();
               } else {
                  data[j * 5 + 2] = -1;
               }

               data[j * 5 + 3] = clan.getMembers().size();
               data[j * 5 + 4] = FlagManager.instance().getClanFlags(clan).size();
            }

            sendToPlayer(player, ClientOpcodes.CLAN_LIST, data);
         }
      } else {
         sendClanGuiUpdate(player);
      }
   }

   public static void sendClanRules(uf player) {
      Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (clan == null) {
         sendClanGuiUpdate(player);
      } else {
         sendToPlayer(player, ClientOpcodes.CLAN_CLEAR_RULES);

         for (int part = 0; part < (clan.getRules().length() - 1) / 1000 + 1; part++) {
            sendToPlayer(player, ClientOpcodes.CLAN_ADD_RULES, clan.getRules().substring(part * 1000, Math.min(clan.getRules().length(), part * 1000 + 1000)));
         }
      }
   }

   public static void sendClanInformation(uf player) {
      PlayerServerInfo info = (PlayerServerInfo)PlayerUtils.getInfo(player);
      Clan clan = (Clan)info.getClan();
      if (clan == null) {
         sendClanGuiUpdate(player);
      } else {
         int salaryState = 0;
         if (clan.specialClan != null) {
            salaryState++;
            if (info.canGetSalary()) {
               salaryState++;
            }
         }

         sendToPlayer(
            player,
            ClientOpcodes.CLAN_INFO,
            clan.getLogo(),
            ClanManager.instance().getClanFlags(clan).size(),
            clan.getReputation(),
            clan.getMembers().size(),
            clan.getOnlineMembers().size(),
            clan.getDissolutionTimer(),
            clan.getLeader().username,
            clan.money,
            salaryState,
            clan.getLoyalePoint()
         );
      }
   }

   public static void sendClanGuiUpdate(uf player) {
      sendToPlayer(player, ClientOpcodes.CLAN_GUI_UPDATE);
   }

   public static void sendClanInvite(Clan clan, uf invited, uf inviter) {
      sendToPlayer(invited, ClientOpcodes.CLAN_INVITE_SERVER_REQUEST, clan.name, inviter.bu);
   }

   public static void sendReputation(uf player) {
      sendToPlayer(player, ClientOpcodes.REPUTATION, PlayerUtils.getInfo(player).getReputation());
   }

   public static void sendDeathScore(uf player) {
      sendToPlayer(player, ClientOpcodes.DEATH_SCORE, PlayerUtils.getInfo(player).getDeathScore());
   }

   public static void sendAllTags(uf receiver) {
      ClanManager clanManager = ClanManager.instance();
      List players = MinecraftServer.F().af().a;
      Object[] data = null;
      uf player = null;
      PlayerServerInfo info = null;

      for (int i = 0; i < (players.size() - 1) / 25 + 1; i++) {
         data = new Object[Math.min(25, players.size() - i * 25) * 4];

         for (int j = 0; j < 25 && j < players.size() - i * 25; j++) {
            player = (uf)players.get(i * 25 + j);
            info = (PlayerServerInfo)PlayerUtils.getInfo(player);
            data[j * 4] = player.bu;
            data[j * 4 + 1] = info.getReputation();
            data[j * 4 + 2] = info.getClan() == null ? "" : info.getClan().getName();
            data[j * 4 + 3] = info.isPlayerAgressive() ? 1 : 0;
         }

         sendToPlayer(receiver, ClientOpcodes.TAG_LIST, data);
      }
   }

   public static void sendPlayerTag(uf player) {
      PlayerServerInfo info = (PlayerServerInfo)PlayerUtils.getInfo(player);
      Clan clan = (Clan)info.getClan();
      String clanStr = clan == null ? "" : clan.name;
      sendToAll(ClientOpcodes.TAG_LIST, player.bu, info.getReputation(), clanStr, info.isPlayerAgressive() ? 1 : 0);
   }

   public static void sendHandcuffsRequest(uf handcuffer, uf toHandcuff) {
      sendToPlayer(toHandcuff, ClientOpcodes.HANDCUFFS_SERVER_REQUEST, handcuffer.k);
   }

   public static void sendEjectionStartToPlayer(Player player, int id, int age) {
      PacketDispatcher.sendPacketToPlayer(createPacket(ClientOpcodes.EJECTION_START, id, age), player);
   }

   public static void sendEjectionStart(int id, int age) {
      sendToAll(ClientOpcodes.EJECTION_START, id, age);
   }

   public static void sendEjectionEnd() {
      sendToAll(ClientOpcodes.EJECTION_END);
   }

   public static void syncContamination(uf player, int[] levels) {
      sendToPlayer(player, ClientOpcodes.CONTAMINATIONS, levels[0], levels[1], levels[2], levels[3]);
   }

   public static void sendShoot(of shooter, boolean hasFlash) {
      if (shooter instanceof uf) {
         sendToTrackingPlayers((uf)shooter, ClientOpcodes.SHOOT, shooter.k, hasFlash);
      } else {
         sendToTrackingPlayers(shooter, ClientOpcodes.SHOOT, shooter.k, hasFlash);
      }
   }

   public static void sendReloadStart(of shooter) {
      if (shooter instanceof uf) {
         sendToTrackingPlayers((uf)shooter, ClientOpcodes.RELOAD_START, shooter.k);
      } else {
         sendToTrackingPlayers(shooter, ClientOpcodes.RELOAD_START, shooter.k);
      }
   }

   public static void sendReloadFinish(uf player) {
      sendToTrackingPlayers(player, ClientOpcodes.RELOAD_END, player.k);
   }

   public static void sendEntityPos(nn entity) {
      sendToTrackingPlayers(entity, ClientOpcodes.ENTITY_POS, entity.k, (float)entity.u, (float)entity.v, (float)entity.w);
   }

   public static void sendBackpack(uf player, int backpack) {
      sendToTrackingPlayers(player, ClientOpcodes.BACKPACK, player.k, backpack);
   }

   public static void sendEquippedWeapons(uf player, ye rifle, ye pistol) {
      sendToTrackingPlayers(player, ClientOpcodes.EQUIPPED_WEAPONS, createEquippedWeaponsPacket(player, rifle, pistol));
   }

   private static Object[] createEquippedWeaponsPacket(uf player, ye rifle, ye pistol) {
      by rifleTag = rifle == null ? new by() : PlayerUtils.getTag(rifle);
      by pistolTag = pistol == null ? new by() : PlayerUtils.getTag(pistol);
      return new Object[]{
         player.k,
         rifle == null ? 0 : rifle.d,
         rifle == null ? 0 : rifle.b,
         rifle == null ? 0 : rifle.k(),
         rifleTag.n("flashlight") ? 1 : 0,
         rifleTag.n("silencer") ? 1 : 0,
         rifleTag.n("sight") ? 1 : 0,
         pistol == null ? 0 : pistol.d,
         pistol == null ? 0 : pistol.b,
         pistol == null ? 0 : pistol.k(),
         pistolTag.n("flashlight") ? 1 : 0,
         pistolTag.n("silencer") ? 1 : 0,
         pistolTag.n("sight") ? 1 : 0
      };
   }

   public static void sendRotation(nn entity, float yaw, float pitch) {
      sendToTrackingPlayers(entity, ClientOpcodes.ROTATION, entity.k, yaw, pitch);
   }

   public static void sendTurrelShoot(EntityTurrel turrel) {
      sendToTrackingPlayers(turrel, ClientOpcodes.TURREL_SHOOT, turrel.k);
   }

   public static void sendFlashlight(uf player, boolean value) {
      sendToTrackingPlayers(player, ClientOpcodes.FLASHLIGHT, value ? 1 : 0, player.k);
   }

   public static void sendMachinegunState(uf player, TileEntityMachineGun gun, boolean join) {
      if (gun == null) {
         sendToTrackingPlayers(player, ClientOpcodes.MACHINEGUN_INFO, join ? 1 : 0, 0, 0, 0, player.k);
      } else {
         sendToTrackingPlayers(player, ClientOpcodes.MACHINEGUN_INFO, join ? 1 : 0, gun.l, gun.m, gun.n, player.k);
      }
   }

   public static void sendHandcuffs(uf player, boolean handcuffs) {
      sendToTrackingPlayers(player, ClientOpcodes.HANDCUFFS, handcuffs ? 1 : 0, player.k);
   }

   public static void sendLeashing(uf follower, uf followed) {
      sendToTrackingPlayers(follower, ClientOpcodes.LEASHING, follower.k, followed == null ? 0 : followed.k);
   }

   public static void sendAddingContent(int itemID, int stackSize) {
      sendToAll(ClientOpcodes.SEND_ADDING_CONTENT, itemID, stackSize);
   }

   public static void sendStartWatchingPackets(uf player, uf watcher) {
      PlayerInfo info = PlayerUtils.getInfo(player);
      sendToPlayer(watcher, ClientOpcodes.BACKPACK, player.k, info.stInv.getBackpack());
      sendToPlayer(watcher, ClientOpcodes.EQUIPPED_WEAPONS, createEquippedWeaponsPacket(player, info.weaponInfo.getRifle(), info.weaponInfo.getPistol()));
      sendToPlayer(watcher, ClientOpcodes.FLASHLIGHT, info.weaponInfo.isFlashlightEnabled() ? 1 : 0, player.k);
      sendToPlayer(watcher, ClientOpcodes.HANDCUFFS, info.getHandcuffs() ? 1 : 0, player.k);
      sendToPlayer(watcher, ClientOpcodes.LEASHING, player.k, info.getLeashingPlayer() == null ? 0 : info.getLeashingPlayer().k);
      if (info.weaponInfo.currentGun == null) {
         sendToPlayer(watcher, ClientOpcodes.MACHINEGUN_INFO, 0, 0, 0, 0, player.k);
      } else {
         TileEntityMachineGun gun = info.weaponInfo.currentGun;
         sendToPlayer(watcher, ClientOpcodes.MACHINEGUN_INFO, 1, gun.l, gun.m, gun.n, player.k);
      }

      if (!StalkerMain.getProxy().isRemote() && ((IServerProxy)StalkerMain.getProxy()).getAntiRelog().isPlayerRelogging((jv)player)) {
         sendToPlayer(watcher, ClientOpcodes.PLAYER_QUIT, player.k);
      }
   }

   public static void sendUpdateStalkerInventory(uf par1) {
      sendToPlayer(par1, ClientOpcodes.UPDATE_STALKER_INVENTORY);
   }

   public static void sendUpdateDonateValue(uf player, int donateValue) {
      sendToPlayer(player, ClientOpcodes.SEND_UPDATE_DONATE_VALUE, donateValue);
   }

   public static void sendUpdateCaseDonateValue(uf player, int caseValue) {
      sendToPlayer(player, ClientOpcodes.SEND_UPDATE_CASE_VALUE, caseValue);
   }

   public static void sendUpdata(uf player, int item_id) {
      sendToPlayer(player, ClientOpcodes.SEND_UPDATE, item_id);
   }

   public static void sendPlayerRespawn(uf player, int x, int y, int z) {
      sendToPlayer(player, ClientOpcodes.SEND_PLAYER_RESPAWN, x, y, z);
   }

   private static void sendToAll(ClientOpcodes opcode, Object... data) {
      PacketDispatcher.sendPacketToAllPlayers(createPacket(opcode, data));
      if (PacketHandler.debugGroups.contains(opcode.getGroup())
         && PacketHandler.debugOpcodes.contains(opcode)
         && PacketHandler.minPriority.ordinal() <= opcode.getPriority().ordinal()) {
         Logger.debug("Sending packet " + opcode.getName() + " to all players");
         PacketHandler.printArgs(data);
      }
   }

   public static void sendUpdateMoneyValue(uf player, int value) {
      sendToPlayer(player, ClientOpcodes.SEND_UPDATE_MONEY_VALUE, value);
   }

   public static void sendFlagCapture(uf player, int capture, int captureTime) {
      sendToPlayer(player, ClientOpcodes.FLAG_CAPTURE_DATA, capture, captureTime);
   }

   public static void sendCaptureData(jv player, String owner, String invader) {
      sendToPlayer(player, ClientOpcodes.SEND_CAPTURE_DATA, String.valueOf(owner), String.valueOf(invader));
   }

   public static void sendBlood(int entityId, double xCoord, double yCoord, double zCoord) {
      sendToAll(ClientOpcodes.SEND_BLOOD, entityId, xCoord, yCoord, zCoord);
   }

   public static void sendCaptureSize(jv player, int captureSize) {
      sendToPlayer(player, ClientOpcodes.SEND_CAPTURE_SIZE, captureSize);
   }

   private static void sendToTrackingPlayers(asp tile, ClientOpcodes opcode, Object... data) {
      ea packet = createPacket(opcode, data);
      adr chunk = tile.az().d(tile.l, tile.n);
      int x = ls.c(tile.l / 16.0);
      int z = ls.c(tile.n / 16.0);
      jq pi = ((js)tile.az()).s().a(x, z, false);
      if (pi != null) {
         pi.a(packet);
      }

      if (PacketHandler.debugGroups.contains(opcode.getGroup())
         && PacketHandler.debugOpcodes.contains(opcode)
         && PacketHandler.minPriority.ordinal() <= opcode.getPriority().ordinal()) {
         Logger.debug("Sending packet " + opcode.getName() + " to all players that tracking tile " + tile);
         PacketHandler.printArgs(data);
      }
   }

   public static void sendSpawnBulletHole(double xCoord, double yCoord, double zCoord, int sideHit) {
      sendToAll(ClientOpcodes.SEND_SPAWN_BULLET_HOLE, xCoord, yCoord, zCoord, sideHit);
   }

   public static void sendGuiClose(jv player) {
      sendToPlayer(player, ClientOpcodes.SEND_GUI_CLOSE);
   }

   public static void sendUpdateSprinting(uf player, float serverSprinting) {
      sendToPlayer(player, ClientOpcodes.SEND_UPDATE_SPRINT, serverSprinting);
   }

   public static void sendInfoAllCapturePlayer(List players, int capture, int captureTime) {
      Iterator<uf> it = players.iterator();

      while (it.hasNext()) {
         sendToPlayer(it.next(), ClientOpcodes.FLAG_CAPTURE_DATA, capture, captureTime);
      }
   }

   public static void sendToRecoil(uf player) {
      sendToPlayer(player, ClientOpcodes.SEND_TO_RECOIL);
   }

   public static void sendGuiParam(uf player, int paramId, int duration) {
      sendToPlayer(player, ClientOpcodes.SEND_GUI_PARAM, paramId, duration);
   }

   public static void sendInviteTrade(jv player, String username) {
      sendToPlayer(player, ClientOpcodes.SEND_INVITE_TRADE, String.valueOf(username));
   }

   public static void sendOpenGuiTrader(uf player, int entityId, boolean isOpen) {
      sendToPlayer(player, ClientOpcodes.SEND_OPEN_GUI_TRADE, entityId, isOpen);
   }

   public static void sendUpdateClientTradeContainer(uf player, int slotIndex, int entityId, int itemID) {
      sendToPlayer(player, ClientOpcodes.SEND_UPDATE_CLIENT_TRADE, slotIndex, entityId, itemID);
   }

   public static void sendUpdateClientTradeContainer(jv player, int slotIndex, int entityId) {
      sendToPlayer(player, ClientOpcodes.SEND_DELETE_CONTAINER, slotIndex, entityId);
   }

   public static void sendToFlash(int entityId) {
      sendToAll(ClientOpcodes.SEND_TO_FLASH, entityId);
   }

   public static void sendAddItemShop(int id, int price, int tab) {
      sendToAll(ClientOpcodes.SEND_ADD_ITEM_SHOP, id, price, tab);
   }

   public static void sendWayPoint(jv player, int x, int y, int z, String name) {
      sendToPlayer(player, ClientOpcodes.SEND_WAY_POINT, x, y, z, String.valueOf(name));
   }

   public static void sendShotSound(int entityId, String sound) {
      sendToAll(ClientOpcodes.SEND_SHOT_SOUND, entityId, String.valueOf(sound));
   }

   public static void sendUpdateWeaponNBTTag(
      uf player, float damageValue, float recoilValue, float spreadValue, int levelDamage, int levelRecoil, int levelSpread, int itemId
   ) {
      sendToPlayer(player, ClientOpcodes.SEND_UPDATE_WEAPON_NBTTAG, damageValue, recoilValue, spreadValue, levelDamage, levelRecoil, levelSpread, itemId);
   }

   public static void sendPlayerClientPosition(jv player, int x, int y, int z) {
      sendToPlayer(player, ClientOpcodes.SEND_PLAYER_POS, x, y, z);
   }

   public static void sendTeleportCoolDown(uf player, int teleportCoolDown) {
      sendToPlayer(player, ClientOpcodes.SEND_TELEPORT_COOLDOWN, teleportCoolDown);
   }

   public static void sendPlayerLoyaleInfo(jv player, int loyalePoint) {
      sendToPlayer(player, ClientOpcodes.SEND_PLAYER_LOYALE_INFO, loyalePoint);
   }

   public static void sendWarehouseInfo(jv player, int id, String name) {
      sendToPlayer(player, ClientOpcodes.SEND_WAREHOUSE_INFO, id, String.valueOf(name));
   }

   public static void sendWarehouseContent(jv player, int itemID) {
      sendToPlayer(player, ClientOpcodes.SEND_WAREHOUSE_CONTENT, itemID);
   }

   public static void sendContentRemove(uf player, int slotIndex) {
      sendToPlayer(player, ClientOpcodes.SEND_CONTENT_REMOVE, slotIndex);
   }

   public static void sendUpdateTag(uf player, int slotIndex, int price) {
   }

   private static void sendToTrackingPlayers(nn entity, ClientOpcodes opcode, Object... data) {
      ea packet = createPacket(opcode, data);
      ((js)entity.q).q().a(entity, packet);
      if (PacketHandler.debugGroups.contains(opcode.getGroup())
         && PacketHandler.debugOpcodes.contains(opcode)
         && PacketHandler.minPriority.ordinal() <= opcode.getPriority().ordinal()) {
         Logger.debug("Sending packet " + opcode.getName() + " to players that tracking entity " + entity.toString());
         PacketHandler.printArgs(data);
      }
   }

   private static void sendToTrackingPlayers(uf player, ClientOpcodes opcode, Object... data) {
      ea packet = createPacket(opcode, data);
      ((js)player.q).q().a(player, packet);
      ((jv)player).a.b(packet);
      if (PacketHandler.debugGroups.contains(opcode.getGroup())
         && PacketHandler.debugOpcodes.contains(opcode)
         && PacketHandler.minPriority.ordinal() <= opcode.getPriority().ordinal()) {
         Logger.debug("Sending packet " + opcode.getName() + " to players that tracking player " + player.bu);
         PacketHandler.printArgs(data);
      }
   }

   private static void sendToPlayer(uf receiver, ClientOpcodes opcode, Object... data) {
      ((jv)receiver).a.b(createPacket(opcode, data));
      if (PacketHandler.debugGroups.contains(opcode.getGroup())
         && PacketHandler.debugOpcodes.contains(opcode)
         && PacketHandler.minPriority.ordinal() <= opcode.getPriority().ordinal()) {
         Logger.debug("Sending packet " + opcode.getName() + " to player " + receiver.bu);
         PacketHandler.printArgs(data);
      }
   }

   private static ea createPacket(IOpcode opcode, Object... data) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(opcode.getOrdinal()).append(":").append(data.length);

      for (int str = 0; str < data.length; str++) {
         buffer.append(":");
         buffer.append(data[str].toString().replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "\\\\:"));
      }

      return new ea("modST", buffer.toString().getBytes(Charset.forName("UTF-8")));
   }
}
