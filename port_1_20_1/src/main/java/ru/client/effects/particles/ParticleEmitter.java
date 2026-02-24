package ru.stalcraft.client.effects.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.stalcraft.tile.IParticleEmmiter;

public abstract class ParticleEmitter {
   public atv mc;
   public final abw world;
   public final ArrayList particles;
   public final IParticleEmmiter emmiter;
   public double centerX;
   public double centerY;
   public double centerZ;
   public double renderDistanceSq;
   public double lastDistanceSq;
   protected asx boundingBox;
   private List collidingBoundingBoxes;
   private List tempList;
   public boolean isCollided = true;

   public ParticleEmitter(IParticleEmmiter emmiter) {
      this.world = emmiter.getWorld();
      this.emmiter = emmiter;
      this.boundingBox = asx.a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
      this.setCenter(emmiter.getPosX(), emmiter.getPosY(), emmiter.getPosZ());
      this.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.particles = new ArrayList();
      this.collidingBoundingBoxes = new ArrayList();
      this.tempList = new ArrayList();
      this.mc = atv.w();
      this.renderDistanceSq = 4096.0;
   }

   public final void setSize(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
      this.boundingBox.a = this.centerX + minX;
      this.boundingBox.b = this.centerY + minY;
      this.boundingBox.c = this.centerZ + minZ;
      this.boundingBox.d = this.centerX + maxX;
      this.boundingBox.e = this.centerY + maxY;
      this.boundingBox.f = this.centerZ + maxZ;
   }

   public List getCollidingBoundingBoxes(asx particleAABB) {
      this.tempList.clear();
      Iterator it = this.collidingBoundingBoxes.iterator();
      asx aabb = null;

      while (it.hasNext()) {
         aabb = (asx)it.next();
         if (particleAABB.b(aabb)) {
            this.tempList.add(aabb);
         }
      }

      return this.tempList;
   }

   public void tick() {
      if (this.world.a(this.boundingBox) != null) {
         this.collidingBoundingBoxes = this.world.a(this.boundingBox);
      }

      Iterator it = this.particles.iterator();
      Particle particle = null;

      while (it.hasNext()) {
         particle = (Particle)it.next();
         if (particle.isDead) {
            it.remove();
         } else {
            particle.tick();
         }
      }
   }

   public void updateDistance(double cameraX, double cameraY, double cameraZ) {
      double deltaX = this.centerX - cameraX;
      double deltaY = this.centerY - cameraY;
      double deltaZ = this.centerZ - cameraZ;
      this.lastDistanceSq = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
   }

   public void reset() {
      this.particles.clear();
   }

   protected void setCenter(double x, double y, double z) {
      this.centerX = x;
      this.centerY = y;
      this.centerZ = z;
   }

   public boolean isValid() {
      return true;
   }

   public asx getBoundingBox() {
      return this.boundingBox;
   }

   public boolean ignoreFrustrumCheck() {
      return false;
   }

   public void addParticle(Particle particle) {
      this.particles.add(particle);
   }

   public static void registerIcons(mt ir) {
   }

   public void onRemove() {
   }
}
