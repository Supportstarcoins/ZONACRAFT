package ru.stalcraft.client.gui.shop;

public interface IGuiScrollable {
   void initScroll(int var1, int var2);

   void drawScrollable(atv var1, int var2, int var3, int var4);

   void drawScreen(atv var1, int var2, int var3);

   int getScrollHeightPerPage();

   int getScrollTotalHeight();

   int getScrollWidth();

   int getScrollHeight();

   int getScrollSize();

   void actionPerformed(aut var1);
}
