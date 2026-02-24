package ru.stalcraft.network;

public enum DebugGroup {
   WEAPONS("WEAPONS", 0),
   CLANS("CLANS", 1),
   PLAYERS_DATA("PLAYERS_DATA", 2),
   CLIENT_DATA("CLIENT_DATA", 3),
   ANOMALIES("ANOMALIES", 4),
   OTHER("OTHER", 5);

   private static final DebugGroup[] $VALUES = new DebugGroup[]{WEAPONS, CLANS, PLAYERS_DATA, CLIENT_DATA, ANOMALIES, OTHER};

   private DebugGroup(String var1, int var2) {
      PacketHandler.debugGroups.add(this);
   }
}
