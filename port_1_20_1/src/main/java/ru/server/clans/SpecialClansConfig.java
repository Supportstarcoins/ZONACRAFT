package ru.stalcraft.server.clans;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import ru.stalcraft.StalkerMain;

public class SpecialClansConfig {
   public HashMap specialClans = new HashMap();

   public void readConfig() {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/special_clans.txt"), "UTF-8"));
         StringBuffer buffer = new StringBuffer();
         boolean flag = false;

         while (!flag) {
            String itemsSplitter = e.readLine();
            if (itemsSplitter == null) {
               flag = true;
            } else {
               itemsSplitter = itemsSplitter.trim();
               if (!itemsSplitter.startsWith("//")) {
                  buffer.append(itemsSplitter.split("//", 2)[0]);
               }
            }
         }

         String itemsSplitter = "\\{";
         String parSplitter = "[\\s]*;[\\s]*";
         String config = buffer.toString().replaceAll("\n|\r", "");
         String[] splitted = config.split("\\}");

         for (String str : splitted) {
            try {
               String[] e1 = str.split(itemsSplitter)[1].split(parSplitter);
               String name = getString(e1, "name");
               int minX = getInt(e1, "minX");
               int minZ = getInt(e1, "minZ");
               int maxX = getInt(e1, "maxX");
               int maxZ = getInt(e1, "maxZ");
               int maxRepTimer = getInt(e1, "max_reputation_timer");
               int baseSalary = getInt(e1, "base_salary");
               int salaryReputationFactor = getInt(e1, "salary_reputation_factor");
               this.specialClans.put(name, new SpecialClanFields(name, minX, minZ, maxX, maxZ, maxRepTimer, baseSalary, salaryReputationFactor));
            } catch (Exception var211) {
               var211.printStackTrace();
            }
         }
      } catch (Exception var221) {
         var221.printStackTrace();
      }
   }

   private static int getInt(String[] parameters, String name) {
      String str = getParStr(parameters, name);
      return str == null ? 0 : Integer.parseInt(str.split(":")[1].trim());
   }

   private static String getString(String[] parameters, String name) {
      String str = getParStr(parameters, name);
      if (str == null) {
         return "";
      } else {
         String value = str.split(":", 2)[1].trim();
         return value.equals("\"\"") ? "" : value.split("\"")[1];
      }
   }

   private static String getParStr(String[] parameters, String name) {
      for (int i$ = 0; i$ < parameters.length; i$++) {
         if (parameters[i$].startsWith(name + ":")) {
            return parameters[i$];
         }
      }

      return null;
   }
}
