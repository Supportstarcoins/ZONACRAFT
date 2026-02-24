package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.stalcraft.entity.EntityTurrel;
import ru.stalcraft.entity.EntityTurrel3;

public class ItemTurrel3 extends ItemTurrel {
   public ItemTurrel3(int par1) {
      super(par1);
      this.b("turrel3");
      this.d("stalker:turrel3");
      LanguageRegistry.addName(this, "Тяжелая турель");
   }

   @Override
   protected EntityTurrel getTurrel(abw world, String clanName, asx agroZone) {
      return new EntityTurrel3(world, clanName, agroZone);
   }
}
