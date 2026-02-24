package ru.stalcraft.clans;

public class ClanMember {
   public final String username;
   public ClanRank rank;
   public int loyalePoint;

   public ClanMember(String username, ClanRank rank) {
      this.username = username;
      this.rank = rank;
   }

   public ClanRank getRank() {
      return this.rank;
   }
}
