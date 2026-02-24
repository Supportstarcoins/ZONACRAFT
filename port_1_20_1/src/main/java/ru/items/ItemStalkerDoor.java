package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.stalcraft.StalkerMain;

public class ItemStalkerDoor extends xk {
   public ItemStalkerDoor(int par1) {
      super(par1, akc.d);
      super.cw = 1;
      this.a(StalkerMain.tab);
      this.b("stalker_door");
      this.d("stalker:item_door");
      LanguageRegistry.addName(this, "Дверь");
   }

   public boolean a(ye par1ItemStack, uf par2EntityPlayer, abw par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      if (par7 != 1) {
         return false;
      } else {
         par5++;
         aqz block = StalkerMain.stalkerDoor;
         if (!par2EntityPlayer.a(par4, par5, par6, par7, par1ItemStack) || !par2EntityPlayer.a(par4, par5 + 1, par6, par7, par1ItemStack)) {
            return false;
         } else if (!block.c(par3World, par4, par5, par6)) {
            return false;
         } else {
            int i1 = ls.c((par2EntityPlayer.A + 180.0F) * 4.0F / 360.0F - 0.5) & 3;
            a(par3World, par4, par5, par6, i1, block);
            par1ItemStack.b--;
            return true;
         }
      }
   }
}
