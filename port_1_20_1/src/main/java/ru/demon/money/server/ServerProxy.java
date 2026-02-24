package ru.demon.money.server;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import ru.demon.money.network.PacketHandler;
import ru.demon.money.proxy.IProxy;
import ru.demon.money.server.network.ClientOpcode;
import ru.demon.money.server.player.PlayerServerTracker;

public class ServerProxy implements IProxy {
   @Override
   public void preInit(FMLPreInitializationEvent event) {
      TickRegistry.registerTickHandler(new ServerTick(), Side.SERVER);
      GameRegistry.registerPlayerTracker(new PlayerServerTracker());
      PacketHandler.addPackets(ClientOpcode.values());
   }
}
