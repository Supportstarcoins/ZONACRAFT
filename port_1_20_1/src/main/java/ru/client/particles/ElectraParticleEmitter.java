package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.tile.TileEntityElectra;

public class ElectraParticleEmitter extends ParticleEmitter {
   public TileEntityElectra emmiter;
   public static ParticleIcon electra;
   public static ParticleIcon glow;
   public int glowTick;

   public ElectraParticleEmitter(TileEntityElectra emmiter) {
      super(emmiter);
      this.emmiter = emmiter;
      super.setCenter(super.centerX + 0.5, super.centerY + 4.5, super.centerZ + 0.5);
      super.setSize(-3.0, -5.0, -3.0, 3.0, 4.0, 3.0);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.emmiter.ticksSleep <= 0) {
         this.glowTick++;
         if (this.glowTick == 4) {
            float x = super.world.s.nextBoolean() ? super.world.s.nextFloat() * 1.0F : -super.world.s.nextFloat() * 1.0F;
            float z = super.world.s.nextBoolean() ? super.world.s.nextFloat() * 1.0F : -super.world.s.nextFloat() * 1.0F;
            super.addParticle(new ElectraParticleGlow(this, electra, x, z));
            super.addParticle(new ElectraParticleGlow(this, glow, x, z));
            this.glowTick = 0;
         }
      }
   }

   public void setSplash() {
      for (int i = 0; i < 8; i++) {
         super.addParticle(new ElectraParticleSplash(this, electra));
      }
   }

   public static void registerIcons(mt ir) {
      glow = (ParticleIcon)ir.a("stalker:electra-1");
      electra = (ParticleIcon)ir.a("stalker:electra-2");
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.mc.f.g.contains(super.emmiter);
   }
}
