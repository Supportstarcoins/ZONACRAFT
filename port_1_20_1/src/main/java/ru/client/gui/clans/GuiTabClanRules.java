package ru.stalcraft.client.gui.clans;

import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.clans.TabType;
import ru.stalcraft.client.network.ClientPacketSender;

public class GuiTabClanRules extends GuiClanTab implements IScrollable {
   private GuiElementTextBox text;
   private GuiElementSlider slider;
   private GuiElementScrollButton topButton;
   private GuiElementScrollButton bottomButton;
   private aut editButton;
   private aut saveButton;

   public GuiTabClanRules(GuiClans parent) {
      super(parent);
      GuiClanTab.texture = new bjo("stalker", "textures/clans/rules.png");
   }

   @Override
   void drawTabForeground(int x, int y) {
      this.text.draw();
      this.slider.draw(x, y);
      this.topButton.draw(x, y);
      this.bottomButton.draw(x, y);
   }

   @Override
   void switchToTab() {
      ClientPacketSender.sendClanRulesRequest();
      atv mc = atv.w();
      this.slider = new GuiElementSlider(super.parent, this, mc.d / 2 + 363, mc.e / 2 - 156, mc.e / 2 + 220, 18, 30);
      this.topButton = new GuiElementScrollButton(
         super.parent, this.slider, GuiElementScrollButton.ScrollButtonDirection.TOP, mc.d / 2 + 363, mc.e / 2 - 176, 18, 18
      );
      this.bottomButton = new GuiElementScrollButton(
         super.parent, this.slider, GuiElementScrollButton.ScrollButtonDirection.BOTTOM, mc.d / 2 + 363, mc.e / 2 + 252, 18, 18
      );
      this.text = new GuiElementTextBox(super.parent, this.slider, mc.d / 2 - 360, mc.e / 2 - 170, 700, 404);
      this.editButton = new aut(0, super.parent.g / 2 - 180, super.parent.h / 2 + 120, 100, 20, "Редактировать");
      this.saveButton = new aut(1, super.parent.g / 2 - 76, super.parent.h / 2 + 120, 100, 20, "Сохранить");
      this.editButton.i = this.saveButton.i = ClientProxy.clanData.thePlayerRank > 0;
      super.parent.getButtonsList().add(this.editButton);
      super.parent.getButtonsList().add(this.saveButton);
   }

   @Override
   TabType getTabType() {
      return TabType.RULES;
   }

   @Override
   void updateScreen() {
      this.slider.updateScreen();
      this.topButton.updateScreen();
      this.bottomButton.updateScreen();
      this.text.updateScreen();
      if (!this.text.isEditable) {
         this.text.setText(ClientProxy.clanData.rules);
      }

      this.editButton.f = this.text.isEditable ? "Отмена" : "Редактировать";
      this.saveButton.h = this.text.isEditable;
   }

   @Override
   void keyTyped(char par1, int par2) {
      this.slider.keyTyped(par1, par2);
      this.text.keyTyped(par1, par2);
   }

   @Override
   void mouseClicked(int x, int y, int button) {
      this.slider.mouseClicked(x, y, button);
      this.topButton.mouseClicked(x, y, button);
      this.bottomButton.mouseClicked(x, y, button);
      this.text.mouseClicked(x, y, button);
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
      if (btn.g == 0) {
         this.text.isEditable = !this.text.isEditable;
      } else if (btn.g == 1) {
         ClientProxy.clanData.rules = this.text.getText();
         ClientPacketSender.sendClanSetRulesRequest(this.text.getText());
         this.text.isEditable = false;
      }
   }

   @Override
   public int getHeightPerPage() {
      return this.text.getHeightPerPage();
   }

   @Override
   public int getTotalHeight() {
      return this.text.getTotalHeight();
   }

   @Override
   public int getMinScroll() {
      return this.text.getMinScroll();
   }
}
