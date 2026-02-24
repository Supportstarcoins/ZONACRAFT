package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class FunnelParticleEye extends Particle {
   public FunnelParticleEye(ParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.1F, 0.1F, icon);
   }

   @Override
   public void tick() {
      super.tick();
   }
}
