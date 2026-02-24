package ru.stalcraft.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.items.ISpecialWeight;

public class WeightMap {
   public static HashMap itemsWeight = new HashMap();

   public static void loadWeightMap() {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/weight.txt"), "UTF-8"));
         new StringBuffer();
         boolean flag = false;

         while (!flag) {
            String str = e.readLine();
            if (str == null) {
               flag = true;
            } else {
               str = str.trim();
               if (!str.startsWith("#") && !str.isEmpty()) {
                  String[] splitted = str.split("-");
                  itemsWeight.put(Integer.parseInt(splitted[0]), Float.parseFloat(splitted[1].replace(",", ".")));
               }
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   public static float getWeight(ye stack) {
      return stack.b() instanceof ISpecialWeight
         ? ((ISpecialWeight)stack.b()).getWeight(stack)
         : (
            itemsWeight.containsKey(stack.d)
               ? (Float)itemsWeight.get(stack.d) * stack.b
               : (stack.d < aqz.s.length && aqz.s[stack.d] != null ? 1 * stack.b : 0.1F * stack.b)
         );
   }

   public static float getWeight(int id) {
      return itemsWeight.containsKey(id) ? (Float)itemsWeight.get(id) : (id < aqz.s.length && aqz.s[id] != null ? 1.0F : 0.1F);
   }
}
