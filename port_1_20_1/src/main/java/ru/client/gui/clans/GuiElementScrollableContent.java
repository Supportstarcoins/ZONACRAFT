package ru.stalcraft.client.gui.clans;

import java.util.List;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.clans.ClientClanData;

public class GuiElementScrollableContent extends GuiElement {
   private static final int LINE_HEIGHT = 26;
   private final GuiClans parent;
   private final GuiElementSlider slider;
   private final List lines;
   private final int x;
   private final int y;
   private final int xSize;
   private final int ySize;
   private int selectedLineId = -1;

   public GuiElementScrollableContent(GuiClans parent, GuiElementSlider slider, List lines, int x, int y, int xSize, int ySize) {
      this.parent = parent;
      this.slider = slider;
      this.lines = lines;
      this.x = x;
      this.y = y;
      this.xSize = xSize;
      this.ySize = ySize;
   }

   void draw(int mouseX, int mouseY) {
      GL11.glEnable(3089);
      GL11.glScissor(this.x, atv.w().e - this.y - this.ySize, this.xSize, this.ySize);

      for (int i = 0; i < this.lines.size(); i++) {
         int y = this.y - Math.round((this.getTotalHeight() - this.getHeightPerPage()) * this.slider.pos) + i * 26;
         if (y + 26 > this.y && y < this.y + this.ySize) {
            this.drawLine(i, y, mouseX, mouseY);
         }
      }

      GL11.glDisable(3089);
   }

   private void drawLine(int id, int y, int mouseX, int mouseY) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      atv.w().N.a(GuiClans.buttonsTexture);
      String str = ((ClientClanData.IListable)this.lines.get(id)).getString();
      boolean isMouseOver = mouseX >= this.x && mouseX < this.x + this.xSize && mouseY >= y && mouseY < y + 26;
      if (this.selectedLineId == id) {
         this.parent.drawTexturedModalRect(this.x, y, 0, 360, this.xSize, 26, 512);
      } else if (isMouseOver) {
         this.parent.drawTexturedModalRect(this.x, y, 0, 334, this.xSize, 26, 512);
      } else {
         this.parent.drawTexturedModalRect(this.x, y, 0, 308, this.xSize, 26, 512);
      }

      String idStr = id + 1 + ".";
      this.drawString(idStr, this.parent.g / 2 - 300 + 128 - this.getWidth(idStr), y / 2 + 5, 16777215);
      this.drawString(
         ((ClientClanData.IListable)this.lines.get(id)).getString(),
         this.parent.g / 2 - 300 + 135,
         y / 2 + 5,
         ((ClientClanData.IListable)this.lines.get(id)).getColor()
      );
   }

   void onClick(int x, int y, int button) {
      if (button == 0 && x >= this.x && x < this.x + this.xSize && y >= this.y && y < this.y + this.ySize) {
         int id = (y - this.y + Math.round((this.getTotalHeight() - this.getHeightPerPage()) * this.slider.pos)) / 26;
         if (id < this.lines.size()) {
            this.selectedLineId = id;
         }
      }
   }

   public int getTotalHeight() {
      return this.lines.size() * 26;
   }

   public int getHeightPerPage() {
      return this.ySize;
   }

   public int getMinScroll() {
      return 26;
   }

   void keyTyped(char par1, int par2) {
      if (par2 == 200) {
         this.selectedLineId = Math.max(0, this.selectedLineId - 1);
      } else if (par2 == 208) {
         this.selectedLineId = Math.min(this.lines.size() - 1, this.selectedLineId + 1);
      }

      int y = this.y - Math.round((this.getTotalHeight() - this.getHeightPerPage()) * this.slider.pos) + this.selectedLineId * 26;
      if (y < this.y) {
         this.slider.pos = this.selectedLineId * 26.0F / (this.getTotalHeight() - this.getHeightPerPage());
      } else if (y + 26 > this.y + this.ySize) {
         this.slider.pos = (float)((this.selectedLineId + 1) * 26 - this.getHeightPerPage()) / (this.getTotalHeight() - this.getHeightPerPage());
      }
   }

   public int getSelectedLine() {
      return this.selectedLineId;
   }

   private int getWidth(String str) {
      return atv.w().l.a(str);
   }

   private void drawString(String str, int x, int y, int color) {
      atv.w().l.b(str, x, y, color);
   }

   void updateScreen() {
      if (this.selectedLineId < -1 || this.selectedLineId >= this.lines.size()) {
         this.selectedLineId = 0;
      }
   }
}
