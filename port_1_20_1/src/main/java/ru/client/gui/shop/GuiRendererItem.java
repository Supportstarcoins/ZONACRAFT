package ru.stalcraft.client.gui.shop;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

public class GuiRendererItem {
   private static final bjo RES_ITEM_GLINT = new bjo("textures/misc/enchanted_item_glint.png");
   public boolean renderWithColor;
   public float zLevel;
   private bfr renderBlocks;
   private avi fontRenderer;
   private atv mc = atv.w();

   public GuiRendererItem() {
      this.fontRenderer = this.mc.l;
      this.renderBlocks = new bfr();
      this.renderWithColor = true;
      this.zLevel = 0.0F;
   }

   public void renderItemOverlayIntoGUI(ye par3ItemStack, int par4, int par5) {
      this.renderItemOverlayIntoGUI(par3ItemStack, par4, par5, (String)null);
   }

   public void renderItemOverlayIntoGUI(ye par3ItemStack, int par4, int par5, String par6Str) {
      if (par3ItemStack != null && (par3ItemStack.b > 1 || par6Str != null)) {
         String s1 = par6Str == null ? String.valueOf(par3ItemStack.b) : par6Str;
         GL11.glDisable(2896);
         GL11.glDisable(2929);
         this.fontRenderer.a(s1, par4 + 19 - 2 - this.fontRenderer.a(s1), par5 + 6 + 3, 16777215);
         GL11.glEnable(2896);
         GL11.glEnable(2929);
      }
   }

   public boolean renderInventoryItem(ye itemStack, boolean inColor, float zLevel, float x, float y, int sizeX, int sizeY) {
      IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemStack, ItemRenderType.INVENTORY);
      if (customRenderer == null) {
         return false;
      } else {
         this.mc.N.a(itemStack.d() == 0 ? bik.b : bik.c);
         if (customRenderer.shouldUseRenderHelper(ItemRenderType.INVENTORY, itemStack, ItemRendererHelper.INVENTORY_BLOCK)) {
            att.a();
            GL11.glPushMatrix();
            GL11.glTranslatef(x - 2.0F, y + 3.0F, -3.0F + zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            if (inColor) {
               int color = yc.g[itemStack.d].a(itemStack, 0);
               float r = (color >> 16 & 0xFF) / 255.0F;
               float g = (color >> 8 & 0xFF) / 255.0F;
               float b = (color & 0xFF) / 255.0F;
               GL11.glColor4f(r, g, b, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.renderBlocks.c = inColor;
            customRenderer.renderItem(ItemRenderType.INVENTORY, itemStack, new Object[]{this.renderBlocks});
            this.renderBlocks.c = true;
            GL11.glPopMatrix();
            att.c();
         } else {
            GL11.glDisable(2896);
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, -3.0F + zLevel);
            if (inColor) {
               int color = yc.g[itemStack.d].a(itemStack, 0);
               float r = (color >> 16 & 0xFF) / 255.0F;
               float g = (color >> 8 & 0xFF) / 255.0F;
               float b = (color & 0xFF) / 255.0F;
               GL11.glColor4f(r, g, b, 1.0F);
            }

            customRenderer.renderItem(ItemRenderType.INVENTORY, itemStack, new Object[]{this.renderBlocks});
            GL11.glPopMatrix();
            GL11.glEnable(2896);
         }

         return true;
      }
   }

   public void renderItemAndEffectIntoGUI(ye itemStack, int par4, int par5, int sizeX, int sizeY) {
      if (itemStack != null && !this.renderInventoryItem(itemStack, this.renderWithColor, this.zLevel, par4, par5, sizeX, sizeY)) {
         this.renderItemIntoGUI(itemStack, par4, par5, sizeX, sizeY, true);
      }
   }

   public void renderItemIntoGUI(ye itemStack, int x, int y, int sizeX, int sizeY) {
      this.renderItemIntoGUI(itemStack, x, y, sizeX, sizeY, false);
   }

   public void renderItemIntoGUI(ye par3ItemStack, int par4, int par5, int sizeX, int sizeY, boolean renderEffect) {
      int k = par3ItemStack.d;
      int l = par3ItemStack.k();
      yc item = yc.g[k];
      ms icon = par3ItemStack.c();
      aqz block = k < aqz.s.length ? aqz.s[k] : null;
      if (block != null && par3ItemStack.d() == 0 && block != null && bfr.a(block.d())) {
         this.mc.N.a(bik.b);
         GL11.glPushMatrix();
         GL11.glTranslatef(par4 - sizeX / 5 + 1, par5 + sizeY / 5, -3.0F + this.zLevel);
         GL11.glScalef(sizeX * 0.625F, sizeY * 0.625F, 10.0F);
         GL11.glTranslatef(1.0F, 0.5F, 1.0F);
         GL11.glScalef(1.0F, 1.0F, -1.0F);
         GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         int i1 = item.a(par3ItemStack, 0);
         float f2 = (i1 >> 16 & 0xFF) / 255.0F;
         float f = (i1 >> 8 & 0xFF) / 255.0F;
         float f1 = (i1 & 0xFF) / 255.0F;
         if (this.renderWithColor) {
            GL11.glColor4f(f2, f, f1, 1.0F);
         }

         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         this.renderBlocks.c = this.renderWithColor;
         this.renderBlocks.a(block, l, 1.0F);
         this.renderBlocks.c = true;
         GL11.glPopMatrix();
      } else if (item.b()) {
         GL11.glDisable(2896);
         int l2 = item.getRenderPasses(l);

         for (int j1 = 0; j1 < l2; j1++) {
            this.mc.N.a(par3ItemStack.d() == 0 ? bik.b : bik.c);
            icon = item.getIcon(par3ItemStack, j1);
            int k1 = item.a(par3ItemStack, j1);
            float f = (k1 >> 16 & 0xFF) / 255.0F;
            float f1 = (k1 >> 8 & 0xFF) / 255.0F;
            float f3 = (k1 & 0xFF) / 255.0F;
            if (this.renderWithColor) {
               GL11.glColor4f(f, f1, f3, 1.0F);
            }

            this.renderIcon(par4, par5, sizeX, sizeY, icon);
            if (par3ItemStack.hasEffect(j1)) {
               this.renderEffect(par4, par5, sizeX, sizeY);
            }
         }

         GL11.glEnable(2896);
      } else {
         GL11.glDisable(2896);
         bjo resourcelocation = this.mc.N.a(par3ItemStack.d());
         this.mc.N.a(resourcelocation);
         if (icon == null) {
            icon = ((bik)this.mc.N.b(resourcelocation)).b("missingno");
         }

         int i1 = item.a(par3ItemStack, 0);
         float f2 = (i1 >> 16 & 0xFF) / 255.0F;
         float fx = (i1 >> 8 & 0xFF) / 255.0F;
         float f1x = (i1 & 0xFF) / 255.0F;
         if (this.renderWithColor) {
            GL11.glColor4f(f2, fx, f1x, 1.0F);
         }

         this.renderIcon(par4, par5, sizeX, sizeY, icon);
         GL11.glEnable(2896);
         if (par3ItemStack.hasEffect(0)) {
            this.renderEffect(par4, par5, sizeX, sizeY);
         }
      }

      GL11.glEnable(2884);
   }

   private void renderEffect(int x, int y, int sizeX, int sizeY) {
      GL11.glDepthFunc(516);
      GL11.glDisable(2896);
      GL11.glDepthMask(false);
      this.mc.N.a(RES_ITEM_GLINT);
      this.zLevel -= 50.0F;
      GL11.glEnable(3042);
      GL11.glBlendFunc(774, 774);
      GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
      this.renderGlint(x * 431278612 + y * 32178161, x - 2, y - 2, 36, 36);
      GL11.glDisable(3042);
      GL11.glDepthMask(true);
      this.zLevel += 50.0F;
      GL11.glEnable(2896);
      GL11.glDepthFunc(515);
   }

   private void renderGlint(int par1, int par2, int par3, int par4, int par5) {
      for (int j1 = 0; j1 < 2; j1++) {
         if (j1 == 0) {
            GL11.glBlendFunc(768, 1);
         }

         if (j1 == 1) {
            GL11.glBlendFunc(768, 1);
         }

         float f = 0.00390625F;
         float f1 = 0.00390625F;
         float f2 = (float)(atv.F() % (3000 + j1 * 1873)) / (3000.0F + j1 * 1873) * 256.0F;
         float f3 = 0.0F;
         bfq tessellator = bfq.a;
         float f4 = 4.0F;
         if (j1 == 1) {
            f4 = -1.0F;
         }

         tessellator.b();
         tessellator.a(par2 + 0, par3 + par5, this.zLevel, (f2 + par5 * f4) * f, (f3 + par5) * f1);
         tessellator.a(par2 + par4, par3 + par5, this.zLevel, (f2 + par4 + par5 * f4) * f, (f3 + par5) * f1);
         tessellator.a(par2 + par4, par3 + 0, this.zLevel, (f2 + par4) * f, (f3 + 0.0F) * f1);
         tessellator.a(par2 + 0, par3 + 0, this.zLevel, (f2 + 0.0F) * f, (f3 + 0.0F) * f1);
         tessellator.a();
      }
   }

   public void renderIcon(int width, int height, int sizeWidth, int sizeHeight, ms icon) {
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(width + 0, height + sizeHeight, this.zLevel, icon.c(), icon.f());
      tessellator.a(width + sizeWidth, height + sizeHeight, this.zLevel, icon.d(), icon.f());
      tessellator.a(width + sizeWidth, height + 0, this.zLevel, icon.d(), icon.e());
      tessellator.a(width + 0, height + 0, this.zLevel, icon.c(), icon.e());
      tessellator.a();
   }
}
