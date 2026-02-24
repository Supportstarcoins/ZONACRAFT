package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemSilencerStandart extends ItemSilencer {
   public ItemSilencerStandart(int id, int index, String name) {
      super(id, index, name);
      LanguageRegistry.addName(this, name);
   }
}
