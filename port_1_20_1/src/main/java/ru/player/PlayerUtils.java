package ru.stalcraft.player;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import ru.stalcraft.Logger;
import ru.stalcraft.inventory.StalkerInventory;

public class PlayerUtils {
   private static final int[] noteIds = new int[]{26978, 26979, 26977, 26976, 26974};
   private static final int[] noteValues = new int[]{1000, 500, 100, 50, 10};
   private static Class classPlayerInfoServer;
   private static Class classPlayerInfoClient;

   public static void createInfo(uf par1) {
      try {
         Class par2 = null;
         if (par1.q.I) {
            par2 = classPlayerInfoClient;
         } else {
            par2 = classPlayerInfoServer;
         }

         if (par2 == null) {
            throw new RuntimeException("[STALKER][" + (par1.q.I ? Side.CLIENT : Side.SERVER) + "] Not is loaded player info!!!!!");
         }

         Constructor par3 = par2.getConstructor(uf.class);

         try {
            Object e = par3.newInstance(par1);
         } catch (InstantiationException var4) {
            var4.printStackTrace();
         } catch (IllegalAccessException var5) {
            var5.printStackTrace();
         } catch (IllegalArgumentException var6) {
            var6.printStackTrace();
         } catch (InvocationTargetException var7) {
            var7.printStackTrace();
         }
      } catch (NoSuchMethodException var8) {
         var8.printStackTrace();
      } catch (SecurityException var9) {
         var9.printStackTrace();
      }
   }

   public static void registerPlayerInfo(Class par1, Side par2) {
      if (par2.isClient()) {
         classPlayerInfoClient = par1;
      } else {
         classPlayerInfoServer = par1;
      }
   }

   public static PlayerInfo getInfo(uf player) {
      try {
         return ((PlayerStalkerCapabilities)player.bG).getInfo();
      } catch (Exception var2) {
         return null;
      }
   }

   public static boolean hasItem(uf p, int id) {
      return p.bn.e(id) || getInfo(p).stInv.hasItem(id);
   }

   public static int countItems(uf p, int itemID) {
      int count = 0;

      for (ye i$ : p.bn.a) {
         if (i$ != null && i$.d == itemID) {
            count += i$.b;
         }
      }

      for (ye i$x : p.bn.b) {
         if (i$x != null && i$x.d == itemID) {
            count += i$x.b;
         }
      }

      StalkerInventory var8 = getInfo(p).stInv;

      for (ye stack : var8.mainInventory) {
         if (stack != null && stack.d == itemID) {
            count += stack.b;
         }
      }

      return count;
   }

   public static int consumeItems(uf p, int itemID, int maxCount, boolean consumeNotMax) {
      if (!consumeNotMax && countItems(p, itemID) < maxCount) {
         return 0;
      } else {
         byte consumed = 0;
         int consumed1 = consumed + consumeInStackArray(p.bn.a, maxCount - consumed, itemID);
         consumed1 += consumeInStackArray(p.bn.b, maxCount - consumed1, itemID);
         consumed1 += consumeInStackArray(getInfo(p).stInv.mainInventory, maxCount - consumed1, itemID);
         if (!p.q.I) {
            jv playerMP = (jv)p;
            playerMP.a(p.bo);
         }

         return consumed1;
      }
   }

   public static void addItem(uf player, ye stack) {
      if ((player.aN() <= 0.0F || player.bn.a(stack) || getInfo(player).stInv.addItemStackToInventory(stack)) && !player.q.I) {
         ss entityitem = new ss(player.q, player.u, player.v, player.w, stack);
         entityitem.b = 5;
         player.q.d(entityitem);
      }
   }

   public static void addMoney(uf player, int sum) {
      for (int i = 0; i < noteIds.length; i++) {
         if (yc.g[noteIds[i]] != null) {
            int notesCount = sum / noteValues[i];
            sum -= notesCount * noteValues[i];

            while (notesCount > 0) {
               int stackSize = notesCount % 64;
               if (stackSize == 0) {
                  stackSize = 64;
               }

               addItem(player, new ye(noteIds[i], stackSize, 0));
               notesCount -= stackSize;
            }
         }
      }

      if (sum > 0) {
         Logger.console("[WARNING] " + sum + " $ remaining for player " + player.bu);
      }
   }

   private static int consumeInStackArray(ye[] stacks, int maxCount, int itemID) {
      int consumed = 0;
      int slot = -1;

      while (consumed < maxCount) {
         if (++slot >= stacks.length) {
            break;
         }

         ye stack = stacks[slot];
         if (stack != null && stack.d == itemID) {
            if (stack.b <= maxCount - consumed) {
               consumed += stack.b;
               stacks[slot] = null;
            } else {
               stack.b -= maxCount - consumed;
               consumed = maxCount;
            }
         }
      }

      return consumed;
   }

   public static by getTag(ye stack) {
      if (!stack.p()) {
         stack.d(new by());
      }

      return stack.q();
   }
}
