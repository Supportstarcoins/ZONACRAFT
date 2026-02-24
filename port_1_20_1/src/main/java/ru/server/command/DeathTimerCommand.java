package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.network.ServerPacketSender;
import ru.stalcraft.server.player.PlayerServerInfo;

public class DeathTimerCommand extends z {
   public String c() {
      return "deathtimer";
   }

   public void b(ad par1, String[] par2) {
      jv par3 = MinecraftServer.F().af().f(par1.c_());
      if (par3 == null || MinecraftServer.F().af().e(par3.bu)) {
         try {
            uf par4 = MinecraftServer.F().af().f(par2[0]);
            if (par2.length == 2 && par4 != null) {
               PlayerServerInfo par5 = (PlayerServerInfo)PlayerUtils.getInfo(par4);
               par5.getPersistedTag().a("deathTimer", Boolean.valueOf(par2[1]));
               if (par5.getPersistedTag().n("deathTimer")) {
                  ServerPacketSender.sendForceCooldown(par4);
                  par1.a(new cv().a("У игрока " + par2[0] + " выключен таймер смерти"));
               } else {
                  par1.a(new cv().a("У игрока " + par2[0] + " включен таймер смерти"));
               }
            } else if (par4 == null) {
               par1.a(new cv().a("Игрока нет на сервере или его ник набран не правильно!"));
            } else {
               par1.a(new cv().a("Формат ввода команды: /deathtimer [Username] <false/true>"));
            }
         } catch (Exception var6) {
            par1.a(new cv().a("Формат ввода команды: /deathtimer [Username] <false/true>"));
         }
      }
   }

   public String c(ad icommandsender) {
      return "";
   }
}
