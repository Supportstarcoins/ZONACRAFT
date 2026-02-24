package ru.stalcraft.client.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import reifnsk.minimap.ReiMinimap;
import reifnsk.minimap.Waypoint;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.WeaponInfo;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.ClientTicker;
import ru.stalcraft.client.ClientWeaponData;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.clans.TagData;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.ejection.ClientEjection;
import ru.stalcraft.client.gui.GuiHandcuffs;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.gui.GuiTrade;
import ru.stalcraft.client.gui.clans.GuiBaseWarehouse;
import ru.stalcraft.client.gui.clans.GuiClanInvite;
import ru.stalcraft.client.particles.BlockParticleEmitter;
import ru.stalcraft.client.particles.ParticleBlockEmitter;
import ru.stalcraft.client.particles.ParticleLivingEmitter;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.client.shop.ClientShopManager;
import ru.stalcraft.entity.EntityBulletHole;
import ru.stalcraft.entity.EntityShot;
import ru.stalcraft.entity.EntitySleeve;
import ru.stalcraft.entity.EntityTurrel;
import ru.stalcraft.inventory.ArmorContainer;
import ru.stalcraft.inventory.ContainerWarehouse;
import ru.stalcraft.inventory.TradeContainer;
import ru.stalcraft.inventory.WeaponContainer;
import ru.stalcraft.inventory.shop.ShopContainer;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.network.DebugGroup;
import ru.stalcraft.network.DebugPriority;
import ru.stalcraft.network.IOpcodeClient;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.tile.TileEntityMachineGun;

public enum ServerOpcode implements IOpcodeClient {
   CONTAMINATIONS("CONTAMINATIONS", 0, DebugPriority.LOW, DebugGroup.CLIENT_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         PlayerUtils.getInfo(atv.w().h)
            .cont
            .setAttackLevels(new int[]{Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3])});
      }
   },
   EJECTION_START("EJECTION_START", 1, DebugPriority.MIDDLE, DebugGroup.CLIENT_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         new ClientEjection(Integer.parseInt(data[0]), Integer.parseInt(data[1])).start();
      }
   },
   EJECTION_END("EJECTION_END", 2, DebugPriority.MIDDLE, DebugGroup.CLIENT_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         StalkerMain.getProxy().getEjectionManager().getEjection().end();
      }
   },
   FORCE_COOLDOWN("FORCE_COOLDOWN", 3, DebugPriority.MIDDLE, DebugGroup.CLIENT_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ((PlayerClientInfo)PlayerUtils.getInfo(atv.w().h)).setForceCooldown(Integer.parseInt(data[0]));
      }
   },
   REPUTATION("REPUTATION", 4, DebugPriority.LOW, DebugGroup.CLIENT_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         PlayerUtils.getInfo(atv.w().h).setReputation(Integer.parseInt(data[0]));
      }
   },
   DEATH_SCORE("DEATH_SCORE", 5, DebugPriority.LOW, DebugGroup.CLIENT_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         PlayerUtils.getInfo(atv.w().h).setDeathScore(Integer.parseInt(data[0]));
      }
   },
   BACKPACK("BACKPACK", 6, DebugPriority.LOW, DebugGroup.PLAYERS_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         nn entity = atv.w().f.a(Integer.parseInt(data[0]));
         if (entity != null && entity instanceof uf) {
            uf p = (uf)entity;
            PlayerUtils.getInfo(p).setBackpackId(Integer.parseInt(data[1]));
         }
      }
   },
   FLASHLIGHT("FLASHLIGHT", 7, DebugPriority.LOW, DebugGroup.PLAYERS_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         int entityId = Integer.parseInt(data[1]);
         if (atv.w().f.a(entityId) instanceof uf) {
            ((ClientWeaponInfo)PlayerUtils.getInfo((uf)atv.w().f.a(entityId)).weaponInfo).setFlashlightOn(data[0].equals("1"));
         }
      }
   },
   EQUIPPED_WEAPONS("EQUIPPED_WEAPONS", 8, DebugPriority.LOW, DebugGroup.PLAYERS_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         int entityId = Integer.parseInt(data[0]);
         if (atv.w().f.a(entityId) instanceof uf) {
            ClientWeaponInfo weaponInfo = (ClientWeaponInfo)PlayerUtils.getInfo((uf)atv.w().f.a(entityId)).weaponInfo;
            ye rifle = new ye(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            by rifleTag = PlayerUtils.getTag(rifle);
            rifleTag.a("flashlight", data[4].equals("1"));
            rifleTag.a("silencer", data[5].equals("1"));
            rifleTag.a("sight", data[6].equals("1"));
            ye pistol = new ye(Integer.parseInt(data[7]), Integer.parseInt(data[8]), Integer.parseInt(data[9]));
            by pistolTag = PlayerUtils.getTag(pistol);
            pistolTag.a("flashlight", data[10].equals("1"));
            pistolTag.a("silencer", data[11].equals("1"));
            pistolTag.a("sight", data[12].equals("1"));
            weaponInfo.setRifle(rifle);
            weaponInfo.setPistol(pistol);
         }
      }
   },
   LEASHING("LEASHING", 9, DebugPriority.MIDDLE, DebugGroup.PLAYERS_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         boolean isLeashing = !data[1].equals("0");
         uf player = (uf)atv.w().f.a(Integer.parseInt(data[0]));
         PlayerInfo info = PlayerUtils.getInfo(player);
         if (isLeashing) {
            info.setLeahingPlayer((uf)atv.w().f.a(Integer.parseInt(data[1])));
         } else {
            info.setLeahingPlayer((uf)null);
         }
      }
   },
   HANDCUFFS("HANDCUFFS", 10, DebugPriority.MIDDLE, DebugGroup.PLAYERS_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         boolean handcuffs = data[0].equals("1");
         int entityId = Integer.parseInt(data[1]);
         if (atv.w().f.a(entityId) instanceof uf) {
            uf player = (uf)atv.w().f.a(entityId);
            PlayerUtils.getInfo(player).setHandcuffs(handcuffs);
            if (handcuffs && player == atv.w().h && atv.w().n instanceof axv) {
               atv.w().a((awe)null);
            }
         }
      }
   },
   TAG_LIST("TAG_LIST", 11, DebugPriority.LOW, DebugGroup.PLAYERS_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         for (int i = 0; i < data.length / 4; i++) {
            ClientProxy.tags.put(data[i * 4], new TagData(Integer.parseInt(data[i * 4 + 1]), data[i * 4 + 2], data[i * 4 + 3].equals("1")));
         }
      }
   },
   PLAYER_QUIT("PLAYER_QUIT", 12, DebugPriority.MIDDLE, DebugGroup.PLAYERS_DATA, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         nn entity = atv.w().f.a(Integer.parseInt(data[0]));
         if (entity instanceof uf) {
            ((PlayerClientInfo)PlayerUtils.getInfo((uf)entity)).hasQuitted = true;
         }
      }
   },
   RELOAD_START("RELOAD_START", 13, DebugPriority.LOW, DebugGroup.WEAPONS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         of shooter = (of)atv.w().f.a(Integer.parseInt(data[0]));
         ye stack = shooter.aZ();
         if (stack != null && yc.g[stack.d] instanceof ItemWeapon) {
            if (shooter instanceof uf) {
               PlayerUtils.getInfo((uf)shooter).weaponInfo.reloadRequest(stack);
            }

            ((ItemWeapon)yc.g[stack.d]).clientReload(shooter, stack);
         }
      }
   },
   RELOAD_END("RELOAD_END", 14, DebugPriority.LOW, DebugGroup.WEAPONS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         nn entity = atv.w().f.a(Integer.parseInt(data[0]));
         if (entity instanceof uf) {
         }
      }
   },
   SHOOT("SHOOT", 15, DebugPriority.LOW, DebugGroup.WEAPONS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         nn entity = atv.w().f.a(Integer.parseInt(data[0]));
         if (entity instanceof of) {
            of shooter = (of)entity;
            if (shooter.aZ() != null && shooter.aZ().b() instanceof ItemWeapon) {
               ItemWeapon weapon = (ItemWeapon)shooter.aZ().b();
               weapon.shoot(shooter, shooter.aZ(), false, !PlayerUtils.getTag(shooter.n(0)).n("silencer"));
            }
         }
      }
   },
   UPDATE_BULLETS("UPDATE_BULLETS", 16, DebugPriority.LOW, DebugGroup.WEAPONS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
      }
   },
   MACHINEGUN_INFO("MACHINEGUN_INFO", 17, DebugPriority.MIDDLE, DebugGroup.WEAPONS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         int x = Integer.parseInt(data[1]);
         int y = Integer.parseInt(data[2]);
         int z = Integer.parseInt(data[3]);
         int entityId = Integer.parseInt(data[4]);
         nn entity = atv.w().f.a(entityId);
         if (data[0].equals("1")) {
            if (entity instanceof uf) {
               uf tile = (uf)entity;
               WeaponInfo player = PlayerUtils.getInfo(tile).weaponInfo;
               if (atv.w().f.r(x, y, z) instanceof TileEntityMachineGun) {
                  TileEntityMachineGun weaponInfo = (TileEntityMachineGun)atv.w().f.r(x, y, z);
                  player.currentGun = weaponInfo;
                  weaponInfo.setShooter(tile);
               }
            }
         } else {
            asp tile1 = atv.w().f.r(x, y, z);
            if (tile1 instanceof TileEntityMachineGun) {
               ((TileEntityMachineGun)tile1).setShooter((uf)null);
            }

            if (entity instanceof uf) {
               uf player1 = (uf)entity;
               WeaponInfo weaponInfo1 = PlayerUtils.getInfo(player1).weaponInfo;
               PlayerUtils.getInfo(player1).weaponInfo.currentGun = null;
            }
         }
      }
   },
   CLAN_INFO("CLAN_INFO", 18, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.parseInfo(data);
      }
   },
   CLAN_ADD_RULES("CLAN_ADD_RULES", 19, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.addRules(data[0]);
      }
   },
   CLAN_MEMBERS("CLAN_MEMBERS", 20, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.parseMembers(data);
      }
   },
   CLAN_LIST("CLAN_LIST", 21, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.parseClans(data);
      }
   },
   CLAN_ADD_ENEMIES("CLAN_ADD_ENEMIES", 22, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.parseEnemies(data);
      }
   },
   CLAN_GUI_UPDATE("CLAN_GUI_UPDATE", 23, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.updateClanGui();
      }
   },
   CLAN_INVITE_SERVER_REQUEST("CLAN_INVITE_SERVER_REQUEST", 24, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         atv.w().a(new GuiClanInvite(data[0], data[1]));
      }
   },
   CLAN_LANDS("CLAN_LANDS", 25, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.parseLands(data);
      }
   },
   CLAN_COMMON_DATA("CLAN_COMMON_DATA", 26, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.parseCommonData(data);
      }
   },
   CLAN_CLEAR_LANDS("CLAN_CLEAR_LANDS", 27, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.clearLands();
      }
   },
   CLAN_CLEAR_ENEMIES("CLAN_CLEAR_ENEMIES", 28, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.clearEnemies();
      }
   },
   CLAN_CLEAR_MEMBERS("CLAN_CLEAR_MEMBERS", 29, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.clearMembers();
      }
   },
   CLAN_CLEAR_RULES("CLAN_CLEAR_RULES", 30, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.clearRules();
      }
   },
   CLAN_CLEAR_LIST("CLAN_CLEAR_LIST", 31, DebugPriority.LOW, DebugGroup.CLANS, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.clearClans();
      }
   },
   HANDCUFFS_SERVER_REQUEST("HANDCUFFS_SERVER_REQUEST", 32, DebugPriority.MIDDLE, DebugGroup.OTHER, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         if (atv.w().f.a(Integer.parseInt(data[0])) != null) {
            atv.w().a(new GuiHandcuffs((uf)atv.w().f.a(Integer.parseInt(data[0]))));
         }
      }
   },
   ENTITY_POS("ENTITY_POS", 33, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         nn entity = atv.w().f.a(Integer.parseInt(data[0]));
         if (entity != null) {
            entity.b(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3]));
         }
      }
   },
   ADD_VELOCITY("ADD_VELOCITY", 34, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         atv.w().f.a(Integer.parseInt(data[0])).g(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3]));
      }
   },
   WINDOW_ID("WINDOW_ID", 35, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         atv.w().h.bp.d = Integer.parseInt(data[0]);
      }
   },
   ROTATION("ROTATION", 36, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         int entityId = Integer.parseInt(data[0]);
         float yaw = Float.parseFloat(data[1]);
         float pitch = Float.parseFloat(data[2]);
         nn entity = atv.w().f.a(entityId);
         entity.A = yaw;
         entity.B = pitch;
         if (entity instanceof of) {
            ((of)entity).aP = yaw;
         }
      }
   },
   TURREL_SHOOT("TURREL_SHOOT", 37, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         int entityId = Integer.parseInt(data[0]);
         EntityTurrel turrel = (EntityTurrel)atv.w().f.a(entityId);
         turrel.D = Math.max(turrel.minPitch, turrel.D - 3.0F);
         if (GuiSettingsStalker.renderSleeves) {
            atv.w()
               .f
               .d(
                  new EntitySleeve(
                     atv.w().f, turrel.u, turrel.v + turrel.f(), turrel.w, turrel.A, turrel.B, turrel.getSleeveDistance(), turrel.getSleeveModelName()
                  )
               );
         }

         float xOffset = -ls.a(turrel.A / 180.0F * (float) Math.PI) * turrel.getRotationPointZ();
         float zOffset = ls.b(turrel.A / 180.0F * (float) Math.PI) * turrel.getRotationPointZ();
         atv.w()
            .f
            .d(new EntityShot(atv.w().f, turrel.u + xOffset, turrel.v + turrel.f(), turrel.w + zOffset, turrel.A, turrel.B, 1.0F, turrel.getLightDistance()));
         turrel.shoot();
      }
   },
   TILE_ENTITY_EVENT("TILE_ENTITY_EVENT", 38, DebugPriority.LOW, DebugGroup.ANOMALIES, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... data) {
         int x = Integer.parseInt(data[0]);
         int y = Integer.parseInt(data[1]);
         int z = Integer.parseInt(data[2]);
         asp tile = atv.w().f.r(x, y, z);
         tile.b(Integer.parseInt(data[3]), Integer.parseInt(data[4]));
      }
   },
   UPDATE_STALKER_INVETORY("UPDATE_STALKER_INVETORY", 39, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @SideOnly(Side.CLIENT)
      @Override
      public void handle(String... par1) {
         atv.w().h.bp = PlayerUtils.getInfo(atv.w().h).inventoryContainer;
      }
   },
   HIT_MARKER_UPDATE("HIT_MARKER_UPDATE", 40, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         ClientTicker.hitMarker = 1.0F;
         if (ClientTicker.hitMarker <= 0.0F) {
            ClientTicker.hitTick = 7;
         }

         ClientTicker.damageLevel = Integer.parseInt(data[0]);
      }
   },
   SEND_ADDING_CONTENT("HIT_MARKER_UPDATE", 41, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int itemID = Integer.parseInt(data[0]);
         int stackSize = Integer.parseInt(data[1]);
         ClientProxy.clientShopData.addContent(itemID, stackSize);
      }
   },
   SEND_UPDATE_DONATE_VALUE("SEND_UPDATE_DONATE_VALUE", 42, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int donateValue = Integer.parseInt(data[0]);
         PlayerUtils.getInfo(atv.w().h).setDonateMoney(donateValue);
      }
   },
   SEND_UPDATE_CASE_VALUE("SEND_UPDATE_CASE_VALUE", 43, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int caseValue = Integer.parseInt(data[0]);
         PlayerUtils.getInfo(atv.w().h).setCaseValue(caseValue);
      }
   },
   SEND_UPDATE("SEND_UPDATE", 44, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int item_id = Integer.parseInt(data[0]);
         yc item = yc.g[item_id];
         ShopContainer container = (ShopContainer)atv.w().h.bp;
         container.setSlotDrop(item);
         ClientProxy.clientShopData.setSlotDrop(true);
      }
   },
   SEND_UPDATE_MONEY_VALUE("SEND_UPDATE_MONEY_VALUE", 45, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int moneyValue = Integer.parseInt(data[0]);
         PlayerUtils.getInfo(atv.w().h).setMoneyValue(moneyValue);
      }
   },
   SEND_PLAYER_RESPAWN("SEND_PLAYER_RESPAWN", 46, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int x = Integer.parseInt(data[0]);
         int y = Integer.parseInt(data[1]);
         int z = Integer.parseInt(data[2]);
         PlayerClientInfo playerInfo = (PlayerClientInfo)PlayerUtils.getInfo(atv.w().h);
         playerInfo.setRespawn(x, y, z);
      }
   },
   FLAG_CAPTURE_DATA("FLAG_CAPTURE_DATA", 44, DebugPriority.MIDDLE, DebugGroup.CLANS, null) {
      @Override
      public void handle(String... data) {
         ClientProxy.clanData.parseFlagCapture(data);
      }
   },
   SEND_CAPTURE_DATA("SEND_CAPTURE_DATA", 48, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         ClientProxy.captureData.parseInfo(data);
      }
   },
   SEND_BLOOD("SEND_CAPTURE_DATA", 49, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int entityId = Integer.parseInt(data[0]);
         double posX = Double.parseDouble(data[1]);
         double posY = Double.parseDouble(data[2]);
         double posZ = Double.parseDouble(data[3]);
         atv mc = atv.w();
         nn entity = mc.f.a(entityId);
         if (entity != null && entity instanceof of) {
            ParticleLivingEmitter p = EffectsEngine.instance.emittersLiving.get(entityId);
            if (p != null) {
               p.addSplash(posX, posY, posZ);
            }
         }
      }
   },
   SEND_CAPTURE_SIZE("SEND_CAPTURE_SIZE", 50, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int captureSize = Integer.parseInt(data[0]);
         ClientProxy.captureData.captureSize = captureSize;
      }
   },
   SEND_SPAWN_BULLET_HOLE("SEND_SPAWN_BULLET_HOLE", 51, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         double xCoord = Double.parseDouble(data[0]);
         double yCoord = Double.parseDouble(data[1]);
         double zCoord = Double.parseDouble(data[2]);
         int hitSide = Integer.parseInt(data[3]);
         atv mc = atv.w();
         EntityBulletHole entityBulletHole = new EntityBulletHole(mc.f, xCoord, yCoord, zCoord, hitSide);
         mc.k.a(entityBulletHole);
         ParticleBlockEmitter particleEmitter = new ParticleBlockEmitter(new BlockParticleEmitter(mc.f, xCoord, yCoord, zCoord));
         particleEmitter.addHitBullet(xCoord, yCoord, zCoord, hitSide);
         EffectsEngine.instance.addParticleEmitter(particleEmitter);
      }
   },
   SEND_GUI_CLOSE("SEND_GUI_CLOSE", 52, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         atv.w().a(null);
      }
   },
   SEND_UPDATE_SPRINT("SEND_UPDATE_SPRINT", 53, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         PlayerUtils.getInfo(atv.w().h).setSprinting(Float.parseFloat(data[0]));
      }
   },
   SEND_TO_RECOIL("SEND_TO_RECOIL", 54, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... par1) {
         ((ClientWeaponInfo)PlayerUtils.getInfo(atv.w().h).weaponInfo).setRecoil(((ItemWeapon)atv.w().h.by().b()).recoil * 0.1F);
      }
   },
   SEND_GUI_PARAM("SEND_GUI_PARAM", 55, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         PlayerClientInfo info = (PlayerClientInfo)PlayerUtils.getInfo(atv.w().h);
         info.setClientParam(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
      }
   },
   SEND_INVITE_TRADE("SEND_INVITE_TRADE", 56, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         String inviterName = data[0];
         PlayerClientInfo info = (PlayerClientInfo)PlayerUtils.getInfo(atv.w().h);
         info.setTradeInvite(inviterName);
      }
   },
   SEND_OPEN_GUI_TRADE("SEND_OPEN_GUI_TRADE", 57, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         if (Boolean.parseBoolean(data[1])) {
            int entityId = Integer.parseInt(data[0]);
            uf player = (uf)atv.w().f.a(entityId);
            atv.w().h.bp = new TradeContainer(atv.w().h.bn, false, atv.w().h, player);
            atv.w().a(new GuiTrade((TradeContainer)atv.w().h.bp, player));
         } else {
            atv.w().a(null);
         }
      }
   },
   SEND_UPDATE_CLIENT_TRADE("SEND_UPDATE_CLIENT_TRADE", 58, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int slotIndex = Integer.valueOf(data[0]);
         int entityId = Integer.parseInt(data[1]);
         int itemId = Integer.parseInt(data[2]);
         ye stack = new ye(yc.g[itemId]);
         ((TradeContainer)atv.w().h.bp).stacks[slotIndex] = stack;
      }
   },
   SEND_DELETE_CONTAINER("SEND_DELETE_CONTAINER", 59, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int slotIndex = Integer.valueOf(data[0]);
         ((TradeContainer)atv.w().h.bp).stacks[slotIndex] = null;
      }
   },
   SEND_TO_FLASH("SEND_TO_FLASH", 60, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         uf shooter = (uf)atv.w().f.a(Integer.parseInt(data[0]));
         ClientProxy proxy = (ClientProxy)StalkerMain.getProxy();
         ClientWeaponData weaponData = (ClientWeaponData)proxy.clientWeaponManager.weaponsData.get(shooter);
         weaponData.setFlash();
      }
   },
   SEND_ADD_ITEM_SHOP("SEND_ADD_ITEM_SHOP", 61, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int id = Integer.parseInt(data[0]);
         int price = Integer.parseInt(data[1]);
         int tab = Integer.parseInt(data[2]);
         ClientShopManager shopManager = ClientProxy.clientShopManager;
         if (tab == 0) {
            shopManager.addArmor(id, price);
         } else if (tab == 1) {
            shopManager.addWeapon(id, price);
         } else if (tab == 2) {
            shopManager.addMisc(id, price);
         }
      }
   },
   SEND_WAY_POINT("SEND_WAY_POINT", 62, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int x = Integer.parseInt(data[0]);
         int y = Integer.parseInt(data[1]);
         int z = Integer.parseInt(data[2]);
         String name = data[3];
         Waypoint point = new Waypoint(name, x, y, z, true, 0.0F, 0.0F, 0.0F);
         ReiMinimap rmm = ReiMinimap.instance;
         rmm.getWaypoints().add(point);
         rmm.saveWaypoints();
      }
   },
   SEND_SHOT_SOUND("SEND_SHOT_SOUND", 63, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         of shooter = (of)atv.w().f.a(Integer.parseInt(data[0]));
         String sound = data[1];
         shooter.q.a((float)shooter.u, (float)shooter.v + 0.5, (float)shooter.w, sound, 1.5F, shooter.q.s.nextFloat() * 0.1F + 0.9F, false);
      }
   },
   SEND_UPDATE_WEAPON_NBTTAG("SEND_UPDATE_WEAPON_NBTTAG", 64, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         float damageValue = Float.parseFloat(data[0]);
         float recoilValue = Float.parseFloat(data[1]);
         float spreadValue = Float.parseFloat(data[2]);
         int itemId = Integer.parseInt(data[6]);
         yc item = yc.g[itemId];
         if (item instanceof ItemWeapon) {
            int levelDamage = Integer.parseInt(data[3]);
            int levelRecoil = Integer.parseInt(data[4]);
            int levelSpread = Integer.parseInt(data[5]);
            WeaponContainer weaponContainer = (WeaponContainer)atv.w().h.bp;
            by stackWeaponNBT = PlayerUtils.getTag(weaponContainer.updatedWeapon);
            stackWeaponNBT.a("damage", damageValue);
            stackWeaponNBT.a("recoil", recoilValue);
            stackWeaponNBT.a("spread", spreadValue);
            stackWeaponNBT.a("levelDamageUp", levelDamage);
            stackWeaponNBT.a("levelRecoilUp", levelRecoil);
            stackWeaponNBT.a("levelSpreadUp", levelSpread);
         }

         if (item instanceof ItemArmorArtefakt) {
            int levelBullet = Integer.parseInt(data[3]);
            int levelSpeed = Integer.parseInt(data[4]);
            int levelRegeneration = Integer.parseInt(data[5]);
            ArmorContainer armorContainer = (ArmorContainer)atv.w().h.bp;
            by stackWeaponNBT = PlayerUtils.getTag(armorContainer.updatedArmor);
            stackWeaponNBT.a("levelBulletUp", levelBullet);
            stackWeaponNBT.a("levelSpeedUp", levelSpeed);
            stackWeaponNBT.a("levelRegenerationUp", levelRegeneration);
         }
      }
   },
   SEND_PLAYER_POS("SEND_PLAYER_POS", 65, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         atv.w().h.b(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
         atv.w().a(null);
      }
   },
   SEND_TELEPORT_COOLDOWN("SEND_TELEPORT_COOLDOWN", 66, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         PlayerClientInfo playerInfo = (PlayerClientInfo)PlayerUtils.getInfo(atv.w().h);
         playerInfo.teleportCoolDown = Integer.parseInt(data[0]);
      }
   },
   SEND_PLAYER_LOYALE_INFO("SEND_PLAYER_LOYALE_INFO", 67, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         GuiBaseWarehouse currentScreen = (GuiBaseWarehouse)atv.w().n;
         if (currentScreen != null) {
            currentScreen.loyalePoint = Integer.parseInt(data[0]);
         }
      }
   },
   SEND_WAREHOUSE_INFO("SEND_WAREHOUSE_INFO", 68, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int baseID = Integer.parseInt(data[0]);
         String baseName = String.valueOf(data[1]);
         GuiBaseWarehouse currentScreen = (GuiBaseWarehouse)atv.w().n;
         currentScreen.baseID = baseID;
         currentScreen.baseName = baseName;
      }
   },
   SEND_WAREHOUSE_CONTENT("SEND_WAREHOUSE_INFO", 69, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         int itemID = Integer.parseInt(data[0]);
         GuiBaseWarehouse currentScreen = (GuiBaseWarehouse)atv.w().n;
         if (currentScreen != null) {
            currentScreen.contentList.add(itemID);
         }
      }
   },
   SEND_CONTENT_REMOVE("SEND_CONTENT_REMOVE", 70, DebugPriority.LOW, DebugGroup.OTHER, null) {
      @Override
      public void handle(String... data) {
         GuiBaseWarehouse currentScreen = (GuiBaseWarehouse)atv.w().n;
         if (currentScreen != null) {
            atv.w().h.bp = new ContainerWarehouse(atv.w().h, ((ContainerWarehouse)currentScreen.inventorySlots).inventory);
         }
      }
   };

   private final DebugPriority priority;
   private final DebugGroup group;
   private static final ServerOpcode[] $VALUES = new ServerOpcode[]{
      CONTAMINATIONS,
      EJECTION_START,
      EJECTION_END,
      FORCE_COOLDOWN,
      REPUTATION,
      DEATH_SCORE,
      BACKPACK,
      FLASHLIGHT,
      EQUIPPED_WEAPONS,
      LEASHING,
      HANDCUFFS,
      TAG_LIST,
      PLAYER_QUIT,
      RELOAD_START,
      RELOAD_END,
      SHOOT,
      UPDATE_BULLETS,
      MACHINEGUN_INFO,
      CLAN_INFO,
      CLAN_ADD_RULES,
      CLAN_MEMBERS,
      CLAN_LIST,
      CLAN_ADD_ENEMIES,
      CLAN_GUI_UPDATE,
      CLAN_INVITE_SERVER_REQUEST,
      CLAN_LANDS,
      CLAN_COMMON_DATA,
      CLAN_CLEAR_LANDS,
      CLAN_CLEAR_ENEMIES,
      CLAN_CLEAR_MEMBERS,
      CLAN_CLEAR_RULES,
      CLAN_CLEAR_LIST,
      HANDCUFFS_SERVER_REQUEST,
      ENTITY_POS,
      ADD_VELOCITY,
      WINDOW_ID,
      ROTATION,
      TURREL_SHOOT,
      TILE_ENTITY_EVENT
   };

   private ServerOpcode(String var1, int var2, DebugPriority priority, DebugGroup group) {
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

   private ServerOpcode(String x0, int x1, DebugPriority x2, DebugGroup x3, Object x4) {
      this(x0, x1, x2, x3);
   }
}
