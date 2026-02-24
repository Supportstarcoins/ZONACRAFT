package ru.stalcraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Config {
   public static int carouselDamage;
   public static int electraDamage;
   public static int kisselDamage;
   public static int coachDamage;
   public static int steamDamage;
   public static int trampolineDamage;
   public static int webDamage;
   public static int ejectionDamage;
   public static String blackHoleDrop;
   public static String carouselDrop;
   public static String coachDrop;
   public static String electraDrop;
   public static String kisselDrop;
   public static String lighterDrop;
   public static String steamDrop;
   public static String trampolineDrop;
   public static int turrel1Damage;
   public static int turrel1Cooldown;
   public static int turrel1Health;
   public static int turrel2Damage;
   public static int turrel2Cooldown;
   public static int turrel2Health;
   public static int turrel3Damage;
   public static int turrel3Cooldown;
   public static int turrel3Health;
   public static HashSet ejectionSavers;
   public static String proxyDir;
   public static boolean isServer;

   public static void readConfigProxy() {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/proxy.txt"), "UTF-8"));
         new StringBuffer();
         boolean flag = false;

         for (String parSplitter = null; !flag; flag = true) {
            proxyDir = e.readLine();
         }

         e.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   public static void readConfig() {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/config.txt"), "UTF-8"));
         StringBuffer buffer = new StringBuffer();
         boolean flag = false;

         while (!flag) {
            String parSplitter = e.readLine();
            if (parSplitter == null) {
               flag = true;
            } else {
               parSplitter = parSplitter.trim();
               if (!parSplitter.startsWith("//")) {
                  buffer.append(parSplitter.split("//", 2)[0]);
               }
            }
         }

         String parSplitter = "[\\s]*;[\\s]*";
         String config = buffer.toString().replaceAll("\n|\r", "");

         try {
            String[] e1 = config.split(parSplitter);
            carouselDamage = getInt(e1, "carousel_damage");
            electraDamage = getInt(e1, "electra_damage");
            kisselDamage = getInt(e1, "kissel_damage");
            coachDamage = getInt(e1, "coach_damage");
            steamDamage = getInt(e1, "steam_damage");
            trampolineDamage = getInt(e1, "trampoline_damage");
            webDamage = getInt(e1, "web_damage");
            ejectionDamage = getInt(e1, "ejection_damage");
            blackHoleDrop = getString(e1, "blackhole_drop");
            carouselDrop = getString(e1, "carousel_drop");
            coachDrop = getString(e1, "coach_drop");
            electraDrop = getString(e1, "electra_drop");
            kisselDrop = getString(e1, "kissel_drop");
            lighterDrop = getString(e1, "lighter_drop");
            steamDrop = getString(e1, "steam_drop");
            trampolineDrop = getString(e1, "trampoline_drop");
            turrel1Damage = getInt(e1, "turrel1_damage");
            turrel1Cooldown = getInt(e1, "turrel1_cooldown");
            turrel1Health = getInt(e1, "turrel1_health");
            turrel2Damage = getInt(e1, "turrel2_damage");
            turrel2Cooldown = getInt(e1, "turrel2_cooldown");
            turrel2Health = getInt(e1, "turrel2_health");
            turrel3Damage = getInt(e1, "turrel3_damage");
            turrel3Cooldown = getInt(e1, "turrel3_cooldown");
            turrel3Health = getInt(e1, "turrel3_health");
            String ejectionSaversStr = getString(e1, "ejection_savers");
            String destroyable = getString(e1, "destroyable_blocks");
            if (!destroyable.isEmpty()) {
               String[] splittedHStr = destroyable.split(",");

               for (String str : splittedHStr) {
                  StalkerMain.destroyableBlocks.add(Integer.parseInt(str));
               }
            }

            ejectionSavers = new HashSet();
            if (!ejectionSaversStr.isEmpty()) {
               String[] splittedHStr = ejectionSaversStr.split(",");

               for (String str : splittedHStr) {
                  ejectionSavers.add(Integer.parseInt(str));
               }
            }

            e.close();
         } catch (Exception var131) {
            var131.printStackTrace();
         }
      } catch (Exception var141) {
         var141.printStackTrace();
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
      String toFound = name + ":";

      for (String str : parameters) {
         if (str.startsWith(toFound)) {
            return str;
         }
      }

      return null;
   }
}
