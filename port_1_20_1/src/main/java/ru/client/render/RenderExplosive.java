package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.entity.EntityExplosive;

public class RenderExplosive extends bgm {
   private static IModelCustom model = AdvancedModelLoader.loadModel("/assets/stalker/models/explosive.obj");
   private static bjo texture = new bjo("stalker", "models/explosive.png");

   public void doRenderExplosive(EntityExplosive explosive, double par2, double par4, double par6, float par8, float frame) {
      GL11.glPushMatrix();

      try {
         GL11.glTranslatef((float)par2 + 0.0125F, (float)par4 + 0.0125F, (float)par6 + 0.0125F);
         atv.w().N.a(texture);
         GL11.glScalef(1.0E-4F, 1.0E-4F, 1.0E-4F);
         int e = explosive.getSidePlaced();
         if (e == 2 || e == 3) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         } else if (e == 4 || e == 5) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         }

         model.renderAll();
      } catch (Exception var111) {
         var111.printStackTrace();
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
      this.doRenderExplosive((EntityExplosive)par1Entity, par2, par4, par6, par8, par9);
   }

   protected bjo a(nn entity) {
      return null;
   }
}
