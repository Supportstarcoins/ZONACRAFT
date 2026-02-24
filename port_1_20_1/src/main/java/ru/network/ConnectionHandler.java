package ru.stalcraft.network;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.server.MinecraftServer;
import ru.stalcraft.player.IPlayerServerInfo;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class ConnectionHandler implements IConnectionHandler {
   public void playerLoggedIn(Player player, ez netHandler, cm manager) {
      if (player instanceof jv) {
         PlayerInfo playerInfo = PlayerUtils.getInfo((jv)player);
         if (playerInfo instanceof IPlayerServerInfo) {
            ((IPlayerServerInfo)playerInfo).startEjection();
         }
      }
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
