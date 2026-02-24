package ru.stalcraft.client.gui.shop;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.inventory.shop.ContainerPersonalWarehouse;

@SideOnly(Side.CLIENT)
public class GuiPersonalWarehouse extends awy {
   private static final bjo field_110421_t = new bjo("textures/gui/container/generic_54.png");
   private mo upperChestInventory;
   private mo lowerChestInventory;
   private int inventoryRows;

   public GuiPersonalWarehouse(mo par1IInventory, mo par2IInventory) {
      super(new ContainerPersonalWarehouse(par1IInventory, par2IInventory));
      this.upperChestInventory = par1IInventory;
      this.lowerChestInventory = par2IInventory;
      this.j = false;
      short short1 = 222;
      int i = short1 - 108;
      this.inventoryRows = par2IInventory.j_() / 9;
      this.d = i + this.inventoryRows * 18;
   }

   protected void a(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.f.J().a(field_110421_t);
      int k = (this.g - this.c) / 2;
      int l = (this.h - this.d) / 2;
      this.b(k, l, 0, 0, this.c, this.inventoryRows * 18 + 17);
      this.b(k, l + this.inventoryRows * 18 + 17, 0, 126, this.c, 96);
      super.a(this.f.l, "Персональный склад", super.g / 2 - 42, super.h / 2 - 79, -1);
   }
}
