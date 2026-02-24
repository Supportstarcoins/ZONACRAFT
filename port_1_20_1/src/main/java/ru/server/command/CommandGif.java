package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;

public class CommandGif extends z {
   public String c() {
      return "gif";
   }

   public String c(ad sender) {
      return "";
   }

   public void b(ad sender, String[] args) {
      jv player = MinecraftServer.F().af().f(sender.c_());
      if (player != null) {
         int id = Integer.parseInt(args[1]);
         int size = Integer.parseInt(args[2]);
         ye stack = new ye(yc.g[id], size, 0);
         player.bn.a(stack);
      }
   }
}
