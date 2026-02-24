package ru.stalcraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SmartMovingHelper {
   public final boolean isSmartmovingEnabled;
   private Class smartMovingFactoryClazz;
   private Method getInstanceMethod;
   private Class smartMovingClazz;
   private Field isFastField;

   public SmartMovingHelper() {
      if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
         boolean classNotFound = false;

         try {
            this.checkSmClasses();
            Logger.console("SmartMoving found");
         } catch (Exception var3) {
            classNotFound = true;
            Logger.console("SmartMoving not found");
         }

         this.isSmartmovingEnabled = !classNotFound;
      } else {
         this.isSmartmovingEnabled = false;
      }
   }

   @SideOnly(Side.CLIENT)
   private void checkSmClasses() throws Exception {
      this.smartMovingFactoryClazz = Class.forName("net.smart.moving.SmartMovingFactory", true, this.getClass().getClassLoader());
      this.smartMovingClazz = Class.forName("net.smart.moving.SmartMoving", true, this.getClass().getClassLoader());
      this.getInstanceMethod = this.smartMovingFactoryClazz.getDeclaredMethod("getInstance", uf.class);
      this.isFastField = this.smartMovingClazz.getDeclaredField("isFast");
   }

   public boolean isPlayerRunning(uf player) {
      return player.ai();
   }
}
