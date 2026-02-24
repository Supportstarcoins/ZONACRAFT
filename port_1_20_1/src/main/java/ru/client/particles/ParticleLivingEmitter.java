package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class ParticleLivingEmitter extends ParticleEmitter {
   public static ParticleIcon blood;
   public EntityParticleEmitter entityEmitter;

   public ParticleLivingEmitter(EntityParticleEmitter entityEmitter) {
      super(entityEmitter);
      this.entityEmitter = entityEmitter;
      super.setCenter(super.emmiter.getPosX(), super.emmiter.getPosY(), super.emmiter.getPosZ());
      super.setSize(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0);
   }

   @Override
   public void tick() {
      super.setCenter(super.emmiter.getPosX(), super.emmiter.getPosY(), super.emmiter.getPosZ());
      super.setSize(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0);
      super.tick();
   }

   public void addSplash(double posX, double posY, double posZ) {
      int l = super.world.s.nextInt(8);

      for (int i = 0; i < 2 + l; i++) {
         super.addParticle(
            new PlayerParticleBlood(
               this,
               blood,
               posX + (-0.1F + super.world.s.nextDouble() * 0.2),
               posY + (-0.125 + super.world.s.nextDouble() * 0.25),
               posZ + (-0.1F + super.world.s.nextDouble() * 0.2)
            )
         );
      }
   }

   public static void registerIcons(mt ir) {
      blood = (ParticleIcon)ir.a("stalker:blood");
   }

   @Override
   public void onRemove() {
      EffectsEngine.instance.emittersLiving.remove(this.entityEmitter.entity.k);
   }

   @Override
   public boolean isValid() {
      return this.entityEmitter.entity.M ? super.particles.size() > 0 : true;
   }
}
