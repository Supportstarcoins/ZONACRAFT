package ru.stalcraft.tile;

import cpw.mods.fml.common.FMLCommonHandler;
import java.lang.reflect.Method;

public final class ElectraClientFxDispatcher {
   private static final String HOOKS_CLASS_NAME = "ru.stalcraft.client.ElectraClientFxHooks";
   private static Class hooksClass;
   private static Method setSplashMethod;
   private static Method playHitSoundMethod;
   private static Method spawnDynamicLightMethod;
   private static Method sendElectraAttackPlayerMethod;
   private static Method getEmitterClassMethod;

   private ElectraClientFxDispatcher() {
   }

   public static void setSplash(Object particleEmitter) {
      invokeVoid(getSetSplashMethod(), particleEmitter);
   }

   public static void playHitSound(float x, float y, float z) {
      invokeVoid(getPlayHitSoundMethod(), x, y, z);
   }

   public static void spawnDynamicLight(TileEntityElectra tileEntityElectra) {
      invokeVoid(getSpawnDynamicLightMethod(), tileEntityElectra);
   }

   public static void sendElectraAttackPlayer() {
      invokeVoid(getSendElectraAttackPlayerMethod());
   }

   public static Class getEmitterClass() {
      Method method = getGetEmitterClassMethod();
      if (method == null) {
         return null;
      }

      try {
         return (Class)method.invoke(null);
      } catch (Exception var2) {
         return null;
      }
   }

   private static boolean isClientSide() {
      return FMLCommonHandler.instance().getEffectiveSide().isClient();
   }

   private static void invokeVoid(Method method, Object... args) {
      if (!isClientSide() || method == null) {
         return;
      }

      try {
         method.invoke(null, args);
      } catch (Exception var3) {
      }
   }

   private static Class getHooksClass() {
      if (!isClientSide()) {
         return null;
      }

      if (hooksClass == null) {
         try {
            hooksClass = Class.forName(HOOKS_CLASS_NAME);
         } catch (ClassNotFoundException var1) {
            return null;
         }
      }

      return hooksClass;
   }

   private static Method getSetSplashMethod() {
      if (setSplashMethod == null) {
         setSplashMethod = findMethod("setSplash", Object.class);
      }

      return setSplashMethod;
   }

   private static Method getPlayHitSoundMethod() {
      if (playHitSoundMethod == null) {
         playHitSoundMethod = findMethod("playHitSound", Float.TYPE, Float.TYPE, Float.TYPE);
      }

      return playHitSoundMethod;
   }

   private static Method getSpawnDynamicLightMethod() {
      if (spawnDynamicLightMethod == null) {
         spawnDynamicLightMethod = findMethod("spawnDynamicLight", TileEntityElectra.class);
      }

      return spawnDynamicLightMethod;
   }

   private static Method getSendElectraAttackPlayerMethod() {
      if (sendElectraAttackPlayerMethod == null) {
         sendElectraAttackPlayerMethod = findMethod("sendElectraAttackPlayer");
      }

      return sendElectraAttackPlayerMethod;
   }

   private static Method getGetEmitterClassMethod() {
      if (getEmitterClassMethod == null) {
         getEmitterClassMethod = findMethod("getEmitterClass");
      }

      return getEmitterClassMethod;
   }

   private static Method findMethod(String name, Class... parameterTypes) {
      Class clazz = getHooksClass();
      if (clazz == null) {
         return null;
      }

      try {
         return clazz.getMethod(name, parameterTypes);
      } catch (Exception var4) {
         return null;
      }
   }
}
