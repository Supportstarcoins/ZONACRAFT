package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.stalcraft.entity.EntityTurrel;
import ru.stalcraft.entity.EntityTurrel1;

public class ItemTurrel1 extends ItemTurrel {
   public ItemTurrel1(int par1) {
      super(par1);
      this.b("turrel1");
      this.d("stalker:turrel1");
      LanguageRegistry.addName(this, "Легкая турель");
   }

   @Override
   protected EntityTurrel getTurrel(abw world, String clanName, asx agroZone) {
      return new EntityTurrel1(world, clanName, agroZone);
   }
}
