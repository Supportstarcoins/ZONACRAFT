package ru.stalcraft.server;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import java.io.File;
import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import ru.stalcraft.SmartMovingHelper;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.clans.IClanManager;
import ru.stalcraft.clans.IFlagManager;
import ru.stalcraft.ejection.IEjectionManager;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.network.PacketHandler;
import ru.stalcraft.player.IAntiRelog;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.proxy.IServerProxy;
import ru.stalcraft.server.capture.CaptureManager;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.clans.SaveHandler;
import ru.stalcraft.server.command.CommandExtraordinaryRespawn;
import ru.stalcraft.server.command.CommandGif;
import ru.stalcraft.server.command.CommandNoDrop;
import ru.stalcraft.server.command.CommandWayPoint;
import ru.stalcraft.server.command.DeathTimerCommand;
import ru.stalcraft.server.command.DonateMoneyCommand;
import ru.stalcraft.server.command.EjectionCommand;
import ru.stalcraft.server.command.FlagCaptureCommand;
import ru.stalcraft.server.command.LandrentCommand;
import ru.stalcraft.server.command.MoneyCommand;
import ru.stalcraft.server.command.PacketCommand;
import ru.stalcraft.server.command.PlayerMoneyCommand;
import ru.stalcraft.server.command.ReputationCommand;
import ru.stalcraft.server.command.SyncCommand;
import ru.stalcraft.server.configs.NewCaseConfig;
import ru.stalcraft.server.configs.ShopConfig;
import ru.stalcraft.server.ejection.ServerEjectionManager;
import ru.stalcraft.server.network.ClientOpcode;
import ru.stalcraft.server.player.PlayerServerInfo;
import ru.stalcraft.server.player.PlayerTracker;
import ru.stalcraft.server.shop.ServerShopData;
import ru.stalcraft.tile.TileEntityFlag;
import ru.stalcraft.world.WorldManager;

public class CommonProxy implements IServerProxy {
   public static AntiRelog antiRelog;
   public static ServerShopData serverShopData = new ServerShopData();
   private ArrayList mobsToDelete = new ArrayList();
   private ArrayList creaturesToDelete = new ArrayList();
   public static ServerEjectionManager serverEjectionManager;
   public static ClanManager clanManager;
   public CaptureManager captureManager = new CaptureManager(this);
   public static SaveHandler clanSaveHandler;
   public ServerTicker ticker;
   public SmartMovingHelper smHelper;
   private final File mcDir = new File(".");
   public WorldManager worldManager;

   public void spawnParticle(String name, double posX, double posY, double posZ, double velX, double velY, double velZ) {
   }

   public void registerWeaponRenderer(ItemWeapon weapon) {
   }

   public void registerRenderers() {
   }

   @Override
   public void preInit(FMLPreInitializationEvent par1) {
   }

   @Override
   public void init(FMLInitializationEvent par1) {
      PacketHandler.addPackets(ClientOpcode.values());
      WeightMap.loadWeightMap();
      clanManager = new ClanManager();
      StalkerMain.clanManager = clanManager;
      StalkerMain.flagManager = clanManager.flagManager;
      this.ticker = new ServerTicker();
      TickRegistry.registerTickHandler(this.ticker, Side.SERVER);
      antiRelog = new AntiRelog();
      PlayerUtils.registerPlayerInfo(PlayerServerInfo.class, Side.SERVER);
      MinecraftForge.EVENT_BUS.register(new ServerEvents());
      GameRegistry.registerPlayerTracker(new PlayerTracker());
      NewCaseConfig.readConfig();
      ShopConfig.readConfig();
   }

   @Override
   public void postInit(FMLPostInitializationEvent par1) {
   }

   @Override
   public void serverStart(FMLServerStartingEvent par1) {
      serverEjectionManager = new ServerEjectionManager();
      par1.registerServerCommand(new DeathTimerCommand());
      par1.registerServerCommand(new CommandNoDrop());
      par1.registerServerCommand(new EjectionCommand());
      par1.registerServerCommand(new LandrentCommand());
      par1.registerServerCommand(new MoneyCommand());
      par1.registerServerCommand(new ReputationCommand());
      par1.registerServerCommand(new PacketCommand());
      par1.registerServerCommand(new SyncCommand());
      par1.registerServerCommand(new DonateMoneyCommand());
      par1.registerServerCommand(new FlagCaptureCommand());
      par1.registerServerCommand(new PlayerMoneyCommand());
      par1.registerServerCommand(new CommandGif());
      par1.registerServerCommand(new CommandWayPoint());
      par1.registerServerCommand(new CommandExtraordinaryRespawn());
      this.mobsToDelete.add(tt.class);
      this.mobsToDelete.add(tr.class);
      this.mobsToDelete.add(tf.class);
      this.mobsToDelete.add(ts.class);
      this.mobsToDelete.add(tg.class);
      this.creaturesToDelete.add(rz.class);
      this.creaturesToDelete.add(ry.class);
      this.creaturesToDelete.add(rq.class);
      this.creaturesToDelete.add(rs.class);
      this.creaturesToDelete.add(rr.class);
      acq[] arr$ = acq.a;
      int len$ = arr$.length;
      clanSaveHandler = new SaveHandler();
      clanSaveHandler.loadClans(clanManager);
   }

   @Override
   public File getMinecraftDir() {
      return this.mcDir;
   }

   @Override
   public IFlagManager getFlagManager() {
      return clanManager.flagManager;
   }

   @Override
   public IEjectionManager getEjectionManager() {
      return serverEjectionManager;
   }

   @Override
   public IClanManager getClanManager() {
      return clanManager;
   }

   @Override
   public boolean isRemote() {
      return false;
   }

   @Override
   public IAntiRelog getAntiRelog() {
      return antiRelog;
   }

   @Override
   public void getRespawn(uf player, asp tile) {
      PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
      playerInfo.setRespawn(tile.l, tile.m, tile.n);
   }

   @Override
   public void getPlayerProperties(uf player, int propIndex, int timeValue, int levelValue) {
      if (propIndex == 0) {
         PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
         playerInfo.setFire(20, 10);
      }
   }

   @Override
   public void onFlagCapture(TileEntityFlag tileEntityFlag) {
      this.captureManager.onFlagCapture(tileEntityFlag);
   }
}
