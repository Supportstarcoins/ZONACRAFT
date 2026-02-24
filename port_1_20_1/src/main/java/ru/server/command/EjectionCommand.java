package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.server.CommonProxy;
import ru.stalcraft.server.ejection.ServerEjection;

public class EjectionCommand extends z {
   public String c() {
      return "ejection";
   }

   public void b(ad icommandsender, String[] astring) {
      jv par3 = MinecraftServer.F().af().f(icommandsender.c_());
      if (par3 == null || MinecraftServer.F().af().e(par3.bu)) {
         if (astring.length != 1) {
            icommandsender.a(new cv().a("Формат ввода команды: /ejection <start/stop/enable/disable>"));
         } else if (astring[0].equals("start")) {
            new ServerEjection().start();
            icommandsender.a(new cv().a("Выброс запущен."));
         } else if (astring[0].equals("stop")) {
            ServerEjection ejection = (ServerEjection)CommonProxy.serverEjectionManager.getEjection();
            if (ejection == null) {
               icommandsender.a(new cv().a("В данный момент выброса нет."));
            } else {
               ejection.end();
               icommandsender.a(new cv().a("Выброс остановлен."));
            }
         } else if (astring[0].equals("enable")) {
            CommonProxy.serverEjectionManager.setRandomStart(true);
            icommandsender.a(new cv().a("Случайный запуск выброса включен."));
         } else if (astring[0].equals("disable")) {
            CommonProxy.serverEjectionManager.setRandomStart(false);
            icommandsender.a(new cv().a("Случайный запуск выброса отключен."));
         } else {
            icommandsender.a(new cv().a("Формат ввода команды: /ejection <start/stop/enable/disable>"));
         }
      }
   }

   public String c(ad icommandsender) {
      return "";
   }
}
