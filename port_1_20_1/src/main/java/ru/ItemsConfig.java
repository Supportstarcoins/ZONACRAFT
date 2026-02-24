package ru.stalcraft;

import cpw.mods.fml.common.FMLCommonHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.minecraftforge.common.EnumHelper;
import ru.stalcraft.blocks.BlockDecorative;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.items.FireMode;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemArtefakt;
import ru.stalcraft.items.ItemBullet;
import ru.stalcraft.items.ItemCommand;
import ru.stalcraft.items.ItemGrenade;
import ru.stalcraft.items.ItemMedicine;
import ru.stalcraft.items.ItemSkin;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.proxy.IClientProxy;

public class ItemsConfig {
   private static HashSet sounds = new HashSet();
   private static HashMap items;
   private static int nextArmorId = 0;
   private static int nextArmorRenderId = 125;
   private static ArrayList zombieWeapons = new ArrayList();
   private static Random rand = new Random();
   public static List<String> medicineSound = new ArrayList<>();

   public static HashSet getSounds() {
      return sounds;
   }

   public static ye getRandomZombieWeapon() {
      return zombieWeapons.size() == 0 ? null : new ye((Integer)zombieWeapons.get(rand.nextInt(zombieWeapons.size())), 1, 0);
   }

   public static void readConfig() {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/items.txt"), "UTF-8"));
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

         Pattern var26 = Pattern.compile(".*weapon.*\\{.*");
         Pattern bulletRegex = Pattern.compile(".*bullet.*\\{.*");
         Pattern grenadeRegex = Pattern.compile(".*grenade.*\\{.*");
         Pattern artefaktRegex = Pattern.compile(".*artefakt.*\\{.*");
         Pattern armorRegex = Pattern.compile(".*armor.*\\{.*");
         Pattern commandItemRegex = Pattern.compile(".*command_item.*\\{.*");
         Pattern skinRegex = Pattern.compile(".*skin.*\\{.*");
         Pattern medicineRegex = Pattern.compile(".*medicine.*\\{.*");
         Pattern blockRegex = Pattern.compile(".*decorative.*\\{.*");
         String itemsSplitter = "\\{";
         String parSplitter = "[\\s]*;[\\s]*";
         String config = buffer.toString().replaceAll("\n|\r", "");
         String[] splitted = config.split("\\}");
         items = new HashMap();
         String[] parameters = null;
         int id = 0;
         int itemExtends = 0;
         String declaration = null;
         ItemsConfig.ItemType e2 = null;

         for (int e1 = 0; e1 < splitted.length; e1++) {
            try {
               if (var26.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.WEAPON;
               } else if (grenadeRegex.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.GRENADE;
               } else if (bulletRegex.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.BULLET;
               } else if (artefaktRegex.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.ARTEFAKT;
               } else if (armorRegex.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.ARMOR;
               } else if (commandItemRegex.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.COMMANDITEM;
               } else if (skinRegex.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.SKIN;
               } else if (medicineRegex.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.MEDICINE;
               } else if (blockRegex.matcher(splitted[e1]).matches()) {
                  e2 = ItemsConfig.ItemType.BLOCK;
               } else {
                  Logger.console("Strange string: " + splitted[e1]);
               }

               declaration = splitted[e1].split(itemsSplitter)[0];
               if (declaration.contains("extends")) {
                  try {
                     itemExtends = Integer.parseInt(declaration.split("extends")[1].trim());
                  } catch (Exception var26x) {
                     var26x.printStackTrace();
                  }
               }

               parameters = splitted[e1].split(itemsSplitter)[1].split(parSplitter);
               id = getInt(parameters, "item_id");
               items.put(id, new ItemsConfig.ItemToAdd(id, itemExtends, e2, parameters));
            } catch (Exception var271) {
               var271.printStackTrace();
               Logger.console("Error item config №= " + Integer.toString(e1) + " type=" + e2 + "!");
            }
         }

         Iterator var27 = items.entrySet().iterator();
         ItemsConfig.ItemToAdd var28x = null;

         while (var27.hasNext()) {
            var28x = (ItemsConfig.ItemToAdd)((Entry)var27.next()).getValue();

            try {
               if (var28x.type != null) {
                  if (var28x.type == ItemsConfig.ItemType.WEAPON) {
                     addWeapon(var28x);
                  } else if (var28x.type == ItemsConfig.ItemType.GRENADE) {
                     addGrenade(var28x);
                  } else if (var28x.type == ItemsConfig.ItemType.BULLET) {
                     addBullet(var28x);
                  } else if (var28x.type == ItemsConfig.ItemType.ARTEFAKT) {
                     addArtefakt(var28x);
                  } else if (var28x.type == ItemsConfig.ItemType.ARMOR) {
                     addArmor(var28x);
                  } else if (var28x.type == ItemsConfig.ItemType.COMMANDITEM) {
                     addCommandItem(var28x);
                  } else if (var28x.type == ItemsConfig.ItemType.SKIN) {
                     addSkin(var28x);
                  } else if (var28x.type == ItemsConfig.ItemType.MEDICINE) {
                     addMedicine(var28x);
                  } else if (var28x.type == ItemsConfig.ItemType.BLOCK) {
                     addBlock(var28x);
                  }
               }
            } catch (Exception var251) {
               Logger.debug("Error occured during adding item " + var28x.itemId);
               var251.printStackTrace();
            }
         }
      } catch (Exception var281) {
         var281.printStackTrace();
      }
   }

   private static void addWeapon(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      int bulletId = item.getInt("bullet_id");
      int bulletPiercingId = item.getInt("bulletPiercing_id");
      int cooldown = item.getInt("cooldown");
      int damage = item.getInt("damage");
      int cageSize = item.getInt("cage_size");
      int reloadTime = item.getInt("reload_time");
      int maxDamage = item.getInt("durability");
      float bulletSpeed = item.getFloat("bullet_speed");
      String name = item.getString("name");
      String textureName = item.getString("item_texture");
      String modelName = item.getString("model_name");
      ArrayList description = item.getList("description");
      String aimTexture = item.getString("aiming_texture");
      String shootSound = item.getString("shoot_sound");
      String hitSound = item.getString("hit_sound");
      String reloadSound = item.getString("reload_sound");
      String sleeveModel = item.getString("sleeve_model");
      String sleeveTexture = item.getString("sleeve_texture");
      int grenadeId = item.getInt("grenade_id");
      int grenadeCooldown = item.getInt("grenade_cooldown");
      boolean isPistol = item.getBoolean("is_pistol");
      boolean renderEquipped = item.getBoolean("render_equipped");
      float lightDistance = item.getFloat("light_distance");
      float lightSize = item.getFloat("light_size");
      float zoom = Math.max(1.0F, item.getFloat("zoom"));
      float posX = item.getFloat("posX");
      float posY = item.getFloat("posY");
      float posZ = item.getFloat("posZ");
      float recoil = item.getFloat("recoil");
      float spread = item.getFloat("spread");
      int bulletsCount = item.getInt("bullets_count");
      String modelTexture = item.getString("model_texture");
      boolean canBeUsedByZombie = item.getBoolean("can_be_used_by_zombie");
      boolean flashlight = item.getBoolean("flashlight");
      boolean silencer = item.getBoolean("silencer");
      boolean sight = item.getBoolean("sight");
      String silencerShoootSound = item.getString("silencer_shoot_sound");
      String sightAimingTexture = item.getString("sight_aiming_texture");
      float sightZoom = item.getFloat("sight_zoom");
      int halfLife = item.getInt("half-life");
      int[] fireModsArray = item.getIntArray("fire_mods");
      FireMode[] fireMods = new FireMode[]{FireMode.AUTO};
      if (fireModsArray.length > 0) {
         fireMods = new FireMode[fireModsArray.length];

         for (int i = 0; i < fireModsArray.length; i++) {
            fireMods[i] = FireMode.values()[fireModsArray[i]];
         }
      }

      if (canBeUsedByZombie) {
         zombieWeapons.add(id);
      }

      sounds.add(shootSound);
      if (silencerShoootSound != null && !silencerShoootSound.isEmpty()) {
         sounds.add(silencerShoootSound);
      }

      sounds.add(hitSound);
      sounds.add(reloadSound);
      float posLeftHandX = item.getFloat("posLeftHandX");
      float posLeftHandY = item.getFloat("posLeftHandY");
      float posLeftHandZ = item.getFloat("posLeftHandZ");
      float posRightHandX = item.getFloat("posRightHandX");
      float posRightHandY = item.getFloat("posRightHandY");
      float posRightHandZ = item.getFloat("posRightHandZ");
      boolean integrateSight = item.getBoolean("integrateSight");
      float aimPosX = item.getFloat("aimingPosX");
      float aimPosY = item.getFloat("aimingPosY");
      float aimPosZ = item.getFloat("aimingPosZ");
      float aimRotX = item.getFloat("aimingRotX");
      float aimRotY = item.getFloat("aimingRotY");
      float aimRotZ = item.getFloat("aimingRotZ");
      float flashSize = item.getFloat("flashSize");
      float flashPosX = item.getFloat("flashPosX");
      float flashPosY = item.getFloat("flashPosY");
      float flashPosZ = item.getFloat("flashPosZ");
      float equippedPosX = item.getFloat("equippedPosX");
      float equippedPosY = item.getFloat("equippedPosY");
      float equippedPosZ = item.getFloat("equippedPosZ");
      int indexGrenadeLauncher = item.getInt("indexGrenadeLauncher");
      int indexSight = item.getInt("indexSight");
      boolean ironSight = item.getBoolean("ironSight");
      boolean sightEnabled = item.getBoolean("sightEnabled");
      boolean silencerEnabled = item.getBoolean("silencerEnabled");
      boolean grenadeEnabled = item.getBoolean("grenadeEnabled");
      boolean autoShooting = item.getBoolean("autoShooting");
      boolean integrateGrenade = item.getBoolean("integrateGrenade");
      ItemWeapon weapon = new ItemWeapon(
         id,
         new int[]{bulletId, bulletPiercingId},
         fireMods,
         cooldown,
         damage,
         cageSize,
         reloadTime,
         maxDamage,
         bulletSpeed,
         name,
         textureName,
         modelName,
         modelTexture,
         description,
         aimTexture,
         shootSound,
         hitSound,
         reloadSound,
         sleeveModel,
         sleeveTexture,
         grenadeId,
         grenadeCooldown,
         isPistol,
         renderEquipped,
         bulletsCount,
         lightDistance,
         lightSize,
         zoom,
         recoil,
         spread,
         posX,
         posY,
         posZ,
         flashlight,
         silencer,
         sight,
         silencerShoootSound,
         sightAimingTexture,
         sightZoom,
         halfLife,
         posLeftHandX,
         posLeftHandY,
         posLeftHandZ,
         posRightHandX,
         posRightHandY,
         posRightHandZ,
         integrateSight,
         aimPosX,
         aimPosY,
         aimPosZ,
         aimRotX,
         aimRotY,
         aimRotZ,
         indexGrenadeLauncher,
         indexSight,
         flashSize,
         flashPosX,
         flashPosY,
         flashPosZ,
         equippedPosX,
         equippedPosY,
         equippedPosZ,
         ironSight,
         sightEnabled,
         silencerEnabled,
         grenadeEnabled,
         autoShooting,
         integrateGrenade
      );
      IClientProxy proxy = (IClientProxy)StalkerMain.getProxy();
      if (FMLCommonHandler.instance().getSide().isClient()) {
         ((ClientProxy)proxy).registerWeaponRenderer(weapon);
      }
   }

   private static void addBullet(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      String name = item.getString("name");
      String textureName = item.getString("item_texture");
      ArrayList description = item.getList("description");
      int stackSize = item.getInt("stack_size");
      int piercing = item.getInt("piercing");
      new ItemBullet(id, name, textureName, description, stackSize, piercing);
   }

   private static void addGrenade(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      String name = item.getString("name");
      String modelName = item.getString("model_name");
      String modelTexture = item.getString("model_texture");
      String textureName = item.getString("item_texture");
      ArrayList description = item.getList("description");
      float explosionSize = item.getFloat("explosion_size");
      float maxStartSpeed = item.getFloat("max_start_speed");
      boolean handUse = item.getBoolean("hand_use");
      int lifetime = item.getInt("lifetime");
      boolean collideExplosion = item.getBoolean("explosion_on_collide");
      new ItemGrenade(id, name, modelName, modelTexture, textureName, description, explosionSize, maxStartSpeed, handUse, lifetime, collideExplosion);
   }

   private static void addArtefakt(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      String name = item.getString("name");
      String textureName = item.getString("item_texture");
      ArrayList description = item.getList("description");
      int radiationProtection = item.getInt("radiation_protection");
      int chemicalProtection = item.getInt("chemical_protection");
      int biologicalProtection = item.getInt("biological_protection");
      boolean radiationImmunity = item.getBoolean("radiation_immunity");
      boolean chemicalImmunity = item.getBoolean("chemical_immunity");
      boolean biologicalImmunity = item.getBoolean("biological_immunity");
      boolean psychoImmunity = item.getBoolean("pchycho_immunity");
      float speedFactor = item.getFloat("speed_factor");
      float bulletDamageFactor = item.getFloat("bullet_damage_factor");
      float jumpIncrease = item.getFloat("jump_increase");
      boolean fireResistance = item.getBoolean("fire_resistance");
      boolean waterWalking = item.getBoolean("water_walking");
      int fall_protection = item.getInt("fall_protection");
      new ItemArtefakt(
         id,
         name,
         textureName,
         description,
         new int[]{radiationProtection, chemicalProtection, biologicalProtection, 0},
         new boolean[]{radiationImmunity, chemicalImmunity, biologicalImmunity, psychoImmunity},
         speedFactor,
         bulletDamageFactor,
         jumpIncrease,
         fireResistance,
         waterWalking,
         fall_protection
      );
   }

   private static void addArmor(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      String name = item.getString("name");
      String setId = item.getString("set_id");
      String textureName = item.getString("item_texture");
      String modelTexture = item.getString("armor_texture");
      String specialModel = item.getString("special_model");
      String extension = item.getString("extension");
      int durability = item.getInt("durability");
      int defence = item.getInt("defence");
      int slot = item.getInt("armor_slot");
      ArrayList description = item.getList("description");
      int radiationProtection = item.getInt("radiation_protection");
      int chemicalProtection = item.getInt("chemical_protection");
      int biologicalProtection = item.getInt("biological_protection");
      boolean radiationImmunity = item.getBoolean("radiation_immunity");
      boolean chemicalImmunity = item.getBoolean("chemical_immunity");
      boolean biologicalImmunity = item.getBoolean("biological_immunity");
      boolean psychoImmunity = item.getBoolean("pchycho_immunity");
      float speedFactor = item.getFloat("speed_factor");
      float bulletDamageFactor = item.getFloat("bullet_damage_factor");
      float jumpIncrease = item.getFloat("jump_increase");
      boolean fireResistance = item.getBoolean("fire_resistance");
      boolean waterWalking = item.getBoolean("water_walking");
      int fall_protection = item.getInt("fall_protection");
      float regeneration = item.getFloat("regeneration");
      float electra_protection = item.getFloat("electra_protection");
      wj material = EnumHelper.addArmorMaterial("STALKERSET" + ++nextArmorId + id, durability, new int[]{0, 0, 0, 0}, 15);
      float speedModifier = item.getFloat("speedModifier");
      boolean backpack = item.getBoolean("backpack");
      new ItemArmorArtefakt(
         id,
         slot,
         material,
         textureName,
         modelTexture,
         specialModel,
         extension,
         name,
         description,
         setId,
         new int[]{radiationProtection, chemicalProtection, biologicalProtection, 0},
         new boolean[]{radiationImmunity, chemicalImmunity, biologicalImmunity, psychoImmunity},
         speedFactor,
         bulletDamageFactor,
         jumpIncrease,
         fireResistance,
         waterWalking,
         fall_protection,
         regeneration,
         electra_protection,
         speedModifier,
         backpack
      );
   }

   private static void addBlock(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      String name = item.getString("name");
      String model = item.getString("model");
      float lightValue = item.getFloat("lightValue");
      boolean isSpectater = item.getBoolean("isSpectater");
      boolean isCollided = item.getBoolean("isCollided");
      new BlockDecorative(id, name, model, lightValue, isSpectater, isCollided);
   }

   private static void addCommandItem(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      String name = item.getString("name");
      String item_texture = item.getString("item_texture");
      ArrayList description = item.getList("description");
      String commands = item.getString("commands");
      int durability = item.getInt("durability");
      new ItemCommand(id, name, item_texture, description, commands, durability);
   }

   private static void addSkin(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      String name = item.getString("name");
      String texture = item.getString("skin_texture");
      new ItemSkin(id, name, texture);
   }

   private static void addMedicine(ItemsConfig.ItemToAdd item) {
      int id = item.getInt("item_id");
      String name = item.getString("name");
      String item_texture = item.getString("item_texture");
      ArrayList description = item.getList("description");
      int instant_regen = item.getInt("instant_regen");
      int radiation_accumulation = item.getInt("radiation_accumulation");
      int biological_accumulation = item.getInt("biological_accumulation");
      int psycho_accumulation = item.getInt("psycho_accumulation");
      int thermo_accumulation = item.getInt("thermo_accumulation");
      String sound = item.getString("sound");
      medicineSound.add(sound);
      int bleeding_protection = item.getInt("bleeding_protection");
      int coolDown = item.getInt("cooldown");
      int regeneration = item.getInt("regeneration");
      int duration = item.getInt("duration");
      new ItemMedicine(
         id,
         item_texture,
         name,
         instant_regen,
         new int[]{radiation_accumulation, thermo_accumulation, biological_accumulation, psycho_accumulation},
         bleeding_protection > 0,
         bleeding_protection,
         sound,
         coolDown,
         description,
         regeneration,
         duration
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
      public ItemsConfig.ItemType type;

      public ItemToAdd(int itemId, int itemExtends, ItemsConfig.ItemType type, String[] parameters) {
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

         return this.itemExtends != 0 && ItemsConfig.items.containsKey(this.itemExtends)
            ? ((ItemsConfig.ItemToAdd)ItemsConfig.items.get(this.itemExtends)).getParStr(name)
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
      WEAPON("WEAPON", 0),
      BULLET("BULLET", 1),
      GRENADE("GRENADE", 2),
      ARTEFAKT("ARTEFAKT", 3),
      ARMOR("ARMOR", 4),
      BLOCK("BLOCK", 5),
      COMMANDITEM("COMMANDITEM", 6),
      SKIN("SKIN", 7),
      MEDICINE("MEDICINE", 8);

      private static final ItemsConfig.ItemType[] $VALUES = new ItemsConfig.ItemType[]{
         WEAPON, BULLET, GRENADE, ARTEFAKT, ARMOR, BLOCK, COMMANDITEM, SKIN, MEDICINE
      };

      private ItemType(String var1, int var2) {
      }
   }
}
