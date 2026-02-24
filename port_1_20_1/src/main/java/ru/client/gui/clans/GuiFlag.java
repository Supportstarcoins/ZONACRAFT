package ru.stalcraft.client.gui.clans;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.network.ClientPacketSender;

public class GuiFlag extends awe {
   private final bjo res = new bjo("stalker", "textures/clans/flagedit.png");
   public ArrayList<avf> fieldsText = new ArrayList<>();
   private boolean place = false;
   private int captureSize;
   private int captureTime;
   private int captureRate;
   private int x;
   private int y;
   private int z;

   public GuiFlag(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public void A_() {
      if (this.fieldsText.size() > 0) {
         this.fieldsText.clear();
      }

      int w = 100;
      int h = -40;
      avf textField = new avf(super.o, super.g / 2 + w, super.h / 2 + h, 100, 12);
      textField.c(true);
      textField.e(true);
      textField.a(true);
      textField.a("Название(Описание)");
      this.fieldsText.add(textField);
      textField = new avf(super.o, super.g / 2 + w, super.h / 2 + h + 20, 30, 12);
      textField.c(true);
      textField.e(true);
      textField.a(true);
      textField.a(Integer.toString(0));
      this.fieldsText.add(textField);
      textField = new avf(super.o, super.g / 2 + w, super.h / 2 + h + 40, 30, 12);
      textField.c(true);
      textField.e(true);
      textField.a(true);
      textField.a(Integer.toString(0));
      this.fieldsText.add(textField);
      textField = new avf(super.o, super.g / 2 + w, super.h / 2 + h + 60, 30, 12);
      textField.c(true);
      textField.e(true);
      textField.a(true);
      textField.a(Integer.toString(0));
      this.fieldsText.add(textField);
      textField = new avf(super.o, super.g / 2 + w, super.h / 2 + h + 80, 100, 12);
      textField.c(true);
      textField.e(true);
      textField.a(true);
      textField.a("Название(Описание)");
      this.fieldsText.add(textField);
      textField = new avf(super.o, super.g / 2 + w, super.h / 2 + h + 100, 100, 12);
      textField.c(true);
      textField.e(true);
      textField.a(true);
      textField.a("День(Описание)");
      this.fieldsText.add(textField);
      textField = new avf(super.o, super.g / 2 + w, super.h / 2 + h + 120, 100, 12);
      textField.c(true);
      textField.e(true);
      textField.a(true);
      textField.a("Время захвата(Описание)");
      this.fieldsText.add(textField);
      textField = new avf(super.o, super.g / 2 + w, super.h / 2 + h + 140, 100, 12);
      textField.c(true);
      textField.e(true);
      textField.a(true);
      textField.a("Длительность захвата(Описание)");
      this.fieldsText.add(textField);
      super.i.add(new aut(0, super.g / 2 - 10, super.h / 2 + 115, 100, 20, "Готово"));
      super.i.add(new aut(1, super.g / 2 + 100, super.h / 2 + 115, 100, 20, "Отмена"));
   }

   public void c() {
      String t = this.fieldsText.get(1).b();
      if (t.isEmpty()) {
         this.captureTime = 0;
      } else {
         this.captureTime = Integer.parseInt(t);
      }

      t = this.fieldsText.get(2).b();
      if (t.isEmpty()) {
         this.captureRate = 0;
      } else {
         this.captureRate = Integer.parseInt(t);
      }

      t = this.fieldsText.get(3).b();
      if (t.isEmpty()) {
         this.captureSize = 0;
      } else {
         this.captureSize = Integer.parseInt(t);
      }

      for (avf textField : this.fieldsText) {
         textField.a();
      }
   }

   protected void a(char par1, int par2) {
      super.a(par1, par2);
      avf textField = this.fieldsText.get(0);
      textField.a(par1, par2);

      for (int i = 4; i < this.fieldsText.size(); i++) {
         textField = this.fieldsText.get(i);
         textField.a(par1, par2);
      }

      String correct = "0123456789";
      if (correct.contains(String.valueOf(par1)) || par2 == 14 || par2 == 203 || par2 == 205) {
         for (int i = 1; i < this.fieldsText.size() - 4; i++) {
            textField = this.fieldsText.get(i);
            textField.a(par1, par2);
         }
      }
   }

   protected void a(int par1, int par2, int par3) {
      super.a(par1, par2, par3);

      for (avf textField : this.fieldsText) {
         textField.a(par1, par2, par3);
      }
   }

   protected void a(aut guiButton) {
      if (guiButton.g == 0) {
         avf textField = this.fieldsText.get(0);
         ClientPacketSender.sendFlagPlace(
            this.x,
            this.y,
            this.z,
            textField.b(),
            this.captureTime,
            this.captureRate,
            this.captureSize,
            this.fieldsText.get(4).b(),
            this.fieldsText.get(5).b(),
            this.fieldsText.get(6).b(),
            this.fieldsText.get(7).b()
         );
         this.place = true;
      }

      super.f.a(null);
   }

   public void a(int mouseX, int mouseY, float frame) {
      GL11.glDisable(3553);
      GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
      this.drawTexturedModalRect(super.g / 2 - 250, super.h / 2 - 100, 101, 175, 803, 457, 1024, 0.6);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      super.a(mouseX, mouseY, frame);
      String[] fieldsName = new String[]{
         "Название:", "Время захвата:", "Скорость захвата:", "Зона захвата:", "Локация:", "День захвата", "Время захвата", "Длительность захвата"
      };
      int i = 0;

      for (avf textField : this.fieldsText) {
         textField.f();
         super.b(super.o, fieldsName[i], super.g / 2 - 10, super.h / 2 - 40 + i * 20, -1);
         i++;
      }
   }

   public void b() {
      if (!this.place) {
         ClientPacketSender.sendFlagCleanPlace(this.x, this.y, this.z);
      }
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
}
