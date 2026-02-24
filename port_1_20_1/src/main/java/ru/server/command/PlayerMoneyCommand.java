package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.demon.money.server.network.ServerPacketMoneySender;
import ru.demon.money.utils.PlayerMoneyUtils;

public class PlayerMoneyCommand extends z {
   public String c() {
      return "moneyValue";
   }

   public String c(ad icommandsender) {
      return "";
   }

   public void b(ad cs, String[] args) {
      jv par3 = MinecraftServer.F().af().f(cs.c_());
      if (par3 == null || MinecraftServer.F().af().e(par3.bu)) {
         if (args.length != 2) {
            cs.a(new cv().a("Формат ввода команды: /moneyValue <player> <value>"));
         } else {
            jv player = MinecraftServer.F().af().f(args[0]);
            if (player == null) {
               cs.a(new cv().a("Игрок не найден!"));
            } else if (!args[1].matches("[\\-0-9][0-9]*")) {
               cs.a(new cv().a("Неверный формат числа!"));
            } else {
               int newValue = Integer.parseInt(args[1]);
               if (newValue <= 1900000000) {
                  by tag = PlayerMoneyUtils.getInfo(player).getPersistedTag();
                  int moneyValue = tag.e("moneyValue");
                  int newMoneyValue = moneyValue + newValue;
                  tag.a("moneyValue", Integer.valueOf(newMoneyValue));
                  ServerPacketMoneySender.sendMoney(player, tag.e("moneyValue"));
                  cs.a(new cv().a("Игроку было выдано: " + newValue + " руб."));
               } else {
                  cs.a(new cv().a("Max value 1600000000"));
               }
            }
         }
      }
   }
}
