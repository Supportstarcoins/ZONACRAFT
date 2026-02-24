package ru.stalcraft.client.gui.clans;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

public class GuiElementTextBox extends GuiElement {
   private static avi fr = atv.w().l;
   private static final int LINE_HEIGHT = 20;
   private String text = "";
   private GuiClans parent;
   private GuiElementSlider slider;
   private ArrayList lines = new ArrayList();
   private ArrayList lineBreaks = new ArrayList();
   private int xPos;
   private int yPos;
   private int width;
   private int height;
   public boolean isEditable = false;
   private boolean isFocused = false;
   private int cursorCounter = 0;
   private int cursor = 0;

   public GuiElementTextBox(GuiClans parent, GuiElementSlider slider, int x, int y, int xSize, int ySize) {
      this.parent = parent;
      this.xPos = x;
      this.yPos = y;
      this.width = xSize;
      this.height = ySize;
      this.slider = slider;
   }

   public void draw() {
      drawRectHD(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
      drawRectHD(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
      if (this.cursor > this.text.length()) {
         this.cursor = this.text.length();
      }

      GL11.glEnable(3089);
      GL11.glScissor(this.xPos, atv.w().e - this.yPos - this.height, this.width, this.height - 6);
      this.splitText();

      for (int i = 0; i < this.lines.size(); i++) {
         String str = (String)this.lines.get(i);
         int y = this.yPos - Math.round((this.getTotalHeight() - this.getHeightPerPage()) * this.slider.pos) + i * 20 + 3;
         if (y + 20 > this.yPos && y < this.yPos + this.height) {
            fr.b(str, this.xPos / 2 + 5, y / 2, 16777215);
            int cursorLine = this.getCursorLine();
            if (this.cursorCounter % 20 < 10 && i == cursorLine && this.canWrite()) {
               int cursorColumn = this.getCursorColumn();
               int x = this.xPos + 9 + fr.a(str.substring(0, cursorColumn)) * 2;
               drawRectHD(x - 1, y + 1, x, y + fr.a * 2 - 1, -1);
            }
         }
      }

      GL11.glDisable(3089);
   }

   public void setText(String newText) {
      this.text = newText;
      this.cursor = 0;
   }

   public String getText() {
      return this.text;
   }

   private void splitText() {
      int maxWidth = this.width / 2 - 6;
      this.lines.clear();
      this.lineBreaks.clear();
      String[] textLines = this.text.split("\n");

      for (String line : textLines) {
         String[] words = line.split("(?<!( )) ");

         for (int s = 1; s < words.length; s++) {
            while (words[s].startsWith(" ")) {
               words[s - 1] = words[s - 1] + " ";
               words[s] = words[s].substring(1);
            }
         }

         String var12 = "";

         for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (fr.a(var12 + (i == 0 ? word : " " + word)) <= maxWidth) {
               var12 = var12 + (i == 0 ? word : " " + word);
            } else if (var12.length() > 0 && fr.a(word) <= maxWidth) {
               this.lines.add(var12);
               var12 = word;
            } else {
               if (var12.length() > 0) {
                  this.lines.add(var12);
                  var12 = "";
               }

               for (int j = 0; j < word.length(); j++) {
                  if (fr.a(var12 + word.charAt(j)) > maxWidth) {
                     this.lineBreaks.add(this.lines.size());
                     this.lines.add(var12);
                     var12 = "";
                  }

                  var12 = var12 + word.charAt(j);
               }
            }
         }

         if (line.endsWith(" ")) {
            var12 = var12 + " ";
         }

         this.lines.add(var12);
      }

      if (this.text.endsWith("\n")) {
         this.lines.add("");
      }
   }

   void keyTyped(char par1, int par2) {
      if (par2 == 205) {
         this.moveCursorTo(this.cursor + 1);
      } else if (par2 == 203) {
         this.moveCursorTo(this.cursor - 1);
      } else if (par2 == 208) {
         int cursorLine = this.getCursorLine();
         if (cursorLine + 1 < this.lines.size()) {
            int currentCursorPos = this.getCursorColumn();
            int cursorColumn = getStringWidthHD(((String)this.lines.get(cursorLine)).substring(0, currentCursorPos));
            this.moveCursorToLineAndX(++cursorLine, cursorColumn);
         }
      } else if (par2 == 200) {
         int cursorLine = this.getCursorLine();
         if (cursorLine > 0) {
            boolean var6 = false;
            int cursorColumn = this.getCursorColumn();
            int currentCursorPos = getStringWidthHD(((String)this.lines.get(cursorLine)).substring(0, cursorColumn));
            this.moveCursorToLineAndX(--cursorLine, currentCursorPos);
         }
      } else if (par2 == 211) {
         if (this.cursor < this.text.length()) {
            this.text = this.text.substring(0, this.cursor) + this.text.substring(this.cursor + 1);
         }

         this.cursorCounter = 0;
      } else if (par2 == 14) {
         if (this.cursor > 0) {
            this.text = this.text.substring(0, this.cursor - 1) + this.text.substring(this.cursor);
            this.moveCursorTo(this.cursor - 1);
         }
      } else if (par2 == 199) {
         int cursorLine = this.getCursorLine();
         this.moveCursorTo(this.getGlobalCursorPos(cursorLine, 0));
      } else if (par2 == 207) {
         int cursorLine = this.getCursorLine();
         if (cursorLine < this.lines.size()) {
            this.moveCursorTo(this.getGlobalCursorPos(cursorLine, ((String)this.lines.get(cursorLine)).length()));
            if (this.lineBreaks.contains(cursorLine)) {
               this.moveCursorTo(this.cursor - 1);
            }
         }
      } else if (par2 == 47 && awe.o()) {
         this.writeText(awe.l().replaceAll("\r", ""));
      } else if (par2 == 28) {
         this.writeText("\n");
      } else if (v.a(par1)) {
         this.writeText(String.valueOf(par1));
      }
   }

   private void writeText(String text) {
      if (this.canWrite()) {
         this.text = this.text.substring(0, this.cursor) + text + this.text.substring(this.cursor);
         this.moveCursorTo(this.cursor + text.length());
      }
   }

   private boolean canWrite() {
      return this.isEditable && this.isFocused;
   }

   private void moveCursorToLineAndX(int cursorLine, int x) {
      if (cursorLine >= this.lines.size()) {
         this.moveCursorTo(this.text.length());
      } else {
         String str = (String)this.lines.get(cursorLine);

         for (int i = 0; i < str.length(); i++) {
            int currentWidth = getStringWidthHD(str.substring(0, i));
            if (currentWidth >= x) {
               if (i == 0) {
                  this.moveCursorTo(this.getGlobalCursorPos(cursorLine, 0));
                  return;
               }

               int currentDifference = currentWidth - x;
               int prevDifference = x - getStringWidthHD(str.substring(0, i - 1));
               if (currentDifference < prevDifference) {
                  this.moveCursorTo(this.getGlobalCursorPos(cursorLine, i));
               } else {
                  this.moveCursorTo(this.getGlobalCursorPos(cursorLine, i - 1));
               }

               return;
            }
         }

         this.moveCursorTo(this.getGlobalCursorPos(cursorLine, str.length()));
      }
   }

   private int getCursorLine() {
      int length = 0;

      for (int i = 0; i < this.lines.size(); i++) {
         length += ((String)this.lines.get(i)).length();
         if (!this.lineBreaks.contains(i)) {
            length++;
         }

         if (length > this.cursor) {
            return i;
         }
      }

      return Math.max(0, this.lines.size() - 1);
   }

   private int getCursorColumn() {
      int length = 0;
      boolean line = false;

      for (int i = 0; i < this.lines.size(); i++) {
         if (length + ((String)this.lines.get(i)).length() + (this.lineBreaks.contains(i) ? 0 : 1) > this.cursor) {
            return Math.max(0, this.cursor - length);
         }

         length += ((String)this.lines.get(i)).length();
         if (!this.lineBreaks.contains(i)) {
            length++;
         }
      }

      return 0;
   }

   private int getGlobalCursorPos(int cursorLine, int cursorColumn) {
      int pos = 0;
      if (cursorLine >= this.lines.size()) {
         return this.text.length();
      } else {
         for (int i = 0; i < cursorLine; i++) {
            pos += ((String)this.lines.get(i)).length();
            if (!this.lineBreaks.contains(i)) {
               pos++;
            }
         }

         return cursorColumn > ((String)this.lines.get(cursorLine)).length() ? pos + ((String)this.lines.get(cursorLine)).length() : pos + cursorColumn;
      }
   }

   private void moveCursorTo(int newCursor) {
      this.cursor = newCursor;
      if (this.cursor < 0) {
         this.cursor = 0;
      } else if (this.cursor > this.text.length()) {
         this.cursor = this.text.length();
      }

      int cursorLine = this.getCursorLine();
      int y = this.yPos - Math.round((this.getTotalHeight() - this.getHeightPerPage()) * this.slider.pos) + cursorLine * 20;
      if (y < this.yPos) {
         this.slider.pos = cursorLine * 20.0F / (this.getTotalHeight() - this.getHeightPerPage());
      } else if (y + 20 > this.yPos + this.height) {
         this.slider.pos = (float)((cursorLine + 1) * 20 - this.getHeightPerPage()) / (this.getTotalHeight() - this.getHeightPerPage());
      }

      this.cursorCounter = 0;
   }

   void updateScreen() {
      this.cursorCounter++;
   }

   void mouseClicked(int x, int y, int button) {
      if (button == 0 && x >= this.xPos && x < this.xPos + this.width && y >= this.yPos && y < this.yPos + this.height) {
         this.isFocused = true;
         int line = (y - this.yPos + Math.round((this.getTotalHeight() - this.getHeightPerPage()) * this.slider.pos)) / 20;
         this.moveCursorToLineAndX(line, x - this.xPos - 7);
      } else if (button == 0) {
         this.isFocused = false;
      }
   }

   public int getMinScroll() {
      return 20;
   }

   public int getTotalHeight() {
      return this.lines.size() * 20;
   }

   public int getHeightPerPage() {
      return this.height - 6;
   }
}
