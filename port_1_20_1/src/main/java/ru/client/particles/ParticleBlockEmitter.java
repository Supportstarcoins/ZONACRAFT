package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class ParticleBlockEmitter extends ParticleEmitter {
   public static ParticleIcon smoke;

   public ParticleBlockEmitter(BlockParticleEmitter emmiter) {
      super(emmiter);
      super.setSize(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0);
   }

   public void addHitBullet(double posX, double posY, double posZ, int side) {
      Random rand = super.world.s;

      for (int i = 0; i < 4; i++) {
         super.addParticle(new BulletHitParticle(this, smoke, posX, posY + super.world.s.nextFloat() * 0.15F, posZ, side));
      }
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.particles.size() > 0;
   }

   public static void registerIcons(mt ir) {
      smoke = (ParticleIcon)ir.a("stalker:dust_poof");
   }
}
