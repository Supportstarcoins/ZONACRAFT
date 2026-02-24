package ru.stalcraft.client.particles;

import java.util.Random;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.tile.TileEntityFunnel;

public class FunnelParticle extends Particle {
   private FunnelParticleEmitter parent;
   public float yawTarget;
   public float pitchTarget;
   public float rotationCircleSpeed;
   public float rotationRiseSpeed;
   public float distance;
   public float radiusMoveSpeed;
   public float motionYFactor;
   private boolean isLeaf;

   public FunnelParticle(FunnelParticleEmitter parent, ParticleIcon icon, boolean isLeaf, float maxTargetDistance) {
      super(parent, isLeaf ? 0.15F : 0.6F, 1.0F, icon);
      this.parent = parent;
      Random rand = parent.world.s;
      this.rotation = rand.nextFloat() * 360.0F;
      this.yawTarget = rand.nextFloat() * 360.0F;
      this.pitchTarget = rand.nextFloat() * 360.0F;
      this.distance = rand.nextFloat() * 3.0F;
      float targetDistance = rand.nextFloat() * maxTargetDistance;
      this.radiusMoveSpeed = (targetDistance - this.distance) / 120.0F;
      double x = -ls.a(this.yawTarget / 180.0F * (float) Math.PI) * ls.b(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance + parent.centerX;
      double z = ls.b(this.yawTarget / 180.0F * (float) Math.PI) * ls.b(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance + parent.centerZ;
      this.setPosition(x, parent.centerY - 4.0, z);
      this.rotationCircleSpeed = 1.5F + rand.nextFloat() / 2.0F;
      this.rotationRiseSpeed = rand.nextFloat() / 10.0F;
      this.rotationSpeed = (parent.world.s.nextFloat() - 0.5F) * 15.0F;
      this.motionYFactor = 0.07F + rand.nextFloat() / 30.0F;
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
      if (((TileEntityFunnel)this.parent.emmiter).activeTimer < 120) {
         this.doTickBeforeActivation();
      } else if (((TileEntityFunnel)this.parent.emmiter).activeTimer == 120) {
         this.onActivate();
      }

      if (!this.isLeaf && ((TileEntityFunnel)this.parent.emmiter).activeTimer > 120) {
         this.alpha = Math.max((200 - ((TileEntityFunnel)this.parent.emmiter).activeTimer) / 80.0F, 0.0F);
      }

      if (this.onGround) {
         this.rotationSpeed = (float)(this.rotationSpeed * 0.75);
      }
   }

   private void doTickBeforeActivation() {
      this.distance = this.distance + this.radiusMoveSpeed;
      this.pitchTarget = (this.pitchTarget + this.rotationRiseSpeed) % 360.0F;
      float endY = -ls.a(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance + (float)this.parent.centerY;
      super.motionY = (float)(this.motionYFactor > Math.abs(endY - super.posY) ? endY - super.posY : this.motionYFactor);
      this.motionYFactor *= 0.992F;
      if (this.ticksExisted > 30) {
         this.rotationCircleSpeed *= 1.03F;
      }

      this.yawTarget = (this.yawTarget + this.rotationCircleSpeed) % 360.0F;
      float endX = -ls.a(this.yawTarget / 180.0F * (float) Math.PI) * ls.b(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance
         + (float)this.parent.emmiter.getPosX()
         + 0.5F;
      float endZ = ls.b(this.yawTarget / 180.0F * (float) Math.PI) * ls.b(this.pitchTarget / 180.0F * (float) Math.PI) * this.distance
         + (float)this.parent.emmiter.getPosZ()
         + 0.5F;
      super.motionX = endX - (float)super.posX;
      super.motionZ = endZ - (float)super.posZ;
   }

   private void onActivate() {
      atc motionVec = atc.a(
            super.posX - this.parent.emmiter.getPosX() - 0.5,
            super.posY - 4.0 - this.parent.emmiter.getPosY() - 0.5,
            super.posZ - this.parent.emmiter.getPosZ() - 0.5
         )
         .a();
      super.motionX = (float)motionVec.c * 0.3F;
      super.motionY = (float)motionVec.d * 0.3F;
      super.motionZ = (float)motionVec.e * 0.3F;
   }
}
