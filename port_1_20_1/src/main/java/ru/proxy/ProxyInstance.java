package ru.stalcraft.proxy;

import cpw.mods.fml.common.FMLCommonHandler;

public final class ProxyInstance {
   private final IProxy proxyInstance;

   public ProxyInstance(String clientSide, String serverSide) {
      IProxy proxy = null;

      try {
         if (FMLCommonHandler.instance().getSide().isClient()) {
            proxy = (IProxy)this.getClass().getClassLoader().loadClass(clientSide).newInstance();
         } else {
            proxy = (IProxy)this.getClass().getClassLoader().loadClass(serverSide).newInstance();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      this.proxyInstance = proxy;
   }

   public IProxy getProxy() {
      return this.proxyInstance;
   }
}
