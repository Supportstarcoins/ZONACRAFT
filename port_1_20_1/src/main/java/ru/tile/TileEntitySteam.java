package ru.stalcraft.tile;

import ru.stalcraft.client.particles.SteamParticleEmitter;

public class TileEntitySteam extends TileEntityAnomaly {
   @Override
   protected Class getEmitterClass() {
      return SteamParticleEmitter.class;
   }
}
