package ru.demon.pickup;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import ru.demon.pickup.handlers.DemonKeyHandler;
import ru.demon.pickup.handlers.EntityItemHooks;
import ru.demon.pickup.handlers.PacketHandler;
import ru.demon.pickup.render.OverlayRenderer;

@Mod(modid = "demonmod", name = "Demon", version = "1.6.4")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = "demonmod", packetHandler = PacketHandler.class)
public class DemonPickupItemBase {
   @Instance("demonmod")
   public static DemonPickupItemBase INSTANCE;

   @EventHandler
   public void load(FMLInitializationEvent ev) {
      MinecraftForge.EVENT_BUS.register(new EntityItemHooks());
      if (ev.getSide() == Side.CLIENT) {
         MinecraftForge.EVENT_BUS.register(new OverlayRenderer());
         TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
         KeyBindingRegistry.registerKeyBinding(new DemonKeyHandler());
      }
   }
}
