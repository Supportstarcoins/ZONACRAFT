package ru.stalcraft.client.render;

import java.util.HashMap;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.entity.EntitySleeve;

public class RenderSleeve extends bgm {
   private static HashMap models = new HashMap();
   public atv mc = atv.w();

   public void doRenderSleeve(EntitySleeve entity, double par2, double par4, double par6, float par8, float frame) {
      if (entity.model != null) {
         GL11.glPushMatrix();

         try {
            if (entity.renderOnGround) {
               par4 += 0.03;
            }

            GL11.glTranslatef((float)par2, (float)par4, (float)par6);
            GL11.glDisable(2884);
            atv.w().N.a(entity.texture);
            if (!models.containsKey(entity.model)) {
               models.put(entity.model, null);
               models.put(entity.model, AdvancedModelLoader.loadModel("/assets/stalker/models/sleeves/" + entity.model + ".obj"));
            }

            IModelCustom e = (IModelCustom)models.get(entity.model);
            if (e != null) {
               GL11.glRotatef(this.angle(entity.xRotation + entity.xRotationSpeed * frame), 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(this.angle(entity.yRotation + entity.yRotationSpeed * frame), 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(this.angle(entity.zRotation + entity.zRotationSpeed * frame), 0.0F, 0.0F, 1.0F);
            }

            GL11.glEnable(2884);
         } catch (Exception var111) {
            var111.printStackTrace();
         }

         GL11.glPopMatrix();
      }
   }

   private float angle(float angle) {
      angle %= 360.0F;
      if (angle > 180.0F) {
         angle += -360.0F;
      }

      return angle;
   }

   public void a(nn par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderSleeve((EntitySleeve)par1Entity, par2, par4, par6, par8, par9);
   }

   protected bjo a(nn entity) {
      return null;
   }
}
