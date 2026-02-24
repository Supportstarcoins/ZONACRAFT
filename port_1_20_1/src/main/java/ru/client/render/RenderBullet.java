package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.entity.EntityBullet;

public class RenderBullet extends bgm {
   public bjo bullet_trace = new bjo("stalker:textures/bullet_trace.png");
   public IModelCustom model = AdvancedModelLoader.loadModel("/assets/stalker/models/tracer.obj");
   public int modelTracer = -1;
   public atv mc = atv.w();
   public float par1 = 0.0F;

   public RenderBullet() {
      this.modelTracer = GL11.glGenLists(1);
      GL11.glNewList(this.modelTracer, 4864);
      this.model.renderAll();
      GL11.glEndList();
   }

   public void doRenderBullet(EntityBullet entity, double x, double y, double z, float rotation, float frame) {
      GL11.glPushMatrix();
      float size = 10.0F;
      GL11.glTranslated(x, y, z);
      GL11.glRotatef(entity.C + (entity.A - entity.C) * frame, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(entity.D + (entity.B - entity.D) * frame, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(0.45F, 0.45F + size, 0.45F);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(770, 1);
      bfq t = bfq.a;
      this.mc.N.a(this.bullet_trace);
      t.b();
      t.b(0.0F, 1.0F, 0.0F);
      t.a(-1.0, 1.0, 0.0, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.0, 0.0, 1.0);
      t.a(1.0, -1.0, 0.0, 1.0, 1.0);
      t.a(1.0, 1.0, 0.0, 1.0, 0.0);
      t.a();
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      t.b();
      t.b(0.0F, 1.0F, 0.0F);
      t.a(-1.0, 1.0, 0.0, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.0, 0.0, 1.0);
      t.a(1.0, -1.0, 0.0, 1.0, 1.0);
      t.a(1.0, 1.0, 0.0, 1.0, 0.0);
      t.a();
      GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      t.b();
      t.b(0.0F, 1.0F, 0.0F);
      t.a(-1.0, 1.0, 0.0, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.0, 0.0, 1.0);
      t.a(1.0, -1.0, 0.0, 1.0, 1.0);
      t.a(1.0, 1.0, 0.0, 1.0, 0.0);
      t.a();
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      t.b();
      t.b(0.0F, 1.0F, 0.0F);
      t.a(-1.0, 1.0, 0.0, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.0, 0.0, 1.0);
      t.a(1.0, -1.0, 0.0, 1.0, 1.0);
      t.a(1.0, 1.0, 0.0, 1.0, 0.0);
      t.a();
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   private float angle(float angle) {
      angle %= 360.0F;
      if (angle > 180.0F) {
         angle += -360.0F;
      }

      return angle;
   }

   public void a(nn par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderBullet((EntityBullet)par1Entity, par2, par4, par6, par8, par9);
   }

   protected bjo a(nn entity) {
      return null;
   }
}
