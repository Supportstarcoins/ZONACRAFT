package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ru.stalcraft.StalkerMain;

public class ItemBolt extends yc {
   private static int nextId = 0;

   public ItemBolt(int id, String name) {
      super(id - 256);
      super.b("grenade" + ++nextId);
      super.a(StalkerMain.tab);
      super.d("stalker:bolt_icon");
      LanguageRegistry.addName(this, name);
      GameRegistry.registerItem(this, name);
   }

   public zj c_(ye par1ItemStack) {
      return zj.e;
   }

   public int d_(ye par1ItemStack) {
      return 72000;
   }

   public ye a(ye is, abw world, uf player) {
      if (player.by() == is) {
         player.a(is, this.d_(is));
      }

      return is;
   }

   public void a(ye is, abw world, uf player, int ticksRemaining) {
      if (!world.I) {
         int ticksUsed = Math.min(20, this.d_(is) - ticksRemaining);
         float throwPower = ticksUsed / 20.0F;
         EntityBolt grenade = new EntityBolt(world, player, throwPower * 3.0F, 0.0F, "bolt.obj", "bolt", 100);
         world.d(grenade);
      }
   }
}
