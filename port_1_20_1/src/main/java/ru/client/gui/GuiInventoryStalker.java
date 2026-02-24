package ru.stalcraft.client.gui;

import noppes.npcs.client.gui.player.GuiFaction;
import noppes.npcs.client.gui.player.GuiQuestLog;
import org.lwjgl.opengl.GL11;
import ru.demon.money.utils.PlayerMoneyUtils;
import ru.stalcraft.client.gui.shop.GuiCustomContainer;
import ru.stalcraft.inventory.ICustomContainer;
import ru.stalcraft.inventory.StalkerContainer;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class GuiInventoryStalker extends GuiCustomContainer {
   public StalkerContainer container;
   public ICustomContainer customContainer;
   private static final int X_INV_SIZE = 227;
   private static final int Y_INV_SIZE = 181;
   public static final bjo commonInventory = new bjo("stalker", "textures/inventory.png");
   public static final bjo backpackInventory = new bjo("stalker", "textures/backpack.png");
   public awe[] tabScreens;

   public GuiInventoryStalker(uf player) {
      super(PlayerUtils.getInfo(player).inventoryContainer);
      PlayerInfo par2 = PlayerUtils.getInfo(player);
      this.container = par2.inventoryContainer;
      this.customContainer = par2.inventoryContainer;
      this.tabScreens = new awe[]{this, new GuiQuestLog(player), new GuiFaction()};
   }

   @Override
   public void A_() {
      super.A_();
      super.i.clear();
      super.xSize = 227;
      super.ySize = 181;
      super.guiLeft = super.g / 2 - super.xSize / 2;
      super.guiTop = super.h / 2 - super.ySize / 2;
      String[] buttonNames = new String[]{"Инвентарь NPC", "Задания", "Фракции"};

      for (int i = 0; i < buttonNames.length; i++) {
         super.i.add(new aut(i, super.g / 2 - 110 + i * 64 - (i > 1 ? 24 : 0), super.h / 2 - 111, super.f.l.a(buttonNames[i]) + 12, 20, buttonNames[i]));
      }
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      super.f.N.a(commonInventory);
      this.b(super.g / 2 - 113, super.h / 2 - 90, 0, 0, 227, 181);
      if (PlayerMoneyUtils.getInfo(this.f.h) != null) {
         int money = PlayerMoneyUtils.getInfo(this.f.h).money;
         String moneyValue = String.valueOf(money);
         super.b(this.f.l, money + " Руб.", super.g - 30 - this.f.l.a(moneyValue), super.h - 13, -1);
      }

      super.b(this.f.l, "Одиночка " + this.f.h.bu, super.g / 2 - 35, super.h / 2 - 123, -1);
   }

   @Override
   public void c() {
      super.c();
      ((aut)super.i.get(0)).h = false;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      this.b(atv.w().l, "Вес: " + (int)this.container.info.getWeight() + "/" + (int)this.container.info.getMaxWeight() + " кг", 126, 159, 16777215);
   }

   @Override
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

   public void a(aut button) {
      super.f.a(this.tabScreens[button.g]);
   }
}
