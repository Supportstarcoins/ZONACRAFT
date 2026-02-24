package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.stalcraft.StalkerMain;

public class ItemWeaponSight extends yc {
   public int index;

   public ItemWeaponSight(int id, int index) {
      super(id);
      this.index = index;
      super.d(1);
      String name = "Прицел ПСО";
      String icon = "pso";
      if (index == 1) {
         name = "Прицел ACOG";
         icon = "acog2x";
      } else if (index == 2) {
         name = "Прицел ПО";
         icon = "po";
      }

      super.d("stalker:" + icon);
      super.b(icon);
      super.a(StalkerMain.tab);
      GameRegistry.registerItem(this, icon);
      LanguageRegistry.addName(this, name);
   }
}
