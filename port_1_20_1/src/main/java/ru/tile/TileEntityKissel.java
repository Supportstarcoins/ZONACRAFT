package ru.stalcraft.tile;

import ru.stalcraft.client.particles.KisselParticleEmitter;

public class TileEntityKissel extends TileEntityAnomaly {
   @Override
   protected Class getEmitterClass() {
      return KisselParticleEmitter.class;
   }
}
