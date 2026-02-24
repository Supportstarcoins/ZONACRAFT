package ru.stalcraft.clans;

import java.util.List;

public interface IFlagManager {
   IFlag getFlagNearby(int var1, int var2, int var3);

   List getClanFlags(IClan var1);

   void onFlagPlace(abw var1, String var2, int var3, int var4, int var5, jv var6);

   void onBlockFlagRemoved(int var1, int var2, int var3, int var4);

   void tryJoinClanLand(uf var1, int var2, int var3, int var4);

   IFlagsLand getLand(int var1, int var2, int var3);

   boolean canPlaceFlagHere(int var1, int var2, int var3);

   void addFlagToCheck(int var1, int var2, int var3, int var4);
}
