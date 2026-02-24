package ru.stalcraft.vector;

public class RayTracingVectorTracer extends RayTracingVector {
   public RayTracingVectorBullet bullet;

   public RayTracingVectorTracer(RayTracingVectorBullet bullet) {
      super(bullet.shooter.q);
      this.bullet = bullet;
      this.isRenderer = true;
      this.setPosition(bullet.shooter.u, bullet.shooter.v + bullet.shooter.f(), bullet.shooter.w);
      this.setRotationAngle(this.rotationYaw, this.rotationPitch);
      float split = 6700.0F;
      this.motionX = -ls.a(this.rotationYaw / 180.0F * (float) Math.PI) * ls.b(this.rotationPitch / 180.0F * (float) Math.PI) * split;
      this.motionZ = ls.b(this.rotationYaw / 180.0F * (float) Math.PI) * ls.b(this.rotationPitch / 180.0F * (float) Math.PI) * split;
      this.motionY = -ls.a(this.rotationPitch / 180.0F * (float) Math.PI) * split;
   }

   @Override
   public void createHitVector() {
      atc vec3 = atc.a(this.posX, this.posY, this.posZ);
      atc vec31 = atc.a(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      this.minX = vec3.c;
      this.minY = vec3.d;
      this.minZ = vec3.e;
      this.maxX = vec31.c;
      this.maxY = vec31.d;
      this.maxZ = vec31.e;
      if (this.bullet.isDead) {
      }
   }
}
