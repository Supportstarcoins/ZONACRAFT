package ru.stalcraft.client.render;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.tile.ParticleDisk;
import ru.stalcraft.tile.TileEntityElectra;

public class RenderElectra extends bje {
   private bjo texture = new bjo("stalker", "textures/anomaly/electra.png");

   public void a(asp tileentity, double x, double y, double z, float frame) {
      GL11.glPushMatrix();
      atv.w().N.a(this.texture);
      TileEntityElectra tile = (TileEntityElectra)tileentity;
      GL11.glDepthMask(false);
      atv mc = atv.w();
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      GL11.glDisable(2896);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glBlendFunc(768, 1);
      x += 0.5;
      y += 0.005;
      z += 0.5;
      Iterator it = tile.particleDisk.iterator();
      bfq t = bfq.a;
      t.b();
      double fixSize = Math.sin(Math.PI / 4);

      while (it.hasNext()) {
         ParticleDisk particle = (ParticleDisk)it.next();
         float alpha = particle.prevAlpha + (particle.alpha - particle.prevAlpha) * frame;
         double rotation = (45.0 - particle.prevRotation + (particle.rotation - particle.prevRotation) * frame) * Math.PI / 180.0;
         float cos = ls.b((float)rotation);
         float sin = ls.a((float)rotation);
         double size = (particle.prevScale + (particle.scale - particle.prevScale) * frame) * fixSize;
         t.a(0.714F * alpha, 0.833F * alpha, 0.95F * alpha, 1.0F);
         t.a(x - size * cos, y, z - size * sin, 0.0, 0.0);
         t.a(x - size * sin, y, z + size * cos, 0.0, 1.0);
         t.a(x + size * cos, y, z + size * sin, 1.0, 1.0);
         t.a(x + size * sin, y, z - size * cos, 1.0, 0.0);
      }

      t.a();
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
      this.renderGlow(tile, x, y, z, frame);
   }

   public void renderGlow(TileEntityElectra tile, double x, double y, double z, float frame) {
   }
}
