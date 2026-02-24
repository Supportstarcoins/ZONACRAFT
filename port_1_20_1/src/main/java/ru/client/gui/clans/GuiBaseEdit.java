package ru.stalcraft.client.gui.clans;

import ru.stalcraft.client.network.ClientPacketSender;

public class GuiBaseEdit extends awe {
   public avf inputOne;
   public avf inputTwo;
   public avf inputFlagName;
   public avf inputRespawnCooldown;

   public void A_() {
      super.A_();
      super.i.clear();
      super.i.add(new aut(0, super.g / 2 + 25, super.h / 2 - 3, 40, 20, "Ебнуть"));
      this.inputOne = new avf(super.f.l, super.g / 2 - 80, super.h / 2, 100, 14);
      this.inputOne.a("ID");
      this.inputTwo = new avf(super.f.l, super.g / 2 - 185, super.h / 2, 100, 14);
      this.inputTwo.a("Цена");
      this.inputFlagName = new avf(super.f.l, super.g / 2 - 185, super.h / 2 - 17, 100, 14);
      this.inputFlagName.a("Название базы");
      this.inputRespawnCooldown = new avf(super.f.l, super.g / 2 - 80, super.h / 2 - 17, 100, 14);
      this.inputRespawnCooldown.a("Задержка");
   }

   public void a(int par1, int par2, float par3) {
      super.e();
      super.a(par1, par2, par3);
      this.inputOne.f();
      this.inputTwo.f();
      this.inputFlagName.f();
      this.inputRespawnCooldown.f();
   }

   public boolean f() {
      return false;
   }

   public void c() {
      super.c();
      this.inputOne.a();
      this.inputTwo.a();
      this.inputFlagName.a();
      this.inputRespawnCooldown.a();
   }

   protected void a(char par1, int par2) {
      super.a(par1, par2);
      this.inputOne.a(par1, par2);
      this.inputTwo.a(par1, par2);
      this.inputFlagName.a(par1, par2);
      this.inputRespawnCooldown.a(par1, par2);
   }

   protected void a(int par1, int par2, int par3) {
      super.a(par1, par2, par3);
      this.inputOne.a(par1, par2, par3);
      this.inputTwo.a(par1, par2, par3);
      this.inputFlagName.a(par1, par2, par3);
      this.inputRespawnCooldown.a(par1, par2, par3);
   }

   public void a(aut button) {
      ClientPacketSender.sendUpdatePrice(
         this.inputFlagName.b().trim(),
         Integer.parseInt(this.inputTwo.b().trim()),
         Integer.parseInt(this.inputOne.b().trim()),
         Integer.parseInt(this.inputRespawnCooldown.b().trim())
      );
   }
}
