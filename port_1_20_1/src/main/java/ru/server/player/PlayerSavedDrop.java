package ru.stalcraft.server.player;

import java.util.ArrayList;
import java.util.List;
import ru.stalcraft.player.PlayerUtils;

public class PlayerSavedDrop {
   public static void initDrop(uf p) {
      by tag = new by();
      mo inventory = p.bn;
      cg stacksList = new cg();
      tag.a("Slots", stacksList);

      for (int i = 0; i < inventory.j_(); i++) {
         ye stack = inventory.a(i);
         if (stack != null && (isNoDrop(stack) || checkNodropBag(p, i))) {
            by stackTag = new by();
            stackTag.a("Slot", (byte)i);
            stack.b(stackTag);
            stacksList.a(stackTag);
            inventory.a(i, null);
         }
      }

      PlayerServerInfo par5 = (PlayerServerInfo)PlayerUtils.getInfo(p);
      cg par7 = new cg();
      tag.a("StalkerInventory", par7);

      for (int ix = 0; ix < par5.stInv.j_(); ix++) {
         ye stack = par5.stInv.a(ix);
         if (stack != null && isNoDrop(stack)) {
            by stackTag = new by();
            stackTag.a("Slot", (byte)ix);
            stack.b(stackTag);
            par7.a(stackTag);
            par5.stInv.a(ix, null);
         }
      }

      by entityData = p.getEntityData();
      if (!entityData.b("PlayerPersisted")) {
         entityData.a("PlayerPersisted", new by());
      }

      entityData.l("PlayerPersisted").a("drops", tag);
   }

   private static boolean checkNodropBag(uf player, int slot) {
      ye bag = null;
      if (slot >= 9 && slot <= 12) {
         bag = player.o(2);
      }

      if (slot >= 13 && slot <= 16) {
         bag = player.o(1);
      }

      if (slot >= 17 && slot <= 22) {
         bag = player.bn.a[34];
      }

      if (slot >= 23 && slot <= 32) {
         bag = player.bn.a[35];
      }

      return bag != null && isNoDrop(bag);
   }

   public static void retrieveDrops(uf p) {
      by tag = p.getEntityData().l("PlayerPersisted").l("drops");
      if (!tag.d()) {
         List<ye> notAdded = new ArrayList<>();
         cg stacksList = tag.m("Slots");

         for (int j = 0; j < stacksList.c(); j++) {
            by stackTag = (by)stacksList.b(j);
            int slot = stackTag.c("Slot") & 255;
            ye stack = ye.a(stackTag);
            if (p.bn.a(slot) == null) {
               p.bn.a(slot, stack);
            } else {
               notAdded.add(stack);
            }
         }

         PlayerServerInfo par5 = (PlayerServerInfo)PlayerUtils.getInfo(p);
         cg par6 = tag.m("StalkerInventory");

         for (int i = 0; i < par6.c(); i++) {
            by stackTag = (by)par6.b(i);
            int slot = stackTag.c("Slot") & 255;
            ye stack = ye.a(stackTag);
            if (par5.stInv.a(slot) == null) {
               par5.stInv.a(slot, stack);
            } else {
               PlayerUtils.getTag(stack).a("stalker_slot", slot);
               notAdded.add(stack);
            }
         }

         for (ye stack : notAdded) {
            addItem(p, stack);
         }

         p.getEntityData().l("PlayerPersisted").o("drops");
      }
   }

   public static void addItem(uf player, ye stack) {
      if (!player.bn.a(stack)) {
         ss entityitem = new ss(player.q, player.u, player.v, player.w, stack);
         entityitem.b = 5;
         player.q.d(entityitem);
      }
   }

   private static void dropItem(uf p, ye stack) {
      p.a(stack, true);
   }

   public static boolean isNoDrop(ye stack) {
      return stack.e != null && stack.e.e("no_drop") != 0;
   }

   public static boolean isPersonal(ye stack) {
      return stack.e != null && stack.e.e("personal") != 0;
   }
}
