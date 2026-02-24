package ru.stalcraft.tile;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.proxy.IServerProxy;
import ru.stalcraft.server.clans.Clan;
import ru.stalcraft.server.clans.Flag;
import ru.stalcraft.server.clans.FlagManager;

public class TileEntityFlag extends asp {
   private boolean firstrun = true;
   private int ticksCaptrue;

   public void h() {
      if (!super.k.I) {
         if (this.ticksCaptrue >= 20) {
            IServerProxy proxy = (IServerProxy)StalkerMain.getProxy();
            proxy.onFlagCapture(this);
            this.ticksCaptrue = 0;
         }

         this.ticksCaptrue++;
      }

      for (nn entity : this.az().a(nn.class, asx.a(this.l - 15.5, this.m - 15.5, this.n - 15.5, this.l + 15.5, this.m + 15.5, this.n + 15.5))) {
         if (entity instanceof uf && !this.k.I) {
            IServerProxy proxy = (IServerProxy)StalkerMain.getProxy();
            Flag flag = FlagManager.instance().getFlagByPos(super.k.t.i, super.l, super.m, super.n);
            if (flag != null) {
               Clan thePlayerClan = (Clan)PlayerUtils.getInfo((uf)entity).getClan();
               Clan owner = flag.owner;
               if (thePlayerClan != null && owner != null && owner == thePlayerClan) {
                  proxy.getRespawn((uf)entity, this);
               }
            }
         }
      }
   }

   public void a(by tagCompound) {
      super.a(tagCompound);
   }

   public void b(by tagCompound) {
      super.b(tagCompound);
   }

   public boolean canUpdate() {
      return true;
   }
}
