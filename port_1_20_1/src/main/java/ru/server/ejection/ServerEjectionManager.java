package ru.stalcraft.server.ejection;

import net.minecraftforge.common.DimensionManager;
import ru.stalcraft.ejection.Ejection;
import ru.stalcraft.ejection.IEjectionManager;
import ru.stalcraft.server.ServerTicker;

public class ServerEjectionManager implements IEjectionManager {
   private ServerEjection ejection = null;
   private int lastEjectionId = -1;
   private static double requiredRand = 5.555555555555556E-5;
   private boolean randomStart = true;

   @Override
   public void tick() {
      if (ServerTicker.tickId % 18L == 0L) {
         js w = DimensionManager.getWorld(0);
         if (this.randomStart && this.ejection == null && w.J() % 24000L < 12000L && w.s.nextDouble() < requiredRand) {
            new ServerEjection().start();
         }
      }

      if (this.ejection != null) {
         this.ejection.tick();
      }
   }

   @Override
   public int getLastEjectionId() {
      return this.lastEjectionId;
   }

   @Override
   public boolean hasEjection() {
      return this.ejection != null;
   }

   @Override
   public Ejection getEjection() {
      return this.ejection;
   }

   @Override
   public void setEjection(Ejection par1) {
      this.ejection = (ServerEjection)par1;
   }

   @Override
   public void setLastEjectionId(int par1) {
      this.lastEjectionId = par1;
   }

   public void setRandomStart(boolean par1) {
      this.randomStart = par1;
   }
}
