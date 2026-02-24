package ru.stalcraft.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import java.io.File;
import java.util.HashMap;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import ru.stalcraft.Logger;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.blocks.TileEntityDebuff;
import ru.stalcraft.client.clans.ClientClanCaptureData;
import ru.stalcraft.client.clans.ClientClanData;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.effects.EffectsEvent;
import ru.stalcraft.client.effects.EffectsTicker;
import ru.stalcraft.client.ejection.ClientEjectionManager;
import ru.stalcraft.client.gui.GuiIngameStalker;
import ru.stalcraft.client.gui.GuiRepair;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.gui.clans.GuiBaseEdit;
import ru.stalcraft.client.gui.clans.GuiClanCreate;
import ru.stalcraft.client.gui.clans.GuiClanInvite;
import ru.stalcraft.client.gui.clans.GuiClans;
import ru.stalcraft.client.gui.clans.GuiFlag;
import ru.stalcraft.client.gui.shop.ClientShopData;
import ru.stalcraft.client.loaders.StalkerModelManager;
import ru.stalcraft.client.loaders.StalkerObjModelLoader;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.network.ServerOpcode;
import ru.stalcraft.client.particles.CampfireParticleEmitter;
import ru.stalcraft.client.particles.CarouselParticleEmitter;
import ru.stalcraft.client.particles.ElectraParticleEmitter;
import ru.stalcraft.client.particles.FunnelParticleEmitter;
import ru.stalcraft.client.particles.KisselParticleEmitter;
import ru.stalcraft.client.particles.LighterParticleEmitter;
import ru.stalcraft.client.particles.ParticleBlockEmitter;
import ru.stalcraft.client.particles.ParticleLivingEmitter;
import ru.stalcraft.client.particles.PlayerParticleEmitter;
import ru.stalcraft.client.particles.ShotParticleEmitter;
import ru.stalcraft.client.particles.SteamParticleEmitter;
import ru.stalcraft.client.particles.TrampolineParticleEmitter;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.client.player.PlayerClientTicker;
import ru.stalcraft.client.render.Render2d;
import ru.stalcraft.client.render.RenderAnomaly;
import ru.stalcraft.client.render.RenderBackpack;
import ru.stalcraft.client.render.RenderBlockKissel;
import ru.stalcraft.client.render.RenderBlockReed;
import ru.stalcraft.client.render.RenderBolt;
import ru.stalcraft.client.render.RenderBoxEjectionSave;
import ru.stalcraft.client.render.RenderBullet;
import ru.stalcraft.client.render.RenderBulletTracer;
import ru.stalcraft.client.render.RenderCampfire;
import ru.stalcraft.client.render.RenderCorpse;
import ru.stalcraft.client.render.RenderDebuff;
import ru.stalcraft.client.render.RenderElectra;
import ru.stalcraft.client.render.RenderExplosive;
import ru.stalcraft.client.render.RenderFlag;
import ru.stalcraft.client.render.RenderGrenade;
import ru.stalcraft.client.render.RenderKnife;
import ru.stalcraft.client.render.RenderMachineGun;
import ru.stalcraft.client.render.RenderShot;
import ru.stalcraft.client.render.RenderSleeve;
import ru.stalcraft.client.render.RenderStalkerPlayer;
import ru.stalcraft.client.render.RenderTestAnimation;
import ru.stalcraft.client.render.RenderTurrel;
import ru.stalcraft.client.render.RenderWarehouse;
import ru.stalcraft.client.render.RenderWeapon;
import ru.stalcraft.client.render.RenderZombieShooter;
import ru.stalcraft.client.render.RendererManager;
import ru.stalcraft.client.renderer.vector.RendererVectorHandler;
import ru.stalcraft.client.shop.ClientShopManager;
import ru.stalcraft.ejection.IEjectionManager;
import ru.stalcraft.entity.EntityBullet;
import ru.stalcraft.entity.EntityCorpse;
import ru.stalcraft.entity.EntityExplosive;
import ru.stalcraft.entity.EntityGrenade;
import ru.stalcraft.entity.EntityRail;
import ru.stalcraft.entity.EntityShot;
import ru.stalcraft.entity.EntitySleeve;
import ru.stalcraft.entity.EntityTracer;
import ru.stalcraft.entity.EntityTurrel1;
import ru.stalcraft.entity.EntityTurrel2;
import ru.stalcraft.entity.EntityTurrel3;
import ru.stalcraft.entity.EntityZombieShooter;
import ru.stalcraft.items.EntityBolt;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.network.PacketHandler;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.proxy.IClientProxy;
import ru.stalcraft.tile.TileEntityAnomaly;
import ru.stalcraft.tile.TileEntityBaseWarehouse;
import ru.stalcraft.tile.TileEntityCampfire;
import ru.stalcraft.tile.TileEntityDecorative;
import ru.stalcraft.tile.TileEntityEjectionSave;
import ru.stalcraft.tile.TileEntityElectra;
import ru.stalcraft.tile.TileEntityFlag;
import ru.stalcraft.tile.TileEntityMachineGun;
import ru.stalcraft.tile.TileEntityReed;

public final class ClientProxy implements IClientProxy {
   public static IModelCustom modelCube = AdvancedModelLoader.loadModel("/assets/stalker/models/cube.obj");
   public ShaderManager shaderManager = new ShaderManager();
   public ClientWeaponManager clientWeaponManager;
   public static ClientShopData clientShopData = new ClientShopData();
   public static ClientShopManager clientShopManager = new ClientShopManager();
   public static HashMap weaponRenders = new HashMap();
   public static HashMap tags;
   public static Configuration mcconfig;
   public static ClientClanData clanData;
   public static StalkerModelManager modelManager;
   public static ShotLightManager shootLights;
   private static float[] cameraTransform = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
   public static ClientClanCaptureData captureData = new ClientClanCaptureData();
   public RendererManager rendererManager;
   public RenderStalkerPlayer renderPlayer;
   public atv mc = atv.w();
   public ClientController controller;
   private ClientEjectionManager ejectionManager;
   private EffectsEngine effectEngine;
   public ClientTicker clientTicker;
   public RendererVectorHandler rendererVectorHandler;
   public static EntityModelManager entityModelManager;

   public ClientProxy() {
      this.rendererManager = new RendererManager(this.mc);
      this.clientWeaponManager = new ClientWeaponManager(this.mc);
      entityModelManager = new EntityModelManager();
   }

   @Override
   public void preInit(FMLPreInitializationEvent par1) {
      long time = System.currentTimeMillis();
      mcconfig = new Configuration(par1.getSuggestedConfigurationFile());
      StalkerMain.serverIP = mcconfig.get("general", "server_ip", "5.9.142.119:25601").getString();
      GuiSettingsStalker.useWeaponModels = mcconfig.get("general", "use_weapons_models", true).getBoolean(true);
      GuiSettingsStalker.renderSleeves = mcconfig.get("general", "render_sleeves", true).getBoolean(true);
      GuiSettingsStalker.autoReload = mcconfig.get("general", "auto_reload", true).getBoolean(true);
      GuiSettingsStalker.renderEquippedWeapons = mcconfig.get("general", "render_equipped_items", true).getBoolean(true);
      GuiSettingsStalker.dynamicLights = mcconfig.get("general", "use_flashlight", true).getBoolean(true);
      GuiSettingsStalker.highRenderDistance = mcconfig.get("general", "high_render_distance", true).getBoolean(true);
      GuiSettingsStalker.shaderRendering = mcconfig.get("general", "shader_rendering", true).getBoolean(true);
      GuiSettingsStalker.advancedShot = mcconfig.get("general", "advanced_shot", true).getBoolean(true);
      GuiSettingsStalker.particleRenderDistance = mcconfig.get("general", "particle_render_distance", 64).getInt(64);

      try {
         this.effectEngine = new EffectsEngine();
      } catch (Exception var5) {
         var5.printStackTrace();
         Logger.console("[ParticlesAPI] Can't setup particle engine!");
      }

      mcconfig.save();
      Logger.console("PreInitialization is done (" + (System.currentTimeMillis() - time) + " ms)");
   }

   @Override
   public void init(FMLInitializationEvent par1) {
      atv.w().u.an = "ru_RU";
      PlayerUtils.registerPlayerInfo(PlayerClientInfo.class, Side.CLIENT);
      PacketHandler.addPackets(ServerOpcode.values());
      this.replaceGuiIngame();
      this.registerControlKeys();
      this.clientTicker = new ClientTicker(this);
      this.rendererVectorHandler = new RendererVectorHandler(this.clientTicker.worldManager);
      TickRegistry.registerScheduledTickHandler(this.clientTicker, Side.CLIENT);
      TickRegistry.registerTickHandler(new ClientRenderTicker(), Side.CLIENT);
      TickRegistry.registerTickHandler(new PlayerClientTicker(), Side.CLIENT);
      MinecraftForge.EVENT_BUS.register(new ClientEvents());
      MinecraftForge.EVENT_BUS.register(new EffectsEvent());
      TickRegistry.registerTickHandler(new EffectsTicker(), Side.CLIENT);
      RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBullet());
      modelManager = new StalkerModelManager();
      AdvancedModelLoader.registerModelHandler(new StalkerObjModelLoader());
      EffectsEngine.instance.registerEmitter(FunnelParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(LighterParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(SteamParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(TrampolineParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(CarouselParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(KisselParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(ShotParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(ElectraParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(CampfireParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(PlayerParticleEmitter.class);
      EffectsEngine.instance.registerEmitter(ParticleLivingEmitter.class);
      EffectsEngine.instance.registerEmitter(ParticleBlockEmitter.class);
      StalkerMain.kisselRenderId = 100;
      RenderingRegistry.registerBlockHandler(100, RenderBlockKissel.instance);
      Logger.console("Proxy loaded!");
   }

   @Override
   public void registerRenderers() {
      this.rendererManager.onRendererLoaded();
      RenderingRegistry.registerEntityRenderingHandler(EntityShot.class, new RenderShot());
      RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, new RenderGrenade());
      RenderingRegistry.registerEntityRenderingHandler(EntityBolt.class, new RenderBolt());
      RenderingRegistry.registerEntityRenderingHandler(EntitySleeve.class, new RenderSleeve());
      RenderingRegistry.registerEntityRenderingHandler(EntityExplosive.class, new RenderExplosive());
      RenderingRegistry.registerEntityRenderingHandler(EntityRail.class, new Render2d(0.5, "rail"));
      RenderingRegistry.registerEntityRenderingHandler(EntityTurrel1.class, new RenderTurrel("turrel1", "turrel1", 1.0F));
      RenderingRegistry.registerEntityRenderingHandler(EntityTurrel2.class, new RenderTurrel("turrel2", "turrel2", 80.0F));
      RenderingRegistry.registerEntityRenderingHandler(EntityTurrel3.class, new RenderTurrel("turrel3", "turrel3", 80.0F));
      RenderingRegistry.registerEntityRenderingHandler(EntityTracer.class, new RenderBulletTracer());
      MinecraftForgeClient.registerItemRenderer(yc.B.cv, new RenderKnife());
      MinecraftForgeClient.registerItemRenderer(yc.l.cv, new RenderTestAnimation());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineGun.class, new RenderMachineGun());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlag.class, new RenderFlag());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectra.class, new RenderElectra());
      RenderingRegistry.registerEntityRenderingHandler(EntityCorpse.class, new RenderCorpse(this));
      RenderingRegistry.registerEntityRenderingHandler(EntityZombieShooter.class, new RenderZombieShooter());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecorative.class, new ru.stalcraft.client.render.RenderDecorative(this));
      this.renderPlayer = new RenderStalkerPlayer(this);
      RenderingRegistry.registerEntityRenderingHandler(uf.class, this.renderPlayer);
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEjectionSave.class, new RenderBoxEjectionSave());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfire.class, new RenderCampfire());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDebuff.class, new RenderDebuff());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAnomaly.class, new RenderAnomaly());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReed.class, new RenderBlockReed());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBaseWarehouse.class, new RenderWarehouse());
      new RenderBackpack();
   }

   public void initWorldStatics() {
      if (atv.w().f == null) {
         tags = new HashMap();
         clanData = new ClientClanData();
         this.ejectionManager = new ClientEjectionManager();
         shootLights = new ShotLightManager();
      }
   }

   public void replaceGuiIngame() {
      this.mc.r = new GuiIngameStalker(this.mc);
      Logger.console("Stalker GUI loaded");
   }

   public void registerControlKeys() {
      ats[] key = new ats[]{
         new ats("Перезарядка", 19),
         new ats("Фонарик", 33),
         new ats("Группировка", 34),
         new ats("Deprecate", 83),
         new ats("Аптечка 1", 2),
         new ats("Аптечка 2", 3),
         new ats("Аптечка 3", 4),
         new ats("Аптечка 4", 5)
      };
      boolean[] repeat = new boolean[]{false, false, false, false, false, false, false, false};
      this.controller = new ClientController(key, repeat);
      KeyBindingRegistry.registerKeyBinding(this.controller);
   }

   public void spawnParticle(String name, double posX, double posY, double posZ, double velX, double velY, double velZ) {
      bel particle = null;
      if (name.equals("hugesmoke")) {
         bdd w = this.mc.f;
         particle = new bel(w, posX, posY, posZ, velX, velY, velZ, 8.0F);
         Float color = w.s.nextFloat() * 0.2F + 0.7F;
         particle.b(color, color, color);
         particle.g(w.s.nextFloat() * 0.1F + 0.1F);
      }

      if (particle != null) {
         this.mc.k.a(particle);
      }
   }

   public void registerWeaponRenderer(ItemWeapon weapon) {
      RenderWeapon render = new RenderWeapon(this, weapon);
      MinecraftForgeClient.registerItemRenderer(weapon.cv, render);
      weaponRenders.put(weapon.cv, render);
   }

   @Override
   public void postInit(FMLPostInitializationEvent par1) {
      this.effectEngine.shouldUseShaders = GuiSettingsStalker.shaderRendering;
      this.effectEngine.loadIcons();
   }

   public static void displayClanGui() {
      if (clanData.thePlayerClan != null) {
         if (clanData.thePlayerClan.equals("")) {
            atv.w().a(new GuiClanCreate());
         } else {
            atv.w().a(new GuiClans());
         }
      }
   }

   public static void updateClanGui() {
      atv mc = atv.w();
      if (mc.n instanceof GuiClans || mc.n instanceof GuiClanCreate || mc.n instanceof GuiClanInvite) {
         displayClanGui();
      }
   }

   public static boolean isGameRunning() {
      atv mc = atv.w();
      return mc.f != null && (mc.n == null || !mc.n.f());
   }

   @Override
   public File getMinecraftDir() {
      return this.mc.x;
   }

   @Override
   public IEjectionManager getEjectionManager() {
      return this.ejectionManager;
   }

   @Override
   public boolean isRemote() {
      return true;
   }

   @Override
   public void onFlagGui(int par2, int par3, int par4) {
      if (this.mc.c.h()) {
         this.mc.a(new GuiFlag(par2, par3, par4));
      }
   }

   @Override
   public void onGuiRepair(uf player, int type) {
      this.mc.a(new GuiRepair(player, type));
      ClientPacketSender.sendOpenGuiContainer(type);
   }

   @Override
   public void onBaseWarehouseGui() {
      atv.w().a(new GuiBaseEdit());
   }
}
