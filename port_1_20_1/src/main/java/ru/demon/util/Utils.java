package ru.demon.util;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.Random;

public class Utils {
   public static void pickUpItem(ss ent, uf player) {
      if (!player.q.I && ent != null) {
         Random rand = new Random();
         ye itemstack = ent.d();
         int stackSize = itemstack.b;
         if (stackSize <= 0 || player.bn.a(itemstack)) {
            GameRegistry.onPickupNotification(player, ent);
            ent.q.a(ent, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.a(ent, stackSize);
            if (itemstack.b <= 0) {
               ent.x();
            }
         }
      }
   }

   public static void pickUpItemWithAchievements(ss ent, uf player) {
      if (!player.q.I && ent != null) {
         Random rand = new Random();
         ye itemstack = ent.d();
         int i = itemstack.b;
         if (i <= 0 || player.bn.a(itemstack)) {
            if (itemstack.d == aqz.O.cF) {
               player.a(kp.g);
            }

            if (itemstack.d == yc.aH.cv) {
               player.a(kp.t);
            }

            if (itemstack.d == yc.p.cv) {
               player.a(kp.w);
            }

            if (itemstack.d == yc.bq.cv) {
               player.a(kp.z);
            }

            GameRegistry.onPickupNotification(player, ent);
            ent.q.a(ent, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.a(ent, i);
            if (itemstack.b <= 0) {
               ent.x();
            }
         }
      }
   }
}
