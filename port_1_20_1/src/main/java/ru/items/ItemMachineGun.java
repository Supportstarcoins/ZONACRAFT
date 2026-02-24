package ru.stalcraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.StalkerMain;

public class ItemMachineGun extends zh {
   public ItemMachineGun(int par1) {
      super(par1);
      this.e(0);
      this.a(StalkerMain.tab);
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:machinegun");
   }

   @SideOnly(Side.CLIENT)
   public ms b_(int par1) {
      return super.cz;
   }

   @SideOnly(Side.CLIENT)
   public int l() {
      return 1;
   }
}
