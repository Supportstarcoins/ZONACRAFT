package ru.stalcraft.client.loaders;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.techne.TechneModel;

public class StalkerModelManager {
   private HashMap models = new HashMap();
   private HashSet loadingTextures = new HashSet();
   private bim tm = atv.w().N;
   private Map mapTextureObjects;

   public StalkerModelManager() {
      Map mapTextureObjects = (Map)ReflectionHelper.getPrivateValue(bim.class, this.tm, new String[]{"mapTextureObjects", "field_110585_a", "a"});
      this.mapTextureObjects = mapTextureObjects;
   }

   public void tick() {
      Iterator it = this.loadingTextures.iterator();
      bjo resource = null;

      while (it.hasNext()) {
         resource = (bjo)it.next();
         if (this.mapTextureObjects.containsKey(resource)) {
            if (this.mapTextureObjects.get(resource) instanceof MultithreadLoadingTexture) {
               ((MultithreadLoadingTexture)this.mapTextureObjects.get(resource)).upload();
            }

            it.remove();
         }
      }
   }

   public IModelCustom getModel(String dir, String name) {
      if (!this.models.containsKey(name)) {
         this.models.put(name, null);
         new StalkerModelManager.ModelLoader(dir, name).start();
      }

      return this.models.containsKey(name) ? (IModelCustom)this.models.get(name) : null;
   }

   public void tryLoadTexture(bjo texture) {
      if (!this.loadingTextures.contains(texture) && !this.mapTextureObjects.containsKey(texture)) {
         this.loadingTextures.add(texture);
         new StalkerModelManager.TextureLoader(texture).start();
      }
   }

   public boolean tryBindTexture(bjo texture) {
      if (this.mapTextureObjects.containsKey(texture) && !this.loadingTextures.contains(texture)) {
         this.tm.a(texture);
         return true;
      } else {
         return false;
      }
   }

   public static IModelCustom addModel(String dir, String name) throws Exception {
      String resourceName = "/assets/stalker/models/" + dir + "/" + name;
      IModelCustom model = null;
      model = AdvancedModelLoader.loadModel(resourceName);
      if (model instanceof TechneModel) {
         fixModel((TechneModel)model);
      }

      return model;
   }

   private static void fixModel(bbo model) {
      for (bcu mr : model.r) {
         mr.i = !mr.i;
         mr.d += 23.4F;
      }
   }

   private class ModelLoader extends Thread {
      private String modelName;
      private String dir;

      public ModelLoader(String dir, String modelName) {
         this.modelName = modelName;
         this.dir = dir;
      }

      @Override
      public void run() {
         try {
            StalkerModelManager.this.models.put(this.modelName, StalkerModelManager.addModel(this.dir, this.modelName));
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }
   }

   private class TextureLoader extends Thread {
      private bjo resource;

      public TextureLoader(bjo texture) {
         this.resource = texture;
      }

      @Override
      public void run() {
         MultithreadLoadingTexture texture = new MultithreadLoadingTexture(this.resource);
         StalkerModelManager.this.tm.a(this.resource, texture);
      }
   }
}
