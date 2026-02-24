package ru.stalcraft.inventory.shop;

public class SlotShop extends we {
   public final int widthSize;
   public final int heightSize;

   public SlotShop(mo inventory, int id, int width, int height) {
      super(inventory, id, width, height);
      this.widthSize = 16;
      this.heightSize = 16;
   }

   public SlotShop(mo inventory, int id, int width, int height, int widthSize, int heightSize) {
      super(inventory, id, width, height);
      this.widthSize = widthSize;
      this.heightSize = heightSize;
   }
}
