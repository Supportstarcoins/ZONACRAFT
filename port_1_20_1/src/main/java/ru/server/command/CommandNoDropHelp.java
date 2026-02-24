package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;

public class CommandNoDropHelp extends z {
   public String c() {
      return "helpgivenodrop";
   }

   public String c(ad par1) {
      return "";
   }

   public void b(ad par1, String[] par2) {
      jv par3 = MinecraftServer.F().af().f(par1.c_());
      if (par3.bG.d) {
         par1.a(
            new cv()
               .a(
                  "Команды ItemHelper: /givenodrop <id> <кол-во> <ник>(если нужно заспавнить игроку придмет), personal(если нужнен персональный придмет), /givenodrop <id> <кол-во> (Заспавнить себе придмет), /givenodrop personal (Заспавнить себе придмет персональный)"
               )
         );
      }
   }
}
