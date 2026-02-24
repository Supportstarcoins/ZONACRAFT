package ru.stalcraft.client.gui.clans;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.clans.ClientClanData;
import ru.stalcraft.client.clans.TabType;
import ru.stalcraft.client.network.ClientPacketSender;

public class GuiTabClanInfo extends GuiClanTab {
   private avf input;
   private aut salaryButton;
   private boolean redaction;
   private avf loyaleInput;

   public GuiTabClanInfo(GuiClans parent) {
      super(parent);
      GuiClanTab.texture = new bjo("stalker", "textures/clans/info.png");
   }

   @Override
   void drawTabForeground(int x, int y) {
      this.drawString("Название:", super.parent.g / 2 - 182, super.parent.h / 2 - 65, 16777215);
      this.drawString("Лидер:", super.parent.g / 2 - 182, super.parent.h / 2 - 52, 16777215);
      this.drawString("Количество баз:", super.parent.g / 2 - 182, super.parent.h / 2 - 39, 16777215);
      this.drawString("Суммарная репутация:", super.parent.g / 2 - 182, super.parent.h / 2 - 26, 16777215);
      this.drawString("Суммарные очки смерти:", super.parent.g / 2 - 182, super.parent.h / 2 - 13, 16777215);
      this.drawString("Количество участников:", super.parent.g / 2 - 182, super.parent.h / 2, 16777215);
      this.drawString("Участников онлайн:", super.parent.g / 2 - 182, super.parent.h / 2 + 13, 16777215);
      this.drawString("ОЛ за секунду захвата базы:", super.parent.g / 2 - 182, super.parent.h / 2 + 26, 16777215);
      String name = ClientProxy.clanData.thePlayerClan;
      String leader = ClientProxy.clanData.leader;
      ClientClanData clanData = ClientProxy.clanData;
      ArrayList clanLands = new ArrayList();

      for (ClientClanData.ClientClanLand land : clanData.lands) {
         if (clanData.thePlayerClan.equals(land.ownerName)) {
            clanLands.add(land);
         }
      }

      String lands = String.valueOf(clanLands.size());
      String rep = String.valueOf(ClientProxy.clanData.reputatuion);
      String death = String.valueOf(ClientProxy.clanData.deathCount);
      String members = String.valueOf(ClientProxy.clanData.membersCount);
      String online = String.valueOf(ClientProxy.clanData.onlineMembersCount);
      String money = ClientProxy.clanData.money + " руб.";
      String loyalePoints = String.valueOf(ClientProxy.clanData.loyalePoints);
      this.drawString(name, super.parent.g / 2 - 3 - this.getWidth(name), super.parent.h / 2 - 65, 16777215);
      this.drawString(leader, super.parent.g / 2 - 3 - this.getWidth(leader), super.parent.h / 2 - 52, 16777215);
      this.drawString(lands, super.parent.g / 2 - 3 - this.getWidth(lands), super.parent.h / 2 - 39, 16777215);
      this.drawString(rep, super.parent.g / 2 - 3 - this.getWidth(rep), super.parent.h / 2 - 26, 16777215);
      this.drawString(death, super.parent.g / 2 - 3 - this.getWidth(death), super.parent.h / 2 - 13, 16777215);
      this.drawString(members, super.parent.g / 2 - 3 - this.getWidth(members), super.parent.h / 2, 16777215);
      this.drawString(online, super.parent.g / 2 - 3 - this.getWidth(online), super.parent.h / 2 + 13, 16777215);
      this.drawString(loyalePoints, super.parent.g / 2 - 3 - this.getWidth(loyalePoints), super.parent.h / 2 + 26, 16777215);
      int warningsY = 39;
      if (this.redaction) {
         super.parent.e();
         GL11.glDisable(3553);
         GL11.glColor4f(0.05F, 0.05F, 0.05F, 0.2F);
         super.parent.drawTexturedModalRect(super.parent.g / 2 - 190, super.parent.h / 2 - 105, 0, 0, 762, 495, 0, 0.5);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glEnable(3553);
         this.loyaleInput.f();
         this.drawString("ОЛ за (сек.) захвата базы: ", super.parent.g / 2 - 178 - this.getWidth(loyalePoints), super.parent.h / 2 + 51, 16777215);
      } else {
         this.input.f();
      }
   }

   @Override
   void switchToTab() {
      ClientPacketSender.sendClanLandsRequest();
      ClientPacketSender.sendClanInfoRequest();
      int var10004 = super.parent.g / 2 - 182;
      avf var10001 = new avf(atv.w().l, var10004, super.parent.h / 2 + 117, 89, 16);
      this.input = var10001;
      this.input.f(32);
      this.salaryButton = new aut(1, super.parent.g / 2 - 89, super.parent.h / 2 + 91, 89, 20, "");
      if (ClientProxy.clanData.thePlayerRank > 0) {
         super.parent.getButtonsList().add(new aut(0, super.parent.g / 2 - 89, super.parent.h / 2 + 115, 89, 20, "Пригласить"));
         if (ClientProxy.clanData.thePlayerRank == 2) {
            super.parent.getButtonsList().add(new aut(2, super.parent.g / 2 - 181, super.parent.h / 2 + 85, 89, 20, "Редактировать"));
         }
      } else {
         this.input.e(false);
      }
   }

   @Override
   void keyTyped(char par1, int par2) {
      super.keyTyped(par1, par2);
      if (par2 == 28 && this.input.l() && this.input.b().length() > 0) {
         ClientPacketSender.sendClanInviteRequest(this.input.b());
      }

      if (ClientProxy.clanData.thePlayerRank > 0) {
         this.input.a(par1, par2);
      }

      String correct = "0123456789";
      if (correct.contains(String.valueOf(par1)) || par2 == 14 || par2 == 203 || par2 == 205) {
         if (this.redaction) {
            this.loyaleInput.a(par1, par2);
         }
      }
   }

   @Override
   public void updateScreen() {
      this.input.a();
      if (this.redaction) {
         this.loyaleInput.a();
      }
   }

   @Override
   void mouseClicked(int x, int y, int button) {
      this.input.a(x / 2, y / 2, button);
      if (this.redaction) {
         this.loyaleInput.a(x / 2, y / 2, button);
      }
   }

   @Override
   TabType getTabType() {
      return TabType.INFO;
   }

   @Override
   void actionPerformed(aut btn) {
      if (btn.g == 0 && this.input != null && this.input.b().length() > 0) {
         ClientPacketSender.sendClanInviteRequest(this.input.b());
      }

      if (btn.g == 2) {
         this.redaction = true;
         super.parent.getButtonsList().remove(1);
         super.parent.getButtonsList().add(new aut(3, super.parent.g / 2 - 89, super.parent.h / 2 + 85, 89, 20, "Готово"));
         int var10004 = super.parent.g / 2 - 91;
         this.loyaleInput = new avf(atv.w().l, var10004, super.parent.h / 2 + 47, 89, 16);
      }

      if (btn.g == 3) {
         this.redaction = false;
         super.parent.getButtonsList().remove(1);
         super.parent.getButtonsList().add(new aut(2, super.parent.g / 2 - 181, super.parent.h / 2 + 85, 89, 20, "Редактировать"));
         if (!this.loyaleInput.b().trim().equals("")) {
            ClientPacketSender.sendClanBasesInfo(ClientProxy.clanData.thePlayerClan, Integer.parseInt(this.loyaleInput.b().trim()));
         }

         ClientPacketSender.sendClanInfoRequest();
      }
   }

   private int getWidth(String str) {
      return atv.w().l.a(str);
   }
}
