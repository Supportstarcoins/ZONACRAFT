package ru.stalcraft.items;

public interface IArtefakt {
   boolean getImmunity(int var1);

   int getProtection(int var1);

   float getSpeedFactor();

   float getBulletDamageFactor();

   float getJumpIncrease();

   boolean getFireResistance();

   boolean getWaterWalking();

   int getFallProtection();
}
