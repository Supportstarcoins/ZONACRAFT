package ru.demon.money;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import ru.demon.money.network.ConnectionHandler;
import ru.demon.money.network.PacketHandler;
import ru.demon.money.proxy.IProxy;
import ru.demon.money.proxy.ProxyInstance;
import ru.demon.money.server.ServerProxy;
import ru.demon.money.utils.PlayerMoneyUtils;

@Mod(modid = "AreaMoneyMod", name = "AreaMoneyMod")
@NetworkMod(
   clientSideRequired = true,
   serverSideRequired = true,
   channels = "money",
   packetHandler = PacketHandler.class,
   connectionHandler = ConnectionHandler.class
)
public class MoneyMod {
   @Instance("AreaMoneyMod")
   public static MoneyMod instance;
   public static final boolean SINGLEPLAYER = true;
   public static ww tabMoney = new ww("tabMoney");
   public static IProxy proxy;
   public static IProxy proxySingleplayer;
   public PlayerMoneyUtils playerUtils;
   public List<ItemMoney> items = new ArrayList<>();

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      this.playerUtils = new PlayerMoneyUtils();
      MinecraftForge.EVENT_BUS.register(new Events());

      try {
         proxy = new ProxyInstance("ru.demon.money.client.ClientProxy", "ru.demon.money.server.ServerProxy").getProxy();
         proxy.preInit(event);
      } catch (Exception var4) {
         var4.getMessage();
      }

      proxySingleplayer = new ServerProxy();
      proxySingleplayer.preInit(event);
      this.items.add(new ItemMoney(28000, "1 рубль", "1rub", 1));
      this.items.add(new ItemMoney(28001, "10 рубль", "10rub", 10));
      this.items.add(new ItemMoney(28002, "50 рубль", "50rub", 50));
      this.items.add(new ItemMoney(28003, "100 рубль", "100rub", 100));
      this.items.add(new ItemMoney(28004, "500 рубль", "500rub", 500));
      this.items.add(new ItemMoney(28005, "1000 рубль", "1000rub", 1000));
      this.items.add(new ItemMoney(28006, "5000 рубль", "5000rub", 5000));

      for (ItemMoney item : this.items) {
         GameRegistry.registerItem(item, "money" + item.cv);
      }
   }
}
