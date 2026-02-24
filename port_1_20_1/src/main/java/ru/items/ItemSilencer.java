package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import ru.stalcraft.StalkerMain;

public class ItemSilencer extends yc {
   public int index;

   public ItemSilencer(int id, int index, String name) {
      super(id);
      this.index = index;
      super.d(1);
      super.d("stalker:silencer");
      super.b("silencer" + id);
      super.a(StalkerMain.tab);
      GameRegistry.registerItem(this, "silencer" + id);
   }
}
