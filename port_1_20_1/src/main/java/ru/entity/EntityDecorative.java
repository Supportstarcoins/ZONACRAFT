package ru.stalcraft.entity;

import net.minecraftforge.client.model.IModelCustom;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.EntityModelManager;

public class EntityDecorative extends nn implements IEntityDecorative {
   public IModelCustom model = null;
   public bjo modelTexture;
   public int modelId;

   public EntityDecorative(uf builder, int modelId) {
      super(builder.q);
      super.b(builder.u, builder.v, builder.w);
      this.modelId = modelId;
      EntityModelManager entityModelManager = ClientProxy.entityModelManager;
      this.model = (IModelCustom)entityModelManager.entityModels.get(modelId);
      this.modelTexture = (bjo)entityModelManager.entityTextures.get(modelId);
   }

   @Override
   public IModelCustom getModelDecorative() {
      return this.model;
   }

   @Override
   public bjo getModelTextureDecorative() {
      return this.modelTexture;
   }

   protected void a(by tag) {
      this.modelId = tag.e("modelId");
   }

   protected void b(by tag) {
      tag.a("modelId", this.modelId);
   }

   protected void a() {
   }
}
