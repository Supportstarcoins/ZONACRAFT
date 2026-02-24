package ru.stalcraft.client.render;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderKnife implements IItemRenderer {
   private static atv mc = atv.w();
   private bjo texture = new bjo("stalker", "models/knife.png");
   protected IModelCustom model = AdvancedModelLoader.loadModel("/assets/stalker/models/knife.obj");
   private static float[] pos = new float[]{0.66F, 1.3F, 0.78F, -76.8F, 14.1F, 45.3F};

   public boolean handleRenderType(ye item, ItemRenderType type) {
      return type != ItemRenderType.ENTITY && type != ItemRenderType.INVENTORY;
   }

   public boolean shouldUseRenderHelper(ItemRenderType type, ye item, ItemRendererHelper helper) {
      return true;
   }

   private void renderModel() {
      mc.N.a(this.texture);
      this.model.renderAll();
   }

   public void renderItem(ItemRenderType type, ye item, Object... data) {
      att.a();

      try {
         if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            if (data[1] == mc.h && mc.u.aa == 0) {
               GL11.glPushMatrix();
               GL11.glRotatef(-33.0F, 0.0F, 1.0F, 0.0F);
               GL11.glTranslatef(0.2F, 1.475F, 1.05F);
               GL11.glScalef(3.0F, 3.0F, 3.0F);
               this.renderModel();
               GL11.glPopMatrix();
            } else {
               GL11.glPushMatrix();
               GL11.glTranslatef(pos[0], pos[1], pos[2]);
               GL11.glRotatef(pos[3], 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(pos[4], 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(pos[5], 0.0F, 0.0F, 1.0F);
               GL11.glScalef(4.0F, 4.0F, 4.0F);
               this.renderModel();
               GL11.glPopMatrix();
            }
         }
      } catch (NullPointerException var5) {
      }

      att.b();
   }
}
