package ru.stalcraft.server.configs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import ru.stalcraft.Logger;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.server.CommonProxy;

public class NewCaseConfig {
   private static HashSet sounds = new HashSet();
   private static HashMap items;
   private static int nextArmorId = 0;
   private static int nextArmorRenderId = 125;
   private static ArrayList zombieWeapons = new ArrayList();
   private static Random rand = new Random();

   public static HashSet getSounds() {
      return sounds;
   }

   public static ye getRandomZombieWeapon() {
      return zombieWeapons.size() == 0 ? null : new ye((Integer)zombieWeapons.get(rand.nextInt(zombieWeapons.size())), 1, 0);
   }

   public static void readConfig() {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/shop/newCase.case"), "UTF-8"));
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

         Pattern var26 = Pattern.compile(".*content.*\\{.*");
         String itemsSplitter = "\\{";
         String parSplitter = "[\\s]*;[\\s]*";
         String config = buffer.toString().replaceAll("\n|\r", "");
         String[] splitted = config.split("\\}");
         items = new HashMap();
         String[] parameters = null;
         int id = 0;
         int itemExtends = 0;
         String declaration = null;
         NewCaseConfig.ItemType e2 = null;

         for (int e1 = 0; e1 < splitted.length; e1++) {
            try {
               if (var26.matcher(splitted[e1]).matches()) {
                  e2 = NewCaseConfig.ItemType.CONTENT;
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
               id = getInt(parameters, "number");
               items.put(id, new NewCaseConfig.ItemToAdd(id, itemExtends, e2, parameters));
            } catch (Exception var19) {
               var19.printStackTrace();
               Logger.console("Error item config №= " + Integer.toString(e1) + " type=" + e2 + "!");
            }
         }

         Iterator var27 = items.entrySet().iterator();
         NewCaseConfig.ItemToAdd item = null;

         while (var27.hasNext()) {
            item = (NewCaseConfig.ItemToAdd)((Entry)var27.next()).getValue();

            try {
               if (item.type == NewCaseConfig.ItemType.CONTENT) {
                  addContent(item);
               }
            } catch (Exception var17) {
               Logger.debug("Error occured during adding item " + item.itemId);
               var17.printStackTrace();
            }
         }
      } catch (Exception var20) {
         var20.printStackTrace();
      }
   }

   public static void addContent(NewCaseConfig.ItemToAdd content) {
      int id = content.getInt("item_id");
      int stackSize = content.getInt("stackSize");
      CommonProxy.serverShopData.addNewCaseContent(id, stackSize);
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
      public NewCaseConfig.ItemType type;

      public ItemToAdd(int itemId, int itemExtends, NewCaseConfig.ItemType type, String[] parameters) {
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

         return this.itemExtends != 0 && NewCaseConfig.items.containsKey(this.itemExtends)
            ? ((NewCaseConfig.ItemToAdd)NewCaseConfig.items.get(this.itemExtends)).getParStr(name)
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
      CONTENT("CONTENT", 0),
      BULLET("BULLET", 1),
      GRENADE("GRENADE", 2),
      ARTEFAKT("ARTEFAKT", 3),
      ARMOR("ARMOR", 4),
      BLOCK("BLOCK", 5);

      private static final NewCaseConfig.ItemType[] $VALUES = new NewCaseConfig.ItemType[]{CONTENT, BULLET, GRENADE, ARTEFAKT, ARMOR, BLOCK};

      private ItemType(String var1, int var2) {
      }
   }
}
