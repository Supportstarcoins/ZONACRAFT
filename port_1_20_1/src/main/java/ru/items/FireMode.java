package ru.stalcraft.items;

import org.lwjgl.input.Mouse;

public enum FireMode {
   AUTO,
   SEMIAUTO,
   BOLT;

   private static boolean isShootPermament;
   private static boolean isShootIndiviual = isShootPermament;

   public boolean isShoot(boolean par1) {
      isShootIndiviual = isShootPermament;
      isShootPermament = Mouse.isButtonDown(0);
      switch (this) {
         case AUTO:
            if (isShootPermament && !par1) {
               return true;
            }
            break;
         case SEMIAUTO:
            if (isShootPermament && !isShootIndiviual && !par1) {
               return true;
            }
            break;
         case BOLT:
            if (isShootPermament && !isShootIndiviual) {
               return true;
            }
      }

      return false;
   }
}
