package ru.stalcraft.client.particles;

import java.util.Random;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.entity.EntityShot;

public class ShotParticleEmitter extends ParticleEmitter {
   private static ParticleIcon lightIcon;
   private static ParticleIcon[] smokeIcons;

   public ShotParticleEmitter(EntityShot entity) {
      super(entity);
      super.setCenter(entity.u, entity.v - 0.15, entity.w);
      super.setSize(-2.0, -2.0, -2.0, 2.0, 2.0, 2.0);
      float distance = entity.distance;
      Random rand = entity.q.s;
      float s = entity.size / 2.0F;
      ShotLightParticle shotLightParticle = null;
      ShotSmokeParticle shotSmokeParticle = null;

      for (int i = 0; i < 5; i++) {
         shotLightParticle = new ShotLightParticle(this, lightIcon);
         float pitch = rand.nextFloat() * 0.9F * s;
         float rotationMatrix = entity.A + (rand.nextFloat() - 0.5F) * 10.0F;
         float particle = entity.B + (rand.nextFloat() - 0.5F) * 10.0F;
         shotLightParticle.motionX = -ls.a(rotationMatrix / 180.0F * (float) Math.PI) * ls.b(particle / 180.0F * (float) Math.PI) * pitch;
         shotLightParticle.motionZ = ls.b(rotationMatrix / 180.0F * (float) Math.PI) * ls.b(particle / 180.0F * (float) Math.PI) * pitch;
         shotLightParticle.motionY = -ls.a(particle / 180.0F * (float) Math.PI) * pitch;
         shotLightParticle.setPosition(
            shotLightParticle.posX + shotLightParticle.motionX * 0.04,
            shotLightParticle.posY + shotLightParticle.motionY * 0.04,
            shotLightParticle.posZ + shotLightParticle.motionZ * 0.04
         );
         shotLightParticle.textureSize *= s;
         shotLightParticle.textureSize *= 2.0F;
         super.particles.add(shotLightParticle);
      }

      for (int var201 = 0; var201 < 10; var201++) {
         shotSmokeParticle = new ShotSmokeParticle(this, smokeIcons[rand.nextInt(smokeIcons.length)]);
         float pitch = rand.nextFloat() * 0.9F * s;
         float rotationMatrix = entity.A + (rand.nextFloat() - 0.5F) * 10.0F;
         float particle = entity.B + (rand.nextFloat() - 0.5F) * 10.0F;
         shotSmokeParticle.motionX = -ls.a(rotationMatrix / 180.0F * (float) Math.PI) * ls.b(particle / 180.0F * (float) Math.PI) * pitch;
         shotSmokeParticle.motionZ = ls.b(rotationMatrix / 180.0F * (float) Math.PI) * ls.b(particle / 180.0F * (float) Math.PI) * pitch;
         shotSmokeParticle.motionY = -ls.a(particle / 180.0F * (float) Math.PI) * pitch;
         shotSmokeParticle.setPosition(
            shotSmokeParticle.posX + shotSmokeParticle.motionX * 0.04,
            shotSmokeParticle.posY + shotSmokeParticle.motionY * 0.04,
            shotSmokeParticle.posZ + shotSmokeParticle.motionZ * 0.04
         );
         shotSmokeParticle.textureSize *= s;
         super.particles.add(shotSmokeParticle);
      }

      float speed = 0.0F;
      float angle = 0.0F;
      float cos = 0.0F;
      float sin = 0.0F;
      float var16 = 0.0F;
      Matrix3f var17 = null;
      Vector3f vec1 = null;
      float vec = 0.0F;

      for (int var21 = 0; var21 < 50; var21++) {
         var16 = entity.A + (rand.nextFloat() - 0.5F) * 10.0F;
         float pitch = entity.B + (rand.nextFloat() - 0.5F) * 10.0F;
         var17 = Matrix3f.mul(EffectsEngine.rotationMatrix(var16, 0.0F, 1.0F, 0.0F), EffectsEngine.rotationMatrix(-pitch, 1.0F, 0.0F, 0.0F), (Matrix3f)null);
         shotLightParticle = new ShotLightParticle(this, lightIcon);
         shotLightParticle.textureSize = 0.4F;
         speed = rand.nextFloat();
         angle = (1.0F - speed * speed) * 0.5F * s;
         cos = rand.nextFloat() * 2.0F * (float) Math.PI;
         sin = ls.b(cos);
         vec = ls.a(cos);
         vec1 = Matrix3f.transform(var17, new Vector3f(vec * angle, sin * angle, 0.0F), (Vector3f)null);
         shotLightParticle.motionX = vec1.x;
         shotLightParticle.motionZ = vec1.z;
         shotLightParticle.motionY = vec1.y;
         shotLightParticle.motionX = shotLightParticle.motionX
            + -ls.a(var16 / 180.0F * (float) Math.PI) * ls.b(pitch / 180.0F * (float) Math.PI) * angle * 0.35F;
         shotLightParticle.motionZ = shotLightParticle.motionZ
            + ls.b(var16 / 180.0F * (float) Math.PI) * ls.b(pitch / 180.0F * (float) Math.PI) * angle * 0.35F;
         shotLightParticle.motionY = shotLightParticle.motionY + -ls.a(pitch / 180.0F * (float) Math.PI) * angle * 0.35F;
         shotLightParticle.setPosition(
            shotLightParticle.posX + shotLightParticle.motionX * 0.04,
            shotLightParticle.posY + shotLightParticle.motionY * 0.04,
            shotLightParticle.posZ + shotLightParticle.motionZ * 0.04
         );
         shotLightParticle.textureSize *= s;
         super.particles.add(shotLightParticle);
      }

      for (int var22 = 0; var22 < 25; var22++) {
         var16 = entity.A + (rand.nextFloat() - 0.5F) * 10.0F;
         float pitch = entity.B + (rand.nextFloat() - 0.5F) * 10.0F;
         var17 = Matrix3f.mul(EffectsEngine.rotationMatrix(var16, 0.0F, 1.0F, 0.0F), EffectsEngine.rotationMatrix(-pitch, 1.0F, 0.0F, 0.0F), (Matrix3f)null);
         shotSmokeParticle = new ShotSmokeParticle(this, smokeIcons[rand.nextInt(smokeIcons.length)]);
         shotSmokeParticle.textureSize = 0.25F;
         speed = rand.nextFloat() * 0.5F * s;
         angle = rand.nextFloat() * 2.0F * (float) Math.PI;
         cos = ls.b(angle);
         sin = ls.a(angle);
         Vector3f var20 = Matrix3f.transform(var17, new Vector3f(sin * speed, cos * speed, 0.0F), (Vector3f)null);
         shotSmokeParticle.motionX = var20.x;
         shotSmokeParticle.motionZ = var20.z;
         shotSmokeParticle.motionY = var20.y;
         shotSmokeParticle.motionX = shotSmokeParticle.motionX
            + -ls.a(var16 / 180.0F * (float) Math.PI) * ls.b(pitch / 180.0F * (float) Math.PI) * speed * 0.35F;
         shotSmokeParticle.motionZ = shotSmokeParticle.motionZ
            + ls.b(var16 / 180.0F * (float) Math.PI) * ls.b(pitch / 180.0F * (float) Math.PI) * speed * 0.35F;
         shotSmokeParticle.motionY = shotSmokeParticle.motionY + -ls.a(pitch / 180.0F * (float) Math.PI) * speed * 0.35F;
         shotSmokeParticle.setPosition(
            shotSmokeParticle.posX + shotSmokeParticle.motionX * 0.04,
            shotSmokeParticle.posY + shotSmokeParticle.motionY * 0.04,
            shotSmokeParticle.posZ + shotSmokeParticle.motionZ * 0.04
         );
         shotSmokeParticle.textureSize *= s;
         super.particles.add(shotSmokeParticle);
      }
   }

   @Override
   public void tick() {
      super.tick();
      super.renderDistanceSq = GuiSettingsStalker.particleRenderDistance * GuiSettingsStalker.particleRenderDistance;
   }

   @Override
   public boolean isValid() {
      return !((nn)super.emmiter).M;
   }

   public static void registerIcons(mt ir) {
      lightIcon = (ParticleIcon)ir.a("stalker:shotlight");
      smokeIcons = new ParticleIcon[4];

      for (int i = 0; i < 4; i++) {
         smokeIcons[i] = (ParticleIcon)ir.a("stalker:smoke/smoke" + (i + 1));
      }
   }
}
