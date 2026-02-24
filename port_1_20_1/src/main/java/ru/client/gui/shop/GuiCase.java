package ru.stalcraft.client.gui.shop;

import java.util.List;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.inventory.shop.ShopContainer;
import ru.stalcraft.player.PlayerUtils;

public class GuiCase extends GuiCustomContainer {
   public bjo caseBackground = new bjo("stalker:textures/case_background.png");
   public ShopContainer shopContainer;
   public GuiCaseButton newCase;
   public GuiCaseButton greenCase;
   public aut buttonBuy;
   public aut buttonOpen;
   public List contents;
   public int caseID = 0;

   public GuiCase(atv mc, List contents, List stackSize) {
      super(new ShopContainer(mc.h, contents, stackSize));
      this.contents = contents;
      super.guiLeft = 1024;
      super.guiTop = 1024;
      this.shopContainer = (ShopContainer)super.inventorySlots;
      ClientProxy.clientShopData.setSlotDrop(false);
   }

   @Override
   public void A_() {
      super.A_();
      this.i.clear();
      this.newCase = new GuiCaseButton(0, super.g / 2 - 233, super.h / 2 - 140, 333, 50, "greencase", 600, "Зеленый кейс");
      super.i.add(this.newCase);
      this.i.add(new aut(1, super.g / 2 - 240, super.h / 2 + 118, 175, 20, "Пополнить счет"));
      this.buttonBuy = new aut(2, super.g / 2 - 240, super.h / 2 + 65, 85, 20, "Купить");
      this.buttonOpen = new aut(3, super.g / 2 - 150, super.h / 2 + 65, 85, 20, "Открыть");
      this.i.add(this.buttonBuy);
      this.i.add(this.buttonOpen);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      this.f.N.a(this.caseBackground);
      this.drawTexturedModalRect(super.g / 2 - 250, super.h / 2 - 150, 0, 0, 800, 456, 1024, 0.65);
      if (this.caseID == 1) {
         if (!ClientProxy.clientShopData.getSlotDrop()) {
            for (int content = 0; content < this.contents.size(); content++) {
               this.f.N.a(this.caseBackground);
               int height = 0;
               int width = 0;
               if (content > 5) {
                  height = 50;
                  width = -294;
               }

               if (content > 11) {
                  height = 100;
                  width = -588;
               }

               if (content > 17) {
                  height = 150;
                  width = -882;
               }

               if (content > 22) {
                  height = 200;
                  width = -1176;
               }

               this.drawTexturedModalRect(super.g / 2 - 50 + content * 49 + width, super.h / 2 - 125 + height, 960, 448, 64, 64, 1024, 0.74);
               this.a(this.f.l, "Из этого кейса вы можете получить:", super.g / 2 + 95, super.h / 2 - 140, -1);
            }
         } else {
            this.drawTexturedModalRect(super.g / 2 - 50, super.h / 2 - 125, 960, 448, 64, 64, 1024, 0.74);
         }
      }

      super.a(this.f.l, PlayerUtils.getInfo(this.f.h).getDonateMoney() + " руб.", super.g / 2 - 200, super.h / 2 + 100, -1);
   }

   public void a(aut button) {
      super.a(button);
      if (button == this.newCase) {
         button.h = false;
         this.caseID = 1;
         this.shopContainer.setSlotsNewContainer();
      } else if (button == this.buttonBuy) {
         ClientPacketSender.sendBuy();
      } else if (button == this.buttonOpen && PlayerUtils.getInfo(this.f.h).getNewCaseValue() >= 0) {
         ClientPacketSender.sendOpen();
      }
   }

   @Override
   public void c() {
      super.c();
      if (this.caseID == 0) {
         this.buttonBuy.h = false;
         this.buttonOpen.h = false;
      } else {
         if (this.caseID == 1 && PlayerUtils.getInfo(this.f.h).getDonateMoney() >= 600) {
            this.buttonBuy.h = true;
         }

         if (PlayerUtils.getInfo(this.f.h).getNewCaseValue() > 0) {
            this.buttonOpen.h = true;
         } else {
            this.buttonOpen.h = false;
         }

         if (PlayerUtils.getInfo(this.f.h).getDonateMoney() >= 600) {
            this.buttonBuy.h = true;
         } else {
            this.buttonBuy.h = false;
         }
      }
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
}
