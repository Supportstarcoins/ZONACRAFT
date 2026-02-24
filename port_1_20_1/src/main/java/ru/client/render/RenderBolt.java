package ru.stalcraft.client.render;

import java.util.HashMap;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.items.EntityBolt;

public class RenderBolt extends bgm {
   private HashMap models = new HashMap();

   public void doRenderBolt(EntityBolt entity, double par2, double par4, double par6, float par8, float frame) {
      GL11.glPushMatrix();

      try {
         GL11.glTranslatef((float)par2, (float)par4, (float)par6);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(0.0125F, 0.0125F, 0.0125F);
         atv.w().N.a(new bjo("stalker", "models/items/bolt.png"));
         GL11.glRotatef(-entity.A, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(entity.D + (entity.B - entity.D) * frame, 0.0F, 0.0F, 1.0F);
         IModelCustom e;
         if (this.models.containsKey(entity.modelName)) {
            e = (IModelCustom)this.models.get(entity.modelName);
         } else {
            e = AdvancedModelLoader.loadModel("/assets/stalker/models/items/bolt.obj");
            this.models.put(entity.modelName, e);
         }

         e.renderAll();
      } catch (Exception var11) {
      }

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
      this.doRenderBolt((EntityBolt)par1Entity, par2, par4, par6, par8, par9);
   }

   protected bjo a(nn entity) {
      return null;
   }
}
