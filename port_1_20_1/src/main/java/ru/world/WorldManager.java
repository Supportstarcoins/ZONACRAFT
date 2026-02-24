package ru.stalcraft.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.stalcraft.vector.RayTracingVector;

public class WorldManager {
   public List<RayTracingVector> hitVectors = new ArrayList<>();

   public void onUpdate() {
      Iterator<RayTracingVector> it = this.hitVectors.iterator();

      while (it.hasNext()) {
         RayTracingVector hitVector = it.next();
         if (hitVector.isDead) {
            it.remove();
         } else {
            hitVector.createHitVector();
         }
      }
   }
}
