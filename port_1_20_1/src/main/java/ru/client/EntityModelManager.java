package ru.stalcraft.client;

import java.util.HashMap;
import net.minecraftforge.client.model.IModelCustom;

public class EntityModelManager {
   public HashMap entityModels = new HashMap();
   public HashMap entityTextures = new HashMap();

   public void addEntityModel(int modelId, IModelCustom model, bjo modelTexture) {
      this.entityModels.put(modelId, model);
      this.entityTextures.put(modelId, modelTexture);
   }
}
