package ru.stalcraft.client.gui.clans;

import java.util.ArrayList;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.clans.ClientClanData;
import ru.stalcraft.client.clans.TabType;
import ru.stalcraft.client.network.ClientPacketSender;

public class GuiTabClansList extends GuiClanTab implements IScrollable {
   private GuiElementSlider slider;
   private GuiElementScrollButton topButton;
   private GuiElementScrollButton bottomButton;
   private GuiElementScrollableContent list;
   private aut peaceOfferButton;
   private aut cancelPeaceOfferButton;
   private aut peaceButton;
   private aut warButton;

   public GuiTabClansList(GuiClans parent) {
      super(parent);
      GuiClanTab.texture = new bjo("stalker", "textures/clans/clans.png");
      this.peaceOfferButton = new aut(0, parent.g / 2 + 52, parent.h / 2 + 108, 120, 20, "Предложить мир");
      this.cancelPeaceOfferButton = new aut(1, parent.g / 2 + 52, parent.h / 2 + 108, 120, 20, "Продолжить войну");
      this.peaceButton = new aut(2, parent.g / 2 + 52, parent.h / 2 + 108, 120, 20, "Заключить мир");
      this.warButton = new aut(3, parent.g / 2 + 52, parent.h / 2 + 108, 120, 20, "Объявить войну");
   }

   @Override
   void drawTabForeground(int x, int y) {
      this.slider.draw(x, y);
      this.topButton.draw(x, y);
      this.bottomButton.draw(x, y);
      this.list.draw(x, y);
      super.parent.getButtonsList().clear();
      if (this.list.getSelectedLine() >= 0) {
         ClientClanData.ClientOtherClan clan = (ClientClanData.ClientOtherClan)ClientProxy.clanData.clans.get(this.list.getSelectedLine());
         ClientClanData clanData = ClientProxy.clanData;
         ArrayList clanLands = new ArrayList();

         for (ClientClanData.ClientClanLand land : clanData.lands) {
            if (clan.name.equals(land.ownerName)) {
               clanLands.add(land);
            }
         }

         this.drawString("Название: " + clan.name, super.parent.g / 2 + 50, super.parent.h / 2 - 75, 16777215);
         this.drawString("Фракция: " + clan.fraction, super.parent.g / 2 + 50, super.parent.h / 2 - 65, 16777215);
         this.drawString("Лидер: " + clan.leader, super.parent.g / 2 + 50, super.parent.h / 2 - 55, 16777215);
         this.drawString("Количество участников: " + clan.membersCount, super.parent.g / 2 + 50, super.parent.h / 2 - 45, 16777215);
         this.drawString("Количество баз: " + clanLands.size(), super.parent.g / 2 + 50, super.parent.h / 2 - 35, 16777215);
         if (!clan.name.equals(clanData.thePlayerClan)) {
            if (clan.warState == -1) {
               this.drawString("Отношения: Мир", super.parent.g / 2 + 50, super.parent.h / 2 - 25, 1157649);
               super.parent.getButtonsList().add(this.warButton);
               this.warButton.h = true;
            } else {
               this.drawString("Отношения: Война", super.parent.g / 2 + 50, super.parent.h / 2 - 25, 11145489);
               if (clan.warState == 0) {
                  super.parent.getButtonsList().add(this.peaceOfferButton);
               } else if (clan.warState == 1) {
                  super.parent.getButtonsList().add(this.cancelPeaceOfferButton);
               } else if (clan.warState == 2) {
                  super.parent.getButtonsList().add(this.peaceButton);
               }
            }
         } else {
            this.drawString("Отношения: Cоюз", super.parent.g / 2 + 50, super.parent.h / 2 - 25, 3500);
         }
      }
   }

   @Override
   void switchToTab() {
      ClientPacketSender.sendClanLandsRequest();
      ClientPacketSender.sendClanListRequest();
      this.slider = new GuiElementSlider(super.parent, this, super.parent.g + 37, super.parent.h - 156, super.parent.h + 220, 18, 30);
      this.topButton = new GuiElementScrollButton(
         super.parent, this.slider, GuiElementScrollButton.ScrollButtonDirection.TOP, super.parent.g + 37, super.parent.h - 176, 18, 18
      );
      this.bottomButton = new GuiElementScrollButton(
         super.parent, this.slider, GuiElementScrollButton.ScrollButtonDirection.BOTTOM, super.parent.g + 37, super.parent.h + 252, 18, 18
      );
      this.list = new GuiElementScrollableContent(
         super.parent, this.slider, ClientProxy.clanData.clans, atv.w().d / 2 - 400 + 26, atv.w().e / 2 - 150, 400, 430
      );
   }

   @Override
   TabType getTabType() {
      return TabType.CLANS;
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
      ClientClanData.ClientOtherClan clan = line < 0 ? null : (ClientClanData.ClientOtherClan)ClientProxy.clanData.clans.get(line);
      if (btn == this.peaceOfferButton || btn == this.peaceButton) {
         ClientPacketSender.sendClanPeaceRequest(clan.name);
      } else if (btn == this.cancelPeaceOfferButton) {
         ClientPacketSender.sendClanPeaceOfferCancelRequest(clan.name);
      } else if (btn == this.warButton) {
         ClientPacketSender.sendClanWarRequest(clan.name);
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
