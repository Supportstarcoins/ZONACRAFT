package ru.stalcraft.client.gui;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.network.ClientPacketSender;

public class GuiHandcuffs extends awe {
   private static final bjo texture = new bjo("stalker", "textures/handcuffs_gui.png");
   private uf handcuffer;

   public GuiHandcuffs(uf handcuffer) {
      this.handcuffer = handcuffer;
   }

   public void A_() {
      super.i.clear();
      super.i.add(new aut(2, super.g / 2 - 76, super.h / 2 + 40, 74, 20, "Да"));
      super.i.add(new aut(3, super.g / 2 + 2, super.h / 2 + 40, 74, 20, "Нет"));
   }

   public void a(int par1, int par2, float par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      super.f.N.a(texture);
      this.b(super.g / 2 - 95, super.h / 2 - 25, 0, 0, 190, 110);
      awf sr = new awf(super.f.u, super.f.d, super.f.e);
      this.a(super.o, "Игрок " + this.handcuffer.bu + " хочет", sr.a() / 2, sr.b() / 2 - 10, 16777215);
      this.a(super.o, "надеть на вас наручники.", sr.a() / 2, sr.b() / 2 + 10, 16777215);
      this.a(super.o, "Позволить ему это?", sr.a() / 2, sr.b() / 2 + 30, 16777215);
      super.a(par1, par2, par3);
   }

   public boolean f() {
      return false;
   }

   protected void a(aut btn) {
      if (btn.g == 2) {
         ClientPacketSender.sendHandcuffsAnswer(this.handcuffer, true);
      } else {
         ClientPacketSender.sendHandcuffsAnswer(this.handcuffer, false);
      }

      atv.w().a((awe)null);
   }
}
