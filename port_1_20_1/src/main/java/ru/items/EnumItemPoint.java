package ru.stalcraft.items;

public enum EnumItemPoint {
   Damage("Улучшение урона", "damage", 0);

   String name;
   String icon;
   int index;

   private EnumItemPoint(String name, String icon, int index) {
      this.name = name;
      this.icon = icon;
      this.index = index;
   }
}
