package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class CarouselTornadeLeafParticle extends Particle {
   public float yawTarget;
   public float rotationCircleSpeed;
   public float distance;
   public float radiusMoveSpeed;
   public float motionYFactor;

   public CarouselTornadeLeafParticle(ParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.5F, 0.7F, icon);
      Random rand = parent.world.s;
      this.yawTarget = rand.nextFloat() * 360.0F;
      this.distance = rand.nextFloat() * 0.7F + 0.5F;
      float targetDistance = rand.nextFloat() * 1.5F;
      this.radiusMoveSpeed = (targetDistance - this.distance) / 40.0F;
      double x = parent.world.s.nextBoolean() ? parent.world.s.nextFloat() * 0.075F : -parent.world.s.nextFloat() * 0.075F;
      double z = parent.world.s.nextBoolean() ? parent.world.s.nextFloat() * 0.075F : -parent.world.s.nextFloat() * 0.075F;
      this.setPosition(parent.centerX + x, parent.centerY - 4.100000190734863 + parent.world.s.nextFloat() * 1.35F, parent.centerZ + z);
      this.rotationCircleSpeed = 5.0F;
      this.rotationSpeed = (parent.world.s.nextFloat() - 0.5F) * 15.0F;
      this.alpha = 1.0F;
      super.blendFunc = 0;
      super.textureSize = 0.1F;
   }

   @Override
   public void tick() {
      super.tick();
      this.yawTarget = this.yawTarget + this.rotationCircleSpeed;
      float endX = -ls.a(this.yawTarget / 180.0F * (float) Math.PI) * this.distance + (float)this.parent.centerX;
      float endZ = ls.b(this.yawTarget / 180.0F * (float) Math.PI) * this.distance + (float)this.parent.centerZ;
      this.motionX = endX - (float)this.posX;
      this.motionZ = endZ - (float)this.posZ;
   }
}
