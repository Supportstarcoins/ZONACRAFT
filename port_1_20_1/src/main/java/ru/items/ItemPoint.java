package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.List;
import ru.stalcraft.StalkerMain;

public class ItemPoint extends yc {
   public boolean isWeapon = true;
   public float[] values = new float[3];
   public List description;

   public ItemPoint(int id, String icon, String name, List description, boolean isWeapon, float[] values) {
      super(id);
      super.d("stalker:" + icon);
      super.b("point:" + id);
      super.a(StalkerMain.tab);
      super.setNoRepair();
      GameRegistry.registerItem(this, icon);
      LanguageRegistry.addName(this, name);
      this.description = description;
      this.isWeapon = isWeapon;
      this.values = values;
   }

   public void a(ye stack, uf player, List list, boolean par4) {
      if (this.description != null) {
         list.addAll(this.description);
      }
   }
}
