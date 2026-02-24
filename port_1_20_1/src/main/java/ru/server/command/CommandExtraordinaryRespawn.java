package ru.stalcraft.server.command;

import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.player.PlayerServerInfo;

public class CommandExtraordinaryRespawn extends z {
   public String c() {
      return "extrarespawn";
   }

   public String c(ad sender) {
      return "";
   }

   public void b(ad sender, String[] args) {
      if (args.length < 3) {
         sender.a(new cv().a("Долбоеб сука команду правильно введи: /extrarespawn x y z"));
      } else {
         int x = Integer.parseInt(args[0]);
         int y = Integer.parseInt(args[1]);
         int z = Integer.parseInt(args[2]);

         for (uf player : ((nn)sender).q.h) {
            PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
            playerInfo.setExtraRespawn(x, y, z);
         }

         sender.a(new cv().a("Ты смог ! Красава блять молодец йбашишь."));
      }
   }
}
