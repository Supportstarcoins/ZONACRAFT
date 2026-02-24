package ru.stalcraft.server;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import ru.stalcraft.player.IAntiRelog;
import ru.stalcraft.server.network.ServerPacketSender;

public class AntiRelog implements IAntiRelog {
   private List reloggingPlayers = new ArrayList();
   private static final int MAX_TIMER = 1200;

   private void fullExit(jv player) {
      if (MinecraftServer.F().V()) {
         try {
            ReflectionHelper.findMethod(hn.class, MinecraftServer.F().af(), new String[]{"writePlayerData", "func_72391_b", "b"}, new Class[]{jv.class})
               .invoke(MinecraftServer.F().af(), player);
         } catch (Exception var3) {
         }

         player.p().e(player);
      }
   }

   public List getPlayers() {
      return this.reloggingPlayers;
   }

   @Override
   public void onDamage(jv player) {
      if (MinecraftServer.F().V()) {
         Iterator i$ = this.reloggingPlayers.iterator();

         AntiRelog.ReloggingPlayer rp;
         do {
            if (!i$.hasNext()) {
               if (player.aN() <= 0.0F) {
                  this.fullExit(player);
               }

               return;
            }

            rp = (AntiRelog.ReloggingPlayer)i$.next();
         } while (player != rp.player);

         rp.timer = 1200;
         rp.player.af = 0;
      }
   }

   @Override
   public void addReloggingPlayer(jv player) {
      if (MinecraftServer.F().V()) {
         this.reloggingPlayers.add(new AntiRelog.ReloggingPlayer(player));

         try {
            ReflectionHelper.findMethod(hn.class, MinecraftServer.F().af(), new String[]{"writePlayerData", "func_72391_b", "b"}, new Class[]{jv.class})
               .invoke(MinecraftServer.F().af(), player);
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         ServerPacketSender.sendHasQuitted(player);
      }
   }

   public jv getAndRemoveReloggingPlayer(String username) {
      Iterator it = this.reloggingPlayers.iterator();

      while (it.hasNext()) {
         AntiRelog.ReloggingPlayer rp = (AntiRelog.ReloggingPlayer)it.next();
         if (rp.player.bu.equals(username)) {
            it.remove();
            return rp.player;
         }
      }

      return null;
   }

   @Override
   public void tick() {
      if (MinecraftServer.F().V()) {
         Iterator it = this.reloggingPlayers.iterator();
         hn scm = MinecraftServer.F().af();

         while (it.hasNext()) {
            AntiRelog.ReloggingPlayer rp = (AntiRelog.ReloggingPlayer)it.next();
            if (--rp.timer <= 0 || scm.f(rp.player.bu) != null) {
               this.fullExit(rp.player);
               it.remove();
            }
         }
      }
   }

   @Override
   public boolean isPlayerRelogging(jv player) {
      if (MinecraftServer.F().V()) {
         Iterator i$ = this.reloggingPlayers.iterator();

         AntiRelog.ReloggingPlayer rp;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            rp = (AntiRelog.ReloggingPlayer)i$.next();
         } while (rp.player != player);
      }

      return false;
   }

   @Override
   public void onSaveAll() {
      if (MinecraftServer.F().V()) {
         for (AntiRelog.ReloggingPlayer rp : this.reloggingPlayers) {
            try {
               ReflectionHelper.findMethod(hn.class, MinecraftServer.F().af(), new String[]{"writePlayerData", "func_72391_b", "b"}, new Class[]{jv.class})
                  .invoke(MinecraftServer.F().af(), rp.player);
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      }
   }

   public class ReloggingPlayer {
      public int timer = 1200;
      public final jv player;

      public ReloggingPlayer(jv player) {
         this.player = player;
      }
   }
}
