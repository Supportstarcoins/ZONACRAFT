package ru.stalcraft.vector;

public class RayTracingVector {
   public abw worldObj;
   public double posX;
   public double posY;
   public double posZ;
   public double motionX;
   public double motionY;
   public double motionZ;
   public double minX;
   public double minY;
   public double minZ;
   public double maxX;
   public double maxY;
   public double maxZ;
   public float rotationYaw;
   public float rotationPitch;
   public boolean isDead;
   public boolean isRenderer = false;

   public RayTracingVector(abw worldObj) {
      this.worldObj = worldObj;
   }

   public void setPosition(double posX, double posY, double posZ) {
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
   }

   public void setRotationAngle(float rotationYaw, float rotationPitch) {
      this.rotationYaw = rotationYaw;
      this.rotationPitch = rotationPitch;
   }

   public void setDead() {
      this.isDead = true;
   }

   public void createHitVector() {
   }
}
