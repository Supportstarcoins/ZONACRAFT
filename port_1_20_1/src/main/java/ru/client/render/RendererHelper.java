package ru.stalcraft.client.render;

import org.lwjgl.opengl.GL11;

public class RendererHelper extends att {
   public static void b() {
      GL11.glEnable(2896);
      GL11.glEnable(16384);
      GL11.glEnable(16385);
      GL11.glEnable(2903);
      GL11.glColorMaterial(1032, 5634);
      float f = 0.4F;
      float f1 = 0.6F;
      float f2 = 0.0F;
      GL11.glLight(16384, 4611, a(b.c, b.d, b.e, 0.0));
      GL11.glLight(16384, 4609, a(f1, f1, f1, 1.0F));
      GL11.glLight(16384, 4608, a(0.0F, 0.0F, 0.0F, 1.0F));
      GL11.glLight(16384, 4610, a(f2, f2, f2, 1.0F));
      GL11.glLight(16385, 4611, a(c.c, c.d, c.e, 0.0));
      GL11.glLight(16385, 4609, a(f1, f1, f1, 1.0F));
      GL11.glLight(16385, 4608, a(0.0F, 0.0F, 0.0F, 1.0F));
      GL11.glLight(16385, 4610, a(f2, f2, f2, 1.0F));
      GL11.glShadeModel(7424);
      GL11.glLightModel(2899, a(f, f, f, 1.0F));
   }
}
