package ru.stalcraft.client.render;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.loaders.StalkerModelManager;
import ru.stalcraft.client.models.ModelHand;

public class RenderFlashlight implements IItemRenderer {
   private static atv mc = atv.w();
   private String modelName = "flashlight.obj";
   private bjo texture = new bjo("stalker", "models/items/flashlight.png");
   protected ModelHand hand = new ModelHand();
   private static float[] fpTransform = new float[]{-0.355F, -0.06F, 0.234F, -0.6F, -15.4F, -3.85F};
   private static float[] playerTransform = new float[]{0.66F, 1.3F, 0.78F, -76.8F, 14.1F, 45.3F};
   private static float[] oldLivingTransform = new float[]{-0.49F, 1.08F, 0.57F, 29.0F, 233.7F, 33.6F};
   private static float[] newLivingTransform = new float[]{0.23F, 0.84F, 0.76F, 9.1F, 316.6F, 0.0F};
   private float aimPosY;
   private float aimPosZ;
   private float aimRotX;
   private float posX = 0.0F;
   private float posY = 0.0F;
   private float posZ = 0.0F;

   public boolean handleRenderType(ye item, ItemRenderType type) {
      return type != ItemRenderType.ENTITY && type != ItemRenderType.INVENTORY && (type != ItemRenderType.EQUIPPED || GuiSettingsStalker.useWeaponModels);
   }

   public boolean shouldUseRenderHelper(ItemRenderType type, ye item, ItemRendererHelper helper) {
      return true;
   }

   private void renderModel(ye stack) {
      StalkerModelManager m = ClientProxy.modelManager;
      m.tryLoadTexture(this.texture);
      IModelCustom model = m.getModel("items", this.modelName);
      if (model != null && m.tryBindTexture(this.texture)) {
         model.renderAll();
      }
   }

   public void renderItem(ItemRenderType type, ye item, Object... data) {
      try {
         if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(fpTransform[0], fpTransform[1], fpTransform[2]);
            GL11.glRotatef(fpTransform[3], 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(fpTransform[4], 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(fpTransform[5], 0.0F, 0.0F, 1.0F);
            GL11.glPushMatrix();
            GL11.glRotatef(-33.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(this.posX - 0.15F, this.posY + 1.325F, this.posZ + 0.95F);
            GL11.glScalef(3.0F, 3.0F, 3.0F);
            this.renderModel(item);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-33.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.3F, -1.35F, 1.075F);
            this.hand.render(mc.h, 1);
            GL11.glPopMatrix();
         } else if (type == ItemRenderType.EQUIPPED) {
            GL11.glPushMatrix();
            if (data[1] instanceof uf) {
               GL11.glTranslatef(playerTransform[0] - 0.05F, playerTransform[1] - 0.9F, playerTransform[2]);
               GL11.glRotatef(playerTransform[3], 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(playerTransform[4], 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(playerTransform[5], 0.0F, 0.0F, 1.0F);
               GL11.glScalef(4.0F, 4.0F, 4.0F);
            } else {
               GL11.glTranslatef(newLivingTransform[0] - 0.05F, newLivingTransform[1] - 0.9F, newLivingTransform[2]);
               GL11.glRotatef(newLivingTransform[3], 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(newLivingTransform[4], 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(newLivingTransform[5], 0.0F, 0.0F, 1.0F);
               GL11.glScalef(2.0F, 2.0F, 2.0F);
            }

            this.renderModel(item);
            GL11.glPopMatrix();
         }
      } catch (NullPointerException var5) {
         var5.printStackTrace();
      }
   }
}
