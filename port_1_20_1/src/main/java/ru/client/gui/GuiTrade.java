package ru.stalcraft.client.gui;

import ru.stalcraft.client.gui.shop.GuiCustomContainer;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.inventory.TradeContainer;
import ru.stalcraft.player.PlayerUtils;

public class GuiTrade extends GuiCustomContainer {
   public static bjo backGround = new bjo("stalker", "textures/trade.png");
   private avf moneyInput;
   public String trader;

   public GuiTrade(TradeContainer container, uf playerTrader) {
      super(container);
      this.trader = playerTrader.bu;
      super.xSize = 176;
      super.ySize = 250;
      super.guiLeft = super.g / 2 - super.xSize / 2;
      super.guiTop = super.h / 2 - super.ySize / 2;
   }

   @Override
   public void A_() {
      super.A_();
      super.i.add(new aut(0, super.g / 2 - 70, super.h / 2 + 118, 55, 20, "Предложить"));
      super.i.add(new aut(1, super.g / 2 - 15, super.h / 2 + 118, 50, 20, "Отмена"));
      this.moneyInput = new avf(super.o, super.g / 2 - 92, super.h / 2 + 12, 69, 12);
      this.moneyInput.f(16);
      this.moneyInput.b(true);
      this.moneyInput.a("0");
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      PlayerClientInfo playerInfo = (PlayerClientInfo)PlayerUtils.getInfo(this.f.h);
      super.f.N.a(backGround);
      this.drawTexturedModalRect(super.g / 2 - 100 + 0.5, super.h / 2 - 100, 1, 0, 348, 445, 512, 0.5);
      this.moneyInput.f();
      super.a(this.f.l, "Cчет: " + String.valueOf(playerInfo.getMoneyValue()) + " руб.", super.g / 2 - 50, super.h / 2 + 28, -1);
      super.a(this.f.l, 0 + " руб.", super.g / 2 + 30, super.h / 2 + 20, -1);
      super.a(this.f.l, 0 + " руб.", super.g / 2 + 30, super.h / 2 + 20, -1);
      super.a(this.f.l, "Вы", super.g / 2 - 59, super.h / 2 - 95, -1);
      super.a(this.f.l, this.trader, super.g / 2 + 35, super.h / 2 - 95, -1);
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

   @Override
   public void b() {
      super.b();
   }

   @Override
   public void c() {
      this.moneyInput.a();
      if (this.inventorySlots != null) {
         ((TradeContainer)super.inventorySlots).updateContainer();
      }
   }
}
