package ru.stalcraft.client.network;

import ru.stalcraft.network.DebugGroup;
import ru.stalcraft.network.DebugPriority;
import ru.stalcraft.network.IOpcode;

public enum ServerOpcodes implements IOpcode {
   SHOOT_REQUEST("SHOOT_REQUEST", 0, DebugPriority.LOW, DebugGroup.WEAPONS, null) {},
   RELOAD_REQUEST("RELOAD_REQUEST", 1, DebugPriority.LOW, DebugGroup.WEAPONS, null) {},
   MACHINE_GUN_RELOAD_REQUEST("MACHINE_GUN_RELOAD_REQUEST", 2, DebugPriority.MIDDLE, DebugGroup.WEAPONS, null) {},
   MACHINE_GUN_SHOOT_REQUEST("MACHINE_GUN_SHOOT_REQUEST", 3, DebugPriority.MIDDLE, DebugGroup.WEAPONS, null) {},
   EXTRACT_AMMO_REQUEST("EXTRACT_AMMO_REQUEST", 4, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   CLAN_INFO_REQUEST("CLAN_INFO_REQUEST", 5, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   CLAN_RULES_REQUEST("CLAN_RULES_REQUEST", 6, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   CLAN_MEMBERS_REQUEST("CLAN_MEMBERS_REQUEST", 7, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   CLAN_LIST_REQUEST("CLAN_LIST_REQUEST", 8, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   CLAN_CREATE_REQUEST("CLAN_CREATE_REQUEST", 9, DebugPriority.HIGH, DebugGroup.CLANS, null) {},
   CLAN_DELETE_REQUEST("CLAN_DELETE_REQUEST", 10, DebugPriority.HIGH, DebugGroup.CLANS, null) {},
   CLAN_SET_LEADER_REQUEST("CLAN_SET_LEADER_REQUEST", 11, DebugPriority.HIGH, DebugGroup.CLANS, null) {},
   CLAN_INVITE_CLIENT_REQUEST("CLAN_INVITE_CLIENT_REQUEST", 12, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {},
   CLAN_KICK_REQUEST("CLAN_KICK_REQUEST", 13, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {},
   CLAN_WAR_REQUEST("CLAN_WAR_REQUEST", 14, DebugPriority.HIGH, DebugGroup.CLANS, null) {},
   CLAN_PEACE_OFFER_REQUEST("CLAN_PEACE_OFFER_REQUEST", 15, DebugPriority.HIGH, DebugGroup.CLANS, null) {},
   CLAN_LANDS_REQUEST("CLAN_LANDS_REQUEST", 16, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   CLAN_LEAVE_REQUEST("CLAN_LEAVE_REQUEST", 17, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {},
   CLAN_RANK_UP_REQUEST("CLAN_RANK_UP_REQUEST", 18, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {},
   CLAN_RANK_DOWN_REQUEST("CLAN_RANK_DOWN_REQUEST", 19, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {},
   CLAN_PEACE_OFFER_CANCEL_REQUEST("CLAN_PEACE_OFFER_CANCEL_REQUEST", 20, DebugPriority.HIGH, DebugGroup.CLANS, null) {},
   CLAN_CLEAR_RULES_REQUEST("CLAN_CLEAR_RULES_REQUEST", 21, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   CLAN_ADD_RULES_REQUEST("CLAN_ADD_RULES_REQUEST", 22, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   CLAN_JOIN_REQUEST("CLAN_JOIN_REQUEST", 23, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {},
   CLAN_LAND_RENAME_REQUEST("CLAN_LAND_RENAME_REQUEST", 24, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {},
   CLAN_GET_MONEY_REQUEST("CLAN_GET_MONEY_REQUEST", 25, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   CLAN_SYNC_REPUTATION_REQUEST("CLAN_SYNC_REPUTATION_REQUEST", 26, DebugPriority.HIGH, DebugGroup.CLANS, null) {},
   HANDCUFFS_ANSWER("HANDCUFFS_ANSWER", 27, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {},
   USE_HEALING_REQUEST("USE_HEALING_REQUEST", 28, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {},
   FLASHLIGHT_TOGGLE_REQUEST("FLASHLIGHT_TOGGLE_REQUEST", 29, DebugPriority.LOW, DebugGroup.OTHER, null) {},
   RIGHT_CLICK_PLAYER_REQUEST("RIGHT_CLICK_PLAYER_REQUEST", 30, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {},
   GUI_OPEN_INVENTORY("GUI_OPEN_INVENTORY", 31, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {},
   RIGHT_CLICK_BLOCK("RIGHT_CLICK_BLOCK", 32, DebugPriority.LOW, DebugGroup.CLANS, null) {},
   WEAPON_AMMO_TYPE("WEAPON_AMMO_TYPE", 33, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   MACHINE_GUN_SHOOTER("MACHINE_GUN_SHOOTER", 34, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_UPGRADE("SEND_UPGRADE", 35, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_CHANCING_SHOOT_GRENADE_LAUNCHER("SEND_CHANCING_SHOOT_GRENADE_LAUNCHER", 36, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_SHOOT_GRENADE_REQUEST("SEND_SHOOT_GRENADE_REQUEST", 37, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   RELOAD_GRENADE_REQUEST("RELOAD_GRENADE_REQUEST", 38, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_BUY("SEND_BUY", 39, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_OPEN("SEND_OPEN", 40, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_ELECTRA_ATTACK_PLAYER("SEND_ELECTRA_ATTACK_PLAYER", 41, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_PLAYER_RESPAWN("SEND_PLAYER_RESPAWN", 42, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   FLAG_PLACE("FLAG_PLACE", 43, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   FLAG_CLEAN_PLACE("FLAG_CLEAN_PLACE", 44, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_COMMAND_ITEM("SEND_COMMAND_ITEM", 45, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_REPAIR("SEND_REPAIR", 46, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_OPEN_GUI_CONTAINER("SEND_OPEN_GUI_CONTAINER", 47, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_LEFT_CLICK_INFO("SEND_LEFT_CLICK_INFO", 48, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_SHOOT("SEND_SHOOT", 49, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_GUI_CORPSE("SEND_GUI_CORPSE", 50, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_INVENTORY_UPDATE("SEND_INVENTORY_UPDATE", 51, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {},
   SEND_BUY_ITEM_SHOP("SEND_BUY_ITEM_SHOP", 52, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_ENTITY_ATTACK("SEND_ENTITY_ATTACK", 53, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_BULLET_HOLE("SEND_BULLET_HOLE", 54, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_EBTITY_DAMAGE("SEND_EBTITY_DAMAGE", 55, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_WEAPON_SHOT("SEND_WEAPON_SHOT", 56, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_WEAPON_JAMMED("SEND_WEAPON_JAMMED", 57, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_TELEPORTATE_BASE("SEND_WEAPON_JAMMED", 58, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_CLAN_BASES_INFO("SEND_WEAPON_JAMMED", 59, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_OPEN_GUI_BASE_WARE_HOUSE("SEND_OPEN_GUI_BASE_WARE_HOUSE", 60, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_PLAYER_LOYALE_INFO("SEND_PLAYER_LOYALE_INFO", 61, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_CLAN_BASE_INFO("SEND_CLAN_BASE_INFO", 62, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_WAREHOUSE_INFO("SEND_CLAN_BASE_INFO", 63, DebugPriority.HIGH, DebugGroup.OTHER, null) {},
   SEND_UPDATE_PRICE("SEND_UPDATE_PRICE", 64, DebugPriority.HIGH, DebugGroup.OTHER, null) {};

   private final DebugPriority priority;
   private final DebugGroup group;

   private ServerOpcodes(String var1, int var2, DebugPriority priority, DebugGroup group) {
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

   private ServerOpcodes(String x0, int x1, DebugPriority x2, DebugGroup x3, Object x4) {
      this(x0, x1, x2, x3);
   }
}
