package ru.stalcraft.client.gui.shop;

import java.util.List;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemPNV;
import ru.stalcraft.items.ItemSkin;
import ru.stalcraft.items.ItemWeapon;

public class GuiSlotItemButton extends aut {
   public bjo slot = new bjo("stalker", "textures/slot.png");
   public bgw renderItem;
   public ye stack;
   public int click;

   public GuiSlotItemButton(int id, GuiSlotItem parent) {
      super(id, 0, 0, "");
      this.stack = new ye(parent.shopItem.item);
      if (this.stack.q() != null) {
         yc item = this.stack.b();
         if (item instanceof ItemWeapon || item instanceof ItemArmorArtefakt || item instanceof ItemSkin || item instanceof ItemPNV) {
            this.stack.q().a("isNoDrop", true);
         }
      }
   }

   public void drawButton(atv mc, bgw renderItem, int mouseX, int mouseY, int x, int y, GuiSlotItem parent) {
      if (this.i) {
         this.renderItem = renderItem;
         this.d = x;
         this.e = y;
         this.b = 40;
         this.c = 46;
         mc.N.a(this.slot);
         parent.drawTexturedModalRect(x, y + 7, 0, 0, 36, 36, 36, 1.12);
         GL11.glPushMatrix();
         float scale = 2.3F;
         GL11.glScalef(scale, scale, 1.0F);
         GL11.glDisable(2896);
         GL11.glDisable(2929);
         att.c();
         renderItem.a(mc.l, mc.N, new ye(parent.shopItem.item), (int)((x + 3) / scale), (int)((y + 10) / scale));
         GL11.glEnable(2896);
         GL11.glEnable(2929);
         att.a();
         GL11.glPopMatrix();
         this.a(mc.l, parent.shopItem.item.v(), x + 94, y + 2, -1);
         this.a(mc.l, parent.shopItem.price + " руб.", x + 60, y + 16, -1);
         if (mouseX >= this.d && mouseY >= this.e && mouseX < this.d + this.b && mouseY < this.e + this.c) {
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 770);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            parent.drawTexturedModalRect(x + 2, y + 9, 0, 0, 36, 36, 36, 1.0, 0.2F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            boolean clickMouse = Mouse.isButtonDown(0);
            if (!clickMouse && this.click > 0) {
               this.click = 0;
            }

            if (this.click <= 0 && clickMouse) {
               this.click = 1;
               if (parent.shopItem.item instanceof ItemArmorArtefakt) {
                  parent.setDrawArmor(!parent.renderArmor);
               } else {
                  parent.inUse = !parent.inUse;
               }
            }
         }
      }
   }

   protected void drawStack(atv mc, ye par1ItemStack, int par2, int par3) {
      List list = par1ItemStack.a(mc.h, mc.u.x);

      for (int k = 0; k < list.size(); k++) {
         if (k == 0) {
            list.set(k, "§" + Integer.toHexString(par1ItemStack.w().e) + (String)list.get(k));
         } else {
            list.set(k, a.h + (String)list.get(k));
         }
      }

      avi font = par1ItemStack.b().getFontRenderer(par1ItemStack);
      this.drawHoveringText(list, par2, par3, font == null ? mc.l : font);
   }

   protected void drawHoveringText(List par1List, int par2, int par3, avi font) {
      if (!par1List.isEmpty()) {
         GL11.glDisable(32826);
         att.a();
         GL11.glDisable(2896);
         GL11.glDisable(2929);
         int k = 0;

         for (String s : par1List) {
            int l = font.a(s);
            if (l > k) {
               k = l;
            }
         }

         int i1 = par2 + 12;
         int j1 = par3 - 12;
         int k1 = 8;
         if (par1List.size() > 1) {
            k1 += 2 + (par1List.size() - 1) * 10;
         }

         this.renderItem.f = 300.0F;
         this.renderItem.f = 300.0F;
         int l1 = -267386864;
         this.a(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
         this.a(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
         this.a(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
         this.a(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
         this.a(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
         int i2 = 1347420415;
         int j2 = (i2 & 16711422) >> 1 | i2 & 0xFF000000;
         this.a(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
         this.a(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
         this.a(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
         this.a(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

         for (int k2 = 0; k2 < par1List.size(); k2++) {
            String s1 = (String)par1List.get(k2);
            font.a(s1, i1, j1, -1);
            if (k2 == 0) {
               j1 += 2;
            }

            j1 += 10;
         }

         this.n = 0.0F;
         this.renderItem.f = 0.0F;
         GL11.glEnable(2896);
         GL11.glEnable(2929);
         att.b();
         GL11.glEnable(32826);
      }
   }

   public void drawScreen(atv mc, bgw renderItem, int mouseX, int mouseY, int x, int y, GuiSlotItem parent) {
      if (mouseX >= this.d && mouseY >= this.e && mouseX < this.d + this.b && mouseY < this.e + this.c) {
         this.drawStack(mc, this.stack, mouseX, mouseY);
      }
   }
}
