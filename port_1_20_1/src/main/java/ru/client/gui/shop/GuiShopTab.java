package ru.stalcraft.client.gui.shop;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.client.shop.ShopContentItems;
import ru.stalcraft.player.PlayerUtils;

public class GuiShopTab extends avk {
   public static bjo backGround = new bjo("stalker:textures/shop_background.png");
   public GuiScrollContent scrollContent;
   public ShopContentItems contentItems;
   public String name;
   public int id;
   public int x;
   public int y;
   public yc currentItem;
   private GuiShop guiShop;
   public aut buttonAddPrice;

   public GuiShopTab(GuiShop guiShop, ShopContentItems contentItems, int id, int x, int y) {
      this.guiShop = guiShop;
      this.contentItems = contentItems;
      this.id = id;
      this.x = x;
      this.y = y;
   }

   public void initTab(atv mc) {
      this.guiShop.buttonsTab.clear();
      int x = 0;

      for (int i = 0; i < this.guiShop.tabs.size(); i++) {
         GuiShopTab tab = this.guiShop.tabs.get(i);
         int width = 10 + mc.l.a(tab.contentItems.name);
         GuiButtonTab button = new GuiButtonTab(tab.id, this.x + 1 + x, this.y, width, 20, tab.contentItems.name);
         x += width + i - 1;
         if (tab.equals(this)) {
            button.select = true;
         }

         this.guiShop.buttonsTab.add(button);
      }

      this.scrollContent = new GuiScrollContent(this.guiShop, new GuiScrollSlotItem(this.contentItems.itemsShop), this.x + 166, this.y + 48);
      this.buttonAddPrice = new aut(2, this.x + 7, this.y + 267, "Пополнить счет");
   }

   public void updateTab() {
      this.scrollContent.updateScroll();
   }

   public void mouseClicked(atv mc, int mouseX, int mouseY) {
      this.scrollContent.mouseClicked(mc, mouseX, mouseY);
   }

   protected void mouseMovedOrUp(int x, int y, int action) {
      this.scrollContent.mouseMovedOrUp(x, y, action);
   }

   private void onItemBuy(GuiSlotItem slotItem) {
      if (slotItem.shopItem != null && slotItem.shopItem.item != null) {
      }
   }

   private void onItemRender(GuiSlotItem slotItem) {
      if (slotItem.shopItem != null && slotItem.shopItem.item != null) {
         this.currentItem = slotItem.shopItem.item;
      }
   }

   public void drawTab(atv mc, int mouseWidth, int mouseHeight) {
      ye itemStack = null;
      mc.N.a(backGround);
      this.guiShop.drawTexturedModalRect(this.x, this.y + 20, 1, 0, 799, 456, 1024, 0.6);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.buttonAddPrice.a(mc, mouseWidth, mouseHeight);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.scrollContent.drawScroll(mc, mouseWidth, mouseHeight);
      PlayerClientInfo info = (PlayerClientInfo)PlayerUtils.getInfo(mc.h);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      this.a(mc.l, info.getDonateMoney() + " руб.", this.x + 48, this.y + 254, -1);
      GL11.glEnable(2896);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.drawPlayer(this.x + 82, this.y + 225, 80, 0.0F, 0.0F, this.guiShop.player);
   }

   private void drawPlayer(int par0, int par1, int par2, float par3, float par4, bdi player) {
      GL11.glEnable(2903);
      GL11.glPushMatrix();
      GL11.glTranslatef(par0, par1, 50.0F);
      GL11.glScalef(-par2, par2, par2);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      float f2 = player.aN;
      float f3 = player.A;
      float f4 = player.B;
      float f5 = player.aQ;
      float f6 = player.aP;
      GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
      att.b();
      GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-((float)Math.atan(par4 / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
      player.aN = (float)Math.atan(par3 / 40.0F) * 20.0F;
      player.A = (float)Math.atan(par3 / 40.0F) * 40.0F;
      player.B = -((float)Math.atan(par4 / 40.0F)) * 20.0F;
      player.aP = player.A;
      player.aQ = player.A;
      GL11.glTranslatef(0.0F, player.N, 0.0F);
      bgl.a.j = 180.0F;
      bgl.a.a(player, 0.0, 0.0, 0.0, 0.0F, 1.0F);
      player.aN = f2;
      player.A = f3;
      player.B = f4;
      player.aQ = f5;
      player.aP = f6;
      this.scrollContent.drawPlayer();
      GL11.glPopMatrix();
      att.a();
      GL11.glDisable(32826);
      bma.a(bma.b);
      GL11.glDisable(3553);
      bma.a(bma.a);
   }

   public void actionPerformed(aut button) {
      this.scrollContent.actionPerformed(button);
   }
}
