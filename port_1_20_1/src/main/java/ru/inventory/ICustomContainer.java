package ru.stalcraft.inventory;

import java.util.ArrayList;

public interface ICustomContainer {
   ArrayList getBackpackSlots();

   ArrayList getArmorSlots();

   boolean hasBackpack();

   void handleBackpackChanged(boolean var1);

   of getOwner();

   boolean isSlotActive(we var1);
}
