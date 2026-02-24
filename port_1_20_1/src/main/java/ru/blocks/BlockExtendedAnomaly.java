package ru.stalcraft.blocks;

import java.util.HashSet;
import ru.stalcraft.AnomalyDrop;

public abstract class BlockExtendedAnomaly extends BlockAnomaly {
   public static HashSet extendedAnomalies = new HashSet();

   public BlockExtendedAnomaly(int par1, akc par2Material, AnomalyDrop drop, String soundName, float soundChance) {
      super(par1, par2Material, drop, soundName, soundChance);
      extendedAnomalies.add(par1);
   }

   public static boolean isExtendedAnomaly(int blockID) {
      return extendedAnomalies.contains(blockID);
   }

   public static double getDistanceSq(int x, int y, int z, nn entity) {
      return entity.e(x + 0.5, y, z + 0.5);
   }
}
