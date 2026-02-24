package ru.stalcraft.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.client.particles.TrampolineParticleEmitter;

public class TileEntityTrampoline extends TileEntityAnomaly {
   @Override
   protected Class getEmitterClass() {
      return TrampolineParticleEmitter.class;
   }

   @SideOnly(Side.CLIENT)
   public void onActivate() {
      ((TrampolineParticleEmitter)this.getParticleEmitter()).onActivate();
   }
}
