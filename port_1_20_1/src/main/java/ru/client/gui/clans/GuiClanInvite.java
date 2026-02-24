package ru.stalcraft.client.gui.clans;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.network.ClientPacketSender;

public class GuiClanInvite extends awe {
   private static final bjo texture = new bjo("stalker", "textures/clans/invite.png");
   private String inviter;
   private String clan;

   public GuiClanInvite(String clan, String inviter) {
      this.inviter = inviter;
      this.clan = clan;
   }

   public void A_() {
      super.i.add(new aut(0, super.g / 2 - 68, super.h / 2 + 17, 66, 20, "Вступить"));
      super.i.add(new aut(1, super.g / 2 + 2, super.h / 2 + 17, 66, 20, "Отказаться"));
   }

   public void a(int x, int y, float frame) {
      this.b(0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      atv.w().N.a(texture);
      this.b(super.g / 2 - 78, super.h / 2 - 45, 0, 0, 156, 90);
      this.a(super.o, "Игрок " + this.inviter + " приглашает", super.g / 2, super.h / 2 - 18, 16777215);
      this.a(super.o, "вас в группировку \"" + this.clan + "\".", super.g / 2, super.h / 2 - 8, 16777215);
      super.a(x, y, frame);
   }

   protected void a(aut btn) {
      if (btn.g == 0) {
         ClientPacketSender.sendClanJoinRequest(this.clan);
      } else {
         atv.w().a((awe)null);
      }
   }

   public boolean f() {
      return false;
   }
}
