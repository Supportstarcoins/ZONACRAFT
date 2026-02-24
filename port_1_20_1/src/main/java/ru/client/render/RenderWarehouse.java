package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderWarehouse extends bje {
   public IModelCustom model = AdvancedModelLoader.loadModel("/assets/stalker/models/sklad.obj");
   public bjo modelTexture = new bjo("stalker:models/sklad.png");
   public atv mc = atv.w();

   public void a(asp tile, double x, double y, double z, float frame) {
      int metadata = tile.k.h(tile.l, tile.m, tile.n);
      int rotationAngle = metadata * 90;
      GL11.glPushMatrix();
      GL11.glTranslated(x + 0.5, y, z + 0.5);
      GL11.glRotatef(90.0F + rotationAngle, 0.0F, 1.0F, 0.0F);
      this.mc.N.a(this.modelTexture);
      this.model.renderAll();
      GL11.glPopMatrix();
   }
}
