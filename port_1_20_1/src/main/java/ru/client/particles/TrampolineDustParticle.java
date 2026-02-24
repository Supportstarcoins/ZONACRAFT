package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class TrampolineDustParticle extends Particle {
   private static final float SIZE_FACTOR = 1.01F;
   private float lifetime;

   public TrampolineDustParticle(TrampolineParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.3F, 0.5F, icon);
      Random rand = parent.world.s;
      this.rotation = rand.nextFloat() * 360.0F;
      float yawTarget = rand.nextFloat() * 360.0F;
      float distance = rand.nextFloat() * 0.5F;
      float speed = rand.nextFloat() * 0.1F + 0.1F;
      atc motionVec = atc.a(rand.nextFloat(), rand.nextFloat() * 4.0F, rand.nextFloat()).a();
      double x = -ls.a(yawTarget / 180.0F * (float) Math.PI) * distance + parent.centerX;
      double z = ls.b(yawTarget / 180.0F * (float) Math.PI) * distance + parent.centerZ;
      this.setPosition(x, parent.centerY, z);
      this.rotationSpeed = (parent.world.s.nextFloat() - 0.5F) * 15.0F;
      this.motionX = (float)motionVec.c * speed * (rand.nextBoolean() ? 2 : -2);
      this.motionY = (float)motionVec.d * speed;
      this.motionZ = (float)motionVec.e * speed * (rand.nextBoolean() ? 2 : -2);
      this.lifetime = 50 + rand.nextInt(50);
      this.speedFactor = 0.93F;
   }

   @Override
   public void tick() {
      super.tick();
      this.motionY = (float)(this.motionY - 0.005);
      this.textureSize *= 1.01F;
      this.alpha -= 0.05F;
      if (this.onGround) {
         this.rotationSpeed = (float)(this.rotationSpeed * 0.75);
      }

      if (this.ticksExisted >= this.lifetime) {
         this.isDead = true;
      }
   }
}
