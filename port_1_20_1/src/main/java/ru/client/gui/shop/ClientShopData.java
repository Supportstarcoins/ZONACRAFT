package ru.stalcraft.client.gui.shop;

import java.util.ArrayList;
import java.util.List;

public class ClientShopData {
   public static List<Integer> contentsNewCaseID = new ArrayList<>();
   public static List<Integer> stackSizeNewCaseID = new ArrayList<>();
   public boolean isActive;

   public void addContent(int id, int stackSize) {
      if (!contentsNewCaseID.contains(id)) {
         contentsNewCaseID.add(id);
         stackSizeNewCaseID.add(stackSize);
      }
   }

   public static void OpenGui() {
      atv.w().a(new GuiCase(atv.w(), contentsNewCaseID, stackSizeNewCaseID));
   }

   public void setSlotDrop(boolean isActive) {
      this.isActive = isActive;
   }

   public boolean getSlotDrop() {
      return this.isActive;
   }
}
