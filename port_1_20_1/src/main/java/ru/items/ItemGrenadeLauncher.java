package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.stalcraft.StalkerMain;

public class ItemGrenadeLauncher extends yc {
   public int index;

   public ItemGrenadeLauncher(int id, int index) {
      super(id);
      super.d(1);
      this.index = index;
      String unlocName = "gp25";
      String locName = "ГП-25 «Костёр»";
      if (index == 1) {
         unlocName = "m203";
         locName = "M203";
      }

      super.d("stalker:" + unlocName);
      super.b(unlocName);
      super.a(StalkerMain.tab);
      GameRegistry.registerItem(this, unlocName);
      LanguageRegistry.addName(this, locName);
   }
}
