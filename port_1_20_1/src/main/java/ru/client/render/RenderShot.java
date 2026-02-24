package ru.stalcraft.client.render;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.particles.ShotParticleEmitter;
import ru.stalcraft.entity.EntityShot;

public class RenderShot extends bgm {
   private bjo texture1 = new bjo("stalker", "textures/light1.png");
   private bjo texture2 = new bjo("stalker", "textures/light2.png");

   public void doRenderShot(EntityShot entity, double par2, double par4, double par6, float par8, float par9) {
      if (entity.size != 0.0F) {
         entity.spawnedParticles = true;
         ShotParticleEmitter emitter = new ShotParticleEmitter(entity);
         EffectsEngine.instance.addParticleEmitter(emitter);
      }
   }

   private void renderSimpleShot(EntityShot entity, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2, (float)par4 - 0.1F, (float)par6);
      GL11.glScalef(entity.size / 2.0F, entity.size / 2.0F, entity.size / 2.0F);
      GL11.glEnable(3553);
      GL11.glEnable(3042);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2896);
      this.a(this.texture2);
      bfq t = bfq.a;
      GL11.glRotatef(90.0F - entity.A, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(entity.B, 0.0F, 0.0F, 1.0F);
      double alpha = Math.toRadians(entity.B);
      double distance1 = -1.0;
      double distance2 = distance1 + 1.0;
      double xMod = 0.0;
      double yMod = 0.5;
      double y1 = 0.0;
      double y2 = 0.0;
      double x1 = -1.25;
      double x2 = 0.0;
      GL11.glRotatef(entity.rotationRoll, 1.0F, 0.0F, 0.0F);
      t.b();
      t.a(x1 + xMod + 0.15F, y1 - yMod, 0.0, 0.0, 1.0);
      t.a(x2 + xMod + 0.15F, y2 - yMod, 0.0, 1.0, 1.0);
      t.a(x2 - xMod + 0.15F, y2 + yMod, 0.0, 1.0, 0.0);
      t.a(x1 - xMod + 0.15F, y1 + yMod, 0.0, 0.0, 0.0);
      t.a();
      t.b();
      t.a(x1 + xMod + 0.15F, y1 - yMod, 0.0, 0.0, 1.0);
      t.a(x1 - xMod + 0.15F, y1 + yMod, 0.0, 0.0, 0.0);
      t.a(x2 - xMod + 0.15F, y2 + yMod, 0.0, 1.0, 0.0);
      t.a(x2 + xMod + 0.15F, y2 - yMod, 0.0, 1.0, 1.0);
      t.a();
      t.b();
      t.a(x1 + 0.15F, y1, -0.5, 0.0, 1.0);
      t.a(x2 + 0.15F, y2, -0.5, 1.0, 1.0);
      t.a(x2 + 0.15F, y2, 0.5, 1.0, 0.0);
      t.a(x1 + 0.15F, y1, 0.5, 0.0, 0.0);
      t.a();
      t.b();
      t.a(x1 + 0.15F, y1, -0.5, 0.0, 1.0);
      t.a(x1 + 0.15F, y1, 0.5, 0.0, 0.0);
      t.a(x2 + 0.15F, y2, 0.5, 1.0, 0.0);
      t.a(x2 + 0.15F, y2, -0.5, 1.0, 1.0);
      t.a();
      this.a(this.texture2);
      t.b();
      t.a(x2 + xMod, y2 - yMod, -0.5, 0.0, 1.0);
      t.a(x2 + xMod, y2 - yMod, 0.5, 1.0, 1.0);
      t.a(x2 - xMod, y2 + yMod, 0.5, 1.0, 0.0);
      t.a(x2 - xMod, y2 + yMod, -0.5, 0.0, 0.0);
      t.a();
      t.b();
      t.a(x2 - xMod, y2 + yMod, -0.5, 0.0, 0.0);
      t.a(x2 - xMod, y2 + yMod, 0.5, 1.0, 0.0);
      t.a(x2 + xMod, y2 - yMod, 0.5, 1.0, 1.0);
      t.a(x2 + xMod, y2 - yMod, -0.5, 0.0, 1.0);
      t.a();
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2896);
      GL11.glDisable(3042);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glPopMatrix();
   }

   public void a(nn par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderShot((EntityShot)par1Entity, par2, par4, par6, par8, par9);
   }

   protected bjo a(nn entity) {
      return null;
   }
}
