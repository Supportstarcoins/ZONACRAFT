package ru.stalcraft.clans;

public enum ClanRank {
   MEMBER("MEMBER", 0),
   OFFICER("OFFICER", 1),
   LEADER("LEADER", 2);

   private static final ClanRank[] $VALUES = new ClanRank[]{MEMBER, OFFICER, LEADER};

   private ClanRank(String var1, int var2) {
   }
}
