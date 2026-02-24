package ru.stalcraft.client.gui.clans;

import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.clans.ClientClanData;
import ru.stalcraft.client.clans.TabType;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.player.PlayerUtils;

public class GuiTabClanLands extends GuiClanTab implements IScrollable {
   private ClientClanData.ClientClanLand land;
   private GuiElementSlider slider;
   private GuiElementScrollButton topButton;
   private GuiElementScrollButton bottomButton;
   private GuiElementScrollableContent list;
   private aut renameButton;
   private avf renameField;
   private aut buttonTeleport;

   public GuiTabClanLands(GuiClans parent) {
      super(parent);
      GuiClanTab.texture = new bjo("stalker", "textures/clans/lands.png");
   }

   @Override
   void drawTabForeground(int x, int y) {
      this.slider.draw(x, y);
      this.topButton.draw(x, y);
      this.bottomButton.draw(x, y);
      this.list.draw(x, y);
      if (this.list.getSelectedLine() >= 0) {
         this.parent.isTeleportate = true;
         ClientClanData clanData = ClientProxy.clanData;
         this.land = (ClientClanData.ClientClanLand)clanData.lands.get(this.list.getSelectedLine());
         this.drawString("Название: " + this.land.name, super.parent.g / 2 + 50, super.parent.h / 2 - 75, 16777215);
         this.drawString("Локация: " + this.land.loc, super.parent.g / 2 + 50, super.parent.h / 2 - 65, 16777215);
         this.drawString(
            "Координаты: X=" + this.land.x + ", Y=" + this.land.y + ", Z=" + this.land.z, super.parent.g / 2 + 50, super.parent.h / 2 - 55, 16777215
         );
         this.drawString("День захвата: " + this.land.captureDay, super.parent.g / 2 + 50, super.parent.h / 2 - 35, 16777215);
         this.drawString("Время захвата: " + this.land.captureTimes, super.parent.g / 2 + 50, super.parent.h / 2 - 25, 16777215);
         this.drawString("Длительность захвата: " + this.land.captureTimeMunute, super.parent.g / 2 + 50, super.parent.h / 2 - 15, 16777215);
         uf player = this.parent.f.h;
         int height = -10;
         this.drawString("Раз в пять минут вы можете", super.parent.g / 2 + 50, super.parent.h / 2 + 80 + height, 16777215);
         this.drawString("мгновенно перемещаться между", super.parent.g / 2 + 50, super.parent.h / 2 + 90 + height, 16777215);
         this.drawString("базами вашей группировки за", super.parent.g / 2 + 50, super.parent.h / 2 + 100 + height, 16777215);
         this.drawString("2500 руб.", super.parent.g / 2 + 50, super.parent.h / 2 + 110 + height, 16777215);
         if (this.land.ownerName == null) {
            this.drawString("Владелец: нет", super.parent.g / 2 + 50, super.parent.h / 2 - 45, 16777215);
         } else {
            this.drawString("Владелец: " + this.land.ownerName, super.parent.g / 2 + 50, super.parent.h / 2 - 45, 16777215);
         }

         double posX = player.u;
         double posY = player.v;
         double posZ = player.w;
         double landPosX = this.land.x;
         double landPosY = this.land.y;
         double landPosZ = this.land.z;
         boolean isValidButton = ClientProxy.clanData.thePlayerClan.equals(this.land.ownerName)
            && ((PlayerClientInfo)PlayerUtils.getInfo(player)).teleportCoolDown <= 20;
         if (isValidButton) {
            this.buttonTeleport.h = true;
         } else {
            this.buttonTeleport.h = false;
         }
      } else {
         this.parent.isTeleportate = false;
      }
   }

   @Override
   void switchToTab() {
      ClientPacketSender.sendClanLandsRequest();
      int var10004 = super.parent.g / 2 + 53;
      new avf(atv.w().l, var10004, super.parent.h / 2 + 84, 118, 16);
      this.slider = new GuiElementSlider(super.parent, this, super.parent.g + 37, super.parent.h - 156, super.parent.h + 220, 18, 30);
      this.topButton = new GuiElementScrollButton(
         super.parent, this.slider, GuiElementScrollButton.ScrollButtonDirection.TOP, super.parent.g + 37, super.parent.h - 176, 18, 18
      );
      this.bottomButton = new GuiElementScrollButton(
         super.parent, this.slider, GuiElementScrollButton.ScrollButtonDirection.BOTTOM, super.parent.g + 37, super.parent.h + 252, 18, 18
      );
      this.list = new GuiElementScrollableContent(
         super.parent, this.slider, ClientProxy.clanData.lands, atv.w().d / 2 - 400 + 26, atv.w().e / 2 - 150, 400, 430
      );
      super.parent.getButtonsList().clear();
      this.buttonTeleport = new aut(5, super.parent.g / 2 + 49, super.parent.h / 2 + 112, 117, 20, "Телепортироваться");
      super.parent.i.add(this.buttonTeleport);
   }

   @Override
   TabType getTabType() {
      return TabType.LANDS;
   }

   @Override
   void actionPerformed(aut btn) {
      if (this.parent.isTeleportate && btn.g == 5 && this.land != null) {
         ClientPacketSender.sendTeleportateBase(this.land.id);
      }
   }

   @Override
   void updateScreen() {
      this.slider.updateScreen();
      this.topButton.updateScreen();
      this.bottomButton.updateScreen();
      this.list.updateScreen();
      if (this.list.getSelectedLine() < ClientProxy.clanData.lands.size() && this.list.getSelectedLine() >= 0) {
         boolean var2 = true;
      } else {
         boolean var10000 = false;
      }
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
