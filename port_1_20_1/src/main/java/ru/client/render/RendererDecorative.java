package ru.stalcraft.client.render;

import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.loaders.StalkerModelManager;

public class RendererDecorative {
   public atv mc;
   public IModelCustom modelDecorative;
   public bjo modelTexture;
   public int modelId = -1;

   public RendererDecorative(String model) {
      this.mc = atv.w();

      try {
         this.modelDecorative = StalkerModelManager.addModel("decor", model + ".obj");
         this.modelTexture = new bjo("stalker", "models/decor/" + model + ".png");
         this.modelId = GL11.glGenLists(1);
         GL11.glNewList(this.modelId, 4864);
         this.modelDecorative.renderAll();
         GL11.glEndList();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public void renderDecorative(asp tile, double x, double y, double z, float f) {
      int metadata = tile.k.h(tile.l, tile.m, tile.n);
      int rotationAngle = metadata * 90;
      if (this.modelId != -1) {
         GL11.glPushMatrix();
         GL11.glTranslated(x + 0.5, y, z + 0.5);
         GL11.glRotatef(90.0F + rotationAngle, 0.0F, 1.0F, 0.0F);
         this.mc.N.a(this.modelTexture);
         GL11.glCallList(this.modelId);
         GL11.glPopMatrix();
      }
   }
}
