package ru.demon.money.client;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import ru.demon.money.client.network.ServerOpcode;
import ru.demon.money.network.PacketHandler;
import ru.demon.money.proxy.IProxy;

public class ClientProxy implements IProxy {
   @Override
   public void preInit(FMLPreInitializationEvent event) {
      TickRegistry.registerTickHandler(new ClientTick(), Side.CLIENT);
      PacketHandler.addPackets(ServerOpcode.values());
   }
}
