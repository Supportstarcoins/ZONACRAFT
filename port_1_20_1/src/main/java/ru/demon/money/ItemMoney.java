package ru.demon.money;

import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.demon.money.client.network.ClientPacketSender;

public class ItemMoney extends yc {
   public int value;

   public ItemMoney(int id, String name, String icon, int value) {
      super(id);
      this.value = value;
      super.d("stalker:" + icon);
      super.b(icon + id);
      super.a(MoneyMod.tabMoney);
      LanguageRegistry.addName(this, name);
   }

   public void a(ye stack, abw world, nn entity, int id, boolean inUse) {
      if (!((uf)entity).bG.d) {
         if (world.I) {
            ClientPacketSender.sendAddingMoney(this.value);
         } else {
            ((uf)entity).bn.d(this.cv);
         }
      }
   }
}
