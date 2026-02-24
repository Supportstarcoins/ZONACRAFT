package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.server.network.ServerPacketSender;

public class CommandWayPoint extends z {
   public String c() {
      return "waypoint";
   }

   public String c(ad sender) {
      return "";
   }

   public void b(ad sender, String[] args) {
      jv player = MinecraftServer.F().af().f(args[0]);
      if (player != null) {
         ServerPacketSender.sendWayPoint(player, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[4]);
      }
   }
}
