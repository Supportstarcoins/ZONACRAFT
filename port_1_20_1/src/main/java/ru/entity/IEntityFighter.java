package ru.stalcraft.entity;

public interface IEntityFighter {
   of getTarget();

   void setTarget(of var1);

   boolean canSee(nn var1);

   void setLookPositionWithEntity(nn var1, float var2, float var3);

   float getRotationYaw();
}
