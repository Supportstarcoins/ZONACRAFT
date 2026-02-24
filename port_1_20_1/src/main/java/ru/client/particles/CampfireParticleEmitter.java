package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.tile.TileEntityCampfire;

public class CampfireParticleEmitter extends ParticleEmitter {
   public static ParticleIcon[] fire;
   public static ParticleIcon smoke;
   public int vector;
   public int vectorTicks;

   public CampfireParticleEmitter(TileEntityCampfire emmiter) {
      super(emmiter);
      super.setCenter(super.centerX + 0.5, super.centerY + 4.5, super.centerZ + 0.5);
      super.setSize(-3.0, -5.0, -3.0, 3.0, 4.0, 3.0);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.world.s.nextInt(2) == 0) {
         for (int i = 0; i < 3; i++) {
            super.addParticle(new CampfireParticle(this, fire[i]));
         }
      }

      for (int i = 0; i < 2; i++) {
         super.addParticle(new CampfireParticle(this, fire[i]));
      }

      super.addParticle(new CampfireParticleSmoke(this, smoke, this.vector));
   }

   public static void registerIcons(mt ir) {
      fire = new ParticleIcon[3];

      for (int i = 0; i < 3; i++) {
         fire[i] = (ParticleIcon)ir.a("stalker:campfire/fire" + i);
      }

      smoke = (ParticleIcon)ir.a("stalker:smoke/smoke1");
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.mc.f.g.contains(super.emmiter);
   }
}
