package ru.demon.money.network;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.server.MinecraftServer;

public class ConnectionHandler implements IConnectionHandler {
   public void playerLoggedIn(Player player, ez netHandler, cm manager) {
      jv par4 = (jv)player;
   }

   public String connectionReceived(jy netHandler, cm manager) {
      return null;
   }

   public void connectionOpened(ez netClientHandler, String server, int port, cm manager) {
   }

   public void connectionOpened(ez netClientHandler, MinecraftServer server, cm manager) {
   }

   public void connectionClosed(cm manager) {
   }

   public void clientLoggedIn(ez clientHandler, cm manager, ep login) {
   }
}
