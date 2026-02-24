package ru.stalcraft.client.render;

public class RenderUtils {
   public static float interpolateRotation(float prev, float current, float frame) {
      float f3 = current - prev;

      while (f3 < -180.0F) {
         f3 += 360.0F;
      }

      while (f3 >= 180.0F) {
         f3 -= 360.0F;
      }

      return prev + frame * f3;
   }
}
