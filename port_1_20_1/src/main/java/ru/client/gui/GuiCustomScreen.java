package ru.stalcraft.client.gui;

public class GuiCustomScreen extends awe {
   public void drawTexturedModalRect(int width, int weight, int minU, int minV, int maxU, int maxV, int textureSize, double scale) {
      double d = 1.0 / textureSize;
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(width + 0, weight + maxV * scale, super.n, (minU + 0) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + maxV * scale, super.n, (minU + maxU) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + 0, super.n, (minU + maxU) * d, (minV + 0) * d);
      tessellator.a(width + 0, weight + 0, super.n, (minU + 0) * d, (minV + 0) * d);
      tessellator.a();
   }
}
