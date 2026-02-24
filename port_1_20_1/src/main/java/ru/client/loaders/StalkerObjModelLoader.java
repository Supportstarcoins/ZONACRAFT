package ru.stalcraft.client.loaders;

import java.net.URL;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.IModelCustomLoader;
import net.minecraftforge.client.model.ModelFormatException;

public class StalkerObjModelLoader implements IModelCustomLoader {
   private static final String[] types = new String[]{"obj"};

   public String getType() {
      return "OBJ model";
   }

   public String[] getSuffixes() {
      return types;
   }

   public IModelCustom loadInstance(String resourceName, URL resource) throws ModelFormatException {
      return new StalkerWavefrontObject(resourceName, resource);
   }
}
