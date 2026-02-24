package ru.stalcraft.client.gui.clans;

import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.clans.TabType;
import ru.stalcraft.client.gui.GuiCustomScreen;

public class GuiClans extends GuiCustomScreen {
   public static final bjo buttonsTexture = new bjo("stalker", "textures/clans/buttons.png");
   private GuiClanTab currentTab;
   private awf sr;
   private boolean wasMouseDown = false;
   private int guiScale;
   public boolean isTeleportate = true;

   public GuiClans() {
      this.guiScale = atv.w().u.al;
      atv.w().u.al = 2;
   }

   public List getButtonsList() {
      return this.i;
   }

   public void A_() {
      Keyboard.enableRepeatEvents(true);
      if (this.currentTab == null) {
         this.switchToTab(TabType.INFO);
      } else {
         this.switchToTab(this.currentTab.getTabType());
      }

      this.wasMouseDown = Mouse.isButtonDown(0);
   }

   public void a(int x, int y, float frame) {
      this.b(0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      this.sr = new awf(super.f.u, super.f.d, super.f.e);
      this.handleMouseUp(x, y);
      this.handleWheel();
      this.currentTab.drawTabBackground();
      this.drawTabList(x, y);
      this.currentTab.drawTabForeground(x * 2, y * 2);
      if (this.isTeleportate) {
         for (int k = 0; k < this.i.size(); k++) {
            aut guibutton = (aut)this.i.get(k);
            guibutton.a(super.f, x, y);
         }
      }
   }

   private void drawTabList(int x, int y) {
      super.f.N.a(buttonsTexture);
      this.drawTexturedModalRect(
         super.g - 400 + 10 + 138 * this.currentTab.getTabType().ordinal(),
         super.h - 300 + 34,
         this.currentTab.getTabType().ordinal() % 3 * 138,
         this.currentTab.getTabType().ordinal() / 3 * 90 + 60,
         138,
         30,
         512
      );
      TabType tabMouseOver = this.getTabMouseOver(x, y);
      if (tabMouseOver != null && tabMouseOver != this.currentTab.getTabType()) {
         this.drawTexturedModalRect(
            super.g - 400 + 10 + 138 * tabMouseOver.ordinal(),
            super.h - 300 + 34,
            tabMouseOver.ordinal() % 3 * 138,
            tabMouseOver.ordinal() / 3 * 90 + 30,
            138,
            30,
            512
         );
      }
   }

   public void drawTexturedModalRect(int x, int y, int u, int v, int i, int j, int textureSize) {
      double d = 1.0 / textureSize;
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a((x + 0) / 2.0, (y + j) / 2.0, super.n, (u + 0) * d, (v + j) * d);
      tessellator.a((x + i) / 2.0, (y + j) / 2.0, super.n, (u + i) * d, (v + j) * d);
      tessellator.a((x + i) / 2.0, (y + 0) / 2.0, super.n, (u + i) * d, (v + 0) * d);
      tessellator.a((x + 0) / 2.0, (y + 0) / 2.0, super.n, (u + 0) * d, (v + 0) * d);
      tessellator.a();
   }

   private TabType getTabMouseOver(int x, int y) {
      if (y >= super.h / 2 - 150 + 17 && y <= super.h / 2 - 150 + 31 && x >= super.g / 2 - 200 + 4 && x <= super.g / 2 - 200 + 348) {
         int position = (x - super.g / 2 + 200 - 4) / 69;
         return TabType.values()[position];
      } else {
         return null;
      }
   }

   protected void a(char par1, int par2) {
      super.a(par1, par2);
      this.currentTab.keyTyped(par1, par2);
   }

   protected void a(int x, int y, int button) {
      super.a(x, y, button);
      if (button == 0) {
         TabType tabMouseOver = this.getTabMouseOver(x, y);
         if (tabMouseOver != null) {
            this.switchToTab(tabMouseOver);
         }
      }

      this.currentTab.mouseClicked(x * 2, y * 2, button);
   }

   private void switchToTab(TabType tab) {
      if (this.currentTab != null) {
         this.currentTab.onClose();
      }

      switch (GuiClans.NamelessClass1062016702.$SwitchMap$mods$ru$gloomyfolken$stalker$client$clans$TabType[tab.ordinal()]) {
         case 1:
            this.currentTab = new GuiTabClansList(this);
            break;
         case 2:
            this.currentTab = new GuiTabClanInfo(this);
            break;
         case 3:
            this.currentTab = new GuiTabClanLands(this);
            break;
         case 4:
            this.currentTab = new GuiTabClanMembers(this);
            break;
         case 5:
            this.currentTab = new GuiTabClanRules(this);
      }

      super.i.clear();
      this.currentTab.switchToTab();
   }

   public boolean f() {
      return false;
   }

   protected void a(aut btn) {
      this.currentTab.actionPerformed(btn);
   }

   protected void b(int x, int y, int action) {
      super.b(x, y, action);
      if (action == 0) {
         this.currentTab.mouseUp(x * 2, y * 2);
      }
   }

   private void handleMouseUp(int x, int y) {
      if (!Mouse.isButtonDown(0) && this.wasMouseDown) {
         this.currentTab.mouseUp(x * 2, y * 2);
      }

      this.wasMouseDown = Mouse.isButtonDown(0);
   }

   public void c() {
      this.currentTab.updateScreen();
   }

   private void handleWheel() {
      if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
         int amountScrolled = 0;

         while (!super.f.u.A && Mouse.next()) {
            int l2 = Mouse.getEventDWheel();
            if (l2 != 0) {
               if (l2 > 0) {
                  l2 = -1;
               } else if (l2 < 0) {
                  l2 = 1;
               }

               amountScrolled += l2;
            }
         }

         this.currentTab.handleWheel(amountScrolled);
      }
   }

   public void b() {
      atv.w().u.al = this.guiScale;
      Keyboard.enableRepeatEvents(false);
   }

   static class NamelessClass1062016702 {
      static final int[] $SwitchMap$mods$ru$gloomyfolken$stalker$client$clans$TabType = new int[TabType.values().length];

      static {
         try {
            $SwitchMap$mods$ru$gloomyfolken$stalker$client$clans$TabType[TabType.CLANS.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
         }

         try {
            $SwitchMap$mods$ru$gloomyfolken$stalker$client$clans$TabType[TabType.INFO.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
         }

         try {
            $SwitchMap$mods$ru$gloomyfolken$stalker$client$clans$TabType[TabType.LANDS.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
         }

         try {
            $SwitchMap$mods$ru$gloomyfolken$stalker$client$clans$TabType[TabType.MEMBERS.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
         }

         try {
            $SwitchMap$mods$ru$gloomyfolken$stalker$client$clans$TabType[TabType.RULES.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
         }
      }
   }
}
