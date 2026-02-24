package ru.stalcraft.client;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.entity.EntityDecorative;

public class RenderDecorative extends bgm {
   public void rendering(EntityDecorative entity, double x, double y, double z, float frame, float rotation) {
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      GL11.glScalef(1.25F, 1.25F, 1.25F);
      entity.model.renderAll();
      GL11.glPopMatrix();
   }

   public void a(nn entity, double d0, double d1, double d2, float f, float f1) {
      this.rendering((EntityDecorative)entity, d0, d1, d2, f, f1);
   }

   protected bjo a(nn entity) {
      return null;
   }
}
