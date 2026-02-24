package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.player.PlayerServerInfo;

public class DonateMoneyCommand extends z {
   public String c() {
      return "donateValue";
   }

   public String c(ad icommandsender) {
      return "";
   }

   public void b(ad cs, String[] args) {
      jv player = MinecraftServer.F().af().f(args[0]);
      if (player != null) {
         int newValue = Integer.parseInt(args[1]);
         if (newValue <= 100000000) {
            PlayerServerInfo info = (PlayerServerInfo)PlayerUtils.getInfo(player);
            info.addDonateValue(newValue);
            cs.a(new cv().a("Игроку было выдано: " + newValue + "руб."));
         } else {
            cs.a(new cv().a("Max value 100000000"));
         }
      }
   }
}
