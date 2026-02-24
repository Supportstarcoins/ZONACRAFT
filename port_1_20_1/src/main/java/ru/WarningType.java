package ru.stalcraft;

public enum WarningType {
   ABUSE_OF_AUTHORITY("ABUSE_OF_AUTHORITY", 0),
   INVALID_OPERATION("INVALID_OPERATION", 1),
   INVALID_TARGET("INVALID_TARGET", 2),
   INVALID_USER("INVALID_USER", 3);

   private static final WarningType[] $VALUES = new WarningType[]{ABUSE_OF_AUTHORITY, INVALID_OPERATION, INVALID_TARGET, INVALID_USER};

   private WarningType(String var1, int var2) {
   }
}
