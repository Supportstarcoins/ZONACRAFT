package ru.stalcraft.client.gui.shop;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.inventory.shop.SlotShop;

public abstract class GuiCustomContainer extends awe {
   protected static final bjo field_110408_a = new bjo("textures/gui/container/inventory.png");
   protected static GuiRendererItem itemRenderer = new GuiRendererItem();
   protected int xSize = 176;
   protected int ySize = 166;
   public uy inventorySlots;
   protected int guiLeft;
   protected int guiTop;
   private we theSlot;
   private we clickedSlot;
   private boolean isRightMouseClick;
   private ye draggedStack;
   private int field_85049_r;
   private int field_85048_s;
   private we returningStackDestSlot;
   private long returningStackTime;
   private ye returningStack;
   private we field_92033_y;
   private long field_92032_z;
   protected final Set field_94077_p = new HashSet();
   protected boolean field_94076_q;
   private int field_94071_C;
   private int field_94067_D;
   private boolean field_94068_E;
   private int field_94069_F;
   private long field_94070_G;
   private we field_94072_H;
   private int field_94073_I;
   private boolean field_94074_J;
   private ye field_94075_K;

   public GuiCustomContainer(uy par1Container) {
      this.inventorySlots = par1Container;
      this.field_94068_E = true;
   }

   public void A_() {
      super.A_();
      this.f.h.bp = this.inventorySlots;
      this.guiLeft = (this.g - this.xSize) / 2;
      this.guiTop = (this.h - this.ySize) / 2;
   }

   public void a(int par1, int par2, float par3) {
      this.e();
      int k = this.guiLeft;
      int l = this.guiTop;
      this.drawGuiContainerBackgroundLayer(par3, par1, par2);
      GL11.glDisable(32826);
      att.a();
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      super.a(par1, par2, par3);
      att.c();
      GL11.glPushMatrix();
      GL11.glTranslatef(k, l, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(32826);
      this.theSlot = null;
      short short1 = 240;
      short short2 = 240;
      bma.a(bma.b, short1 / 1.0F, short2 / 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

      for (int j1 = 0; j1 < this.inventorySlots.c.size(); j1++) {
         we slot = (we)this.inventorySlots.c.get(j1);
         this.drawSlotShop(slot);
         if (this.isMouseOverSlot(slot, par1, par2) && slot.b()) {
            this.theSlot = slot;
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            int k1 = slot.h;
            int i1 = slot.i;
            if (slot instanceof SlotShop) {
               SlotShop slotInv = (SlotShop)slot;
               this.a(k1, i1, k1 + slotInv.widthSize, i1 + slotInv.heightSize, -2130706433, -2130706433);
            } else {
               this.a(k1, i1, k1 + 16, i1 + 16, -2130706433, -2130706433);
            }

            GL11.glEnable(2896);
            GL11.glEnable(2929);
         }
      }

      GL11.glDisable(2896);
      this.drawGuiContainerForegroundLayer(par1, par2);
      GL11.glEnable(2896);
      ud inventoryplayer = this.f.h.bn;
      ye itemstack = this.draggedStack == null ? inventoryplayer.o() : this.draggedStack;
      if (itemstack != null) {
         byte b0 = 8;
         int i1 = this.draggedStack == null ? 8 : 16;
         String s = null;
         if (this.draggedStack != null && this.isRightMouseClick) {
            itemstack = itemstack.m();
            itemstack.b = ls.f(itemstack.b / 2.0F);
         } else if (this.field_94076_q && this.field_94077_p.size() > 1) {
            itemstack = itemstack.m();
            itemstack.b = this.field_94069_F;
            if (itemstack.b == 0) {
               s = "" + a.o + "0";
            }
         }

         this.drawItemStack(this.theSlot, itemstack, par1 - k - b0, par2 - l - i1, s);
      }

      if (this.returningStack != null) {
         float f1 = (float)(atv.F() - this.returningStackTime) / 100.0F;
         if (f1 >= 1.0F) {
            f1 = 1.0F;
            this.returningStack = null;
         }

         int i1 = this.returningStackDestSlot.h - this.field_85049_r;
         int l1 = this.returningStackDestSlot.i - this.field_85048_s;
         int i2 = this.field_85049_r + (int)(i1 * f1);
         int j2 = this.field_85048_s + (int)(l1 * f1);
         this.drawItemStack(this.theSlot, this.returningStack, i2, j2, (String)null);
      }

      GL11.glPopMatrix();
      if (inventoryplayer.o() == null && this.theSlot != null && this.theSlot.e()) {
         ye itemstack1 = this.theSlot.d();
         this.drawItemStackTooltip(itemstack1, par1, par2);
      }

      GL11.glEnable(2896);
      GL11.glEnable(2929);
      att.b();
   }

   private void drawItemStack(we slot, ye par1ItemStack, int par2, int par3, String par4Str) {
      GL11.glTranslatef(0.0F, 0.0F, 32.0F);
      this.n = 200.0F;
      itemRenderer.zLevel = 200.0F;
      avi font = null;
      if (par1ItemStack != null) {
         font = par1ItemStack.b().getFontRenderer(par1ItemStack);
      }

      if (font == null) {
         font = this.o;
      }

      if (slot instanceof SlotShop) {
         SlotShop slotInv = (SlotShop)slot;
         itemRenderer.renderItemAndEffectIntoGUI(par1ItemStack, par2, par3, slotInv.widthSize, slotInv.heightSize);
         itemRenderer.renderItemOverlayIntoGUI(par1ItemStack, par2, par3 - (this.draggedStack == null ? 0 : 8), par4Str);
      } else {
         itemRenderer.renderItemAndEffectIntoGUI(par1ItemStack, par2, par3, 16, 16);
         itemRenderer.renderItemOverlayIntoGUI(par1ItemStack, par2, par3 - (this.draggedStack == null ? 0 : 8), par4Str);
      }

      this.n = 0.0F;
      itemRenderer.zLevel = 0.0F;
   }

   protected void drawItemStackTooltip(ye par1ItemStack, int par2, int par3) {
      List list = par1ItemStack.a(this.f.h, this.f.u.x);

      for (int k = 0; k < list.size(); k++) {
         if (k == 0) {
            list.set(k, "§" + Integer.toHexString(par1ItemStack.w().e) + (String)list.get(k));
         } else {
            list.set(k, a.h + (String)list.get(k));
         }
      }

      avi font = par1ItemStack.b().getFontRenderer(par1ItemStack);
      this.drawHoveringText(list, par2, par3, font == null ? this.o : font);
   }

   protected void drawCreativeTabHoveringText(String par1Str, int par2, int par3) {
      this.func_102021_a(Arrays.asList(par1Str), par2, par3);
   }

   protected void func_102021_a(List par1List, int par2, int par3) {
      this.drawHoveringText(par1List, par2, par3, this.o);
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

         if (i1 + k > this.g) {
            i1 -= 28 + k;
         }

         if (j1 + k1 + 6 > this.h) {
            j1 = this.h - k1 - 6;
         }

         this.n = 300.0F;
         itemRenderer.zLevel = 300.0F;
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
         itemRenderer.zLevel = 0.0F;
         GL11.glEnable(2896);
         GL11.glEnable(2929);
         att.b();
         GL11.glEnable(32826);
      }
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
   }

   protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

   protected void drawSlotShop(we par1Slot) {
      int i = par1Slot.h;
      int j = par1Slot.i;
      ye itemstack = par1Slot.d();
      boolean flag = false;
      boolean flag1 = par1Slot == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
      ye itemstack1 = this.f.h.bn.o();
      String s = null;
      if (par1Slot == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
         itemstack = itemstack.m();
         itemstack.b /= 2;
      } else if (this.field_94076_q && this.field_94077_p.contains(par1Slot) && itemstack1 != null) {
         if (this.field_94077_p.size() == 1) {
            return;
         }

         if (uy.a(par1Slot, itemstack1, true) && this.inventorySlots.b(par1Slot)) {
            itemstack = itemstack1.m();
            flag = true;
            uy.a(this.field_94077_p, this.field_94071_C, itemstack, par1Slot.d() == null ? 0 : par1Slot.d().b);
            if (itemstack.b > itemstack.e()) {
               s = a.o + "" + itemstack.e();
               itemstack.b = itemstack.e();
            }

            if (itemstack.b > par1Slot.a()) {
               s = a.o + "" + par1Slot.a();
               itemstack.b = par1Slot.a();
            }
         } else {
            this.field_94077_p.remove(par1Slot);
            this.func_94066_g();
         }
      }

      this.n = 100.0F;
      itemRenderer.zLevel = 100.0F;
      if (itemstack == null) {
         ms icon = par1Slot.c();
         if (icon != null) {
            GL11.glDisable(2896);
            this.f.J().a(bik.c);
            this.a(i, j, icon, 16, 16);
            GL11.glEnable(2896);
            flag1 = true;
         }
      }

      if (!flag1) {
         if (flag) {
            if (par1Slot instanceof SlotShop) {
               SlotShop slotInv = (SlotShop)par1Slot;
               a(i, j, i + slotInv.widthSize, j + slotInv.heightSize, -2130706433);
            } else {
               a(i, j, i + 16, j + 16, -2130706433);
            }
         }

         GL11.glEnable(2929);
         if (par1Slot instanceof SlotShop) {
            SlotShop slotInv = (SlotShop)par1Slot;
            itemRenderer.renderItemAndEffectIntoGUI(itemstack, i, j, slotInv.widthSize, slotInv.heightSize);
            itemRenderer.renderItemOverlayIntoGUI(itemstack, i + (slotInv.widthSize - 16), j + (slotInv.heightSize - 16), s);
         } else {
            itemRenderer.renderItemAndEffectIntoGUI(itemstack, i, j, 16, 16);
            itemRenderer.renderItemOverlayIntoGUI(itemstack, i, j - (this.draggedStack == null ? 0 : 8), s);
         }
      }

      itemRenderer.zLevel = 0.0F;
      this.n = 0.0F;
   }

   private void func_94066_g() {
      ye itemstack = this.f.h.bn.o();
      if (itemstack != null && this.field_94076_q) {
         this.field_94069_F = itemstack.b;

         for (we slot : this.field_94077_p) {
            ye itemstack1 = itemstack.m();
            int i = slot.d() == null ? 0 : slot.d().b;
            uy.a(this.field_94077_p, this.field_94071_C, itemstack1, i);
            if (itemstack1.b > itemstack1.e()) {
               itemstack1.b = itemstack1.e();
            }

            if (itemstack1.b > slot.a()) {
               itemstack1.b = slot.a();
            }

            this.field_94069_F = this.field_94069_F - (itemstack1.b - i);
         }
      }
   }

   private we getSlotAtPosition(int par1, int par2) {
      for (int k = 0; k < this.inventorySlots.c.size(); k++) {
         we slot = (we)this.inventorySlots.c.get(k);
         if (this.isMouseOverSlot(slot, par1, par2)) {
            return slot;
         }
      }

      return null;
   }

   protected void a(int par1, int par2, int par3) {
      super.a(par1, par2, par3);
      boolean flag = par3 == this.f.u.U.d + 100;
      we slot = this.getSlotAtPosition(par1, par2);
      long l = atv.F();
      this.field_94074_J = this.field_94072_H == slot && l - this.field_94070_G < 250L && this.field_94073_I == par3;
      this.field_94068_E = false;
      if (par3 == 0 || par3 == 1 || flag) {
         int i1 = this.guiLeft;
         int j1 = this.guiTop;
         boolean flag1 = par1 < i1 || par2 < j1 || par1 >= i1 + this.xSize || par2 >= j1 + this.ySize;
         int k1 = -1;
         if (slot != null) {
            k1 = slot.g;
         }

         if (flag1) {
            k1 = -999;
         }

         if (this.f.u.A && flag1 && this.f.h.bn.o() == null) {
            this.f.a((awe)null);
            return;
         }

         if (k1 != -1) {
            if (this.f.u.A) {
               if (slot != null && slot.e()) {
                  this.clickedSlot = slot;
                  this.draggedStack = null;
                  this.isRightMouseClick = par3 == 1;
               } else {
                  this.clickedSlot = null;
               }
            } else if (!this.field_94076_q) {
               if (this.f.h.bn.o() == null) {
                  if (par3 == this.f.u.U.d + 100) {
                     this.handleMouseClick(slot, k1, par3, 3);
                  } else {
                     boolean flag2 = k1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                     byte b0 = 0;
                     if (flag2) {
                        this.field_94075_K = slot != null && slot.e() ? slot.d() : null;
                        b0 = 1;
                     } else if (k1 == -999) {
                        b0 = 4;
                     }

                     this.handleMouseClick(slot, k1, par3, b0);
                  }

                  this.field_94068_E = true;
               } else {
                  this.field_94076_q = true;
                  this.field_94067_D = par3;
                  this.field_94077_p.clear();
                  if (par3 == 0) {
                     this.field_94071_C = 0;
                  } else if (par3 == 1) {
                     this.field_94071_C = 1;
                  }
               }
            }
         }
      }

      this.field_94072_H = slot;
      this.field_94070_G = l;
      this.field_94073_I = par3;
   }

   protected void a(int par1, int par2, int par3, long par4) {
      we slot = this.getSlotAtPosition(par1, par2);
      ye itemstack = this.f.h.bn.o();
      if (this.clickedSlot != null && this.f.u.A) {
         if (par3 == 0 || par3 == 1) {
            if (this.draggedStack == null) {
               if (slot != this.clickedSlot) {
                  this.draggedStack = this.clickedSlot.d().m();
               }
            } else if (this.draggedStack.b > 1 && slot != null && uy.a(slot, this.draggedStack, false)) {
               long i1 = atv.F();
               if (this.field_92033_y == slot) {
                  if (i1 - this.field_92032_z > 500L) {
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.g, 0, 0);
                     this.handleMouseClick(slot, slot.g, 1, 0);
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.g, 0, 0);
                     this.field_92032_z = i1 + 750L;
                     this.draggedStack.b--;
                  }
               } else {
                  this.field_92033_y = slot;
                  this.field_92032_z = i1;
               }
            }
         }
      } else if (this.field_94076_q
         && slot != null
         && itemstack != null
         && itemstack.b > this.field_94077_p.size()
         && uy.a(slot, itemstack, true)
         && slot.a(itemstack)
         && this.inventorySlots.b(slot)) {
         this.field_94077_p.add(slot);
         this.func_94066_g();
      }
   }

   protected void b(int par1, int par2, int par3) {
      we slot = this.getSlotAtPosition(par1, par2);
      int l = this.guiLeft;
      int i1 = this.guiTop;
      boolean flag = par1 < l || par2 < i1 || par1 >= l + this.xSize || par2 >= i1 + this.ySize;
      int j1 = -1;
      if (slot != null) {
         j1 = slot.g;
      }

      if (flag) {
         j1 = -999;
      }

      if (this.field_94074_J && slot != null && par3 == 0 && this.inventorySlots.a((ye)null, slot)) {
         if (p()) {
            if (slot != null && slot.f != null && this.field_94075_K != null) {
               for (we slot1 : this.inventorySlots.c) {
                  if (slot1 != null && slot1.a(this.f.h) && slot1.e() && slot1.f == slot.f && uy.a(slot1, this.field_94075_K, true)) {
                     this.handleMouseClick(slot1, slot1.g, par3, 1);
                  }
               }
            }
         } else {
            this.handleMouseClick(slot, j1, par3, 6);
         }

         this.field_94074_J = false;
         this.field_94070_G = 0L;
      } else {
         if (this.field_94076_q && this.field_94067_D != par3) {
            this.field_94076_q = false;
            this.field_94077_p.clear();
            this.field_94068_E = true;
            return;
         }

         if (this.field_94068_E) {
            this.field_94068_E = false;
            return;
         }

         if (this.clickedSlot != null && this.f.u.A) {
            if (par3 == 0 || par3 == 1) {
               if (this.draggedStack == null && slot != this.clickedSlot) {
                  this.draggedStack = this.clickedSlot.d();
               }

               boolean flag1 = uy.a(slot, this.draggedStack, false);
               if (j1 != -1 && this.draggedStack != null && flag1) {
                  this.handleMouseClick(this.clickedSlot, this.clickedSlot.g, par3, 0);
                  this.handleMouseClick(slot, j1, 0, 0);
                  if (this.f.h.bn.o() != null) {
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.g, par3, 0);
                     this.field_85049_r = par1 - l;
                     this.field_85048_s = par2 - i1;
                     this.returningStackDestSlot = this.clickedSlot;
                     this.returningStack = this.draggedStack;
                     this.returningStackTime = atv.F();
                  } else {
                     this.returningStack = null;
                  }
               } else if (this.draggedStack != null) {
                  this.field_85049_r = par1 - l;
                  this.field_85048_s = par2 - i1;
                  this.returningStackDestSlot = this.clickedSlot;
                  this.returningStack = this.draggedStack;
                  this.returningStackTime = atv.F();
               }

               this.draggedStack = null;
               this.clickedSlot = null;
            }
         } else if (this.field_94076_q && !this.field_94077_p.isEmpty()) {
            this.handleMouseClick((we)null, -999, uy.d(0, this.field_94071_C), 5);

            for (we slot1x : this.field_94077_p) {
               this.handleMouseClick(slot1x, slot1x.g, uy.d(1, this.field_94071_C), 5);
            }

            this.handleMouseClick((we)null, -999, uy.d(2, this.field_94071_C), 5);
         } else if (this.f.h.bn.o() != null) {
            if (par3 == this.f.u.U.d + 100) {
               this.handleMouseClick(slot, j1, par3, 3);
            } else {
               boolean flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
               if (flag1) {
                  this.field_94075_K = slot != null && slot.e() ? slot.d() : null;
               }

               this.handleMouseClick(slot, j1, par3, flag1 ? 1 : 0);
            }
         }
      }

      if (this.f.h.bn.o() == null) {
         this.field_94070_G = 0L;
      }

      this.field_94076_q = false;
   }

   private boolean isMouseOverSlot(we par1Slot, int par2, int par3) {
      if (par1Slot instanceof SlotShop) {
         SlotShop slot = (SlotShop)par1Slot;
         return this.isPointInRegion(par1Slot.h, par1Slot.i, slot.widthSize, slot.heightSize, par2, par3);
      } else {
         return this.isPointInRegion(par1Slot.h, par1Slot.i, 16, 16, par2, par3);
      }
   }

   protected boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6) {
      int k1 = this.guiLeft;
      int l1 = this.guiTop;
      par5 -= k1;
      par6 -= l1;
      return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
   }

   protected void handleMouseClick(we par1Slot, int par2, int par3, int par4) {
      if (par1Slot != null) {
         par2 = par1Slot.g;
      }

      this.f.c.a(this.inventorySlots.d, par2, par3, par4, this.f.h);
   }

   protected void a(char par1, int par2) {
      if (par2 == 1 || par2 == this.f.u.N.d) {
         this.f.h.i();
      }

      this.checkHotbarKeys(par2);
      if (this.theSlot != null && this.theSlot.e()) {
         if (par2 == this.f.u.U.d) {
            this.handleMouseClick(this.theSlot, this.theSlot.g, 0, 3);
         } else if (par2 == this.f.u.O.d) {
            this.handleMouseClick(this.theSlot, this.theSlot.g, o() ? 1 : 0, 4);
         }
      }
   }

   protected boolean checkHotbarKeys(int par1) {
      if (this.f.h.bn.o() == null && this.theSlot != null) {
         for (int j = 0; j < 9; j++) {
            if (par1 == 2 + j) {
               this.handleMouseClick(this.theSlot, this.theSlot.g, j, 2);
               return true;
            }
         }
      }

      return false;
   }

   public void b() {
      if (this.f.h != null) {
         this.inventorySlots.b(this.f.h);
      }
   }

   public boolean f() {
      return false;
   }

   public void c() {
      super.c();
      if (!this.f.h.T() || this.f.h.M) {
         this.f.h.i();
      }
   }

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
