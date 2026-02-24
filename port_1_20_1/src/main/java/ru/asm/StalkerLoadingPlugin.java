package ru.stalcraft.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import java.util.Map;

@MCVersion("1.6.4")
public class StalkerLoadingPlugin implements IFMLCallHook, IFMLLoadingPlugin {
   public String[] getLibraryRequestClass() {
      return null;
   }

   public String[] getASMTransformerClass() {
      return new String[]{StalkerTransformer.class.getName()};
   }

   public String getModContainerClass() {
      return null;
   }

   public String getSetupClass() {
      return StalkerLoadingPlugin.class.getName();
   }

   public void injectData(Map data) {
   }

   public Void call() throws Exception {
      return null;
   }
}
