package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.player.PlayerUtils;

public class CommandNoDrop extends z {
   public String c() {
      return "givenodrop";
   }

   public String c(ad par1) {
      return "";
   }

   public void b(ad par1, String[] par2) {
      jv par3 = MinecraftServer.F().af().f(par1.c_());
      if (par3 == null || MinecraftServer.F().af().e(par3.bu)) {
         int par4 = 0;
         int par5 = 1;
         boolean par6 = false;
         if (par2.length == 0) {
            if (par3.bn.h() != null) {
               PlayerUtils.getTag(par3.bn.h()).a("no_drop", 1);
               par1.a(new cv().a("Предмет не выпадайющий!"));
            } else {
               par1.a(new cv().a("Неверный ввод команды!"));
            }
         } else if (par2.length == 1) {
            if (par3.bn.h() != null && par2[0].equals("personal")) {
               PlayerUtils.getTag(par3.bn.h()).a("no_drop", 1);
               PlayerUtils.getTag(par3.bn.h()).a("personal", 1);
               par1.a(new cv().a("Предмет не выпадайющий и персональный!"));
            } else {
               par1.a(new cv().a("Неверный ввод команды!"));
            }
         } else if (par2.length == 2) {
            if (par3.bn.h() != null && par2[0].equals("personal") && par2[1].equals("clan")) {
               PlayerUtils.getTag(par3.bn.h()).a("no_drop", 1);
               PlayerUtils.getTag(par3.bn.h()).a("personal", 1);
               PlayerUtils.getTag(par3.bn.h()).a("setClan", 0);
               PlayerUtils.getTag(par3.bn.h()).a("clan", "");
               par1.a(new cv().a("Предмет не выпадайющий и персональный!"));
            } else {
               par1.a(new cv().a("Неверный ввод команды!"));
            }
         } else {
            if (par2.length == 3) {
               if (par3.bn.h() != null && par2[0].equals("personal") && par2[1].equals("clan") && !par2[2].equals("0")) {
                  PlayerUtils.getTag(par3.bn.h()).a("no_drop", 1);
                  PlayerUtils.getTag(par3.bn.h()).a("personal", 1);
                  PlayerUtils.getTag(par3.bn.h()).a("setClan", 1);
                  PlayerUtils.getTag(par3.bn.h()).a("clan", par2[2]);
                  par1.a(new cv().a("Предмет не выпадайющий и персональный!"));
                  return;
               }

               PlayerUtils.getTag(par3.bn.h()).a("no_drop", 1);
               PlayerUtils.getTag(par3.bn.h()).a("personal", 1);
               PlayerUtils.getTag(par3.bn.h()).a("setClan", 1);
               PlayerUtils.getTag(par3.bn.h()).a("clan", par2[2]);
            }
         }
      }
   }
}
