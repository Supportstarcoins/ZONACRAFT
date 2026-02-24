package ru.stalcraft.client.gui;

import java.text.NumberFormat;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.inventory.ArmorContainer;
import ru.stalcraft.inventory.ICustomContainer;
import ru.stalcraft.items.ItemPoint;
import ru.stalcraft.player.PlayerUtils;

public class GuiArmorUpgrade extends awy {
   public ArmorContainer container;
   public ICustomContainer customContainer;
   private static final int X_INV_SIZE = 227;
   private static final int Y_INV_SIZE = 181;
   public static final bjo commonUpgrade = new bjo("stalker", "textures/upgrade.png");
   public static final bjo backpackUpgrade = new bjo("stalker", "textures/upgrade_b.png");
   public static final bjo flashlightTexture = new bjo("stalker", "textures/flashlight.png");
   public static final bjo silencerTexture = new bjo("stalker", "textures/silencer.png");
   public static final bjo sightTexture = new bjo("stalker", "textures/sight.png");
   private aut backButton;
   private aut buttonUpgrade;
   public bgw renderItem = new bgw();
   public boolean isSlotActive;
   public int itemId;

   public GuiArmorUpgrade(ArmorContainer container) {
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
      this.buttonUpgrade = new aut(7, super.p + 28, super.q + 125, 80, 20, "Улучшить");
      this.displayUpgradeButtons(this.container.updatedArmor);
      this.itemId = this.container.updatedArmor.b().cv;
   }

   protected void a(float par1, int par2, int par3) {
      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(3);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      if (this.customContainer.hasBackpack()) {
         atv.w().N.a(backpackUpgrade);
      } else {
         atv.w().N.a(commonUpgrade);
      }

      this.b(super.g / 2 - 113, super.h / 2 - 90, 0, 0, 227, 181);
      if (this.container.updatedArmor != null) {
         ye stackArmor = this.container.updatedArmor;
         ye stackPoint = this.container.upgrade.d();
         this.buttonUpgrade.h = stackPoint != null;
      }

      by tag = PlayerUtils.getTag(this.container.updatedArmor);
      int level = 0;
      if (this.container.upgrade.d() != null) {
         ye stackUpgrade = this.container.upgrade.d();
         ItemPoint itemPoint = (ItemPoint)stackUpgrade.b();
         by stackUpgradeNBT = PlayerUtils.getTag(stackUpgrade);
         if (itemPoint.values[0] > 0.0F) {
            level = tag.e("levelBulletUp");
         } else if (itemPoint.values[1] > 0.0F) {
            level = tag.e("levelSpeedUp");
         } else if (itemPoint.values[2] > 0.0F) {
            level = tag.e("levelRegenerationUp");
         }

         nf.setMinimumFractionDigits(2);
         float levelFloat = level;
         String levelFormat = nf.format(levelFloat > 1.0F ? 100.0F / levelFloat : (levelFloat == 0.0F ? 100.0 : 80.0));
         this.a(this.f.l, "Текущий уровень: " + level, super.g / 2 - 46 + (level >= 10 ? 2 : 0), super.h / 2 - 17, -1);
         this.a(this.f.l, "Шанс успеха: " + levelFormat + "%", super.g / 2 - 43, super.h / 2 - 8, -1);
      }

      GL11.glPushMatrix();
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      att.c();
      float scale = 1.95F;
      GL11.glScalef(scale, scale, 1.0F);
      this.renderItem.a(this.f.l, this.f.N, this.container.updatedArmor, (int)((super.g / 2 - 101) / scale), (int)((super.h / 2 - 80) / scale));
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
      if (b.g == 7) {
         ClientPacketSender.sendUpgrade(this.itemId);
      }
   }

   public void displayUpgradeButtons(ye stack) {
      super.i.add(this.backButton);
      super.i.add(this.buttonUpgrade);
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
