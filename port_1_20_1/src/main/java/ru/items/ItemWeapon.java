package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.text.NumberFormat;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.Util;
import ru.stalcraft.client.ClientTicker;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.entity.EntityShot;
import ru.stalcraft.entity.EntitySleeve;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.WeightMap;
import ru.stalcraft.server.network.ServerPacketSender;

public class ItemWeapon extends yc implements ISpecialWeight, IFlashlight {
   public by weaponNBT;
   public ye stack;
   private static int nextId = 0;
   private static int ticksToChangeMotion = 0;
   private static int motionPause = 0;
   private static float motionYaw = 0.0F;
   private static float motionPitch = 0.0F;
   public final FireMode[] fireMods;
   private List description;
   public final String textureName;
   public final String modelName;
   public final String shootSound;
   public final String hitSound;
   public final String reloadSound;
   public final String aimingTexture;
   public final String modelTexture;
   public final String sleeveModel;
   public final String silencerShootSound;
   public final String aimingTextureSight;
   public final bjo sleeveTexture;
   public final int[] bulletsID;
   public final int cooldown;
   public final float damage;
   public final int cageSize;
   public final int reloadTime;
   public final int bulletsCount;
   public final int grenadeId;
   public final int grenadeLaunchCooldown;
   public final float lightSize;
   public final float lightDistance;
   public final float bulletSpeed;
   public final float zoom;
   public final float recoil;
   public final float spread;
   public final float zoomSight;
   public float aimPosX;
   public float aimPosY;
   public float aimPosZ;
   public float aimRotX;
   public float aimRotY;
   public float aimRotZ;
   public final float posX;
   public final float posY;
   public final float posZ;
   public float posLeftHandX;
   public float posLeftHandY;
   public float posLeftHandZ;
   public float posRightHandX;
   public float posRightHandY;
   public float posRightHandZ;
   public final boolean hasGrenadeLauncher;
   public final boolean isPistol;
   public final boolean renderEquipped;
   public final boolean flashlight;
   public final boolean silencer;
   public final boolean sight;
   public boolean integrateSight;
   public final double damageFactor;
   public int indexGrenadeLauncher;
   public int indexSight;
   public float flashSize;
   public float flashPosX;
   public float flashPosY;
   public float flashPosZ;
   public float equippedPosX;
   public float equippedPosY;
   public float equippedPosZ;
   public final boolean ironSight;
   public final boolean sightEnabled;
   public final boolean silencerEnabled;
   public final boolean grenadeEnabled;
   public int durability;
   public boolean autoShooting;
   public boolean integrateGrenade;
   public int oneShoot = 0;

   public ItemWeapon(
      int id,
      int[] bulletsID,
      FireMode[] fireMods,
      int cooldown,
      int damage,
      int cageSize,
      int reloadTime,
      int maxDamage,
      float bulletSpeed,
      String name,
      String textureName,
      String modelName,
      String modelTexture,
      List description,
      String aimingTexture,
      String shootSound,
      String hitSound,
      String reloadSound,
      String sleeveModel,
      String sleeveTexture,
      int grenadeId,
      int grenadeLaunchCooldown,
      boolean isPistol,
      boolean renderEquipped,
      int bulletsCount,
      float lightDistance,
      float lightSize,
      float zoom,
      float recoil,
      float spread,
      float x,
      float y,
      float z,
      boolean flashlight,
      boolean silencer,
      boolean sight,
      String silencerShootSound,
      String aimingTextureSight,
      float zoomSight,
      int halfLife,
      float posLeftHandX,
      float posLeftHandY,
      float posLeftHandZ,
      float posRightHandX,
      float posRightHandY,
      float posRightHandZ,
      boolean integrateSight,
      float aimPosX,
      float aimPosY,
      float aimPosZ,
      float aimRotX,
      float aimRotY,
      float aimRotZ,
      int indexGrenadeLauncher,
      int indexSight,
      float flashSize,
      float flashPosX,
      float flashPosY,
      float flashPosZ,
      float equippedPosX,
      float equippedPosY,
      float equippedPosZ,
      boolean ironSight,
      boolean sightEnabled,
      boolean silencerEnabled,
      boolean grenadeEnabled,
      boolean autoShooting,
      boolean integrateGrenade
   ) {
      super(id - 256);
      this.b("weapon" + ++nextId);
      this.textureName = textureName;
      this.a(StalkerMain.tab);
      LanguageRegistry.addName(this, name);
      this.fireMods = fireMods;
      super.cw = 1;
      this.bulletsID = bulletsID;
      this.cooldown = cooldown;
      this.e(maxDamage);
      this.durability = maxDamage;
      this.description = description;
      this.damage = damage;
      this.cageSize = cageSize;
      this.lightSize = lightSize;
      this.reloadTime = reloadTime;
      this.hasGrenadeLauncher = grenadeId != 0;
      if (sleeveModel != null) {
         if (sleeveModel.isEmpty()) {
            sleeveModel = null;
            this.sleeveTexture = null;
         } else {
            this.sleeveTexture = new bjo("stalker", "models/sleeves/" + sleeveTexture + ".png");
         }
      } else {
         this.sleeveTexture = null;
      }

      this.sleeveModel = sleeveModel;
      this.grenadeId = grenadeId;
      this.grenadeLaunchCooldown = grenadeLaunchCooldown;
      this.lightDistance = lightDistance;
      this.aimingTexture = aimingTexture != null && !aimingTexture.isEmpty() ? aimingTexture : null;
      this.modelName = modelName;
      this.bulletSpeed = bulletSpeed;
      this.isPistol = isPistol;
      this.zoom = zoom;
      this.shootSound = "stalker:" + shootSound;
      this.hitSound = "stalker:" + hitSound;
      this.reloadSound = "stalker:" + reloadSound;
      this.recoil = recoil;
      this.renderEquipped = renderEquipped;
      this.bulletsCount = bulletsCount;
      this.spread = spread;
      this.modelTexture = modelTexture;
      this.flashlight = flashlight;
      this.silencer = silencer;
      this.sight = sight;
      this.silencerShootSound = "stalker:" + silencerShootSound;
      this.aimingTextureSight = aimingTextureSight;
      this.zoomSight = zoomSight;
      this.damageFactor = Math.pow(0.5, 1.0 / (halfLife - 1));
      this.posX = x;
      this.posY = y;
      this.posZ = z;
      this.integrateSight = integrateSight;
      this.posLeftHandX = posLeftHandX;
      this.posLeftHandY = posLeftHandY;
      this.posLeftHandZ = posLeftHandZ;
      this.posRightHandX = posRightHandX;
      this.posRightHandY = posRightHandY;
      this.posRightHandZ = posRightHandZ;
      this.aimPosX = aimPosX;
      this.aimPosY = aimPosY;
      this.aimPosZ = aimPosZ;
      this.aimRotX = aimRotX;
      this.aimRotY = aimRotY;
      this.aimRotZ = aimRotZ;
      this.flashSize = flashSize;
      this.flashPosX = flashPosX;
      this.flashPosY = flashPosY;
      this.flashPosZ = flashPosZ;
      this.equippedPosX = equippedPosX;
      this.equippedPosY = equippedPosY;
      this.equippedPosZ = equippedPosZ;
      this.indexGrenadeLauncher = indexGrenadeLauncher;
      this.indexSight = indexSight;
      this.ironSight = ironSight;
      this.sightEnabled = sightEnabled;
      this.silencerEnabled = silencerEnabled;
      this.grenadeEnabled = grenadeEnabled;
      this.autoShooting = autoShooting;
      this.integrateGrenade = integrateGrenade;
      if (!sightEnabled) {
         this.integrateSight = false;
      }
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:" + this.textureName);
   }

   public ye a(ye is, abw world, uf player) {
      return is;
   }

   public void onUsingItemTick(ye stack, uf player, int count) {
      if (player.bq() == 20) {
         Util.setPrivateValue(uf.class, player, 21, "itemInUseCount", "field_71072_f", "g");
      }

      if (player.q.I) {
         this.onItemUsingTickClient(stack, player, count);
      }
   }

   private void onItemUsingTickClient(ye stack, uf player, int count) {
      if (atv.w().h == player && ((ClientWeaponInfo)((PlayerClientInfo)PlayerUtils.getInfo(player)).weaponInfo).isAiming()) {
         bdi p = atv.w().h;
         if (--ticksToChangeMotion <= 0) {
            ticksToChangeMotion = 5 + (int)(Math.random() * 15.0);
            motionPitch = ((float)Math.random() - 0.5F) * 0.5F;
            motionYaw = ((float)Math.random() - 0.5F) * 0.5F;
            motionPause = (int)(Math.random() * 5.0);
         }

         if (--motionPause <= 0) {
            atv.w().h.c(motionYaw, motionPitch);
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public void clientShoot(of shooter, ye stack, boolean hasFlash) {
      boolean isShooterPlayer = shooter instanceof uf;
      if (!((ClientWeaponInfo)PlayerUtils.getInfo((uf)shooter).weaponInfo).isAiming()) {
         shooter.q.d(new EntitySleeve(shooter.q, shooter, isShooterPlayer, this));
      }

      EntityShot entityShot = new EntityShot(shooter, this, shooter instanceof uf);
      shooter.q.d(entityShot);
      shooter.q
         .a(
            (float)shooter.u,
            (float)shooter.v + 0.5,
            (float)shooter.w,
            PlayerUtils.getTag(stack).n("silencer") ? this.silencerShootSound : this.shootSound,
            1.5F,
            shooter.q.s.nextFloat() * 0.1F + 0.9F,
            false
         );
   }

   @SideOnly(Side.CLIENT)
   public void clientReload(of shooter, ye stack) {
      ClientTicker.soundPlayAtEntity(shooter, ((ItemWeapon)yc.g[stack.d]).reloadSound, 1.0F, 0.9F + shooter.q.s.nextFloat() * 0.1F, false);
   }

   public void shootRequest(uf player, int slot, boolean leftClick) {
      ye stack = player.bn.h();
      if (stack != null && stack.d == super.cv) {
         PlayerUtils.getInfo(player).weaponInfo.onShoot(stack);
         this.shoot(player, stack, leftClick, !PlayerUtils.getTag(stack).n("silencer"));
         stack.a(1, player);
         if (stack.b == 0 && player instanceof jv) {
            player.bz();
            ((jv)player).a(player.bo);
         }
      }
   }

   public void shoot(of shooter, ye stack, boolean leftClick, boolean hasFlash) {
      abw w = shooter.q;
      by tag = ((uf)shooter).by().q();
      if (tag != null && tag.e("fireModes") < 2 && !w.I) {
         ServerPacketSender.sendShotSound(shooter.k, PlayerUtils.getTag(((uf)shooter).by()).n("silencer") ? this.silencerShootSound : this.shootSound);
         ServerPacketSender.sendShoot(shooter, hasFlash);
      }
   }

   public void grenadeShootRequest(uf player) {
      if (player.by() != null && player.by().q().e("fireModes") == 2) {
         ClientPacketSender.sendShootGrenadeRequest();
      }
   }

   public boolean a(ye par1ItemStack, uf par2EntityPlayer, abw par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      return false;
   }

   public boolean onLeftClickEntity(ye par1, uf par2, nn par3) {
      return true;
   }

   public boolean onEntitySwing(of par1, ye par2) {
      return true;
   }

   public float a(ye par1, aqz par2) {
      return 0.0F;
   }

   public boolean a(ye par1, of par2, of par3) {
      return false;
   }

   public boolean onItemUseFirst(ye par1, uf par2, abw par3, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      return true;
   }

   public boolean onBlockStartBreak(ye par1, int par2, int par3, int par4, uf par5) {
      return true;
   }

   public void a(ye stack, abw world, nn entity, int par4, boolean par5) {
      if (stack.q() != null) {
         this.weaponNBT = stack.q();
      }

      if (world.I && this.oneShoot > 0 && !Mouse.isButtonDown(0)) {
         stack.q().a("oneShoot", 0);
      }
   }

   public void a(ye stack, uf player, List list, boolean par4) {
      by tag = PlayerUtils.getTag(stack);
      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(3);
      list.add(a.o + "Патронов: " + tag.e("cage") + "/" + this.cageSize);
      if (tag.g("damage") > 0.0F) {
         String damage = nf.format((double)tag.g("damage"));
         list.add(a.o + "Урон: " + damage + " ед.");
         int fireResistance = 1200 / this.cooldown;
         list.add(a.o + "Скорострельность: " + fireResistance + " выстрелов/мин.");
         float reloadTime = tag.e("reloadTime") / 20;
         list.add(a.o + "Перезарядка: " + reloadTime + " c.");
         float spread = tag.g("spread") * 0.5F;
         String spreadFormat = nf.format((double)spread);
         list.add(a.o + "Разброс: " + spreadFormat + "°");
         float recoil = tag.g("recoil") * 0.1F;
         String recoilFormat = nf.format((double)recoil);
         list.add(a.o + "Отдача: " + recoilFormat + "°");
         list.add("");
         int levelDamageUp = tag.e("levelDamageUp");
         int levelRecoilUp = tag.e("levelRecoilUp");
         int levelSpreadUp = tag.e("levelSpreadUp");
         if (levelDamageUp > 0) {
            list.add(a.c + "Увеличение урона " + levelDamageUp);
         }

         if (levelRecoilUp > 0) {
            list.add(a.c + "Увеличение контроля " + levelRecoilUp);
         }

         if (levelSpreadUp > 0) {
            list.add(a.c + "Увеличение точности " + levelSpreadUp);
         }

         if (tag.n("silencer")) {
            if (!this.isPistol) {
               list.add("§2Стандартный глушитель");
            } else {
               list.add("§2Пистолетный глушитель");
            }
         }

         if (tag.n("grenade_launcher")) {
            list.add("§2Подствольный гранатомёт");
         }

         if (tag.n("sight")) {
            if (this.indexSight == 1) {
               list.add("§2Прицел Acog");
            } else if (this.indexSight == 2) {
               list.add("§2Прицел ПО");
            } else {
               list.add("§2Прицел ПСО");
            }
         }
      } else {
         list.add(a.o + "Урон: " + this.damage + " ед.");
         float relTime = this.reloadTime / 20;
         int fireResistancex = 1200 / this.cooldown;
         list.add(a.o + "Скорострельность: " + fireResistancex + " выстрелов/мин.");
         list.add(a.o + "Перезарядка: " + relTime + " c.");
         float spreads = this.spread * 0.5F;
         String spreadFormatx = nf.format((double)spreads);
         list.add(a.o + "Разброс: " + spreadFormatx + "°");
         float recoils = this.recoil * 0.1F;
         String recoilFormatx = nf.format((double)recoils);
         list.add(a.o + "Отдача: " + recoilFormatx + "°");
         list.add("");
         int levelDamageUpx = tag.e("levelDamageUp");
         int levelRecoilUpx = tag.e("levelRecoilUp");
         int levelSpreadUpx = tag.e("levelSpreadUp");
         if (levelDamageUpx > 0) {
            list.add(a.c + "Увеличение урона " + levelDamageUpx);
         }

         if (levelRecoilUpx > 0) {
            list.add(a.c + "Контроль " + levelRecoilUpx);
         }

         if (levelSpreadUpx > 0) {
            list.add(a.c + "Точность " + levelSpreadUpx);
         }

         if (tag.n("silencer")) {
            if (!this.isPistol) {
               list.add("§2Стандартный глушитель");
            } else {
               list.add("§2Пистолетный глушитель");
            }
         }

         if (tag.n("grenade_launcher")) {
            list.add("§2Подствольный гранатомёт");
         }

         if (tag.n("sight")) {
            if (this.indexSight == 1) {
               list.add("§2Прицел Acog");
            } else if (this.indexSight == 2) {
               list.add("§2Прицел ПО");
            } else {
               list.add("§2Прицел ПСО");
            }
         }
      }

      if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) {
         list.add("Зажмите <Shift> для просмотра подробностей");
      } else {
         list.addAll(this.description);
      }
   }

   public zj c_(ye par1ItemStack) {
      return zj.e;
   }

   public boolean n_() {
      return true;
   }

   @Override
   public float getWeight(ye stack) {
      float weight = 0.0F;
      weight = WeightMap.itemsWeight.containsKey(super.cv) ? (Float)WeightMap.itemsWeight.get(super.cv) : 5.0F;
      by tag = PlayerUtils.getTag(stack);
      weight += tag.e("cage") * WeightMap.getWeight(this.bulletsID[0]);
      return weight + tag.e("cage") * WeightMap.getWeight(this.bulletsID[1]);
   }

   @Override
   public boolean canShine(ye stack) {
      return PlayerUtils.getTag(stack).n("flashlight");
   }

   @Override
   public boolean shouldRotateWhenSprinting() {
      return !this.isPistol;
   }

   public FireMode getFireMod(ye par1) {
      return this.fireMods[PlayerUtils.getTag(par1).e("fireMode")];
   }
}
