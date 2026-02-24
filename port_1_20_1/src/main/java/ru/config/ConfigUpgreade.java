package ru.stalcraft.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import ru.stalcraft.Logger;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.items.ItemPoint;

public class ConfigUpgreade {
   private static HashMap items = new HashMap();
   private static Random rand = new Random();

   public static void readConfig() {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/upgreade.txt"), "UTF-8"));
         StringBuffer buffer = new StringBuffer();
         boolean flag = false;
         String weaponRegex = null;

         while (!flag) {
            weaponRegex = e.readLine();
            if (weaponRegex == null) {
               flag = true;
            } else {
               weaponRegex = weaponRegex.trim();
               if (!weaponRegex.startsWith("//")) {
                  buffer.append(weaponRegex.split("//", 2)[0]);
               }
            }
         }

         Pattern weapon_upgrade = Pattern.compile(".*upgrade.*\\{.*");
         String itemsSplitter = "\\{";
         String parSplitter = "[\\s]*;[\\s]*";
         String config = buffer.toString().replaceAll("\n|\r", "");
         String[] splitted = config.split("\\}");
         items = new HashMap();
         String[] parameters = null;
         int id = 0;
         int itemExtends = 0;
         String declaration = null;
         ConfigUpgreade.ItemType e2 = null;

         for (int e1 = 0; e1 < splitted.length; e1++) {
            try {
               if (weapon_upgrade.matcher(splitted[e1]).matches()) {
                  e2 = ConfigUpgreade.ItemType.POINT;
               } else {
                  Logger.console("Strange string: " + splitted[e1]);
               }

               declaration = splitted[e1].split(itemsSplitter)[0];
               if (declaration.contains("extends")) {
                  try {
                     itemExtends = Integer.parseInt(declaration.split("extends")[1].trim());
                  } catch (Exception var18) {
                     var18.printStackTrace();
                  }
               }

               parameters = splitted[e1].split(itemsSplitter)[1].split(parSplitter);
               id = getInt(parameters, "item_id");
               items.put(id, new ConfigUpgreade.ItemToAdd(id, itemExtends, e2, parameters));
            } catch (Exception var19) {
               var19.printStackTrace();
               Logger.console("Error item config №= " + Integer.toString(e1) + " type=" + e2 + "!");
            }
         }

         Iterator var27 = items.entrySet().iterator();
         ConfigUpgreade.ItemToAdd var28 = null;

         while (var27.hasNext()) {
            var28 = (ConfigUpgreade.ItemToAdd)((Entry)var27.next()).getValue();

            try {
               if (var28.type != null && var28.type == ConfigUpgreade.ItemType.POINT) {
                  addItemPoint(var28);
               }
            } catch (Exception var17) {
               Logger.debug("Error occured during adding item " + var28.itemId);
               var17.printStackTrace();
            }
         }
      } catch (Exception var20) {
         var20.printStackTrace();
      }
   }

   private static void addItemPoint(ConfigUpgreade.ItemToAdd item) {
      int item_id = item.getInt("item_id");
      String icon = item.getString("item_texture");
      String name = item.getString("name");
      List description = item.getList("description");
      float damage = item.getFloat("damage_modify");
      float recoil = item.getFloat("recoil_modify");
      float spread = item.getFloat("spread_modify");
      float bulletFactor = item.getFloat("bulletFactor");
      float speedFactor = item.getFloat("speedFactor");
      float regenerationFactor = item.getFloat("regenerationFactor");
      boolean isWeapon = item.getBoolean("isWeapon");
      new ItemPoint(
         item_id,
         icon,
         name,
         description,
         isWeapon,
         isWeapon ? new float[]{damage, recoil, spread} : new float[]{bulletFactor, speedFactor, regenerationFactor}
      );
   }

   private static int getInt(String[] parameters, String name) {
      String str = getParStr(parameters, name);
      return str == null ? 0 : Integer.parseInt(str.split(":")[1].trim());
   }

   private static String getParStr(String[] parameters, String name) {
      for (int i$ = 0; i$ < parameters.length; i$++) {
         if (parameters[i$].startsWith(name + ":")) {
            return parameters[i$];
         }
      }

      return null;
   }

   private static class ItemToAdd {
      public String[] parameters;
      public int itemExtends;
      public int itemId;
      public ConfigUpgreade.ItemType type;

      public ItemToAdd(int itemId, int itemExtends, ConfigUpgreade.ItemType type, String[] parameters) {
         this.itemId = itemId;
         this.itemExtends = itemExtends;
         this.parameters = parameters;
         this.type = type;
      }

      private String getParStr(String name) {
         for (int i = 0; i < this.parameters.length; i++) {
            if (this.parameters[i].startsWith(name + ":")) {
               return this.parameters[i];
            }
         }

         return this.itemExtends != 0 && ConfigUpgreade.items.containsKey(this.itemExtends)
            ? ((ConfigUpgreade.ItemToAdd)ConfigUpgreade.items.get(this.itemExtends)).getParStr(name)
            : null;
      }

      public ArrayList getList(String name) {
         ArrayList list = new ArrayList();
         String descriptionStr = this.getString(name);
         if (!descriptionStr.isEmpty()) {
            String[] descriptionArray = descriptionStr.split("@");

            for (int i = 0; i < descriptionArray.length; i++) {
               list.add(descriptionArray[i]);
            }
         }

         return list;
      }

      public int[] getIntArray(String name) {
         new ArrayList();
         String descriptionStr = this.getString(name);
         int[] array = new int[0];
         if (!descriptionStr.isEmpty()) {
            String[] descriptionArray = descriptionStr.split(", ");
            if (descriptionArray.length > 0) {
               array = new int[descriptionArray.length];

               for (int i = 0; i < descriptionArray.length; i++) {
                  array[i] = Integer.parseInt(descriptionArray[i]);
               }
            } else {
               int value = 0;

               try {
                  value = Integer.parseInt(descriptionArray[0]);
               } catch (Exception var11) {
                  var11.printStackTrace();
               } finally {
                  int[] var10000 = new int[]{value};
               }
            }
         }

         return array;
      }

      public int getInt(String name) {
         String str = this.getParStr(name);
         return str == null ? 0 : Integer.parseInt(str.split(":")[1].trim());
      }

      public String getString(String name) {
         String str = this.getParStr(name);
         if (str == null) {
            return "";
         } else {
            String value = str.split(":", 2)[1].trim();
            return value.equals("\"\"") ? "" : value.substring(value.indexOf("\"") + 1, value.lastIndexOf("\"")).trim();
         }
      }

      public boolean getBoolean(String name) {
         String str = this.getParStr(name);
         return str == null ? false : str.split(":", 2)[1].trim().equals("true");
      }

      public float getFloat(String name) {
         String str = this.getParStr(name);
         return str == null ? 0.0F : Float.parseFloat(str.split(":")[1].replace(",", ".").trim());
      }
   }

   private static enum ItemType {
      POINT("POINT", 0);

      private static final ConfigUpgreade.ItemType[] $VALUES = new ConfigUpgreade.ItemType[]{POINT};

      private ItemType(String var1, int var2) {
      }
   }
}
