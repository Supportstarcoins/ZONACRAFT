package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.server.clans.Clan;
import ru.stalcraft.server.clans.ClanManager;

public class MoneyCommand extends z {
   public String c() {
      return "clanmoney";
   }

   public void b(ad icommandsender, String[] astring) {
      jv par3 = MinecraftServer.F().af().f(icommandsender.c_());
      if (par3 == null || MinecraftServer.F().af().e(par3.bu)) {
         if (astring.length == 2 && astring[1].matches("[0-9]*")) {
            Clan clan = ClanManager.instance().getClan(astring[0]);
            if (clan == null) {
               icommandsender.a(new cv().a("Такого клана не существует!"));
            } else {
               icommandsender.a(new cv().a("Сумма на счету клана " + clan.name + " изменена с " + clan.money + " на " + astring[1] + " руб."));
               clan.money = Integer.parseInt(astring[1]);
            }
         } else {
            icommandsender.a(new cv().a("Формат ввода команды: /clanmoney <клан> <сумма>"));
         }
      }
   }

   public String c(ad icommandsender) {
      return "";
   }
}
