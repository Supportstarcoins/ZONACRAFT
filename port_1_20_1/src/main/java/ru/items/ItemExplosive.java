package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.HashSet;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.entity.EntityExplosive;

public class ItemExplosive extends yc {
   private static HashSet canExplode = new HashSet();

   public ItemExplosive(int par1) {
      super(par1);
      this.a(StalkerMain.tab);
      this.b("stalker_explosive");
      this.d("stalker:explosive");
      LanguageRegistry.addName(this, "Взрывчатка");
   }

   public void applyExplosibleBlocks(HashSet blocks) {
      canExplode = blocks;
   }

   public static boolean isBlockExplosible(int id) {
      return canExplode.contains(id);
   }

   public boolean a(ye stack, uf player, abw world, int x, int y, int z, int side, float x1, float y1, float z1) {
      if (!canExplode.contains(world.a(x, y, z))) {
         return false;
      } else if (!world.I && (StalkerMain.flagManager.getLand(player.ar, x, z) != null || StalkerMain.flagManager.getFlagNearby(player.ar, x, z) != null)) {
         EntityExplosive entity = new EntityExplosive(world);
         entity.applyAttributes(side, x, y, z);
         if (side == 0) {
            y1 = (float)(y1 - 0.0225);
         }

         if (side == 1) {
            y1 = (float)(y1 + 0.0225);
         }

         if (side == 2) {
            z1 = (float)(z1 - 0.0225);
         }

         if (side == 3) {
            z1 = (float)(z1 + 0.0225);
         }

         if (side == 4) {
            x1 = (float)(x1 - 0.0225);
         }

         if (side == 5) {
            x1 = (float)(x1 + 0.0225);
         }

         entity.b(x + x1, y + y1, z + z1);
         world.d(entity);
         stack.b--;
         return true;
      } else {
         return false;
      }
   }
}
