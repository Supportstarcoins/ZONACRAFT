package ru.stalcraft.tile;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.particles.CampfireParticleEmitter;
import ru.stalcraft.proxy.IServerProxy;

public class TileEntityCampfire extends TileEntityAnomaly {
   @Override
   public void h() {
      super.h();

      for (nn entity : this.az().a(nn.class, asx.a(this.l - 0.5, this.m - 0.5, this.n - 0.5, this.l + 0.5, this.m + 0.5, this.n + 0.5))) {
         if (this.k != null && !this.k.I) {
            IServerProxy proxy = (IServerProxy)StalkerMain.getProxy();
            if (proxy != null && entity != null && entity instanceof uf && !entity.M) {
               proxy.getPlayerProperties((uf)entity, 0, 20, 10);
            }
         }
      }
   }

   @Override
   public boolean canUpdate() {
      return true;
   }

   @Override
   protected Class getEmitterClass() {
      return CampfireParticleEmitter.class;
   }
}
