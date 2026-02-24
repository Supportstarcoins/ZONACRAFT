package ru.stalcraft;

public class Target {
   public uf entity;
   public double startX;
   public double startY;
   public double startZ;
   public int worldID;
   public int timer = 0;

   public Target(uf entity, double x, double y, double z, int w) {
      this.entity = entity;
      this.startX = x;
      this.startY = y;
      this.startZ = z;
      this.worldID = w;
   }
}
