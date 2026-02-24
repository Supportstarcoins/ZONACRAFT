package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.server.clans.FlagManager;

public class LandrentCommand extends z {
   public String c() {
      return "landrent";
   }

   public void b(ad icommandsender, String[] astring) {
      jv par3 = MinecraftServer.F().af().f(icommandsender.c_());
      if (par3 == null || MinecraftServer.F().af().e(par3.bu)) {
         if (astring.length == 2 && astring[0].matches("[0-9]*") && astring[1].matches("[0-9]*")) {
            FlagManager m = FlagManager.instance();
            m.maxRentTimer = Integer.parseInt(astring[0]);
            m.rent = Integer.parseInt(astring[1]);
            icommandsender.a(
               new cv()
                  .a(
                     "Теперь на счета кланов будет начисляться по "
                        + m.rent
                        + " руб. каждые "
                        + m.maxRentTimer
                        + " сек. за каждый флаг на территориях, которые вы сейчас установите"
                  )
            );
         } else {
            icommandsender.a(new cv().a("Формат ввода команды: /landrent <промежуток между выплатами в секундах> <сумма за раз>"));
         }
      }
   }

   public String c(ad icommandsender) {
      return "";
   }
}
