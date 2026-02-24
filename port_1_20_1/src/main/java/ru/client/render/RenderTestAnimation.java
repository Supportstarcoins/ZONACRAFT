package ru.stalcraft.client.render;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderTestAnimation implements IItemRenderer {
   public IModelCustom model = AdvancedModelLoader.loadModel("/assets/stalker/models/weapons/xm8/xm8.obj");
   public bjo texture = new bjo("stalker:models/weapons/xm8/xm8.png");
   public atv mc = atv.w();
   public float animation;
   public float prevAnimation;

   public boolean handleRenderType(ye item, ItemRenderType type) {
      return false;
   }

   public boolean shouldUseRenderHelper(ItemRenderType type, ye item, ItemRendererHelper helper) {
      return false;
   }

   public void renderItem(ItemRenderType type, ye item, Object... data) {
   }
}
