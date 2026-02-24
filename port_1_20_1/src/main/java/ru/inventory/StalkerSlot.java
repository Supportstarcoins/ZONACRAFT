package ru.stalcraft.inventory;

import ru.stalcraft.inventory.shop.SlotShop;

public class StalkerSlot extends SlotShop {
   public uy parent;
   public ICustomContainer customContainer;

   public StalkerSlot(uy parent, ICustomContainer customContainer, mo inventory, int index, int x, int y) {
      super(inventory, index, x, y);
      this.parent = parent;
      this.customContainer = customContainer;
   }

   public StalkerSlot(uy parent, ICustomContainer customContainer, mo inventory, int id, int x, int y, int widthSize, int heightSize) {
      super(inventory, id, x, y, widthSize, heightSize);
      this.parent = parent;
      this.customContainer = customContainer;
   }

   public void f() {
      super.f();
   }

   public boolean b() {
      return this.customContainer.isSlotActive(this);
   }
}
