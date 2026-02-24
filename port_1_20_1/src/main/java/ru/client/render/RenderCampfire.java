package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderCampfire extends bje {
   public IModelCustom model = AdvancedModelLoader.loadModel("/assets/stalker/models/campfire.obj");
   public bjo texture = new bjo("stalker:models/campfire.png");
   public atv mc;
   public int modelCampfire = -1;

   public RenderCampfire() {
      this.mc = atv.w();
      this.modelCampfire = GL11.glGenLists(1);
      GL11.glNewList(this.modelCampfire, 4864);
      GL11.glEndList();
   }

   public void a(asp tile, double x, double y, double z, float frame) {
      att.a();
      GL11.glPushMatrix();
      GL11.glBlendFunc(770, 771);
      this.mc.N.a(this.texture);
      GL11.glTranslated(x + 0.5, y, z + 0.5);
      GL11.glScalef(1.15F, 1.15F, 1.15F);
      this.model.renderAll();
      GL11.glPopMatrix();
      att.b();
   }
}
