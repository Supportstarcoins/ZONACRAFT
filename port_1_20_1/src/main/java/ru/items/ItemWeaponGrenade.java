package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.stalcraft.StalkerMain;

public class ItemWeaponGrenade extends yc {
   public int index;

   public ItemWeaponGrenade(int id, int index) {
      super(id);
      this.index = index;
      String name = "Граната ВОГ-25";
      String icon = "vog";
      if (index == 1) {
         name = "Граната M381";
         icon = "m381";
      }

      super.d("stalker:" + icon);
      super.b(icon);
      super.a(StalkerMain.tab);
      GameRegistry.registerItem(this, icon);
      LanguageRegistry.addName(this, name);
   }
}
