package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.StalkerMain;

public class RenderBackpack {
   public static bjo backPackMap = new bjo("stalker", "models/backpack/backpacks.png");
   public static IModelCustom[] models = new IModelCustom[3];
   public static atv mc;

   public RenderBackpack() {
      mc = atv.w();

      for (int i = 0; i < 3; i++) {
         models[i] = AdvancedModelLoader.loadModel("/assets/stalker/models/backpack/" + i + ".obj");
      }
   }

   public static void renderBackpack(nn entity, int backpack) {
      GL11.glPushMatrix();
      att.a();
      GL11.glTranslatef(0.0F, 1.3F, 0.0F);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      mc.N.a(backPackMap);
      if (backpack == StalkerMain.backpack1.cv) {
         models[0].renderAll();
      }

      if (backpack == StalkerMain.backpack2.cv) {
         GL11.glTranslatef(0.0F, -0.24F, 0.0F);
         GL11.glScalef(1.25F, 1.25F, 1.25F);
         models[1].renderAll();
      }

      if (backpack == StalkerMain.backpack3.cv) {
         GL11.glTranslatef(0.0F, -0.14F, 0.0F);
         models[2].renderAll();
      }

      att.b();
      GL11.glPopMatrix();
   }
}
