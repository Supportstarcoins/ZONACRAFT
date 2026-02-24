package ru.stalcraft.client.gui.clans;

import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.clans.ClientClanData;
import ru.stalcraft.client.clans.TabType;
import ru.stalcraft.client.network.ClientPacketSender;

public class GuiTabClanMembers extends GuiClanTab implements IScrollable {
   private GuiElementSlider slider;
   private GuiElementScrollButton topButton;
   private GuiElementScrollButton bottomButton;
   private GuiElementScrollableContent list;
   private static final String[] rankNames = new String[]{"Рядовой", "Офицер", "Лидер"};
   private aut kickButton;
   private aut leaveButton;
   private aut rankUpButton;
   private aut rankDownButton;
   private aut leaderButton;
   private aut dissolutionButton;

   public GuiTabClanMembers(GuiClans parent) {
      super(parent);
      GuiClanTab.texture = new bjo("stalker", "textures/clans/members.png");
      this.kickButton = new aut(0, parent.g / 2 + 52, parent.h / 2 + 60, 120, 20, "Исключить");
      this.leaveButton = new aut(1, parent.g / 2 + 52, parent.h / 2 + 60, 120, 20, "Покинуть группировку");
      this.rankUpButton = new aut(2, parent.g / 2 + 52, parent.h / 2 + 84, 120, 20, "Повысить ранг");
      this.rankDownButton = new aut(3, parent.g / 2 + 52, parent.h / 2 + 84, 120, 20, "Понизить ранг");
      this.leaderButton = new aut(4, parent.g / 2 + 52, parent.h / 2 + 108, 120, 20, "Сделать лидером");
      this.dissolutionButton = new aut(5, parent.g / 2 + 52, parent.h / 2 + 108, 120, 20, "Распустить группировку");
   }

   @Override
   void drawTabForeground(int x, int y) {
      this.slider.draw(x, y);
      this.topButton.draw(x, y);
      this.bottomButton.draw(x, y);
      this.list.draw(x, y);
      super.parent.getButtonsList().clear();
      if (this.list.getSelectedLine() >= 0 && this.list.getSelectedLine() < ClientProxy.clanData.members.size()) {
         ClientClanData.ClientClanMember member = (ClientClanData.ClientClanMember)ClientProxy.clanData.members.get(this.list.getSelectedLine());
         this.drawString("Ник: " + member.username, super.parent.g / 2 + 50, super.parent.h / 2 - 75, 16777215);
         this.drawString("Ранг: " + rankNames[member.rank], super.parent.g / 2 + 50, super.parent.h / 2 - 65, 16777215);
         this.drawString("Очки лояльности: " + member.loyalePoint, super.parent.g / 2 + 50, super.parent.h / 2 - 55, 16777215);
         if (member.online) {
            this.drawString("Игрок в сети", super.parent.g / 2 + 50, super.parent.h / 2 - 45, 1157649);
         } else {
            this.drawString("Игрок не в сети", super.parent.g / 2 + 50, super.parent.h / 2 - 45, 11145489);
         }

         if (ClientProxy.clanData.thePlayerRank != 2 && member.username.equals(atv.w().h.bu)) {
            super.parent.getButtonsList().add(this.leaveButton);
            this.leaveButton.e = super.parent.h / 2 + 108;
         } else if (ClientProxy.clanData.thePlayerRank == 1 && member.rank != 2) {
            super.parent.getButtonsList().add(this.kickButton);
            super.parent.getButtonsList().add(member.rank == 0 ? this.rankUpButton : this.rankDownButton);
            this.kickButton.e = super.parent.h / 2 + 84;
            this.rankUpButton.e = super.parent.h / 2 + 108;
            this.rankDownButton.e = super.parent.h / 2 + 108;
         } else if (ClientProxy.clanData.thePlayerRank == 2 && !member.username.equals(atv.w().h.bu)) {
            super.parent.getButtonsList().add(this.kickButton);
            super.parent.getButtonsList().add(member.rank == 0 ? this.rankUpButton : this.rankDownButton);
            super.parent.getButtonsList().add(this.leaderButton);
            this.kickButton.e = super.parent.h / 2 + 60;
            this.rankUpButton.e = super.parent.h / 2 + 84;
            this.rankDownButton.e = super.parent.h / 2 + 84;
            this.leaderButton.e = super.parent.h / 2 + 108;
         } else if (ClientProxy.clanData.thePlayerRank == 2) {
            super.parent.getButtonsList().add(this.dissolutionButton);
            this.dissolutionButton.e = super.parent.h / 2 + 108;
         }
      }
   }

   @Override
   void switchToTab() {
      ClientPacketSender.sendClanMembersRequest();
      this.slider = new GuiElementSlider(super.parent, this, super.parent.g + 37, super.parent.h - 156, super.parent.h + 220, 18, 30);
      this.topButton = new GuiElementScrollButton(
         super.parent, this.slider, GuiElementScrollButton.ScrollButtonDirection.TOP, super.parent.g + 37, super.parent.h - 176, 18, 18
      );
      this.bottomButton = new GuiElementScrollButton(
         super.parent, this.slider, GuiElementScrollButton.ScrollButtonDirection.BOTTOM, super.parent.g + 37, super.parent.h + 252, 18, 18
      );
      this.list = new GuiElementScrollableContent(
         super.parent, this.slider, ClientProxy.clanData.members, atv.w().d / 2 - 400 + 26, atv.w().e / 2 - 150, 400, 430
      );
   }

   @Override
   TabType getTabType() {
      return TabType.MEMBERS;
   }

   @Override
   void updateScreen() {
      this.slider.updateScreen();
      this.topButton.updateScreen();
      this.bottomButton.updateScreen();
      this.list.updateScreen();
   }

   @Override
   void keyTyped(char par1, int par2) {
      this.slider.keyTyped(par1, par2);
      this.list.keyTyped(par1, par2);
   }

   @Override
   void mouseClicked(int x, int y, int button) {
      this.slider.mouseClicked(x, y, button);
      this.topButton.mouseClicked(x, y, button);
      this.bottomButton.mouseClicked(x, y, button);
      this.list.onClick(x, y, button);
   }

   @Override
   void handleWheel(int amountScrolled) {
      if (amountScrolled > 0) {
         this.slider.scrollDown(amountScrolled * 3);
      } else {
         this.slider.scrollUp(-amountScrolled * 3);
      }
   }

   @Override
   void mouseUp(int x, int y) {
      this.slider.mouseUp(x, y);
      this.topButton.mouseUp(x, y);
      this.bottomButton.mouseUp(x, y);
   }

   @Override
   void actionPerformed(aut btn) {
      int line = this.list.getSelectedLine();
      ClientClanData.ClientClanMember member = line < 0 ? null : (ClientClanData.ClientClanMember)ClientProxy.clanData.members.get(line);
      if (btn == this.kickButton) {
         ClientPacketSender.sendClanKickRequest(member.username);
      } else if (btn == this.leaveButton) {
         ClientPacketSender.sendClanLeaveRequest();
      } else if (btn == this.rankUpButton) {
         ClientPacketSender.sendRankUpRequest(member.username);
      } else if (btn == this.rankDownButton) {
         ClientPacketSender.sendRankDownRequest(member.username);
      } else if (btn == this.leaderButton) {
         ClientPacketSender.sendSetLeaderRequest(member.username);
      } else if (btn == this.dissolutionButton) {
         ClientPacketSender.sendClanDeleteRequest();
      }
   }

   @Override
   public int getHeightPerPage() {
      return this.list.getHeightPerPage();
   }

   @Override
   public int getTotalHeight() {
      return this.list.getTotalHeight();
   }

   @Override
   public int getMinScroll() {
      return this.list.getMinScroll();
   }
}
