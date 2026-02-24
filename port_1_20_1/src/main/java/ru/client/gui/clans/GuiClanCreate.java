package ru.stalcraft.client.gui.clans;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.demon.money.utils.PlayerMoneyUtils;
import ru.stalcraft.client.network.ClientPacketSender;

public class GuiClanCreate extends awe {
   private static final bjo texture = new bjo("stalker", "textures/clans/create.png");
   private avf input;
   private aut button;
   public boolean validate;

   public void A_() {
      Keyboard.enableRepeatEvents(true);
      this.button = new aut(0, super.g / 2 - 68, super.h / 2 + 17, 66, 20, "Создать");
      super.i.add(this.button);
      super.i.add(new aut(1, super.g / 2 + 2, super.h / 2 + 17, 66, 20, "Отмена"));
      this.input = new avf(super.o, super.g / 2 - 37, super.h / 2 - 7, 74, 14);
      this.input.f(16);
      this.input.b(true);
      this.validate = PlayerMoneyUtils.getInfo(atv.w().h).money >= 300000;
   }

   public void a(int x, int y, float frame) {
      this.b(0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      String name = this.input.b();
      atv.w().N.a(texture);
      this.b(super.g / 2 - 78, super.h / 2 - 45, 0, 0, 156, 90);
      this.input.f();
      if (!this.button.h) {
         this.a(super.o, "Название дожно быть длиной от 4 до 16 символов,", super.g / 2, super.h / 2 - 85, 16777215);
         this.a(super.o, "состоять целиком из русских либо английских", super.g / 2, super.h / 2 - 75, 16777215);
         this.a(super.o, "букв, также допускаются пробелы.", super.g / 2, super.h / 2 - 65, 16777215);
         if (this.validate) {
            this.a(super.o, "Стоимость создания группировки: 300000 руб.", super.g / 2, super.h / 2 - 55, 16777215);
         } else {
            this.a(super.o, a.e + "Стоимость создания группировки: 300000 руб.", super.g / 2, super.h / 2 - 55, -1);
         }
      }

      this.a(super.o, "Укажите название:", super.g / 2, super.h / 2 - 18, 16777215);
      super.a(x, y, frame);
   }

   protected void a(aut btn) {
      if (btn.g == 1) {
         atv.w().a((awe)null);
      } else {
         ClientPacketSender.sendClanCreateRequest(this.input.b().trim());
      }
   }

   public void c() {
      String name = this.input.b().trim();
      this.button.h = isNameCorrect(name) && this.validate;
      this.input.a();
   }

   private static boolean isNameCorrect(String name) {
      name = name.trim();
      return name.length() >= 4 && name.length() <= 16 && (name.matches("[a-zA-Z\\s]*") || name.matches("[а-яА-Я\\s]*"));
   }

   public void b() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void a(char par1, int par2) {
      super.a(par1, par2);
      String name = this.input.b().trim();
      boolean enabled = isNameCorrect(name) && this.validate;
      if (par2 == 28 && enabled) {
         ClientPacketSender.sendClanCreateRequest(name);
      }

      this.input.a(par1, par2);
   }

   protected void a(int par1, int par2, int par3) {
      super.a(par1, par2, par3);
      this.input.a(par1, par2, par3);
   }

   public boolean f() {
      return false;
   }
}
