package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class CarouselTornadoParticle extends Particle {
   public float yawTarget;
   public float rotationCircleSpeed;
   public float distance;
   public float radiusMoveSpeed;
   public float motionYFactor;

   public CarouselTornadoParticle(ParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.5F, 0.7F, icon);
      Random rand = parent.world.s;
      this.yawTarget = rand.nextFloat() * 360.0F;
      this.distance = rand.nextFloat() * 0.7F + 0.5F;
      float targetDistance = rand.nextFloat() * 1.5F;
      this.radiusMoveSpeed = (targetDistance - this.distance) / 40.0F;
      double x = -ls.a(this.yawTarget / 180.0F * (float) Math.PI) * this.distance + parent.centerX;
      double z = ls.b(this.yawTarget / 180.0F * (float) Math.PI) * this.distance + parent.centerZ;
      this.setPosition(x, parent.centerY - 4.3F, z);
      this.rotationCircleSpeed = 5.0F + rand.nextFloat() * 5.0F;
      this.rotationSpeed = (parent.world.s.nextFloat() - 0.5F) * 15.0F;
      this.motionY = 0.045F + rand.nextFloat() / 100.0F;
      this.alpha = 0.15F;
      super.blendFunc = 0;
   }

   @Override
   public void tick() {
      super.tick();
      this.textureSize *= 1.03F;
      this.alpha -= 0.005F;
      this.distance = this.distance + this.radiusMoveSpeed;
      if (this.ticksExisted > 30 && this.ticksExisted < 150) {
         this.rotationCircleSpeed *= 1.03F;
      }

      this.yawTarget = (this.yawTarget + this.rotationCircleSpeed) % 360.0F;
      float endX = -ls.a(this.yawTarget / 180.0F * (float) Math.PI) * this.distance + (float)this.parent.centerX;
      float endZ = ls.b(this.yawTarget / 180.0F * (float) Math.PI) * this.distance + (float)this.parent.centerZ;
      this.motionX = endX - (float)this.posX;
      this.motionZ = endZ - (float)this.posZ;
      if (this.ticksExisted > 35) {
         this.isDead = true;
      }
   }
}
