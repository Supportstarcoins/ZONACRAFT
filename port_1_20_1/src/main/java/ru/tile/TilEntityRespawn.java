package ru.stalcraft.tile;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.proxy.IServerProxy;

public class TilEntityRespawn extends asp {
   public void h() {
      super.h();

      for (nn entity : this.az().a(nn.class, asx.a(this.l - 15.5, this.m - 15.5, this.n - 15.5, this.l + 15.5, this.m + 15.5, this.n + 15.5))) {
         if (entity != null && entity instanceof uf && !this.k.I) {
            IServerProxy proxy = (IServerProxy)StalkerMain.getProxy();
            proxy.getRespawn((uf)entity, this);
         }
      }
   }
}
