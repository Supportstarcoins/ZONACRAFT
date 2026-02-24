package ru.demon.money.proxy;

import cpw.mods.fml.common.FMLCommonHandler;

public class ProxyInstance {
   public IProxy proxy;

   public ProxyInstance(String clientSide, String serverSide) throws Exception {
      String side = null;
      if (FMLCommonHandler.instance().getSide().isClient()) {
         side = clientSide;
      } else {
         side = serverSide;
      }

      this.proxy = (IProxy)this.getClass().getClassLoader().loadClass(side).newInstance();
   }

   public IProxy getProxy() {
      return this.proxy;
   }
}
