package ru.stalcraft.items;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.text.NumberFormat;
import java.util.List;
import org.lwjgl.input.Keyboard;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.player.PlayerUtils;

public class ItemArmorArtefakt extends wh implements IArtefakt, IStalkerArmor {
   private static String EMPTY_TEXTURE = "empty";
   public int[] protection = new int[]{0, 0, 0, 0};
   public float[] anomalyProtection = new float[]{0.0F, 0.0F, 0.0F, 0.0F};
   public int rendererId = -1;
   public int gapProtect;
   public int bangProtect;
   public float regeneration = 0.0F;
   public boolean[] immunity = new boolean[]{false, false, false, false};
   private String itemTexture;
   public final String setID;
   private String modelTexture;
   public bjo specialModelTexture;
   public final String specialModelName;
   private float speedFactor;
   private float bulletDamageFactor;
   private float jumpIncrease;
   private boolean fireResistance;
   private boolean waterWalking;
   private int fallProtection;
   private List description;
   public String extension;
   private static int nextId = 0;
   public float speedModifier;
   public boolean backpack = false;

   public ItemArmorArtefakt(
      int id,
      int armorSlot,
      wj material,
      String itemTexture,
      String modelTexture,
      String specialModel,
      String extension,
      String localizedName,
      List description,
      String setID,
      int[] protection,
      boolean[] immunity,
      float speedFactor,
      float bulletDamageFactor,
      float jumpIncrease,
      boolean fireResistance,
      boolean waterWalking,
      int fallProtection,
      float regeneration,
      float electra_protection,
      float speedModifier,
      boolean backpack
   ) {
      super(id - 256, material, addArmor(material.name().toLowerCase() + getSuffix(armorSlot)), armorSlot);
      this.protection = protection;
      this.immunity = immunity;
      this.a(StalkerMain.tab);
      this.b(material.name().toLowerCase() + getSuffix(super.b));
      this.itemTexture = itemTexture;
      if (specialModel != null && !specialModel.isEmpty() && !modelTexture.isEmpty()) {
         this.specialModelTexture = new bjo("stalker", "models/armor/" + modelTexture + ".png");
         modelTexture = EMPTY_TEXTURE;
      }

      this.regeneration = regeneration;
      this.anomalyProtection[0] = electra_protection;
      this.modelTexture = "stalker:textures/armor/" + modelTexture + ".png";
      this.specialModelName = specialModel;
      this.speedFactor = speedFactor;
      this.bulletDamageFactor = bulletDamageFactor;
      this.jumpIncrease = jumpIncrease;
      this.fireResistance = fireResistance;
      this.waterWalking = waterWalking;
      this.description = description;
      this.fallProtection = fallProtection;
      this.setID = setID != null && setID.isEmpty() ? null : setID;
      this.extension = extension;
      this.speedModifier = speedModifier;
      if (backpack) {
         this.backpack = backpack;
      }

      LanguageRegistry.addName(this, localizedName);
   }

   @Override
   public int getFallProtection() {
      return this.fallProtection;
   }

   @Override
   public float getSpeedFactor() {
      return this.speedFactor;
   }

   @Override
   public float getBulletDamageFactor() {
      return this.bulletDamageFactor;
   }

   public ye a(ye par1ItemStack, abw par2World, uf par3EntityPlayer) {
      return par1ItemStack;
   }

   @Override
   public float getJumpIncrease() {
      return this.jumpIncrease;
   }

   @Override
   public boolean getFireResistance() {
      return this.fireResistance;
   }

   @Override
   public boolean getWaterWalking() {
      return this.waterWalking;
   }

   @SideOnly(Side.CLIENT)
   public void a(ye stack, uf player, List list, boolean par4) {
      float electra_protection = this.anomalyProtection[0] != 0.0F ? this.anomalyProtection[0] * 20.5F : 0.0F;
      by tag = PlayerUtils.getTag(stack);
      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(2);
      if (!this.backpack) {
         list.add("§4Невозможно носить с рюкзаком");
      }

      int levelBullet = tag.e("levelBulletUp");
      int levelSpeed = tag.e("levelSpeedUp");
      int levelRegeneration = tag.e("levelRegenerationUp");
      if (levelBullet > 0) {
         list.add(a.c + "«Каменная кожа» " + levelBullet);
      }

      if (levelRegeneration > 0) {
         list.add(a.c + "Восстановление здоровья " + levelRegeneration);
      }

      if (levelSpeed > 0) {
         list.add(a.c + "Увеличение скорости " + levelSpeed);
      }

      float bulletFactor = this.bulletDamageFactor + tag.g("bulletFactor");
      String bulletFactorFormat = nf.format((double)bulletFactor);
      list.add("§" + (bulletFactor > 0.0F ? "2" : "4") + "Пулестойкость: +" + bulletFactorFormat + "%");
      list.add("§" + (electra_protection > 0.0F ? "2" : "4") + "Электрошок: +" + electra_protection + "%");
      list.add("§" + (this.anomalyProtection[1] > 0.0F ? "2" : "4") + "Ожог: +" + this.anomalyProtection[1] + "%");
      float regeneration = this.regeneration + tag.g("regenerationFactor");
      list.add("§" + (regeneration > 0.0F ? "2" : "4") + "Регенерация: +" + (int)(this.regeneration * 1000.0F + tag.g("regenerationFactor")) + "%");
      list.add("§" + (this.protection[0] > 0 ? "2" : "4") + "Радиозащита: +" + this.protection[0] + "%");
      list.add("§" + (this.protection[1] > 0 ? "2" : "4") + "Хим. ожог: +" + this.protection[1] + "%");
      list.add("§" + (this.protection[2] > 0 ? "2" : "4") + "Био. заражение: +" + this.protection[2] + "%");
      list.add("§" + (this.protection[3] > 0 ? "2" : "4") + "Пси-защита: +" + this.protection[3] + "%");
      list.add("§" + (this.fallProtection > 0 ? "2" : "4") + "Разрыв: +" + this.fallProtection + "%");
      list.add("§" + (this.bangProtect > 0 ? "2" : "4") + "Взрыв: +" + this.bangProtect + "%");
      float speedModifier = this.speedModifier + tag.g("speedFactor");
      String speedMofifierFormat = nf.format((double)speedModifier);
      list.add("§" + (speedModifier > 0.0F ? "2" : "4") + "Скорость: " + (speedModifier > 0.0F ? "+" : "") + speedMofifierFormat + "%");
      if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) {
         list.add("Зажмите <Shift> для просмотра подробностей");
      } else {
         list.addAll(this.description);
      }
   }

   @Override
   public boolean getImmunity(int effectID) {
      return this.immunity[effectID];
   }

   @Override
   public int getProtection(int effectID) {
      return this.protection[effectID];
   }

   @SideOnly(Side.CLIENT)
   public boolean b() {
      return false;
   }

   public boolean a(ye par1ItemStack) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public String getArmorTexture(ye stack, nn entity, int slot, int layer) {
      return "stalker:textures/armor/empty.png";
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:" + this.itemTexture);
   }

   public static String getSuffix(int armorType) {
      return armorType == 0 ? "_helm" : (armorType == 1 ? "_chest" : (armorType == 2 ? "_legs" : (armorType == 3 ? " _boots" : "")));
   }

   public static int addArmor(String set) {
      Side side = FMLCommonHandler.instance().getEffectiveSide();
      return side == Side.CLIENT ? ModLoader.addArmor(set) : 1;
   }

   public int getEntityLifespan(ye itemStack, abw world) {
      return 288000;
   }

   @Override
   public String getSetID() {
      return this.setID;
   }

   @Override
   public int getArmorType() {
      return super.b;
   }
}
