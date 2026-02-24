package ru.stalcraft.client.effects.particles;

import java.util.List;

public abstract class Particle implements Comparable<Particle> {
   public final asx boundingBox;
   public ParticleEmitter parent;
   public double posX;
   public double posY;
   public double posZ;
   public double prevPosX;
   public double prevPosY;
   public double prevPosZ;
   public float motionX;
   public float motionY;
   public float motionZ;
   public float speedFactor = 1.0F;
   public float rotation;
   public float prevRotation;
   public float rotationSpeed;
   protected boolean clip = true;
   public boolean onGround;
   public int ticksExisted;
   public boolean isDead;
   private int posVboId = 0;
   public float alpha = 1.0F;
   public float prevAlpha = 1.0F;
   public ParticleIcon icon;
   public float textureSize;
   public float prevTextureSize;
   public float collisionSize;
   protected float halfCollisionSize;
   public double lastDistanceSq;
   public float burn = 0.0F;
   public float prevBurn = 0.0F;
   public int blendFunc = 0;
   public float red = 1.0F;
   public float green = 1.0F;
   public float blue = 1.0F;

   public Particle(ParticleEmitter parent, float collisionSize, float size, ParticleIcon icon) {
      this.parent = parent;
      this.boundingBox = asx.a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
      this.halfCollisionSize = collisionSize / 2.0F;
      this.setCollisionSize(collisionSize);
      this.textureSize = size;
      this.icon = icon;
   }

   public void tick() {
      this.ticksExisted++;
      this.prevAlpha = this.alpha;
      this.prevBurn = this.burn;
      this.prevTextureSize = this.textureSize;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.prevRotation = this.rotation;
      this.motionX = this.motionX * this.speedFactor;
      this.motionY = this.motionY * this.speedFactor;
      this.motionZ = this.motionZ * this.speedFactor;
      this.rotation = this.rotation + this.rotationSpeed;
      this.move(this.motionX, this.motionY, this.motionZ);
   }

   public void move(double x, double y, double z) {
      if (this.clip) {
         this.clippedMove(this.motionX, this.motionY, this.motionZ);
      } else {
         this.simpleMove(this.motionX, this.motionY, this.motionZ);
      }
   }

   public void updateDistance(double cameraX, double cameraY, double cameraZ, float frame) {
      double deltaX = this.prevPosX + (this.posX - this.prevPosX) * frame - cameraX;
      double deltaY = this.prevPosY + (this.posY - this.prevPosY) * frame - cameraY;
      double deltaZ = this.prevPosZ + (this.posZ - this.prevPosZ) * frame - cameraZ;
      this.lastDistanceSq = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
   }

   protected void setCollisionSize(float size) {
      if (size != this.collisionSize) {
         this.collisionSize = size;
         this.boundingBox.d = this.boundingBox.a + this.collisionSize;
         this.boundingBox.f = this.boundingBox.c + this.collisionSize;
         this.boundingBox.e = this.boundingBox.b + this.collisionSize;
      }
   }

   public void setPosition(double x, double y, double z) {
      this.prevPosX = this.posX = x;
      this.prevPosY = this.posY = y;
      this.prevPosZ = this.posZ = z;
      this.updateBounds();
   }

   public void moveTo(double x, double y, double z) {
      this.posX = x;
      this.posY = y;
      this.posZ = z;
      this.updateBounds();
   }

   protected void simpleMove(double moveX, double moveY, double moveZ) {
      this.posX += moveX;
      this.posY += moveY;
      this.posZ += moveZ;
      this.updateBounds();
   }

   protected void updateBounds() {
      this.boundingBox
         .b(
            this.posX - this.halfCollisionSize,
            this.posY - this.halfCollisionSize,
            this.posZ - this.halfCollisionSize,
            this.posX + this.halfCollisionSize,
            this.posY + this.halfCollisionSize,
            this.posZ + this.halfCollisionSize
         );
   }

   protected void clippedMove(double moveX, double moveY, double moveZ) {
      double tempX = this.posX;
      double tempY = this.posY;
      double tempZ = this.posZ;
      double tempMoveX = moveX;
      double tempMoveY = moveY;
      double tempMoveZ = moveZ;
      asx axisalignedbb = this.boundingBox.c();
      List list = this.parent.getCollidingBoundingBoxes(this.boundingBox.a(moveX, moveY, moveZ));

      for (int j = 0; j < list.size(); j++) {
         moveY = ((asx)list.get(j)).b(this.boundingBox, moveY);
      }

      this.boundingBox.d(0.0, moveY, 0.0);

      for (int var22 = 0; var22 < list.size(); var22++) {
         moveX = ((asx)list.get(var22)).a(this.boundingBox, moveX);
      }

      this.boundingBox.d(moveX, 0.0, 0.0);

      for (int var23 = 0; var23 < list.size(); var23++) {
         moveZ = ((asx)list.get(var23)).c(this.boundingBox, moveZ);
      }

      this.boundingBox.d(0.0, 0.0, moveZ);
      this.posX = (float)((this.boundingBox.a + this.boundingBox.d) / 2.0);
      this.posY = (float)(this.boundingBox.b + this.collisionSize / 2.0);
      this.posZ = (float)((this.boundingBox.c + this.boundingBox.f) / 2.0);
      this.onGround = tempMoveY != moveY && tempMoveY < 0.0;
      if (tempMoveX != moveX) {
         this.motionX = 0.0F;
      }

      if (tempMoveY != moveY) {
         this.motionY = 0.0F;
      }

      if (tempMoveZ != moveZ) {
         this.motionZ = 0.0F;
      }
   }

   public boolean shouldRender(float frame) {
      return true;
   }

   public float getLightmaskBrightness() {
      int x = ls.c(this.posX);
      int z = ls.c(this.posZ);
      if (this.parent.world.f(x, 0, z)) {
         double d0 = (this.boundingBox.e - this.boundingBox.b) * 0.5;
         int y = ls.c(this.posY + d0);
         return this.parent.world.n(x, y, z) / 16.0F;
      } else {
         return 0.0F;
      }
   }

   public int getTessellatorBrightness() {
      int i = ls.c(this.posX);
      int j = ls.c(this.posZ);
      if (this.parent.world.f(i, 0, j)) {
         double d0 = (this.boundingBox.e - this.boundingBox.b) * 0.5;
         int k = ls.c(this.posY + d0);
         return this.parent.world.h(i, k, j, 0);
      } else {
         return 0;
      }
   }

   public int compareTo(Particle anotherParticle) {
      return this.lastDistanceSq > anotherParticle.lastDistanceSq ? -1 : (this.lastDistanceSq < anotherParticle.lastDistanceSq ? 1 : 0);
   }

   public void setColorRGB(float red, float green, float blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
   }

   public void setColorRGBA(float red, float green, float blue, float alpha) {
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
   }

   public void onRender(bfq t, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float sizeOffset, float frame) {
      float alph = this.prevAlpha + (this.alpha - this.prevAlpha) * frame;
      float alphac = this.alpha * (1.0F - (this.prevBurn + (this.burn - this.prevBurn) * frame));
      float xSize = (this.prevTextureSize + (this.textureSize - this.prevTextureSize) * frame) * sizeOffset;
      float ySize = (this.prevTextureSize + (this.textureSize - this.prevTextureSize) * frame) * sizeOffset;
      double x = this.prevPosX + (this.posX - this.prevPosX) * frame - bgl.b;
      double y = this.prevPosY + (this.posY - this.prevPosY) * frame - bgl.c;
      double z = this.prevPosZ + (this.posZ - this.prevPosZ) * frame - bgl.d;
      float rot = (45.0F - this.prevRotation - (this.rotation - this.prevRotation) * frame) * (float) Math.PI / 180.0F;
      float rotSin = ls.a(rot);
      float rotCos = ls.b(rot);
      t.a(this.red * this.alpha, this.green * this.alpha, this.blue * this.alpha, alphac);
      t.c(this.getTessellatorBrightness());
      t.a(
         x + rotationX * xSize * rotSin + rotationYZ * ySize * rotCos,
         y + rotationXZ * ySize * rotCos,
         z + rotationZ * xSize * rotSin + rotationXY * ySize * rotCos,
         this.icon.c(),
         this.icon.e()
      );
      t.a(
         x + rotationX * xSize * rotCos - rotationYZ * ySize * rotSin,
         y - rotationXZ * ySize * rotSin,
         z + rotationZ * xSize * rotCos - rotationXY * ySize * rotSin,
         this.icon.c(),
         this.icon.f()
      );
      t.a(
         x - rotationX * xSize * rotSin - rotationYZ * ySize * rotCos,
         y - rotationXZ * ySize * rotCos,
         z - rotationZ * xSize * rotSin - rotationXY * ySize * rotCos,
         this.icon.d(),
         this.icon.f()
      );
      t.a(
         x - rotationX * xSize * rotCos + rotationYZ * ySize * rotSin,
         y + rotationXZ * ySize * rotSin,
         z - rotationZ * xSize * rotCos + rotationXY * ySize * rotSin,
         this.icon.d(),
         this.icon.e()
      );
   }
}
