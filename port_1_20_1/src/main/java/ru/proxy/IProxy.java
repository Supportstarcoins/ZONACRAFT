package ru.stalcraft.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import java.io.File;
import ru.stalcraft.ejection.IEjectionManager;

public interface IProxy {
   void preInit(FMLPreInitializationEvent var1);

   void init(FMLInitializationEvent var1);

   void postInit(FMLPostInitializationEvent var1);

   File getMinecraftDir();

   IEjectionManager getEjectionManager();

   boolean isRemote();
}
