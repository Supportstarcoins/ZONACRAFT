package ru.stalcraft.client.render;

import org.lwjgl.opengl.GL11;

public class RenderBlockReed extends bje {
   public static bjo texture = new bjo("stalker:textures/kamish.png");

   public void a(asp tile, double x, double y, double z, float frame) {
      GL11.glPushMatrix();
      GL11.glBlendFunc(770, 771);
      atv.w().N.a(texture);
      att.a();
      GL11.glDepthMask(false);
      GL11.glTranslated(x + 0.5, y + 0.95, z + 0.5);
      bfq t = bfq.a;
      t.b();
      t.b(0.0F, 1.0F, 0.0F);
      t.a(-1.0, 1.0, 0.0, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.0, 0.0, 1.0);
      t.a(1.0, -1.0, 0.0, 1.0, 1.0);
      t.a(1.0, 1.0, 0.0, 1.0, 0.0);
      t.a();
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      t.b();
      t.b(0.0F, 1.0F, 0.0F);
      t.a(-1.0, 1.0, 0.0, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.0, 0.0, 1.0);
      t.a(1.0, -1.0, 0.0, 1.0, 1.0);
      t.a(1.0, 1.0, 0.0, 1.0, 0.0);
      t.a();
      GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      t.b();
      t.b(0.0F, 1.0F, 0.0F);
      t.a(-1.0, 1.0, 0.0, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.0, 0.0, 1.0);
      t.a(1.0, -1.0, 0.0, 1.0, 1.0);
      t.a(1.0, 1.0, 0.0, 1.0, 0.0);
      t.a();
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      t.b();
      t.b(0.0F, 1.0F, 0.0F);
      t.a(-1.0, 1.0, 0.0, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.0, 0.0, 1.0);
      t.a(1.0, -1.0, 0.0, 1.0, 1.0);
      t.a(1.0, 1.0, 0.0, 1.0, 0.0);
      t.a();
      att.b();
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }
}
