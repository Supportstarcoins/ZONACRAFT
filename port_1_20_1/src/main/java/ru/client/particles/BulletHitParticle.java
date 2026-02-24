package ru.stalcraft.client.particles;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class BulletHitParticle extends Particle {
   public BulletHitParticle(ParticleEmitter parent, ParticleIcon icon, double posX, double posY, double posZ, int hitSide) {
      super(parent, 1.1F, 1.1F, icon);
      super.blendFunc = 0;
      super.textureSize = 0.35F;
      super.prevRotation = super.rotation = super.parent.world.s.nextFloat() * 360.0F;
      if (hitSide == 1) {
         super.motionY = 0.02F;
         posY += 0.05F;
      } else if (hitSide == 2) {
         super.motionZ -= 0.035F;
         posZ -= 0.1F;
      } else if (hitSide == 3) {
         super.motionZ = 0.035F;
         posZ += 0.1F;
      } else if (hitSide == 4) {
         super.motionX = -0.035F;
         posX -= 0.1F;
      } else if (hitSide == 5) {
         super.motionX = 0.035F;
         posX += 0.1F;
      }

      super.setPosition(posX, posY, posZ);
   }

   @Override
   public void tick() {
      super.tick();
      super.alpha -= 0.15F;
      super.textureSize += 0.005F;
      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }

   @Override
   public void onRender(bfq t, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float sizeOffset, float frame) {
      GL11.glBlendFunc(772, 771);
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
