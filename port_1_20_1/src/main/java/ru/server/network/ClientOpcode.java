package ru.stalcraft.server.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.EntityNPCInterface;
import ru.demon.money.server.network.ServerPacketMoneySender;
import ru.demon.money.utils.PlayerMoneyUtils;
import ru.stalcraft.StalkerDamage;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.WeaponInfo;
import ru.stalcraft.clans.ClanMember;
import ru.stalcraft.clans.ClanRank;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.entity.EntityBullet;
import ru.stalcraft.entity.EntityCorpse;
import ru.stalcraft.entity.EntityGrenade;
import ru.stalcraft.inventory.ArmorContainer;
import ru.stalcraft.inventory.ContainerWarehouse;
import ru.stalcraft.inventory.WeaponContainer;
import ru.stalcraft.inventory.WeaponRepairContainer;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemBullet;
import ru.stalcraft.items.ItemCommand;
import ru.stalcraft.items.ItemMedicine;
import ru.stalcraft.items.ItemPoint;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.network.DebugGroup;
import ru.stalcraft.network.DebugPriority;
import ru.stalcraft.network.IOpcodeServer;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerStalkerCapabilities;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.CommonProxy;
import ru.stalcraft.server.WeaponServerInfo;
import ru.stalcraft.server.clans.BaseContent;
import ru.stalcraft.server.clans.Clan;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.clans.Flag;
import ru.stalcraft.server.clans.FlagManager;
import ru.stalcraft.server.clans.FlagsLand;
import ru.stalcraft.server.player.PlayerServerInfo;
import ru.stalcraft.server.shop.ServerShopData;
import ru.stalcraft.tile.TileEntityMachineGun;

public enum ClientOpcode implements IOpcodeServer {
   SHOOT_REQUEST("SHOOT_REQUEST", 0, DebugPriority.LOW, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         PlayerInfo par3 = ((PlayerStalkerCapabilities)player.bG).getInfo();
         int slot = player.bn.c;
         if (player.bn.a[slot] != null && ((WeaponServerInfo)par3.weaponInfo).canShoot(player.bn.a[slot])) {
            int itemID = player.bn.a[slot].d;
            ItemWeapon weapon = (ItemWeapon)yc.g[itemID];
            int type = Integer.parseInt(data[0]);
            if (type == 0) {
               weapon.shootRequest(player, slot, false);
            } else if (type == 1) {
               weapon.shootRequest(player, slot, true);
            }
         }
      }
   },
   RELOAD_REQUEST("RELOAD_REQUEST", 1, DebugPriority.LOW, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         if (player.bn.h() != null && player.bn.h().b() instanceof ItemWeapon) {
            PlayerUtils.getInfo(player).weaponInfo.reloadRequest(player.bn.h());
         }
      }
   },
   MACHINE_GUN_RELOAD_REQUEST("MACHINE_GUN_RELOAD_REQUEST", 2, DebugPriority.MIDDLE, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         WeaponInfo wi = PlayerUtils.getInfo(player).weaponInfo;
         if (wi.currentGun != null) {
            wi.currentGun.reloadRequest();
         }
      }
   },
   MACHINE_GUN_SHOOT_REQUEST("MACHINE_GUN_SHOOT_REQUEST", 3, DebugPriority.MIDDLE, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         WeaponInfo wi = PlayerUtils.getInfo(player).weaponInfo;
         if (wi.currentGun != null) {
            wi.currentGun.shootRequest();
         }
      }
   },
   EXTRACT_AMMO_REQUEST("EXTRACT_AMMO_REQUEST", 4, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         ((WeaponContainer)player.bp).tryExtractAmmo();
      }
   },
   CLAN_INFO_REQUEST("CLAN_INFO_REQUEST", 5, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ServerPacketSender.sendClanInformation(player);
      }
   },
   CLAN_RULES_REQUEST("CLAN_RULES_REQUEST", 6, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ServerPacketSender.sendClanRules(player);
      }
   },
   CLAN_MEMBERS_REQUEST("CLAN_MEMBERS_REQUEST", 7, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ServerPacketSender.sendClanMembers(player);
      }
   },
   CLAN_LIST_REQUEST("CLAN_LIST_REQUEST", 8, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ServerPacketSender.sendClansList(player);
         ServerPacketSender.sendClanInformation(player);
      }
   },
   CLAN_CREATE_REQUEST("CLAN_CREATE_REQUEST", 9, DebugPriority.HIGH, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryRegisterClan(data[0], player);
         ClanManager.instance().tryRegisterClan(data[0], player);
         by tag = PlayerMoneyUtils.getInfo(player).getPersistedTag();
         tag.a("moneyValue", tag.e("moneyValue") - 300000);
         if (!player.q.I) {
            ServerPacketMoneySender.sendMoney(player, tag.e("moneyValue"));
         }
      }
   },
   CLAN_DELETE_REQUEST("CLAN_DELETE_REQUEST", 10, DebugPriority.HIGH, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryRemoveClan(player);
      }
   },
   CLAN_SET_LEADER_REQUEST("CLAN_SET_LEADER_REQUEST", 11, DebugPriority.HIGH, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().trySetLeader(player, data[0]);
      }
   },
   CLAN_INVITE_CLIENT_REQUEST("CLAN_INVITE_CLIENT_REQUEST", 12, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryAddInvite(player, data[0]);
      }
   },
   CLAN_KICK_REQUEST("CLAN_KICK_REQUEST", 13, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryKickPlayer(player, data[0]);
      }
   },
   CLAN_WAR_REQUEST("CLAN_WAR_REQUEST", 14, DebugPriority.HIGH, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryStartWar(player, data[0]);
      }
   },
   CLAN_PEACE_OFFER_REQUEST("CLAN_PEACE_OFFER_REQUEST", 15, DebugPriority.HIGH, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryEndWar(player, data[0]);
      }
   },
   CLAN_LANDS_REQUEST("CLAN_LANDS_REQUEST", 16, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ServerPacketSender.sendClanLands(player);
      }
   },
   CLAN_LEAVE_REQUEST("CLAN_LEAVE_REQUEST", 17, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryLeaveClan(player);
      }
   },
   CLAN_RANK_UP_REQUEST("CLAN_RANK_UP_REQUEST", 18, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().trySetRank(player, data[0], ClanRank.OFFICER);
      }
   },
   CLAN_RANK_DOWN_REQUEST("CLAN_RANK_DOWN_REQUEST", 19, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().trySetRank(player, data[0], ClanRank.MEMBER);
      }
   },
   CLAN_PEACE_OFFER_CANCEL_REQUEST("CLAN_PEACE_OFFER_CANCEL_REQUEST", 20, DebugPriority.HIGH, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryCancelPeaceOffer(player, data[0]);
      }
   },
   CLAN_CLEAR_RULES_REQUEST("CLAN_CLEAR_RULES_REQUEST", 21, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryClearRules(player);
      }
   },
   CLAN_ADD_RULES_REQUEST("CLAN_ADD_RULES_REQUEST", 22, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryAddRules(player, data[0]);
      }
   },
   CLAN_JOIN_REQUEST("CLAN_JOIN_REQUEST", 23, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryJoinClan(player, data[0]);
      }
   },
   CLAN_LAND_RENAME_REQUEST("CLAN_LAND_RENAME_REQUEST", 24, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         FlagManager.instance().trySetFlagName(player, Integer.parseInt(data[0]), data[1]);
      }
   },
   CLAN_GET_MONEY_REQUEST("CLAN_GET_MONEY_REQUEST", 25, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().tryGetMoney(player);
      }
   },
   CLAN_SYNC_REPUTATION_REQUEST("CLAN_SYNC_REPUTATION_REQUEST", 26, DebugPriority.HIGH, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         ClanManager.instance().trySyncReputation(player);
      }
   },
   HANDCUFFS_ANSWER("HANDCUFFS_ANSWER", 27, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         jv handcuffer = (jv)player.q.a(Integer.parseInt(data[0]));
         boolean confirmed = data[1].equals("1");
         if (handcuffer != null) {
            if (!confirmed) {
               handcuffer.a(handcuffer.bu + " отказался надевать наручники.");
            } else if (handcuffer.by() != null && handcuffer.by().d == StalkerMain.handcuffs.cv) {
               handcuffer.a(handcuffer.bu + " согласился надеть наручники.");
               handcuffer.c(0, (ye)null);
               PlayerUtils.getInfo(player).setHandcuffs(true);
            } else {
               player.a(handcuffer.bu + " уже убрал наручники.");
               handcuffer.a(handcuffer.bu + " согласился надеть наручники, но вы их уже убрали.");
            }
         } else {
            player.a("Тот, кто хотел надеть на вас наручники, куда-то делся.");
         }
      }
   },
   USE_HEALING_REQUEST("USE_HEALING_REQUEST", 28, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         int number = Integer.parseInt(data[0]) + 7;
         PlayerServerInfo info = (PlayerServerInfo)PlayerUtils.getInfo(player);
         if (number > 7 && number < 12 && info.stInv.mainInventory[number] != null && info.medicineCooldown == 0) {
            ItemMedicine meds = (ItemMedicine)info.stInv.mainInventory[number].b();
            meds.useHealing(player);
            info.medicineCooldown = 100;
            if (meds.regeneration > 0) {
               info.setRegeneration(meds.regeneration, meds.duration);
            }

            info.stInv.mainInventory[number].b--;
            if (info.stInv.mainInventory[number] != null && info.stInv.mainInventory[number].b < 1) {
               info.stInv.mainInventory[number] = null;
            }

            ((PlayerServerInfo)PlayerUtils.getInfo(player)).sendUpdateStalkerContainer();
         }
      }
   },
   FLASHLIGHT_TOGGLE_REQUEST("FLASHLIGHT_TOGGLE_REQUEST", 29, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         ((WeaponServerInfo)PlayerUtils.getInfo(player).weaponInfo).flashlightToggleRequest(player.bn.a[player.bn.c]);
      }
   },
   RIGHT_CLICK_PLAYER_REQUEST("RIGHT_CLICK_PLAYER_REQUEST", 30, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         jv inventoryToOpen = MinecraftServer.F().af().f(data[0]);
         if (inventoryToOpen != null) {
            PlayerInfo info = PlayerUtils.getInfo(inventoryToOpen);
            if (info.getLeashingPlayer() == player) {
               info.setLeahingPlayer((uf)null);
               player.bn.a(new ye(StalkerMain.rope.cv, 1, 0));
            } else if (info.getHandcuffs()) {
               player.openGui(StalkerMain.instance, 3, player.q, inventoryToOpen.k, 0, 0);
               ServerPacketSender.sendWindowId(player, player.bp.d);
            }
         }
      }
   },
   GUI_OPEN_INVENTORY("GUI_OPEN_INVENTORY", 31, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         player.openGui(StalkerMain.instance, 4, player.q, 0, 0, 0);
         ((PlayerServerInfo)PlayerUtils.getInfo(player)).sendUpdateStalkerContainer();
      }
   },
   RIGHT_CLICK_BLOCK("RIGHT_CLICK_BLOCK", 32, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @Override
      public void handle(jv player, String... data) {
         int par3 = Integer.parseInt(data[0]);
         int par4 = Integer.parseInt(data[1]);
         int par5 = Integer.parseInt(data[2]);
         int par6 = Integer.parseInt(data[3]);
         FlagManager.instance().addFlagsLand(new FlagsLand(player.q.N().j(), par3, par4, par5, par6, 30000, 72000));
      }
   },
   WEAPON_AMMO_TYPE("WEAPON_AMMO_TYPE", 33, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         ItemWeapon itemWeapon = (ItemWeapon)player.by().b();
         ItemBullet itemAmmo = null;
         by stackWeaponNBT = PlayerUtils.getTag(player.by());
         int ammoType = Integer.parseInt(data[0]);
         int ammoCage = stackWeaponNBT.e("cage");
         player.by().q().a("ammoType", ammoType);
         if (ammoType == 0) {
            itemAmmo = (ItemBullet)yc.g[itemWeapon.bulletsID[1]];
            PlayerUtils.addItem(player, new ye(itemAmmo, ammoCage));
            stackWeaponNBT.a("cage", 0);
         } else {
            itemAmmo = (ItemBullet)yc.g[itemWeapon.bulletsID[0]];
            PlayerUtils.addItem(player, new ye(itemAmmo, ammoCage));
            stackWeaponNBT.a("cage", 0);
         }
      }
   },
   MACHINE_GUN_SHOOTER("WEAPON_FIRE_MODE", 34, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         int posX = Integer.parseInt(data[0]);
         int posY = Integer.parseInt(data[1]);
         int posZ = Integer.parseInt(data[2]);
         ((TileEntityMachineGun)player.q.r(posX, posY, posZ)).onRightClick(player);
      }
   },
   SEND_UPGRADE("SEND_UPGRADE", 35, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         int itemid = Integer.parseInt(data[0]);
         yc item = yc.g[itemid];
         if (item instanceof ItemWeapon) {
            ye stack = ((WeaponContainer)player.bp).updatedWeapon;
            int l = ((WeaponContainer)player.bp).upgrade.d().b;
            if (l > 1) {
               ((WeaponContainer)player.bp).upgrade.d().b--;
            } else if (l <= 1) {
               ((WeaponContainer)player.bp).upgrade.f.consumeInventoryItem(0);
            }

            if (stack != null && stack.q() != null) {
               by tag = stack.q();
               abw world = player.q;
               Random rand = world.s;
               ItemPoint itemPoint = (ItemPoint)((WeaponContainer)player.bp).upgrade.d().b();
               if (tag != null && itemPoint.isWeapon) {
                  boolean validate = tag.e("levelDamageUp") != 0
                        && (!rand.nextBoolean() || tag.e("levelDamageUp") > 10 || tag.e("levelDamageUp") < 1)
                        && (tag.e("levelDamageUp") < 11 || !rand.nextBoolean())
                     ? (
                        tag.e("levelDamageUp") >= 18 && rand.nextBoolean()
                           ? (rand.nextBoolean() ? (rand.nextBoolean() ? rand.nextBoolean() : false) : false)
                           : (
                              tag.e("levelRecoilUp") != 0
                                    && (!rand.nextBoolean() || tag.e("levelRecoilUp") > 10 || tag.e("levelRecoilUp") < 1)
                                    && (tag.e("levelRecoilUp") < 11 || !rand.nextBoolean())
                                 ? (
                                    tag.e("levelRecoilUp") >= 18 && rand.nextBoolean()
                                       ? (rand.nextBoolean() ? (rand.nextBoolean() ? rand.nextBoolean() : false) : false)
                                       : (
                                          tag.e("levelSpreadUp") != 0
                                                && (!rand.nextBoolean() || tag.e("levelSpreadUp") > 10 || tag.e("levelSpreadUp") < 1)
                                                && (tag.e("levelSpreadUp") < 11 || !rand.nextBoolean())
                                             ? (
                                                tag.e("levelSpreadUp") >= 18 && rand.nextBoolean()
                                                   ? (rand.nextBoolean() ? (rand.nextBoolean() ? rand.nextBoolean() : false) : false)
                                                   : false
                                             )
                                             : (rand.nextBoolean() ? rand.nextBoolean() : false)
                                       )
                                 )
                                 : (rand.nextBoolean() ? rand.nextBoolean() : false)
                           )
                     )
                     : (rand.nextBoolean() ? rand.nextBoolean() : false);
                  if (validate) {
                     float prevDamageValue = tag.g("damage");
                     float prevRecoilValue = tag.g("recoil");
                     float prevSpreadValue = tag.g("spread");
                     if (itemPoint.values[0] > 0.0F) {
                        tag.a("damage", prevDamageValue + 0.1F + player.q.s.nextFloat() * itemPoint.values[0]);
                     }

                     if (itemPoint.values[1] > 0.0F) {
                        if (prevRecoilValue - 0.1F - player.q.s.nextFloat() * itemPoint.values[1] <= 0.0F) {
                           tag.a("recoil", 0.0F);
                        } else {
                           tag.a("recoil", prevRecoilValue - 0.1F - player.q.s.nextFloat() * itemPoint.values[1]);
                        }
                     }

                     if (itemPoint.values[2] > 0.0F) {
                        if (prevSpreadValue - 0.1F - player.q.s.nextFloat() * itemPoint.values[2] <= 0.0F) {
                           tag.a("spread", 0.0F);
                        } else {
                           tag.a("spread", prevSpreadValue - 0.1F - player.q.s.nextFloat() * itemPoint.values[2]);
                        }
                     }

                     float damageValue = tag.g("damage");
                     float recoilValue = tag.g("recoil");
                     float spreadValue = tag.g("spread");
                     tag.a("upgrade", true);
                     int levelDamage = tag.e("levelDamageUp");
                     int levelRecoil = tag.e("levelRecoilUp");
                     int levelSpread = tag.e("levelSpreadUp");
                     tag.a("levelDamageUp", damageValue != prevDamageValue ? levelDamage + 1 : levelDamage);
                     tag.a("levelRecoilUp", recoilValue != prevRecoilValue ? levelRecoil + 1 : levelRecoil);
                     tag.a("levelSpreadUp", spreadValue != prevSpreadValue ? levelSpread + 1 : levelSpread);
                     ServerPacketSender.sendUpdateWeaponNBTTag(player, damageValue, recoilValue, spreadValue, levelDamage, levelRecoil, levelSpread, item.cv);
                  }
               }
            }
         }

         if (item instanceof ItemArmorArtefakt) {
            ye stackx = ((ArmorContainer)player.bp).updatedArmor;
            int lx = ((ArmorContainer)player.bp).upgrade.d().b;
            if (lx > 1) {
               ((ArmorContainer)player.bp).upgrade.d().b--;
            } else if (lx <= 1) {
               ((ArmorContainer)player.bp).armorInventory.consumeInventoryItem(0);
            }

            if (stackx != null && player.bp != null) {
               by tag = PlayerUtils.getTag(stackx);
               abw world = player.q;
               Random rand = world.s;
               ItemPoint itemPoint = (ItemPoint)((ArmorContainer)player.bp).upgrade.d().b();
               ye stackPoint = ((ArmorContainer)player.bp).upgrade.d();
               if (tag != null && !itemPoint.isWeapon) {
                  boolean validate = tag.e("levelBulletUp") != 0
                        && (!rand.nextBoolean() || tag.e("levelBulletUp") > 10 || tag.e("levelBulletUp") < 1)
                        && (tag.e("levelBulletUp") < 11 || !rand.nextBoolean())
                     ? (
                        tag.e("levelBulletUp") >= 18 && rand.nextBoolean()
                           ? (rand.nextBoolean() ? (rand.nextBoolean() ? rand.nextBoolean() : false) : false)
                           : (
                              tag.e("levelSpeedlUp") != 0
                                    && (!rand.nextBoolean() || tag.e("levelSpeedlUp") > 10 || tag.e("levelSpeedlUp") < 1)
                                    && (tag.e("levelSpeedlUp") < 11 || !rand.nextBoolean())
                                 ? (
                                    tag.e("levelSpeedlUp") >= 18 && rand.nextBoolean()
                                       ? (rand.nextBoolean() ? (rand.nextBoolean() ? rand.nextBoolean() : false) : false)
                                       : (
                                          tag.e("levelSpreadUp") != 0
                                                && (!rand.nextBoolean() || tag.e("levelRegenerationUp") > 10 || tag.e("levelRegenerationUp") < 1)
                                                && (tag.e("levelRegenerationUp") < 11 || !rand.nextBoolean())
                                             ? (
                                                tag.e("levelRegenerationUp") >= 18 && rand.nextBoolean()
                                                   ? (rand.nextBoolean() ? (rand.nextBoolean() ? rand.nextBoolean() : false) : false)
                                                   : false
                                             )
                                             : (rand.nextBoolean() ? rand.nextBoolean() : false)
                                       )
                                 )
                                 : (rand.nextBoolean() ? rand.nextBoolean() : false)
                           )
                     )
                     : (rand.nextBoolean() ? rand.nextBoolean() : false);
                  if (validate) {
                     float prevBulletValue = tag.g("bulletFactor");
                     float prevSpeedValue = tag.g("speedFactor");
                     float prevRegenerationValue = tag.g("regenerationFactor");
                     if (itemPoint.values[0] > 0.0F) {
                        tag.a("bulletFactor", prevBulletValue + 0.4F + player.q.s.nextFloat() * itemPoint.values[0]);
                     }

                     if (itemPoint.values[1] > 0.0F) {
                        float speedValue = prevSpeedValue + 0.1F + player.q.s.nextFloat() * itemPoint.values[1];
                        tag.a("speedFactor", speedValue);
                     }

                     boolean isBulletUp = false;
                     if (stackPoint.s().equals("«Каменная кожа»")) {
                        tag.a("speedFactor", prevSpeedValue + itemPoint.values[1]);
                        isBulletUp = true;
                     }

                     if (itemPoint.values[2] > 0.0F) {
                        if (prevRegenerationValue + 0.1F + player.q.s.nextFloat() * itemPoint.values[2] <= 0.0F) {
                           tag.a("regenerationFactor", 0.0F);
                        } else {
                           tag.a("regenerationFactor", prevRegenerationValue + 0.1F + player.q.s.nextFloat() * itemPoint.values[2]);
                        }
                     }

                     float bulletValue = tag.g("bulletFactor");
                     float speedValue = tag.g("speedFactor");
                     float regenerationValue = tag.g("regenerationFactor");
                     tag.a("upgrade", true);
                     int levelBullet = tag.e("levelBulletUp");
                     int levelSpeed = tag.e("levelSpeedUp");
                     int levelRegeneration = tag.e("levelRegenerationUp");
                     boolean levelSpeedUp = speedValue != prevSpeedValue && !isBulletUp;
                     tag.a("levelBulletUp", bulletValue != prevBulletValue ? levelBullet + 1 : levelBullet);
                     tag.a("levelSpeedUp", levelSpeedUp ? levelSpeed + 1 : levelSpeed);
                     tag.a("levelRegenerationUp", regenerationValue != prevRegenerationValue ? levelRegeneration + 1 : levelRegeneration);
                     levelBullet = tag.e("levelBulletUp");
                     levelSpeed = tag.e("levelSpeedUp");
                     levelRegeneration = tag.e("levelRegenerationUp");
                     ServerPacketSender.sendUpdateWeaponNBTTag(
                        player, bulletValue, speedValue, regenerationValue, levelBullet, levelSpeed, levelRegeneration, item.cv
                     );
                  }
               }
            }
         }
      }
   },
   SEND_CHANCING_SHOOT_GRENADE_LAUNCHER("SEND_CHANCING_SHOOT_GRENADE_LAUNCHER", 36, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         ye stack = player.by();
         by tag = stack.q();
         int fireMode = tag.e("fireModes");
         boolean autoShooting = ((ItemWeapon)stack.b()).autoShooting;
         tag.a("fireModes", fireMode + 1);
         if (tag.e("fireModes") == 2) {
            tag.a("grenade_shooting", true);
         } else {
            tag.a("grenade_shooting", false);
         }

         if (!tag.n("grenade_launcher") && tag.e("fireModes") >= 2) {
            if (autoShooting) {
               tag.a("fireModes", 0);
            } else {
               tag.a("fireModes", 1);
            }
         }

         if (tag.e("fireModes") >= 3) {
            if (autoShooting) {
               tag.a("fireModes", 0);
            } else {
               tag.a("fireModes", 1);
            }
         }
      }
   },
   SEND_SHOOT_GRENADE_REQUEST("SEND_SHOOT_GRENADE_REQUEST", 37, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         if (player.by() != null && player.by().b() instanceof ItemWeapon && player.by().q().e("fireModes") == 2) {
            EntityGrenade grenade = new EntityGrenade(player.q, player, 2.0F, 8.0F, "g_vog.obj", "g_vog", 85, true);
            player.q.d(grenade);
            player.by().q().a("grenade_weapon", 0);
         }
      }
   },
   RELOAD_GRENADE_REQUEST("RELOAD_GRENADE_REQUEST", 38, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         ye stack = null;
         ItemWeapon weapon = (ItemWeapon)player.by().b();
         if (weapon.indexGrenadeLauncher == 1) {
            stack = new ye(StalkerMain.weaponGrenadeM203);
         } else {
            stack = new ye(StalkerMain.weaponGrenadeVOG);
         }

         if (player.bn.c(stack)) {
            by tag = player.by().q();
            int value = tag.e("grenadeLauncher_reloadTime");
            boolean validate = value <= 0;
            if (validate) {
               player.bn.d(stack.b().cv);
               tag.a("grenade_weapon", 1);
               tag.a("grenadeLauncher_reloadTime", 30);
               tag.a("grenadeLauncher_coolDown", 10);
            }
         }
      }
   },
   SEND_BUY("SEND_BUY", 39, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
         playerInfo.addCaseValue(1);
         playerInfo.addDonateValue(-600);
      }
   },
   SEND_OPEN("SEND_OPEN", 40, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         ServerShopData serverShopData = CommonProxy.serverShopData;
         ArrayList<Integer> list = (ArrayList<Integer>)serverShopData.contentsNewCaseID;
         int size = list.size();
         int getIn = player.q.s.nextInt(size);
         int id = list.get(getIn);
         yc item = yc.g[id];
         ye stack = new ye(item, 1);
         by tag = PlayerUtils.getTag(stack);
         if (item instanceof ItemWeapon || item instanceof ItemArmorArtefakt) {
            tag.a("no_drop", 1);
         }

         tag.a("PlayerOwner", player.bu);
         player.bn.a(stack);
         ((PlayerServerInfo)PlayerUtils.getInfo(player)).addCaseValue(-1);
         ServerPacketSender.sendUpdata(player, id);
      }
   },
   SEND_ELECTRA_ATTACK_PLAYER("SEND_ELECTRA_ATTACK_PLAYER", 41, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         if (player.o(2) != null && player.o(2).b() instanceof ItemArmorArtefakt) {
            ItemArmorArtefakt armor = (ItemArmorArtefakt)player.o(2).b();
            float protection = armor.anomalyProtection[0];
            float damage = 12.0F / protection;
            player.a(StalkerDamage.electra, damage);
         } else {
            player.a(StalkerDamage.electra, 12.0F);
         }
      }
   },
   SEND_PLAYER_RESPAWN("SEND_PLAYER_RESPAWN", 42, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         int x = Integer.parseInt(data[0]);
         int y = Integer.parseInt(data[1]);
         int z = Integer.parseInt(data[2]);
         PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
         playerInfo.setRespawnPoint(player.q.t.i, x, y, z);
      }
   },
   FLAG_PLACE("FLAG_PLACE", 43, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         int x = Integer.parseInt(data[0]);
         int y = Integer.parseInt(data[1]);
         int z = Integer.parseInt(data[2]);
         String name = data[3];
         int time = Integer.parseInt(data[4]);
         int rate = Integer.parseInt(data[5]);
         int size = Integer.parseInt(data[6]);
         String locationName = String.valueOf(data[7]);
         String captureDay = String.valueOf(data[8]);
         String captureTimes = String.valueOf(data[9]);
         String captureTimeMunute = String.valueOf(data[10]);
         FlagManager.instance().onFlagPlace(player.q, name, x, y, z, player);
         FlagManager.instance().addFlagToCheck(player.q.t.i, x, y, z);
         Flag flag = FlagManager.instance().getFlagByPos(player.q.t.i, x, y, z);
         if (flag != null) {
            flag.captureTime = time;
            flag.captureRate = rate;
            flag.captureSize = size;
            flag.loc = locationName;
            flag.captureDay = captureDay;
            flag.captureTimes = captureTimes;
            flag.captureTimeMunute = captureTimeMunute;
         }
      }
   },
   FLAG_CLEAN_PLACE("FLAG_CLEAN_PLACE", 44, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         int x = Integer.parseInt(data[0]);
         int y = Integer.parseInt(data[1]);
         int z = Integer.parseInt(data[2]);
         int blockId = player.q.a(x, y, z);
         if (blockId >= 0) {
            player.q.c(x, y, z, 0);
         }
      }
   },
   SEND_COMMAND_ITEM("SEND_COMMAND_ITEM", 45, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         MinecraftServer mcServer = MinecraftServer.F();
         String commands = data[0];
         ye stack = null;
         if (player.by() != null && player.by().b() instanceof ItemCommand) {
            stack = player.by();
            by tag = stack.q();
            int value = tag.e("durability");
            tag.a("durability", value - 1);
            if (tag.e("durability") <= 0) {
               player.bn.d(stack.b().cv);
            }

            mcServer.G().a(player, commands);
         }
      }
   },
   SEND_REPAIR("SEND_REPAIR", 46, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         int number = Integer.parseInt(data[0]);
         WeaponRepairContainer container = PlayerUtils.getInfo(player).repairContainer;
         ye stack = container.inventory.contents[number];
         stack.b().setDamage(stack, 0);
         ServerPacketSender.sendGuiClose(player);
         player.bn.d(player.by().b().cv);
         ((PlayerServerInfo)PlayerUtils.getInfo(player)).sendUpdateStalkerContainer();
      }
   },
   SEND_OPEN_GUI_CONTAINER("SEND_OPEN_GUI_CONTAINER", 47, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         PlayerUtils.getInfo(player).repairContainer = new WeaponRepairContainer(player, Integer.parseInt(data[0]));
      }
   },
   SEND_LEFT_CLICK_INFO("SEND_LEFT_CLICK_INFO", 48, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         ((WeaponServerInfo)PlayerUtils.getInfo(player).weaponInfo).setLeftClick(Boolean.parseBoolean(data[0]));
      }
   },
   SEND_SHOOT("SEND_SHOOT", 49, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         ItemWeapon weapon = (ItemWeapon)player.by().b();
         boolean leftClick = Boolean.parseBoolean(data[0]);
         boolean hasFlash = Boolean.parseBoolean(data[1]);
         float yaw = player instanceof of ? player.aP : player.A;
         float pitch = player.B;
         if (leftClick) {
            yaw += (float)(Math.random() - 0.5) * 10.0F;
            pitch += (float)(Math.random() - 0.5) * 10.0F;
         }

         for (int bulletNumber = 0; bulletNumber < weapon.bulletsCount; bulletNumber++) {
            EntityBullet bullet = new EntityBullet(
               player, (int)player.by().q().g("damage"), leftClick, weapon.spread, weapon.bulletSpeed, weapon.hitSound, weapon.damageFactor, yaw, pitch
            );
            player.q.d(bullet);
            ServerPacketSender.sendRotation(bullet, bullet.A, bullet.B);
         }
      }
   },
   SEND_GUI_CORPSE("SEND_GUI_CORPSE", 50, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         int entityId = Integer.parseInt(data[0]);
         nn entity = player.q.a(entityId);
         if (entity instanceof EntityCorpse) {
            ((EntityCorpse)entity).openGui(player);
         } else if (entity instanceof EntityNPCInterface) {
            ((EntityNPCInterface)entity).openGui(player);
         } else if (entity instanceof uf) {
            jv entityPlayerHit = (jv)entity;
            ServerPacketSender.sendInviteTrade(entityPlayerHit, player.bu);
         }
      }
   },
   SEND_INVENTORY_UPDATE("SEND_INVENTORY_UPDATE", 51, DebugPriority.HIGH, DebugGroup.WEAPONS, null) {
      @Override
      public void handle(jv player, String... data) {
         ((PlayerServerInfo)PlayerUtils.getInfo(player)).sendUpdateStalkerContainer();
      }
   },
   SEND_BUY_ITEM_SHOP("SEND_BUY_ITEM_SHOP", 52, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         int price = Integer.parseInt(data[0]);
         int itemId = Integer.parseInt(data[1]);
         PlayerServerInfo info = (PlayerServerInfo)PlayerUtils.getInfo(player);
         info.addDonateValue(-price);
         ye stack = new ye(yc.g[itemId]);
         by tag = PlayerUtils.getTag(stack);
         if (tag != null) {
            tag.a("no_drop", 1);
            tag.a("owner", player.bu);
         }

         player.bn.a(stack);
      }
   },
   SEND_ENTITY_ATTACK("SEND_ENTITY_ATTACK", 53, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         int entityShooterId = Integer.parseInt(data[0]);
         int entityAttackId = Integer.parseInt(data[1]);
         double damage = Float.parseFloat(data[2]);
         nn entityHit = player.q.a(entityAttackId);
         if (entityHit instanceof uf && player instanceof uf) {
            IClan clanShooter = PlayerUtils.getInfo(player).getClan();
            IClan clanEntityImpact = PlayerUtils.getInfo((uf)entityHit).getClan();
            if (clanEntityImpact != null && clanShooter != null && clanShooter == clanEntityImpact) {
               player.a(new cv().a(a.e + "Невозможно нанести урон союзнику"));
               damage = 0.0;
            } else {
               float damageFactor = ls.a(PlayerUtils.getInfo((uf)entityHit).getBulletDamageFactor(), 0.0F, 100.0F);
               ye stackArmor = ((uf)entityHit).o(2);
               if (stackArmor != null && stackArmor.b() instanceof ItemArmorArtefakt) {
                  by stackArmorNBT = PlayerUtils.getTag(stackArmor);
                  damageFactor = ls.a(PlayerUtils.getInfo((uf)entityHit).getBulletDamageFactor() + stackArmorNBT.g("bulletFactor"), 0.0F, 100.0F);
               }

               damageFactor *= 0.008F;
               damageFactor = 1.0F - damageFactor;
               damage *= damageFactor;
            }
         }

         int savedResistantTime = entityHit.af;
         entityHit.af = 0;
         if (damage > 0.0) {
            entityHit.a(StalkerDamage.causeBulletDamage(player), (float)damage);
            player.q.a(entityHit, "stalker:hitmarker", 1.0F, 0.9F + player.q.s.nextFloat() * 0.1F);
         }

         entityHit.af = savedResistantTime;
      }
   },
   SEND_BULLET_HOLE("SEND_BULLET_HOLE", 54, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         ServerPacketSender.sendSpawnBulletHole(
            Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Integer.parseInt(data[3])
         );
      }
   },
   SEND_EBTITY_DAMAGE("SEND_EBTITY_DAMAGE", 55, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
      }
   },
   SEND_WEAPON_SHOT("SEND_WEAPON_SHOT", 56, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         ServerPacketSender.sendBlood(Integer.parseInt(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]));
         if (player instanceof uf) {
            player.q.a(player.q.a(Integer.parseInt(data[0])), "stalker:hitmarker", 3.0F, 0.9F + player.q.s.nextFloat() * 0.1F);
            ServerPacketSender.sendUpdateHitmarker(player, Integer.parseInt(data[4]));
         }
      }
   },
   SEND_WEAPON_JAMMED("SEND_WEAPON_JAMMED", 57, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         PlayerUtils.getTag(player.by()).a("jammed", 1);
      }
   },
   SEND_TELEPORTATE_BASE("SEND_WEAPON_JAMMED", 58, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         int baseId = Integer.parseInt(data[0]);
         Flag flag = FlagManager.instance().getFlagById(baseId);
         PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
         ru.demon.money.player.PlayerInfo playerMoneyInfo = PlayerMoneyUtils.getInfo(player);
         if (playerInfo.teleportCoolDown <= 0 && playerInfo.getPersistedTag().e("moneyValue") >= 2500) {
            playerInfo.teleportCoolDown = 6000;
            player.a(flag.x, flag.y, flag.z, player.A, player.B);
            ServerPacketSender.sendPlayerClientPosition(player, flag.x, flag.y, flag.z);
            by tag = playerMoneyInfo.getPersistedTag();
            int moneyValue = tag.e("moneyValue");
            int newMoneyValue = moneyValue - 2500;
            tag.a("moneyValue", Integer.valueOf(newMoneyValue));
            ServerPacketMoneySender.sendMoney(player, tag.e("moneyValue"));
         }
      }
   },
   SEND_CLAN_BASES_INFO("SEND_CLAN_BASES_INFO", 59, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         String clanName = data[0];
         int loyalePoints = Integer.parseInt(data[1]);
         Clan clan = ClanManager.instance().getClan(clanName);
         clan.loyalePoint = loyalePoints;
      }
   },
   SEND_OPEN_GUI_BASE_WARE_HOUSE("SEND_OPEN_GUI_BASE_WARE_HOUSE", 60, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         int x = Integer.parseInt(data[0]);
         int y = Integer.parseInt(data[1]);
         int z = Integer.parseInt(data[2]);
         player.openGui(StalkerMain.instance, 7, player.q, x, y, z);
      }
   },
   SEND_PLAYER_LOYALE_INFO("SEND_PLAYER_LOYALE_INFO", 61, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         if (ClanManager.instance().getPlayerClan(player) != null) {
            ClanMember clanMember = ClanManager.instance().getPlayerClan(player).getClanMember(player);
            ServerPacketSender.sendPlayerLoyaleInfo(player, clanMember.loyalePoint);
         }
      }
   },
   SEND_CLAN_BASE_INFO("SEND_CLAN_BASE_INFO", 62, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         String flagName = String.valueOf(data[0]);
         int x = Integer.parseInt(data[1]);
         int y = Integer.parseInt(data[2]);
         int z = Integer.parseInt(data[3]);
         List<Flag> flagsList = FlagManager.instance().getClanFlags(PlayerUtils.getInfo(player).getClan());
         Flag flag = null;

         for (Flag currentFlag : flagsList) {
            if (currentFlag.getName().equals(flagName)) {
               flag = currentFlag;
            }
         }

         if (flag != null) {
            flag.baseWareX = x;
            flag.baseWareY = y;
            flag.baseWareZ = z;
         }
      }
   },
   SEND_WAREHOUSE_INFO("SEND_CLAN_BASE_INFO", 63, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         int x = Integer.parseInt(data[0]);
         int y = Integer.parseInt(data[1]);
         int z = Integer.parseInt(data[2]);
         List<Flag> flagsList = FlagManager.instance().getClanFlags(PlayerUtils.getInfo(player).getClan());
         Flag flag = null;

         for (Flag currentFlag : flagsList) {
            if (currentFlag.baseWareX == x && currentFlag.baseWareY == y && currentFlag.baseWareZ == z) {
               flag = currentFlag;
            }
         }

         if (flag != null) {
            ServerPacketSender.sendWarehouseInfo(player, flag.id, flag.getName());
            ContainerWarehouse container = (ContainerWarehouse)player.bp;
            container.baseID = flag.id;
            List<Integer> contents = new ArrayList<>();
            Iterator it = flag.baseContents.iterator();

            while (it.hasNext()) {
               int itemID = ((BaseContent)it.next()).itemID;
               contents.add(itemID);
               ServerPacketSender.sendWarehouseContent(player, itemID);
            }

            container.readList(contents);
         }
      }
   },
   SEND_UPDATE_PRICE("SEND_UPDATE_PRICE", 64, DebugPriority.HIGH, DebugGroup.OTHER, null) {
      @Override
      public void handle(jv player, String... data) {
         String flagName = String.valueOf(data[0]);
         int price = Integer.parseInt(data[1]);
         int itemID = Integer.parseInt(data[2]);
         int cooldown = Integer.parseInt(data[3]);
         Flag flag = null;

         for (Flag currentFlag : FlagManager.instance().getFlags(PlayerUtils.getInfo(player).getClan())) {
            if (currentFlag.getName().equals(flagName)) {
               flag = currentFlag;
            }
         }

         if (flag != null) {
            for (int i = 0; i < flag.baseContents.size(); i++) {
               BaseContent baseContent = flag.baseContents.get(i);
               if (baseContent.itemID == itemID) {
                  flag.prices[i] = price;
                  flag.cooldowns[i] = cooldown;
               }
            }
         }
      }
   };

   private final DebugPriority priority;
   private final DebugGroup group;

   private ClientOpcode(String var1, int var2, DebugPriority priority, DebugGroup group) {
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

   private ClientOpcode(String x0, int x1, DebugPriority x2, DebugGroup x3, Object x4) {
      this(x0, x1, x2, x3);
   }
}
