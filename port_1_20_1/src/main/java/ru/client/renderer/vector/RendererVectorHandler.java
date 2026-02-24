package ru.stalcraft.client.renderer.vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.stalcraft.vector.RayTracingVector;
import ru.stalcraft.world.WorldManager;

public class RendererVectorHandler {
   public List<RendererVector> rendererVectors = new ArrayList<>();
   public WorldManager worldManager;

   public RendererVectorHandler(WorldManager worldManager) {
      this.worldManager = worldManager;
   }

   public void renderRayTracingVectors() {
      Iterator<RayTracingVector> it = this.worldManager.hitVectors.iterator();
      RayTracingVector vector = null;

      while (it.hasNext()) {
         vector = it.next();
         this.rendererVectors.add(new RendererVector(vector));
      }

      Iterator<RendererVector> it1 = this.rendererVectors.iterator();

      while (it1.hasNext()) {
         RendererVector rendererVector = it1.next();
         if (!rendererVector.vector.isDead) {
            RayTracingVector var5 = rendererVector.vector;
         } else {
            it1.remove();
         }
      }
   }
}
