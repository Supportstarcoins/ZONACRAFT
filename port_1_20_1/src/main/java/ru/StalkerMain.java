package ru.stalcraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.ArrayList;
import java.util.HashSet;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.Display;
import ru.stalcraft.blocks.BlockAnomaly;
import ru.stalcraft.blocks.BlockAnomalyNeighbor;
import ru.stalcraft.blocks.BlockBaseWarehouse;
import ru.stalcraft.blocks.BlockCampfire;
import ru.stalcraft.blocks.BlockCarousel;
import ru.stalcraft.blocks.BlockCoach;
import ru.stalcraft.blocks.BlockDebuff;
import ru.stalcraft.blocks.BlockDecorative;
import ru.stalcraft.blocks.BlockEjectionSave;
import ru.stalcraft.blocks.BlockElectra;
import ru.stalcraft.blocks.BlockFlag;
import ru.stalcraft.blocks.BlockFunnel;
import ru.stalcraft.blocks.BlockKisselFluid;
import ru.stalcraft.blocks.BlockLighter;
import ru.stalcraft.blocks.BlockMachineGun;
import ru.stalcraft.blocks.BlockManager;
import ru.stalcraft.blocks.BlockReed;
import ru.stalcraft.blocks.BlockRespawn;
import ru.stalcraft.blocks.BlockStalkerLadder;
import ru.stalcraft.blocks.BlockSteam;
import ru.stalcraft.blocks.BlockTrampoline;
import ru.stalcraft.blocks.MaterialFakeAir;
import ru.stalcraft.blocks.StalkerBlockStairs;
import ru.stalcraft.blocks.StalkerChest;
import ru.stalcraft.blocks.StalkerDoor;
import ru.stalcraft.clans.IClanManager;
import ru.stalcraft.clans.IFlagManager;
import ru.stalcraft.config.ConfigUpgreade;
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
import ru.stalcraft.inventory.StalkerTab;
import ru.stalcraft.items.EntityBolt;
import ru.stalcraft.items.ItemBackpack;
import ru.stalcraft.items.ItemBolt;
import ru.stalcraft.items.ItemBullet;
import ru.stalcraft.items.ItemDetector;
import ru.stalcraft.items.ItemEmptyBottle;
import ru.stalcraft.items.ItemEnergy;
import ru.stalcraft.items.ItemExplosive;
import ru.stalcraft.items.ItemFlag;
import ru.stalcraft.items.ItemGrenadeLauncherKoster;
import ru.stalcraft.items.ItemGrenadeLauncherM203;
import ru.stalcraft.items.ItemHandcuffs;
import ru.stalcraft.items.ItemKey;
import ru.stalcraft.items.ItemMachineGun;
import ru.stalcraft.items.ItemPNV;
import ru.stalcraft.items.ItemRepair;
import ru.stalcraft.items.ItemRope;
import ru.stalcraft.items.ItemSilencer;
import ru.stalcraft.items.ItemSilencerPistol;
import ru.stalcraft.items.ItemSilencerStandart;
import ru.stalcraft.items.ItemStalkerDoor;
import ru.stalcraft.items.ItemTurrel1;
import ru.stalcraft.items.ItemTurrel2;
import ru.stalcraft.items.ItemTurrel3;
import ru.stalcraft.items.ItemVodka;
import ru.stalcraft.items.ItemWeaponGrenadeM203;
import ru.stalcraft.items.ItemWeaponGrenadeVOG;
import ru.stalcraft.items.ItemWeaponSightAcog;
import ru.stalcraft.items.ItemWeaponSightPO;
import ru.stalcraft.items.ItemWeaponSightPSO;
import ru.stalcraft.network.ConnectionHandler;
import ru.stalcraft.network.GuiHandler;
import ru.stalcraft.network.PacketHandler;
import ru.stalcraft.proxy.IClientProxy;
import ru.stalcraft.proxy.IProxy;
import ru.stalcraft.proxy.IServerProxy;
import ru.stalcraft.proxy.ProxyInstance;
import ru.stalcraft.server.CommonProxy;
import ru.stalcraft.tile.TileEntityAnomaly;
import ru.stalcraft.tile.TileEntityCampfire;
import ru.stalcraft.tile.TileEntityCarousel;
import ru.stalcraft.tile.TileEntityCoach;
import ru.stalcraft.tile.TileEntityEjectionSave;
import ru.stalcraft.tile.TileEntityElectra;
import ru.stalcraft.tile.TileEntityFlag;
import ru.stalcraft.tile.TileEntityFunnel;
import ru.stalcraft.tile.TileEntityKissel;
import ru.stalcraft.tile.TileEntityLighter;
import ru.stalcraft.tile.TileEntityMachineGun;
import ru.stalcraft.tile.TileEntityReed;
import ru.stalcraft.tile.TileEntitySteam;
import ru.stalcraft.tile.TileEntityTrampoline;

@Mod(modid = "StalkerMod", name = "Stalker Mod", version = "1.0[1.6.4]")
@NetworkMod(
   clientSideRequired = true,
   serverSideRequired = true,
   channels = "modST",
   packetHandler = PacketHandler.class,
   connectionHandler = ConnectionHandler.class
)
public class StalkerMain {
   @Instance("StalkerMod")
   public static StalkerMain instance;
   private static final IProxy proxy = new ProxyInstance("ru.stalcraft.client.ClientProxy", "ru.stalcraft.server.CommonProxy").getProxy();
   private static IServerProxy proxySinglePlayer;
   public static final boolean SINGLEPLAYER = true;
   public static final boolean LOCALHOST = false;
   public static ww tab;
   public static ww tabDecor = new ww(ww.getNextID(), "Tab decorative");
   public static HashSet destroyableBlocks = new HashSet();
   public static String serverIP;
   public static int kisselRenderId;
   public static akc fakeAir;
   public static aqz tramp;
   public static BlockCarousel carousel;
   public static BlockFunnel funnel;
   public static BlockAnomaly lighter;
   public static aqz coach;
   public static aqz electra;
   public static aqz steam;
   public static aqz kisselFluidBlock;
   public static BlockAnomalyNeighbor anomalyNeighbor;
   public static aqz radiation1;
   public static aqz radiation2;
   public static aqz radiation3;
   public static aqz chemical1;
   public static aqz chemical2;
   public static aqz chemical3;
   public static aqz biological1;
   public static aqz biological2;
   public static aqz biological3;
   public static aqz psycho;
   public static aqz machineGun;
   public static aqz flag;
   public static aqz respawnBlock;
   public static aqz stalkerWood;
   public static aqz stalkerStairs;
   public static aqz stalkerLadder;
   public static aqz stalkerDoor;
   public static aqz stalkerChest;
   public static Fluid kisselFluid;
   public static yc testArtefakt;
   public static yc radiationDetector;
   public static yc chemicalDetector;
   public static yc biologicalDetector;
   public static yc medicine1;
   public static yc medicine2;
   public static yc medicine3;
   public static yc bandage;
   public static yc radiationProtector;
   public static yc biologicalProtector;
   public static yc psychoProtector;
   public static yc novokaine;
   public static yc vodka;
   public static yc emptyBottle;
   public static yc backpack1;
   public static yc backpack2;
   public static yc backpack3;
   public static yc handcuffs;
   public static yc rope;
   public static yc key;
   public static yc flagAxe;
   public static yc flagSword;
   public static yc itemStalkerDoor;
   public static ItemExplosive explosive;
   public static yc turrel1;
   public static yc turrel2;
   public static yc turrel3;
   public static ItemSilencer silencer;
   public static ItemGrenadeLauncherM203 grenadeLauncherM203;
   public static ItemGrenadeLauncherKoster grenadeLauncherKoster;
   public static ItemWeaponGrenadeM203 weaponGrenadeM203;
   public static ItemWeaponGrenadeVOG weaponGrenadeVOG;
   public static ItemWeaponSightPSO weaponSightPSO;
   public static ItemWeaponSightAcog weaponSightAcog;
   public static ItemWeaponSightPO weaponSightPO;
   public static ItemRepair weaponRepaitKit;
   public static ItemRepair armorRepaitKit;
   public static BlockDecorative spectateBlock;
   public SmartMovingHelper smHelper;
   public static ItemSilencer pistolsilencer;
   public static IFlagManager flagManager;
   public static IClanManager clanManager;
   public static BlockReed blockReed;
   public static BlockManager blockManager;

   @EventHandler
   public void preInit(FMLPreInitializationEvent par1) {
      if (FMLCommonHandler.instance().getSide().isClient()) {
         Display.setTitle("MysteryZone");
      }

      try {
         proxySinglePlayer = CommonProxy.class.newInstance();
         proxySinglePlayer.preInit(par1);
      } catch (InstantiationException var3) {
         var3.printStackTrace();
      } catch (IllegalAccessException var4) {
         var4.printStackTrace();
      }

      proxy.preInit(par1);
   }

   @EventHandler
   public void init(FMLInitializationEvent par1) {
      long time = System.currentTimeMillis();
      proxySinglePlayer.init(par1);
      blockManager = new BlockManager();
      blockManager.loadBlock(par1.getSide());
      proxy.init(par1);
      Logger.console("Proxy is loaded (" + (System.currentTimeMillis() - time) + " ms)");
      time = System.currentTimeMillis();
      Config.readConfig();
      tab = new StalkerTab("Stalker mod");
      ItemsConfig.readConfig();
      ConfigUpgreade.readConfig();
      Logger.console("Custom items config is read (" + (System.currentTimeMillis() - time) + " ms)");
      time = System.currentTimeMillis();
      this.registerBlocks();
      this.registerItems();
      this.registerEntities();
      this.registerExplosibleBlocks();
      Logger.console("Items, blocks and entities are loaded (" + (System.currentTimeMillis() - time) + " ms)");
      if (proxy instanceof IClientProxy) {
         ((IClientProxy)proxy).registerRenderers();
         time = System.currentTimeMillis();
         Logger.console("Renderers are registered (" + (System.currentTimeMillis() - time) + " ms)");
      }

      NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
   }

   private void registerExplosibleBlocks() {
      HashSet blocks = new HashSet();
      blocks.add(stalkerWood.cF);
      blocks.add(stalkerStairs.cF);
      blocks.add(stalkerLadder.cF);
      blocks.add(stalkerDoor.cF);
      blocks.add(stalkerChest.cF);
      explosive.applyExplosibleBlocks(blocks);
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent par1) {
      this.smHelper = new SmartMovingHelper();
      proxySinglePlayer.postInit(par1);
      proxy.postInit(par1);
   }

   @EventHandler
   public void serverStart(FMLServerStartingEvent par1) throws Exception {
      proxySinglePlayer.serverStart(par1);
      if (proxy instanceof IServerProxy) {
         ((IServerProxy)proxy).serverStart(par1);
      }
   }

   private void registerEntities() {
      byte entityId = 1;
      int var2 = entityId + 1;
      EntityRegistry.registerModEntity(EntityBullet.class, "EntityBullet", entityId, this, 64, 10000, true);
      EntityRegistry.registerModEntity(EntityShot.class, "EntityLight", var2++, this, 64, 3, true);
      EntityRegistry.registerModEntity(EntityGrenade.class, "EntityGrenade", var2++, this, 64, 10000, true);
      EntityRegistry.registerModEntity(EntityBolt.class, "EntityBolt", var2++, this, 64, 10000, true);
      EntityRegistry.registerModEntity(EntitySleeve.class, "EntitySleeve", var2++, this, 64, 3, true);
      EntityRegistry.registerModEntity(EntityRail.class, "EntityRail", var2++, this, 64, 10000, true);
      EntityRegistry.registerModEntity(EntityExplosive.class, "EntityExplosive", var2++, this, 64, 10000, true);
      EntityRegistry.registerModEntity(EntityTurrel1.class, "EntityTurrel1", var2++, this, 64, 10000, true);
      EntityRegistry.registerModEntity(EntityTurrel2.class, "EntityTurrel2", var2++, this, 64, 10000, true);
      EntityRegistry.registerModEntity(EntityTurrel3.class, "EntityTurrel3", var2++, this, 64, 10000, true);
      EntityRegistry.registerModEntity(EntityCorpse.class, "EntityCorpse", var2++, this, 64, 20, true);
      EntityRegistry.registerModEntity(EntityTracer.class, "EntityTracer", var2++, this, 64, 10000, true);
      EntityRegistry.registerGlobalEntityID(EntityZombieShooter.class, "EntityZombieShooter", 118, 20496, 16777215);
   }

   public void registerBlocks() {
      fakeAir = new MaterialFakeAir(ake.b);
      int blockID = 3100;
      int var3 = blockID + 1;
      BlockTrampoline var10000 = new BlockTrampoline(var3++, new AnomalyDrop(Config.trampolineDrop));
      tramp = var10000.c("tramp");
      BlockCarousel var4 = new BlockCarousel(var3++, new AnomalyDrop(Config.carouselDrop));
      carousel = (BlockCarousel)var4.c("carousel");
      BlockFunnel var5 = new BlockFunnel(var3++, new AnomalyDrop(Config.blackHoleDrop));
      funnel = (BlockFunnel)var5.c("hole");
      BlockLighter var6 = new BlockLighter(var3++, new AnomalyDrop(Config.lighterDrop));
      lighter = (BlockAnomaly)var6.c("lighter");
      BlockCoach var7 = new BlockCoach(var3++, new AnomalyDrop(Config.coachDrop));
      coach = var7.c("coach");
      BlockElectra var8 = new BlockElectra(var3++, new AnomalyDrop(Config.electraDrop));
      electra = var8.c("electra");
      BlockSteam var9 = new BlockSteam(var3++, new AnomalyDrop(Config.steamDrop));
      steam = var9.c("steam");
      BlockDebuff var10 = new BlockDebuff(var3++, 0, 1, 5);
      radiation1 = var10.c("radiation1");
      var10 = new BlockDebuff(var3++, 0, 2, 5);
      radiation2 = var10.c("radiation2");
      var10 = new BlockDebuff(var3++, 0, 3, 5);
      radiation3 = var10.c("radiation3");
      var10 = new BlockDebuff(var3++, 1, 1, 5);
      chemical1 = var10.c("chemical1");
      var10 = new BlockDebuff(var3++, 1, 2, 5);
      chemical2 = var10.c("chemical2");
      var10 = new BlockDebuff(var3++, 1, 3, 5);
      chemical3 = var10.c("chemical3");
      var10 = new BlockDebuff(var3++, 2, 1, 5);
      biological1 = var10.c("biological1");
      var10 = new BlockDebuff(var3++, 2, 2, 5);
      biological2 = var10.c("biological2");
      var10 = new BlockDebuff(var3++, 2, 3, 5);
      biological3 = var10.c("biological3");
      var10 = new BlockDebuff(var3++, 3, 3, 5);
      psycho = var10.c("psycho");
      BlockMachineGun var11 = new BlockMachineGun(var3++);
      machineGun = var11;
      int kisselId = var3++;
      BlockFlag var12 = new BlockFlag(var3++);
      flag = var12;
      BlockRespawn var13 = new BlockRespawn(var3++);
      respawnBlock = var13;
      aqz var14 = new aqz(var3++, akc.d);
      stalkerWood = var14.c(1.0F).b(5.0F).a(aqz.h).c("stalker_wood").a(tab).d("stalker:wood");
      StalkerBlockStairs var15 = new StalkerBlockStairs(var3++, stalkerWood, 1);
      stalkerStairs = var15.c("stalker_stairs").d("stalker:wood").a(tab);
      BlockStalkerLadder var16 = new BlockStalkerLadder(var3++);
      stalkerLadder = var16.c(0.4F).a(aqz.q).c("stalker_ladder").d("stalker:ladder").a(tab);
      StalkerDoor var17 = new StalkerDoor(var3++);
      stalkerDoor = var17.c(3.0F).a(aqz.h).c("stalker_door").d("stalker:door");
      aqz.x[stalkerStairs.cF] = true;
      BlockAnomalyNeighbor var18 = new BlockAnomalyNeighbor(var3++);
      anomalyNeighbor = var18;
      StalkerChest var19 = new StalkerChest(var3++);
      stalkerChest = var19.a(tab).c(2.5F).a(aqz.h).c("stalker_chest");
      kisselFluid = new Fluid("kisselFluid").setBlockID(kisselId);
      FluidRegistry.registerFluid(kisselFluid);
      kisselFluidBlock = new BlockKisselFluid(kisselId, kisselFluid);
      GameRegistry.registerBlock(kisselFluidBlock, "kisselFluidBlock");
      kisselFluid.setUnlocalizedName(kisselFluidBlock.a());
      new BlockEjectionSave(var3++);
      LanguageRegistry.addName(kisselFluidBlock, "Кисель");
      GameRegistry.registerBlock(carousel, "Carousel");
      GameRegistry.registerBlock(tramp, "Trampoline");
      GameRegistry.registerBlock(funnel, "BlackHole");
      GameRegistry.registerBlock(lighter, "Lighter");
      GameRegistry.registerBlock(coach, "Coach");
      GameRegistry.registerBlock(electra, "Electra");
      GameRegistry.registerBlock(steam, "Steam");
      GameRegistry.registerBlock(radiation1, "Radiation (Level 1)");
      GameRegistry.registerBlock(radiation2, "Radiation (Level 2)");
      GameRegistry.registerBlock(radiation3, "Radiation (Level 3)");
      GameRegistry.registerBlock(chemical1, "Chemical cont. (Level 1)");
      GameRegistry.registerBlock(chemical2, "Chemical cont. (Level 2)");
      GameRegistry.registerBlock(chemical3, "Chemical cont. (Level 3)");
      GameRegistry.registerBlock(biological1, "Biological cont. (Level 1)");
      GameRegistry.registerBlock(biological2, "Biological cont. (Level 2)");
      GameRegistry.registerBlock(biological3, "Biological cont. (Level 3)");
      GameRegistry.registerBlock(psycho, "Psycho cont.");
      GameRegistry.registerBlock(respawnBlock, "Respawn");
      GameRegistry.registerBlock(stalkerWood, "StalkerWood");
      GameRegistry.registerBlock(stalkerStairs, "StalkerStairs");
      GameRegistry.registerBlock(stalkerLadder, "StalkerLadder");
      GameRegistry.registerBlock(stalkerDoor, "StalkerDoor");
      GameRegistry.registerBlock(stalkerChest, "StalkerChest");
      GameRegistry.registerBlock(anomalyNeighbor, "AnomalyNeighbor");
      GameRegistry.registerBlock(flag, ItemFlag.class, "Flag");
      GameRegistry.registerBlock(machineGun, ItemMachineGun.class, "Machine Gun");
      GameRegistry.registerTileEntity(TileEntityEjectionSave.class, "EjectionSave");
      GameRegistry.registerTileEntity(TileEntitySteam.class, "steamTile");
      GameRegistry.registerTileEntity(TileEntityMachineGun.class, "MachineGunTile");
      GameRegistry.registerTileEntity(TileEntityFunnel.class, "BlackHoleTile");
      GameRegistry.registerTileEntity(TileEntityTrampoline.class, "TrampolineTile");
      GameRegistry.registerTileEntity(TileEntityLighter.class, "LighterTile");
      GameRegistry.registerTileEntity(TileEntityElectra.class, "ElectraTile");
      GameRegistry.registerTileEntity(TileEntityCoach.class, "CoachTile");
      GameRegistry.registerTileEntity(TileEntityCarousel.class, "CarouselTile");
      GameRegistry.registerTileEntity(TileEntityKissel.class, "KisselTile");
      GameRegistry.registerTileEntity(TileEntityFlag.class, "FlagTile");
      GameRegistry.registerTileEntity(TileEntityAnomaly.class, "anomaly");
      LanguageRegistry.addName(tramp, "Батут");
      LanguageRegistry.addName(carousel, "Карусель");
      LanguageRegistry.addName(funnel, "Воронка");
      LanguageRegistry.addName(lighter, "Жарка");
      LanguageRegistry.addName(coach, "Тренер");
      LanguageRegistry.addName(electra, "Электра");
      LanguageRegistry.addName(steam, "Пар");
      LanguageRegistry.addName(radiation1, "Radiation (Level 1)");
      LanguageRegistry.addName(radiation2, "Radiation (Level 2)");
      LanguageRegistry.addName(radiation3, "Radiation (Level 3)");
      LanguageRegistry.addName(chemical1, "Chemical cont. (Level 1)");
      LanguageRegistry.addName(chemical2, "Chemical cont. (Level 2)");
      LanguageRegistry.addName(chemical3, "Chemical cont. (Level 3)");
      LanguageRegistry.addName(biological1, "Biological cont. (Level 1)");
      LanguageRegistry.addName(biological2, "Biological cont. (Level 2)");
      LanguageRegistry.addName(biological3, "Biological cont. (Level 3)");
      LanguageRegistry.addName(psycho, "Psycho cont.");
      LanguageRegistry.addName(stalkerWood, "Строительный блок");
      LanguageRegistry.addName(stalkerStairs, "Ступеньки");
      LanguageRegistry.addName(stalkerLadder, "Лестница");
      LanguageRegistry.addName(stalkerDoor, "Дверь");
      LanguageRegistry.addName(stalkerChest, "Хранилище");
      new BlockCampfire(var3++);
      blockReed = new BlockReed(var3++);
      GameRegistry.registerTileEntity(TileEntityReed.class, "TileEntityReed");
      GameRegistry.registerTileEntity(TileEntityCampfire.class, "TileEntityCampfire");
      spectateBlock = new BlockDecorative(var3++, "Невидимый блок", "null", 0.0F, true, true);
      new BlockBaseWarehouse(var3++);
   }

   public void registerItems() {
      short itemID = 14700;
      int var2 = itemID + 1;
      ItemDetector var10000 = new ItemDetector(var2++, new int[]{radiation1.cF, radiation2.cF, radiation3.cF}, "radiation_detector", "Детектор радиации");
      radiationDetector = var10000;
      var10000 = new ItemDetector(var2++, new int[]{chemical1.cF, chemical2.cF, chemical3.cF}, "chemical_detector", "Термометр");
      chemicalDetector = var10000;
      var10000 = new ItemDetector(var2++, new int[]{biological1.cF, biological2.cF, biological3.cF}, "biological_detector", "Детектор биозаражения");
      biologicalDetector = var10000;
      new ItemEnergy(var2++);
      ItemVodka var5 = new ItemVodka(var2++);
      vodka = var5;
      ItemEmptyBottle var6 = new ItemEmptyBottle(var2++);
      emptyBottle = var6;
      ItemBackpack var7 = new ItemBackpack(var2++, "backpack_a", "Малый рюкзак", 10);
      backpack1 = var7;
      var7 = new ItemBackpack(var2++, "backpack_b", "Средний рюкзак", 20);
      backpack2 = var7;
      var7 = new ItemBackpack(var2++, "backpack_c", "Большой рюкзак", 30);
      backpack3 = var7;
      ItemHandcuffs var8 = new ItemHandcuffs(var2++);
      handcuffs = var8;
      ItemRope var9 = new ItemRope(var2++);
      rope = var9;
      ItemKey var10 = new ItemKey(var2++);
      key = var10;
      new ItemBullet(14955, "Пулеметная лента", "machinegun_shell", new ArrayList(), 1, 0);
      yc var11 = new yc(var2++);
      flagAxe = var11.b("flag_axe").d(1).d("wood_axe").a(tab).q();
      var11 = new yc(var2++);
      flagSword = var11.b("flag_sword").d(1).d("wood_sword").a(tab).q();
      ItemStalkerDoor var12 = new ItemStalkerDoor(var2++);
      itemStalkerDoor = var12;
      ItemExplosive var13 = new ItemExplosive(var2++);
      explosive = var13;
      ItemTurrel1 var14 = new ItemTurrel1(var2++);
      turrel1 = var14;
      ItemTurrel2 var15 = new ItemTurrel2(var2++);
      turrel2 = var15;
      ItemTurrel3 var16 = new ItemTurrel3(var2++);
      turrel3 = var16;
      silencer = new ItemSilencerStandart(var2++, 0, "Глушитель");
      pistolsilencer = new ItemSilencerPistol(var2++, 1, "Пистолетный глушитель");
      grenadeLauncherM203 = new ItemGrenadeLauncherM203(var2++);
      weaponGrenadeM203 = new ItemWeaponGrenadeM203(var2++);
      grenadeLauncherKoster = new ItemGrenadeLauncherKoster(var2++);
      weaponGrenadeVOG = new ItemWeaponGrenadeVOG(var2++);
      weaponSightPSO = new ItemWeaponSightPSO(var2++);
      weaponSightAcog = new ItemWeaponSightAcog(var2++);
      weaponSightPO = new ItemWeaponSightPO(var2++);
      new ItemBolt(var2++, "Болт");
      weaponRepaitKit = new ItemRepair(var2++, 0);
      armorRepaitKit = new ItemRepair(var2++, 1);
      new ItemPNV(var2++);
   }

   public static IProxy getProxy() {
      return (IProxy)(FMLCommonHandler.instance().getEffectiveSide().isServer() ? proxySinglePlayer : proxy);
   }
}
