package ru.stalcraft.client.gui.clans;

import java.util.ArrayList;
import java.util.List;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.clans.ClientClanData;
import ru.stalcraft.client.gui.shop.GuiCustomContainer;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.inventory.ContainerWarehouse;
import ru.stalcraft.inventory.SlotWarehouse;
import ru.stalcraft.player.PlayerUtils;

public class GuiBaseWarehouse extends GuiCustomContainer {
   private static bjo containerBackground = new bjo("textures/gui/container/generic_54.png");
   public String baseName = "";
   public int loyalePoint;
   public int page = 1;
   public int maxPage = 3;
   public int x;
   public int y;
   public int z;
   public int baseID;
   public boolean redaction = false;
   public avf baseNameInput;
   public List<Integer> contentList = new ArrayList<>();

   public GuiBaseWarehouse(uf player, ContainerWarehouse container, int x, int y, int z) {
      super(container);
      ClientPacketSender.sendPlayerLoyaleInfo();
      ClientPacketSender.sendWarehouseInfo(x, y, z);
      container.readList(this.contentList);
      container.baseID = this.baseID;
      this.x = x;
      this.y = y;
      this.z = z;
      if (player.bG.d) {
         this.redaction = true;
      }
   }

   @Override
   public void A_() {
      super.A_();
      super.i.clear();
      this.contentList.clear();
      ClientPacketSender.sendPlayerLoyaleInfo();
      ClientPacketSender.sendWarehouseInfo(this.x, this.y, this.z);
      ((ContainerWarehouse)super.inventorySlots).readList(this.contentList);
      ((ContainerWarehouse)super.inventorySlots).baseID = this.baseID;
      String[] buttonsName = new String[]{"<", "Сортировать по ID", ">"};

      for (int i = 0; i < buttonsName.length; i++) {
         String currentName = buttonsName[i];
         super.i
            .add(
               new aut(i, super.g / 2 - 82 + i * 40 + (i == 2 ? 45 : 0), super.h / 2 - 105, 15 + this.f.l.a(currentName) + (i != 1 ? 20 : 0), 20, currentName)
            );
      }

      if (this.redaction) {
         super.i.add(new aut(4, super.g / 2 - 190, super.h / 2 + 20, 75, 20, "Обновить"));
         this.baseNameInput = new avf(this.f.l, super.g / 2 - 200, super.h / 2, 100, 13);
         this.baseNameInput.a(this.baseName.equals("") ? "Название базы" : this.baseName);
      }
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      ClientClanData clanData = ClientProxy.clanData;
      super.guiLeft = super.g / 2 - super.xSize / 2;
      super.guiTop = super.h / 2 - super.ySize / 2;
      super.f.N.a(containerBackground);
      super.drawTexturedModalRect(super.g / 2 - 0.5 - 87.0, super.h / 2 - 9, 1, 252, 348, 192, 512, 0.5);
      super.drawTexturedModalRect(super.g / 2 - 0.5 - 87.0, super.h / 2 - 9 - 72, 1, 0, 348, 143, 512, 0.5);
      super.a(this.f.l, "Склад базы", super.g / 2 - 60, super.h / 2 - 75, -1);
      super.a(this.f.l, "Страница: " + this.page, super.g / 2 + 55, super.h / 2 - 75, -1);
      super.a(this.f.l, "Инвентарь", super.g / 2 - 60, super.h / 2 - 7, -1);
      String loyalePoint = String.valueOf(this.loyalePoint);
      super.a(this.f.l, "Очки лояльности: " + loyalePoint, super.g / 2 + 42 + (int)(this.f.l.a(loyalePoint) * 0.3F), super.h / 2 - 117, -1);
      we slot = this.getTheSlot(par2, par3);
      if (slot != null && slot instanceof SlotWarehouse && slot.d() != null) {
         by slotStackNBT = PlayerUtils.getTag(slot.d());
         int color = slotStackNBT.e("prices") <= Integer.parseInt(loyalePoint) ? -1 : 11145489;
         super.a(this.f.l, "Цена покупки: " + slotStackNBT.e("prices"), super.g / 2 - 50 + (int)(this.f.l.a(loyalePoint) * 0.3F), super.h / 2 - 117, color);
      }

      if (this.redaction) {
         this.baseNameInput.f();
      }
   }

   public void a(aut button) {
      int buttonID = button.g;
      if (buttonID == 0 && this.page >= 1) {
         this.page--;
         ((ContainerWarehouse)super.inventorySlots).deloadPage(this.page);
      }

      if (buttonID == 2 && this.page <= this.maxPage) {
         this.page++;
         ((ContainerWarehouse)super.inventorySlots).loadPage(this.page);
      }

      if (buttonID == 4) {
         ClientPacketSender.sendClanBaseInfo(this.baseNameInput.b().trim(), this.x, this.y, this.z);
      }
   }

   @Override
   public void c() {
      ClientPacketSender.sendPlayerLoyaleInfo();
      super.c();
      ((aut)super.i.get(0)).h = this.page != 1;
      ((aut)super.i.get(2)).h = this.page != this.maxPage;
      if (this.redaction) {
         this.baseNameInput.a();
      }
   }

   @Override
   protected void a(char par1, int par2) {
      super.a(par1, par2);
      if (this.redaction) {
         this.baseNameInput.a(par1, par2);
      }
   }

   @Override
   protected void a(int par1, int par2, int par3) {
      super.a(par1, par2, par3);
      if (this.redaction) {
         this.baseNameInput.a(par1, par2, par3);
      }
   }

   private we getTheSlot(int var1, int var2) {
      for (int var3 = 0; var3 < super.inventorySlots.c.size(); var3++) {
         we var4 = (we)super.inventorySlots.c.get(var3);
         if (this.isPointInRegion(var4.h, var4.i, 16, 16, var1, var2) && var4.b()) {
            return var4;
         }
      }

      return null;
   }
}
