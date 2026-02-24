package ru.stalcraft.client.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import java.nio.charset.Charset;

public class ClientPacketSender {
   public static void sendMachineGunShooter(int xCoord, int yCoord, int zCoord) {
      send(ServerOpcodes.MACHINE_GUN_SHOOTER, xCoord, yCoord, zCoord);
   }

   public static void sendWeaponFireMode() {
   }

   public static void sendExtractAmmoRequest() {
      send(ServerOpcodes.EXTRACT_AMMO_REQUEST);
   }

   public static void sendSyncReputationRequest() {
      send(ServerOpcodes.CLAN_SYNC_REPUTATION_REQUEST);
   }

   public static void sendWithdrawRequest() {
      send(ServerOpcodes.CLAN_GET_MONEY_REQUEST);
   }

   public static void sendLandRenameRequest(int landId, String newName) {
      send(ServerOpcodes.CLAN_LAND_RENAME_REQUEST, landId, newName);
   }

   public static void sendClanSetRulesRequest(String newRules) {
      send(ServerOpcodes.CLAN_CLEAR_RULES_REQUEST);

      for (int part = 0; part < (newRules.length() - 1) / 1000 + 1; part++) {
         send(ServerOpcodes.CLAN_ADD_RULES_REQUEST, newRules.substring(part * 1000, Math.min(newRules.length(), part * 1000 + 1000)));
      }
   }

   public static void sendClanJoinRequest(String clan) {
      send(ServerOpcodes.CLAN_JOIN_REQUEST, clan);
   }

   public static void sendSetLeaderRequest(String username) {
      send(ServerOpcodes.CLAN_SET_LEADER_REQUEST, username);
   }

   public static void sendRankUpRequest(String username) {
      send(ServerOpcodes.CLAN_RANK_UP_REQUEST, username);
   }

   public static void sendRankDownRequest(String username) {
      send(ServerOpcodes.CLAN_RANK_DOWN_REQUEST, username);
   }

   public static void sendClanInfoRequest() {
      send(ServerOpcodes.CLAN_INFO_REQUEST);
   }

   public static void sendClanRulesRequest() {
      send(ServerOpcodes.CLAN_RULES_REQUEST);
   }

   public static void sendClanListRequest() {
      send(ServerOpcodes.CLAN_LIST_REQUEST);
   }

   public static void sendClanMembersRequest() {
      send(ServerOpcodes.CLAN_MEMBERS_REQUEST);
   }

   public static void sendClanLandsRequest() {
      send(ServerOpcodes.CLAN_LANDS_REQUEST);
   }

   public static void sendClanCreateRequest(String clanName) {
      send(ServerOpcodes.CLAN_CREATE_REQUEST, clanName);
   }

   public static void sendClanDeleteRequest() {
      send(ServerOpcodes.CLAN_DELETE_REQUEST);
   }

   public static void sendClanWarRequest(String clan) {
      send(ServerOpcodes.CLAN_WAR_REQUEST, clan);
   }

   public static void sendClanPeaceRequest(String clan) {
      send(ServerOpcodes.CLAN_PEACE_OFFER_REQUEST, clan);
   }

   public static void sendClanPeaceOfferCancelRequest(String clan) {
      send(ServerOpcodes.CLAN_PEACE_OFFER_CANCEL_REQUEST, clan);
   }

   public static void sendClanInviteRequest(String username) {
      send(ServerOpcodes.CLAN_INVITE_CLIENT_REQUEST, username);
   }

   public static void sendClanKickRequest(String username) {
      send(ServerOpcodes.CLAN_KICK_REQUEST, username);
   }

   public static void sendClanLeaveRequest() {
      send(ServerOpcodes.CLAN_LEAVE_REQUEST);
   }

   public static void sendHandcuffsAnswer(uf handcuffer, boolean confirmed) {
      send(ServerOpcodes.HANDCUFFS_ANSWER, handcuffer.k, confirmed ? "1" : "0");
   }

   public static void sendRightClickRequest(uf player) {
      send(ServerOpcodes.RIGHT_CLICK_PLAYER_REQUEST, player.bu);
   }

   public static void sendRightClickBlock(int par1, int par2, int par3) {
      send(ServerOpcodes.RIGHT_CLICK_BLOCK, par1, par2, par3);
   }

   public static void sendUseMedicine(int slotNumber) {
      send(ServerOpcodes.USE_HEALING_REQUEST, slotNumber);
   }

   public static void sendReloadRequest(int armorType) {
      send(ServerOpcodes.RELOAD_REQUEST, armorType);
   }

   public static void sendRelaodGrenadeRequest() {
      send(ServerOpcodes.RELOAD_GRENADE_REQUEST);
   }

   public static void sendShootRequest(int currentItem, int type) {
      send(ServerOpcodes.SHOOT_REQUEST, type);
   }

   public static void sendFlashlightRequest() {
      send(ServerOpcodes.FLASHLIGHT_TOGGLE_REQUEST);
   }

   public static void sendMachineGunReloadRequest() {
      send(ServerOpcodes.MACHINE_GUN_RELOAD_REQUEST);
   }

   public static void sendMachineGunShootRequest() {
      send(ServerOpcodes.MACHINE_GUN_SHOOT_REQUEST);
   }

   public static void sendOpenGuiInventory() {
      send(ServerOpcodes.GUI_OPEN_INVENTORY);
   }

   public static void sendRightClickBlockClan(int par1, int par2, int par3, int par4) {
      send(ServerOpcodes.RIGHT_CLICK_BLOCK, par1, par2, par3, par4);
   }

   private static void send(ServerOpcodes opcode, Object... data) {
      PacketDispatcher.sendPacketToServer(createPacket(opcode, data));
   }

   public static void sendUpgrade(int itemId) {
      send(ServerOpcodes.SEND_UPGRADE, itemId);
   }

   public static void sendChanchingShootGrenadeLauncher() {
      send(ServerOpcodes.SEND_CHANCING_SHOOT_GRENADE_LAUNCHER);
   }

   public static void sendShootGrenadeRequest() {
      send(ServerOpcodes.SEND_SHOOT_GRENADE_REQUEST);
   }

   public static void sendBuy() {
      send(ServerOpcodes.SEND_BUY);
   }

   public static void sendOpen() {
      send(ServerOpcodes.SEND_OPEN);
   }

   public static void sendElectraAttackPlayer() {
      send(ServerOpcodes.SEND_ELECTRA_ATTACK_PLAYER);
   }

   public static void sendPlayerRespawn(int x, int y, int z) {
      send(ServerOpcodes.SEND_PLAYER_RESPAWN, x, y, z);
   }

   public static void sendFlagCleanPlace(int x, int y, int z) {
      send(ServerOpcodes.FLAG_CLEAN_PLACE, x, y, z);
   }

   public static void sendFlagPlace(
      int x,
      int y,
      int z,
      String flagName,
      int captureTime,
      int captureRate,
      int captureSize,
      String locationName,
      String captureDay,
      String captureTimes,
      String captureTimeMunute
   ) {
      send(
         ServerOpcodes.FLAG_PLACE,
         x,
         y,
         z,
         flagName,
         captureTime,
         captureRate,
         captureSize,
         String.valueOf(locationName),
         String.valueOf(captureDay),
         String.valueOf(captureTimes),
         String.valueOf(captureTimeMunute)
      );
   }

   public static void sendCommandItem(String commands) {
      send(ServerOpcodes.SEND_COMMAND_ITEM, String.valueOf(commands));
   }

   public static void sendRepair(int id) {
      send(ServerOpcodes.SEND_REPAIR, id);
   }

   public static void sendOpenGuiContainer(int id) {
      send(ServerOpcodes.SEND_OPEN_GUI_CONTAINER, id);
   }

   public static void sendLeftClick(boolean isLeftClick) {
      send(ServerOpcodes.SEND_LEFT_CLICK_INFO, isLeftClick);
   }

   public static void sendShoot(boolean leftClick, boolean hasFlash) {
      send(ServerOpcodes.SEND_SHOOT, leftClick, hasFlash);
   }

   public static void sendOpenGuiCorpse(int entityId) {
      send(ServerOpcodes.SEND_GUI_CORPSE, entityId);
   }

   public static void sendUpdateContainer() {
      send(ServerOpcodes.SEND_INVENTORY_UPDATE);
   }

   public static void sendOpenTrade(String inviter) {
   }

   public static void sendUpdateContent(int slotIndex, int entityId, int itemID) {
   }

   public static void sendUpdateContent(int slotIndex, int entityId) {
   }

   public static void sendWeaponShot() {
      send(ServerOpcodes.SEND_WEAPON_SHOT);
   }

   public static void sendBuyItem(int price, int id) {
      send(ServerOpcodes.SEND_BUY_ITEM_SHOP, price, id);
   }

   public static void sendOpenPersonalWarehouse() {
   }

   public static void spawnBulletHole(double xCoord, double yCoord, double zCoord, int sideHit) {
      send(ServerOpcodes.SEND_BULLET_HOLE, xCoord, yCoord, zCoord, sideHit);
   }

   public static void setEntityDamage() {
      send(ServerOpcodes.SEND_EBTITY_DAMAGE);
   }

   public static void sendBulletEntityHit(double xCoord, double yCoord, double zCoord, int entityId, int damageLevel) {
      send(ServerOpcodes.SEND_WEAPON_SHOT, entityId, xCoord, yCoord, zCoord, damageLevel);
   }

   public static void sendAttackBullet(int entityShooterId, int entityAttackId, float damage) {
      send(ServerOpcodes.SEND_ENTITY_ATTACK, entityShooterId, entityAttackId, damage);
   }

   public static void sendAmmoType(int ammoType) {
      send(ServerOpcodes.WEAPON_AMMO_TYPE, ammoType);
   }

   public static void sendWeaponJammed() {
      send(ServerOpcodes.SEND_WEAPON_JAMMED);
   }

   public static void sendTeleportateBase(int id) {
      send(ServerOpcodes.SEND_TELEPORTATE_BASE, id);
   }

   public static void sendClanBasesInfo(String clanName, int loyalePoints) {
      send(ServerOpcodes.SEND_CLAN_BASES_INFO, String.valueOf(clanName), loyalePoints);
   }

   public static void sendOpenGuiBaseWarehouse(int x, int y, int z) {
      send(ServerOpcodes.SEND_OPEN_GUI_BASE_WARE_HOUSE, x, y, z);
   }

   public static void sendPlayerLoyaleInfo() {
      send(ServerOpcodes.SEND_PLAYER_LOYALE_INFO);
   }

   public static void sendClanBaseInfo(String baseName, int x, int y, int z) {
      send(ServerOpcodes.SEND_CLAN_BASE_INFO, String.valueOf(baseName), x, y, z);
   }

   public static void sendWarehouseInfo(int x, int y, int z) {
      send(ServerOpcodes.SEND_WAREHOUSE_INFO, x, y, z);
   }

   public static void sendUpdatePrice(String flagName, int price, int itemID, int coolDown) {
      send(ServerOpcodes.SEND_UPDATE_PRICE, String.valueOf(flagName), price, itemID, coolDown);
   }

   private static ea createPacket(ServerOpcodes opcode, Object... data) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(opcode.getOrdinal()).append(":").append(data.length);

      for (int str = 0; str < data.length; str++) {
         buffer.append(":");
         buffer.append(data[str].toString().replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "\\\\:"));
      }

      String var5 = buffer.toString();
      ea packet = new ea();
      packet.a = "modST";
      packet.c = var5.getBytes(Charset.forName("UTF-8"));
      packet.b = packet.c.length;
      return packet;
   }
}
