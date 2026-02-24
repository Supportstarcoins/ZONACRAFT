package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.tile.TileEntityCarousel;

public class CarouselParticle extends Particle {
   private CarouselParticleEmitter parent;
   public float yawTarget;
   public float pitchTarget;
   public float rotationCircleSpeed;
   public float rotationRiseSpeed;
   public float distance;
   public float radiusMoveSpeed;
   public float motionYFactor;
   private boolean isLeaf;

   public CarouselParticle(CarouselParticleEmitter parent, ParticleIcon icon, boolean isLeaf, float maxTargetDistance) {
      super(parent, isLeaf ? 0.15F : 0.6F, 1.0F, icon);
      this.parent = parent;
      Random rand = parent.world.s;
      this.rotation = rand.nextFloat() * 360.0F;
      this.yawTarget = rand.nextFloat() * 360.0F;
      this.pitchTarget = rand.nextFloat() * 360.0F;
      this.distance = rand.nextFloat() * 3.0F;
      float targetDistance = rand.nextFloat() * maxTargetDistance;
      this.radiusMoveSpeed = (targetDistance - this.distance) / 120.0F;
      double x = -ls.a(this.yawTarget / 180.0F * (float) Math.PI) * ls.b(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance + parent.centerX + 0.5;
      double z = ls.b(this.yawTarget / 180.0F * (float) Math.PI) * ls.b(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance + parent.centerZ + 0.5;
      this.setPosition(x, parent.centerY - 4.0, z);
      this.rotationCircleSpeed = 1.5F + rand.nextFloat() / 2.0F;
      this.rotationRiseSpeed = rand.nextFloat() / 10.0F;
      this.rotationSpeed = (parent.world.s.nextFloat() - 0.5F) * 15.0F;
      this.motionYFactor = 0.04F + rand.nextFloat() / 50.0F;
      this.speedFactor = 0.9F;
      this.isLeaf = isLeaf;
      if (isLeaf) {
         this.prevTextureSize = this.textureSize = 0.15F;
      }
   }

   @Override
   public void tick() {
      super.tick();
      this.motionY = (float)(this.motionY - 0.02);
      TileEntityCarousel tile = (TileEntityCarousel)this.parent.emmiter;
      if (tile.activeTimer >= 0) {
         this.doTickBeforeActivation();
      }

      if (!this.isLeaf && tile.activeTimer == -1) {
         this.alpha = Math.max((80 - tile.ticksSinceActive) / 80.0F, 0.0F);
         this.textureSize *= 0.97F;
      }

      if (this.onGround) {
         this.rotationSpeed = (float)(this.rotationSpeed * 0.75);
      }
   }

   private void doTickBeforeActivation() {
      this.distance = this.distance + this.radiusMoveSpeed;
      this.pitchTarget = (this.pitchTarget + this.rotationRiseSpeed) % 360.0F;
      float endY = -ls.a(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance + (float)this.parent.centerY;
      this.motionY = (float)(this.motionYFactor > Math.abs(endY - this.posY) ? endY - this.posY : this.motionYFactor);
      this.motionYFactor *= 0.992F;
      if (this.ticksExisted > 30 && this.ticksExisted < 150) {
         this.rotationCircleSpeed *= 1.03F;
      }

      this.yawTarget = (this.yawTarget + this.rotationCircleSpeed) % 360.0F;
      float endX = -ls.a(this.yawTarget / 180.0F * (float) Math.PI) * ls.b(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance
         + (float)this.parent.emmiter.getPosX()
         + 0.5F;
      float endZ = ls.b(this.yawTarget / 180.0F * (float) Math.PI) * ls.b(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance
         + (float)this.parent.emmiter.getPosZ()
         + 0.5F;
      this.motionX = endX - (float)this.posX;
      this.motionZ = endZ - (float)this.posZ;
   }
}
