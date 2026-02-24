package ru.stalcraft.client.gui.shop;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.gui.GuiCustomScreen;
import ru.stalcraft.client.shop.ShopContentItems;

public class GuiShop extends GuiCustomScreen {
   public List<GuiShopTab> tabs = new ArrayList<>();
   public List<ShopContentItems> contentsItems;
   public List<GuiButtonTab> buttonsTab = new ArrayList<>();
   public static int currentTab = -1;
   public bdi player;
   private boolean wasMouseDown;

   public GuiShop(bdi player, List<ShopContentItems> contentsItems) {
      this.player = player;
      this.contentsItems = contentsItems;
   }

   public void A_() {
      this.tabs.clear();

      for (int i = 0; i < this.contentsItems.size(); i++) {
         this.tabs.add(new GuiShopTab(this, this.contentsItems.get(i), i, super.g / 2 - 230, super.h / 2 - 140));
      }

      if (this.tabs.size() > 0) {
         currentTab = 0;
      }

      if (currentTab != -1) {
         GuiShopTab tab = this.tabs.get(currentTab);
         tab.initTab(super.f);
      }
   }

   public void c() {
      if (currentTab != -1) {
         GuiShopTab tab = this.tabs.get(currentTab);
         tab.updateTab();
      }
   }

   protected void a(int mouseX, int mouseY, int mouseButton) {
      super.a(mouseX, mouseY, mouseButton);
      if (mouseButton == 0) {
         if (currentTab != -1) {
            GuiShopTab tab = this.tabs.get(currentTab);
            tab.mouseClicked(super.f, mouseX, mouseY);
         }

         int prevCurrentTab = currentTab;

         for (aut button : this.buttonsTab) {
            if (button.c(this.f, mouseX, mouseY)) {
               currentTab = button.g;
            }
         }

         if (prevCurrentTab != currentTab) {
            GuiShopTab tab = this.tabs.get(currentTab);
            tab.initTab(this.f);
         }
      }
   }

   protected void b(int x, int y, int action) {
      super.b(x, y, action);
      if (currentTab != -1) {
         GuiShopTab tab = this.tabs.get(currentTab);
         tab.mouseMovedOrUp(x, y, action);
      }
   }

   public void a(int mouseX, int mouseY, float frame) {
      super.e();

      for (GuiButtonTab b : this.buttonsTab) {
         b.a(this.f, mouseX, mouseY);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (currentTab != -1) {
         GuiShopTab tab = this.tabs.get(currentTab);
         tab.drawTab(super.f, mouseX, mouseY);
      }
   }

   public boolean f() {
      return false;
   }
}
