package ru.stalcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.inventory.ICustomContainer;

@SideOnly(Side.CLIENT)
public class GuiOtherPlayerInventory extends axp {
   private float xSize_lo;
   private float ySize_lo;
   public ICustomContainer customContainer;
   private static final int X_INV_SIZE = 227;
   private static final int Y_INV_SIZE = 181;
   public static final bjo commonInventory = new bjo("stalker", "textures/inventory.png");
   public static final bjo backpackInventory = new bjo("stalker", "textures/backpack.png");

   public GuiOtherPlayerInventory(uy container, ICustomContainer customContainer) {
      super(container);
      super.j = true;
      this.customContainer = customContainer;
   }

   public void c() {
   }

   public void A_() {
      super.i.clear();
      super.c = 227;
      super.d = 181;
      super.p = super.g / 2 - super.c / 2;
      super.q = super.h / 2 - super.d / 2;
   }

   public void a(int par1, int par2, float par3) {
      super.a(par1, par2, par3);
      this.xSize_lo = par1;
      this.ySize_lo = par2;
   }

   protected void a(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      if (this.customContainer.hasBackpack()) {
         atv.w().N.a(backpackInventory);
      } else {
         atv.w().N.a(commonInventory);
      }

      this.b(super.g / 2 - 113, super.h / 2 - 90, 0, 0, 227, 181);
      super.a(this.f.l, "Чтобы забрать предмет, кликните по нему ПКМ", super.g / 2, super.h / 2 - 110, -1);
   }

   protected void b(int par1, int par2) {
   }

   protected void a(aut par1GuiButton) {
      if (par1GuiButton.g == 0) {
         super.f.a(new awq(super.f.y));
      }

      if (par1GuiButton.g == 1) {
         super.f.a(new awr(this, super.f.y));
      }
   }
}
