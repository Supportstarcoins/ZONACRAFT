package ru.stalcraft.ejection;

public interface IEjectionManager {
   int getLastEjectionId();

   boolean hasEjection();

   Ejection getEjection();

   void setEjection(Ejection var1);

   void setLastEjectionId(int var1);

   void tick();
}
