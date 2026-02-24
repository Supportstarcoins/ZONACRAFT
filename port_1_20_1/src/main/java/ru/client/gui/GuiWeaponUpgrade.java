package ru.stalcraft.client.gui;

import java.text.NumberFormat;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.inventory.ICustomContainer;
import ru.stalcraft.inventory.WeaponContainer;
import ru.stalcraft.items.ItemPoint;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;

public class GuiWeaponUpgrade extends awy {
   public WeaponContainer container;
   public ICustomContainer customContainer;
   private static final int X_INV_SIZE = 227;
   private static final int Y_INV_SIZE = 181;
   public static final bjo commonUpgrade = new bjo("stalker", "textures/upgrade.png");
   public static final bjo backpackUpgrade = new bjo("stalker", "textures/upgrade_b.png");
   public static final bjo flashlightTexture = new bjo("stalker", "textures/flashlight.png");
   public static final bjo silencerTexture = new bjo("stalker", "textures/silencer.png");
   public static final bjo sightTexture = new bjo("stalker", "textures/sight.png");
   private aut backButton;
   private aut ammoExtractButton;
   private aut buttonUpgrade;
   public bgw renderItem = new bgw();
   public boolean isSlotActive;
   public int itemId;

   public GuiWeaponUpgrade(WeaponContainer container) {
      super(container);
      this.container = container;
      this.customContainer = container;
   }

   public void A_() {
      super.i.clear();
      super.c = 227;
      super.d = 181;
      super.p = super.g / 2 - super.c / 2;
      super.q = super.h / 2 - super.d / 2;
      this.backButton = new aut(5, super.g / 2 - 85, super.h / 2 + 57, 80, 20, "Назад");
      this.ammoExtractButton = new aut(6, super.p + 28, super.q + 123, 80, 20, "Извлечь патроны");
      this.buttonUpgrade = new aut(7, super.p + 28, super.q + 98, 80, 20, "Улучшить");
      this.displayUpgradeButtons(this.container.updatedWeapon);
   }

   protected void a(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      if (this.customContainer.hasBackpack()) {
         atv.w().N.a(backpackUpgrade);
      } else {
         atv.w().N.a(commonUpgrade);
      }

      this.b(super.g / 2 - 113, super.h / 2 - 90, 0, 0, 227, 181);
      if (this.container.upgrade.d() == null) {
         this.drawTexturedModalRect(super.g / 2 - 98, super.h / 2 - 16, 5, 450, 20, 30, 512, 0.5);
      }

      if (this.container.updatedWeapon != null) {
         ItemWeapon weapon = (ItemWeapon)this.container.updatedWeapon.b();
         ye stack = this.container.updatedWeapon;
         by tag = PlayerUtils.getTag(stack);
         this.drawTexturedModalRect(super.g / 2 - 102, super.h / 2 - 38, 474, 474, 36, 36, 512, 0.5);
         if (!tag.n("silencer")) {
            this.drawTexturedModalRect(super.g / 2 - 101, super.h / 2 - 37, 32, 481, 30, 31, 512, 0.5);
         }

         this.drawTexturedModalRect(super.g / 2 - 82, super.h / 2 - 38, 474, 474, 36, 36, 512, 0.5);
         if (!tag.n("grenade_launcher") || weapon.integrateGrenade) {
            this.drawTexturedModalRect(super.g / 2 - 81, super.h / 2 - 37, 96, 482, 30, 30, 512, 0.5);
         }

         this.drawTexturedModalRect(super.g / 2 - 62, super.h / 2 - 38, 474, 474, 36, 36, 512, 0.5);
         if (!tag.n("sight")) {
            this.drawTexturedModalRect(super.g / 2 - 61, super.h / 2 - 37, 63, 481, 32, 31, 512, 0.5);
         }

         this.itemId = weapon.cv;
         this.isSlotActive = this.container.upgrade.d() != null;
         NumberFormat nf = NumberFormat.getInstance();
         nf.setMaximumFractionDigits(3);
         String damage = nf.format((double)tag.g("damage"));
         this.b(atv.w().l, "Урон: " + damage + " ед.", super.g / 2 - 62, super.h / 2 - 90, 16777215);
         int fireResistance = 1200 / weapon.cooldown;
         this.b(atv.w().l, "Скорострельность: " + fireResistance, super.g / 2 - 62, super.h / 2 - 80, 16777215);
         float reloadTime = tag.e("reloadTime") / 20;
         this.b(atv.w().l, "Перезарядка: " + reloadTime + " c.", super.g / 2 - 62, super.h / 2 - 70, 16777215);
         float spread = tag.g("spread") * 0.5F;
         String spreadFormat = nf.format((double)spread);
         this.b(atv.w().l, "Разброс: " + spreadFormat + "°", super.g / 2 - 62, super.h / 2 - 60, 16777215);
         float recoil = tag.g("recoil") * 0.1F;
         String recoilFormat = nf.format((double)recoil);
         this.b(atv.w().l, "Отдача: " + recoilFormat + "°", super.g / 2 - 62, super.h / 2 - 50, 16777215);
         int level = 0;
         if (this.container.upgrade.d() != null) {
            ye stackUpgrade = this.container.upgrade.d();
            ItemPoint itemPoint = (ItemPoint)stackUpgrade.b();
            by stackUpgradeNBT = PlayerUtils.getTag(stackUpgrade);
            if (itemPoint.values[0] > 0.0F) {
               level = tag.e("levelDamageUp");
            } else if (itemPoint.values[1] > 0.0F) {
               level = tag.e("levelRecoilUp");
            } else if (itemPoint.values[2] > 0.0F) {
               level = tag.e("levelSpreadUp");
            }

            nf.setMinimumFractionDigits(2);
            float levelFloat = level;
            String levelFormat = nf.format(levelFloat > 1.0F ? 100.0F / levelFloat : (levelFloat == 0.0F ? 100.0 : 80.0));
            this.a(this.f.l, "Текущий уровень: " + level, super.g / 2 - 46 + (level >= 10 ? 2 : 0), super.h / 2 - 17, -1);
            this.a(this.f.l, "Шанс успеха: " + levelFormat + "%", super.g / 2 - 43, super.h / 2 - 8, -1);
         }
      }

      GL11.glPushMatrix();
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      att.c();
      float scale = 1.95F;
      GL11.glScalef(scale, scale, 1.0F);
      this.renderItem.a(this.f.l, this.f.N, this.container.updatedWeapon, (int)((super.g / 2 - 101) / scale), (int)((super.h / 2 - 80) / scale));
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      att.a();
      GL11.glPopMatrix();
   }

   public void c() {
      super.c();
      this.buttonUpgrade.h = this.isSlotActive;
   }

   protected void b(int par1, int par2) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
   }

   protected void a(aut b) {
      super.a(b);
      if (b == this.ammoExtractButton) {
         ClientPacketSender.sendExtractAmmoRequest();
         this.ammoExtractButton.h = false;
      } else if (b == this.backButton) {
         this.f.h.i();
         this.f.a(new GuiInventoryStalker(this.f.h));
      } else if (b.g == 7) {
         ClientPacketSender.sendUpgrade(this.itemId);
      }
   }

   public void displayUpgradeButtons(ye stack) {
      super.i.add(this.ammoExtractButton);
      super.i.add(this.backButton);
      super.i.add(this.buttonUpgrade);
      this.ammoExtractButton.h = PlayerUtils.getTag(stack).e("cage") > 0;
   }

   public void drawTexturedModalRect(double width, double weight, int minU, int minV, int maxU, int maxV, int textureSize, double scale) {
      double d = 1.0 / textureSize;
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(width + 0.0, weight + maxV * scale, super.n, (minU + 0) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + maxV * scale, super.n, (minU + maxU) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + 0.0, super.n, (minU + maxU) * d, (minV + 0) * d);
      tessellator.a(width + 0.0, weight + 0.0, super.n, (minU + 0) * d, (minV + 0) * d);
      tessellator.a();
   }
}
