package ru.stalcraft.client.gui.clans;

import ru.stalcraft.client.clans.TabType;

public abstract class GuiClanTab {
   protected final GuiClans parent;
   protected static bjo texture;
   private avi fontRenderer;

   public GuiClanTab(GuiClans parent) {
      this.parent = parent;
      this.fontRenderer = atv.w().l;
   }

   abstract void actionPerformed(aut var1);

   abstract void drawTabForeground(int var1, int var2);

   abstract void switchToTab();

   abstract TabType getTabType();

   void drawTabBackground() {
      atv.w().N.a(texture);
      this.parent.drawTexturedModalRect(this.parent.g - 400, this.parent.h - 300, 0, 0, 800, 600, 1024);
   }

   void updateScreen() {
   }

   void keyTyped(char par1, int par2) {
   }

   void mouseClicked(int x, int y, int button) {
   }

   void onClose() {
   }

   void mouseUp(int x, int y) {
   }

   void handleWheel(int wheel) {
   }

   protected void drawString(String str, int x, int y, int color) {
      this.fontRenderer.b(str, x, y, color);
   }
}
