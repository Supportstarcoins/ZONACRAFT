package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.stalcraft.entity.EntityTurrel;
import ru.stalcraft.entity.EntityTurrel2;

public class ItemTurrel2 extends ItemTurrel {
   public ItemTurrel2(int par1) {
      super(par1);
      this.b("turrel2");
      this.d("stalker:turrel2");
      LanguageRegistry.addName(this, "Средняя турель");
   }

   @Override
   protected EntityTurrel getTurrel(abw world, String clanName, asx agroZone) {
      return new EntityTurrel2(world, clanName, agroZone);
   }
}
