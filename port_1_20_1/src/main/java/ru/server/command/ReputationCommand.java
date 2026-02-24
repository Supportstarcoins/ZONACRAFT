package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.player.PlayerServerInfo;

public class ReputationCommand extends z {
   public String c() {
      return "reputation";
   }

   public String c(ad icommandsender) {
      return "";
   }

   public void b(ad cs, String[] args) {
      jv par3 = MinecraftServer.F().af().f(cs.c_());
      if (par3 == null || MinecraftServer.F().af().e(par3.bu)) {
         if (args.length != 2) {
            cs.a(new cv().a("Формат ввода команды: /reputation <player> <value>"));
         } else {
            jv player = MinecraftServer.F().af().f(args[0]);
            if (player == null) {
               cs.a(new cv().a("Игрок не найден!"));
            } else if (!args[1].matches("[\\-0-9][0-9]*")) {
               cs.a(new cv().a("Неверный формат числа!"));
            } else {
               int newValue = Integer.parseInt(args[1]);
               if (newValue >= -10 && newValue <= 10) {
                  PlayerServerInfo info = (PlayerServerInfo)PlayerUtils.getInfo(player);
                  info.addReputation(newValue - info.getReputation());
                  cs.a(new cv().a("Репутация игрока " + player.bu + " изменена на " + newValue));
               } else {
                  cs.a(new cv().a("Репутация должна быть от -10 до 10!"));
               }
            }
         }
      }
   }
}
