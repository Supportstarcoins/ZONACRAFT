package ru.stalcraft.tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.player.PlayerServerInfo;

public class TileEntityEjectionSave extends asp {
   public List<uf> playersSave = new ArrayList<>();

   public void h() {
      if (!super.k.I) {
         asx box = asx.a().a(this.l - 5.0, this.m, this.n - 5.0, this.l + 5.0, this.m + 5.0, this.n + 5.0);
         List<jv> players = this.az().a(jv.class, box);

         for (jv player : players) {
            PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(player);
            if (!this.playersSave.contains(player)) {
               playerInfo.isEjectionSave = true;
               this.playersSave.add(player);
            }
         }

         if (this.playersSave != null) {
            Iterator it = this.playersSave.iterator();

            while (it.hasNext()) {
               uf playerx = (uf)it.next();
               if (!players.contains(playerx)) {
                  PlayerServerInfo playerInfo = (PlayerServerInfo)PlayerUtils.getInfo(playerx);
                  playerInfo.isEjectionSave = false;
                  it.remove();
               }
            }
         }
      }
   }
}
