package ru.stalcraft.client.render;

import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.loaders.StalkerModelManager;
import ru.stalcraft.items.ItemArmorArtefakt;

public class RenderItemArmor {
   private RendererManager rendererManager;
   private ItemArmorArtefakt itemArmor;
   private int modelHeadArmor = -1;
   private int modelBodyArmor = -1;
   private int modelRightArmArmor = -1;
   private int modelLeftArmArmor = -1;
   private int modelRightLegArmor = -1;
   private int modelLeftLegArmor = -1;
   private int modelTexture = -1;

   public RenderItemArmor(RendererManager rendererManager, atv mc, ItemArmorArtefakt itemArmor) throws Exception {
      itemArmor.rendererId = rendererManager.renderersItemArmor.size();
      bio texture = mc.N.b(itemArmor.specialModelTexture);
      if (texture == null) {
         bio var6 = new bif(itemArmor.specialModelTexture);
         mc.N.a(itemArmor.specialModelTexture, var6);
      }

      texture = mc.N.b(itemArmor.specialModelTexture);
      if (texture != null) {
         this.modelTexture = texture.b();
      }

      IModelCustom modelArmor = StalkerModelManager.addModel("armor", itemArmor.specialModelName + "_head." + itemArmor.extension);
      this.modelHeadArmor = GL11.glGenLists(1);
      GL11.glNewList(this.modelHeadArmor, 4864);
      modelArmor.renderAll();
      GL11.glEndList();
      modelArmor = StalkerModelManager.addModel("armor", itemArmor.specialModelName + "_body." + itemArmor.extension);
      this.modelBodyArmor = GL11.glGenLists(1);
      GL11.glNewList(this.modelBodyArmor, 4864);
      modelArmor.renderAll();
      GL11.glEndList();
      modelArmor = StalkerModelManager.addModel("armor", itemArmor.specialModelName + "_rightarm." + itemArmor.extension);
      this.modelRightArmArmor = GL11.glGenLists(1);
      GL11.glNewList(this.modelRightArmArmor, 4864);
      modelArmor.renderAll();
      GL11.glEndList();
      modelArmor = StalkerModelManager.addModel("armor", itemArmor.specialModelName + "_leftarm." + itemArmor.extension);
      this.modelLeftArmArmor = GL11.glGenLists(1);
      GL11.glNewList(this.modelLeftArmArmor, 4864);
      modelArmor.renderAll();
      GL11.glEndList();
      modelArmor = StalkerModelManager.addModel("armor", itemArmor.specialModelName + "_rightleg." + itemArmor.extension);
      this.modelRightLegArmor = GL11.glGenLists(1);
      GL11.glNewList(this.modelRightLegArmor, 4864);
      modelArmor.renderAll();
      GL11.glEndList();
      modelArmor = StalkerModelManager.addModel("armor", itemArmor.specialModelName + "_leftleg." + itemArmor.extension);
      this.modelLeftLegArmor = GL11.glGenLists(1);
      GL11.glNewList(this.modelLeftLegArmor, 4864);
      modelArmor.renderAll();
      GL11.glEndList();
      this.rendererManager = rendererManager;
      this.itemArmor = itemArmor;
   }

   public void onBindTexture() {
      GL11.glBindTexture(3553, this.modelTexture);
   }

   public void onHeadRender(float frame) {
      GL11.glTranslatef(0.0F, 0.01F, 0.0F);
      GL11.glScalef(1.05F, 1.05F, 1.05F);
      if (this.modelHeadArmor != -1) {
         GL11.glCallList(this.modelHeadArmor);
      }
   }

   public void onBodyRender(float frame) {
      GL11.glTranslatef(0.0F, 0.02F, 0.0F);
      GL11.glScalef(1.03F, 1.03F, 1.03F);
      if (this.modelHeadArmor != -1) {
         GL11.glCallList(this.modelBodyArmor);
      }
   }

   public void onRightArmRender(float frame) {
      GL11.glTranslatef(0.325F, -0.15F, 0.0F);
      GL11.glScalef(1.03F, 1.03F, 1.03F);
      if (this.modelRightArmArmor != -1) {
         GL11.glCallList(this.modelRightArmArmor);
      }
   }

   public void onLeftArmRender(float frame) {
      GL11.glTranslatef(-0.325F, -0.16F, 0.0F);
      GL11.glScalef(1.03F, 1.03F, 1.03F);
      if (this.modelLeftArmArmor != -1) {
         GL11.glCallList(this.modelLeftArmArmor);
      }
   }

   public void onRightArmRender() {
      if (this.modelRightArmArmor != -1) {
         GL11.glCallList(this.modelRightArmArmor);
      }
   }

   public void onLeftArmRender() {
      if (this.modelLeftArmArmor != -1) {
         GL11.glCallList(this.modelLeftArmArmor);
      }
   }

   public void onRightLegRender(float frame) {
      GL11.glTranslatef(0.125F, -0.81F, 0.0F);
      GL11.glScalef(1.045F, 1.045F, 1.045F);
      if (this.modelRightLegArmor != -1) {
         GL11.glCallList(this.modelRightLegArmor);
      }
   }

   public void onLeftLegRender(float frame) {
      GL11.glTranslatef(-0.125F, -0.799F, -0.0022F);
      GL11.glScalef(1.035F, 1.035F, 1.035F);
      if (this.modelLeftLegArmor != -1) {
         GL11.glCallList(this.modelLeftLegArmor);
      }
   }
}
