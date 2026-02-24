package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.StalkerMain;

public class ItemEmptyBottle extends yc {
   public ItemEmptyBottle(int par1) {
      super(par1);
      this.a(StalkerMain.tab);
      this.b("empty_bottle");
      LanguageRegistry.addName(this, "Бутылка");
   }

   public int getDamageVsEntity(nn par1Entity) {
      return 2;
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:empty_bottle");
   }
}
