package ru.stalcraft.client.gui.shop;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.client.shop.ShopItem;
import ru.stalcraft.player.PlayerUtils;

public class GuiSlotItem extends avk {
   public bjo shop_widgets = new bjo("stalker:textures/shop_widgets.png");
   public atv mc = atv.w();
   public bgw renderItem;
   public int x;
   public int y;
   public boolean renderArmor = false;
   public boolean inUse = false;
   public aut buttonItem;
   public aut buttonBuy;
   public GuiSlotItemButton buttonSlotItem;
   public GuiScrollSlotItem scroll;
   public ShopItem shopItem;
   private String desc;

   public GuiSlotItem(ShopItem shopItem, bgw renderItem, int x, int y) {
      this.shopItem = shopItem;
      this.renderItem = renderItem;
      this.x = x;
      this.y = y;
   }

   public void initSlot() {
      this.buttonSlotItem = new GuiSlotItemButton(0, this);
      this.buttonBuy = new GuiButtonBuy(1, 0, 0, 100, 20, "Купить", this);
   }

   public boolean isItemBuy(atv mc, int mouseX, int mouseY) {
      return this.buttonBuy.c(mc, mouseX, mouseY);
   }

   public boolean isItemRender(atv mc, int mouseX, int mouseY) {
      return this.buttonItem.c(mc, mouseX, mouseY);
   }

   public void drawSlot(atv mc, int mouseX, int mouseY, int scrollPosY, GuiScrollSlotItem scroll) {
      if (this.shopItem != null) {
         this.scroll = scroll;
         this.buttonBuy.d = this.x + 42;
         this.buttonBuy.e = this.y + 26 + scrollPosY;
         ((GuiButtonBuy)this.buttonBuy).update(mouseX, mouseY);
         this.mc.N.a(this.shop_widgets);
         if (!this.renderArmor) {
            this.drawTexturedModalRect(this.x, this.y + scrollPosY, 12, 2, 234, 79, 256, 0.62);
         } else {
            this.drawTexturedModalRect(this.x, this.y + scrollPosY, 12, 82, 234, 79, 256, 0.62);
         }

         this.buttonSlotItem.drawButton(mc, this.renderItem, mouseX, mouseY, this.x, this.y + scrollPosY, this);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.buttonBuy.a(mc, mouseX, mouseY);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public void setBuy() {
      PlayerClientInfo info = (PlayerClientInfo)PlayerUtils.getInfo(this.mc.h);
      int money = info.getDonateMoney();
      if (money >= this.shopItem.price) {
         ClientPacketSender.sendBuyItem(this.shopItem.price, this.shopItem.item.cv);
      }
   }

   public void setShopItem(ShopItem item) {
      this.shopItem = item;
   }

   public void setDesc(String desc) {
      this.desc = desc;
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

   public void drawTexturedModalRect(double width, double weight, int minU, int minV, int maxU, int maxV, int textureSize, double scale, float alpha) {
      double d = 1.0 / textureSize;
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(width + 0.0, weight + maxV * scale, super.n, (minU + 0) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + maxV * scale, super.n, (minU + maxU) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + 0.0, super.n, (minU + maxU) * d, (minV + 0) * d);
      tessellator.a(width + 0.0, weight + 0.0, super.n, (minU + 0) * d, (minV + 0) * d);
      tessellator.a();
   }

   public void drawScreen(atv mc, int mouseX, int mouseY) {
      if (this.shopItem != null) {
         this.buttonSlotItem.drawScreen(mc, this.renderItem, mouseX, mouseY, this.x, this.y, this);
      }

      PlayerClientInfo info = (PlayerClientInfo)PlayerUtils.getInfo(mc.h);
      int money = info.getDonateMoney();
      if (money < this.shopItem.price) {
         this.buttonBuy.h = false;
      } else {
         this.buttonBuy.h = true;
      }
   }

   public void setDrawArmor(boolean renderArmor) {
      this.renderArmor = renderArmor;
      this.scroll.disableDrawArmor(this);
   }

   public void drawPlayer() {
   }
}
