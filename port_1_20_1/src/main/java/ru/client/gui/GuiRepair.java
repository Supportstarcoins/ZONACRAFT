package ru.stalcraft.client.gui;

import ru.stalcraft.client.gui.shop.GuiCustomContainer;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.inventory.WeaponRepairContainer;

public class GuiRepair extends GuiCustomContainer {
   public static bjo backGround = new bjo("stalker", "textures/repair.png");
   public static bgw itemRenderer = new bgw();
   public WeaponRepairContainer container = (WeaponRepairContainer)this.inventorySlots;
   public int type;

   public GuiRepair(uf player, int type) {
      super(new WeaponRepairContainer(player, type));
   }

   @Override
   public void A_() {
      for (int i = 0; i < this.container.number; i++) {
         super.i.add(new aut(i, super.g / 2 - 47, super.h / 2 - 74 + i * 24, 120, 20, "Починить"));
      }
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int k, int j) {
      super.guiLeft = this.g / 2;
      super.guiTop = this.h / 2;
      super.f.N.a(backGround);
      this.drawTexturedModalRect(super.g / 2 - 80, super.h / 2 - 80, 1, 0, 319, 360, 512, 0.5);

      for (int i = 0; i < this.container.number; i++) {
         itemRenderer.c(super.f.l, super.f.J(), this.container.inventory.contents[i], super.g / 2 - 67, super.h / 2 - 70 + i * 24);
      }
   }

   public void a(aut button) {
      ClientPacketSender.sendRepair(button.g);
   }

   @Override
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
