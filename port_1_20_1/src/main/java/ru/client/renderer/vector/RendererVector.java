package ru.stalcraft.client.renderer.vector;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.vector.RayTracingVector;

public class RendererVector implements IRendererVector {
   public RayTracingVector vector;

   public RendererVector(RayTracingVector vector) {
      this.vector = vector;
   }

   @Override
   public void renderer(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
      if (this.vector.isRenderer) {
         bfq tessellator = bfq.a;
         GL11.glPushMatrix();
         GL11.glTranslated(this.vector.posX, this.vector.posY, this.vector.posZ);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glDisable(3553);
         ClientProxy.modelCube.renderAll();
         GL11.glEnable(3553);
         GL11.glPopMatrix();
      }
   }
}
