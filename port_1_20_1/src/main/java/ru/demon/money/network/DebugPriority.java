package ru.demon.money.network;

public enum DebugPriority {
   LOW("LOW", 0),
   MIDDLE("MIDDLE", 1),
   HIGH("HIGH", 2);

   private static final DebugPriority[] $VALUES = new DebugPriority[]{LOW, MIDDLE, HIGH};

   private DebugPriority(String var1, int var2) {
   }
}
