package ru.stalcraft.inventory;

import ru.stalcraft.inventory.shop.SlotShop;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemWeapon;

public class WeaponRepairContainer extends uy {
   public WeaponRepairInventory inventory;
   public int number = 0;

   public WeaponRepairContainer(uf player, int type) {
      this.inventory = new WeaponRepairInventory();
      if (type == 0) {
         for (int j = 0; j < player.bn.a.length; j++) {
            if (player.bn.a[j] != null && player.bn.a[j].b() instanceof ItemWeapon && this.number <= 5) {
               this.inventory.contents[this.number] = player.bn.a[j];
               super.a(new SlotShop(this.inventory, this.number, -68, -73 + this.number * 24, 17, 18));
               this.number++;
            }
         }
      } else {
         for (int jx = 0; jx < player.bn.a.length; jx++) {
            if (player.bn.a[jx] != null && player.bn.a[jx].b() instanceof ItemArmorArtefakt && this.number <= 5) {
               this.inventory.contents[this.number] = player.bn.a[jx];
               super.a(new SlotShop(this.inventory, this.number, 412, 181 + this.number * 24, 17, 18));
               this.number++;
            }
         }
      }
   }

   public boolean a(uf entityplayer) {
      return false;
   }

   public ye a(int par1, int par2, int par3, uf par4EntityPlayer) {
      return null;
   }
}
