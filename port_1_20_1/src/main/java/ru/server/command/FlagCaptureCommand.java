package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.server.clans.Flag;
import ru.stalcraft.server.clans.FlagManager;

public class FlagCaptureCommand extends z {
   public String c() {
      return "flagcapture";
   }

   public String c(ad icommandsender) {
      return "";
   }

   public void b(ad icommandsender, String[] args) {
      MinecraftServer mc = MinecraftServer.F();
      abw world = mc.f_();
      if (mc.af().e(icommandsender.c_())) {
         if (args.length == 4) {
            boolean correct = true;
            String capture = args[3];
            if (!capture.equals("true") && !capture.equals("false")) {
               correct = false;
            }

            if (correct) {
               int x = Integer.parseInt(args[0]);
               int y = Integer.parseInt(args[1]);
               int z = Integer.parseInt(args[2]);
               Flag flag = FlagManager.instance().getFlagByPos(world.t.i, x, y, z);
               if (flag != null) {
                  flag.isCapture = Boolean.parseBoolean(capture);
                  icommandsender.a(new cv().a("Захват запущен!"));
               } else {
                  icommandsender.a(new cv().a("Флага по этим координатам не существует!"));
               }
            } else {
               icommandsender.a(new cv().a("Формат ввода команды: /flagcapture [x] [y] [z] <true/false>"));
            }
         } else {
            icommandsender.a(new cv().a("Формат ввода команды: /flagcapture [x] [y] [z] <true/false>"));
         }
      } else {
         icommandsender.a(new cv().a("Нет прав!"));
      }
   }
}
