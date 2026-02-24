package ru.stalcraft.clans;

public interface IClan {
   ClanMember getClanMember(uf var1);

   ISpecialClan getSpecialClan();

   void withdraw(uf var1);

   boolean isClanEnemy(IClan var1);

   String getName();

   int getMaxLandsCount();

   boolean isAdminClan();

   void addReputation(int var1);
}
