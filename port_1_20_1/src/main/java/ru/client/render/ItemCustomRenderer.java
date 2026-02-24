package ru.stalcraft.client.render;

import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import org.lwjgl.opengl.GL11;

public class ItemCustomRenderer extends bfj {
   private static final bjo b = new bjo("textures/misc/enchanted_item_glint.png");
   private static final bjo c = new bjo("textures/map/map_background.png");
   private static final bjo d = new bjo("textures/misc/underwater.png");
   private atv e;
   private ye f;
   private float g;
   private float h;
   private bfr i = new bfr();
   public final avv a;
   private int j = -1;

   public ItemCustomRenderer(atv par1Minecraft) {
      super(par1Minecraft);
      this.e = par1Minecraft;
      this.a = new avv(par1Minecraft.u, par1Minecraft.J());
   }

   public void a(of par1EntityLivingBase, ye par2ItemStack, int par3) {
      this.renderItem(par1EntityLivingBase, par2ItemStack, par3, ItemRenderType.EQUIPPED);
   }

   public void renderItem(of par1EntityLivingBase, ye par2ItemStack, int par3, ItemRenderType type) {
      GL11.glPushMatrix();
      bim texturemanager = this.e.J();
      aqz block = null;
      if (par2ItemStack.b() instanceof zh && par2ItemStack.d < aqz.s.length) {
         block = aqz.s[par2ItemStack.d];
      }

      IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(par2ItemStack, type);
      if (customRenderer != null) {
         texturemanager.a(texturemanager.a(par2ItemStack.d()));
         ForgeHooksClient.renderEquippedItem(type, customRenderer, this.i, par1EntityLivingBase, par2ItemStack);
      } else if (block != null && par2ItemStack.d() == 0 && bfr.a(aqz.s[par2ItemStack.d].d())) {
         texturemanager.a(texturemanager.a(0));
         this.i.a(aqz.s[par2ItemStack.d], par2ItemStack.k(), 1.0F);
      } else {
         ms icon = par1EntityLivingBase.b(par2ItemStack, par3);
         if (icon == null) {
            GL11.glPopMatrix();
            return;
         }

         texturemanager.a(texturemanager.a(par2ItemStack.d()));
         bfq tessellator = bfq.a;
         float f = icon.c();
         float f1 = icon.d();
         float f2 = icon.e();
         float f3 = icon.f();
         float f4 = 0.0F;
         float f5 = 0.3F;
         GL11.glEnable(32826);
         GL11.glTranslatef(-f4, -f5, 0.0F);
         float f6 = 1.5F;
         GL11.glScalef(f6, f6, f6);
         GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
         a(tessellator, f1, f2, f, f3, icon.a(), icon.b(), 0.0625F);
         if (par2ItemStack.hasEffect(par3)) {
            GL11.glDepthFunc(514);
            GL11.glDisable(2896);
            texturemanager.a(b);
            GL11.glEnable(3042);
            GL11.glBlendFunc(768, 1);
            float f7 = 0.76F;
            GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            float f8 = 0.125F;
            GL11.glScalef(f8, f8, f8);
            float f9 = (float)(atv.F() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(f9, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            a(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f8, f8, f8);
            f9 = (float)(atv.F() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-f9, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            a(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glDepthFunc(515);
         }

         GL11.glDisable(32826);
      }

      GL11.glPopMatrix();
   }

   public static void a(bfq par0Tessellator, float par1, float par2, float par3, float par4, int par5, int par6, float par7) {
      par0Tessellator.b();
      par0Tessellator.b(0.0F, 0.0F, 1.0F);
      par0Tessellator.a(0.0, 0.0, 0.0, par1, par4);
      par0Tessellator.a(1.0, 0.0, 0.0, par3, par4);
      par0Tessellator.a(1.0, 1.0, 0.0, par3, par2);
      par0Tessellator.a(0.0, 1.0, 0.0, par1, par2);
      par0Tessellator.a();
      par0Tessellator.b();
      par0Tessellator.b(0.0F, 0.0F, -1.0F);
      par0Tessellator.a(0.0, 1.0, 0.0F - par7, par1, par2);
      par0Tessellator.a(1.0, 1.0, 0.0F - par7, par3, par2);
      par0Tessellator.a(1.0, 0.0, 0.0F - par7, par3, par4);
      par0Tessellator.a(0.0, 0.0, 0.0F - par7, par1, par4);
      par0Tessellator.a();
      float f5 = 0.5F * (par1 - par3) / par5;
      float f6 = 0.5F * (par4 - par2) / par6;
      par0Tessellator.b();
      par0Tessellator.b(-1.0F, 0.0F, 0.0F);

      for (int k = 0; k < par5; k++) {
         float f7 = (float)k / par5;
         float f8 = par1 + (par3 - par1) * f7 - f5;
         par0Tessellator.a(f7, 0.0, 0.0F - par7, f8, par4);
         par0Tessellator.a(f7, 0.0, 0.0, f8, par4);
         par0Tessellator.a(f7, 1.0, 0.0, f8, par2);
         par0Tessellator.a(f7, 1.0, 0.0F - par7, f8, par2);
      }

      par0Tessellator.a();
      par0Tessellator.b();
      par0Tessellator.b(1.0F, 0.0F, 0.0F);

      for (int var14 = 0; var14 < par5; var14++) {
         float f7 = (float)var14 / par5;
         float f8 = par1 + (par3 - par1) * f7 - f5;
         float f9 = f7 + 1.0F / par5;
         par0Tessellator.a(f9, 1.0, 0.0F - par7, f8, par2);
         par0Tessellator.a(f9, 1.0, 0.0, f8, par2);
         par0Tessellator.a(f9, 0.0, 0.0, f8, par4);
         par0Tessellator.a(f9, 0.0, 0.0F - par7, f8, par4);
      }

      par0Tessellator.a();
      par0Tessellator.b();
      par0Tessellator.b(0.0F, 1.0F, 0.0F);

      for (int var15 = 0; var15 < par6; var15++) {
         float f7 = (float)var15 / par6;
         float f8 = par4 + (par2 - par4) * f7 - f6;
         float f9 = f7 + 1.0F / par6;
         par0Tessellator.a(0.0, f9, 0.0, par1, f8);
         par0Tessellator.a(1.0, f9, 0.0, par3, f8);
         par0Tessellator.a(1.0, f9, 0.0F - par7, par3, f8);
         par0Tessellator.a(0.0, f9, 0.0F - par7, par1, f8);
      }

      par0Tessellator.a();
      par0Tessellator.b();
      par0Tessellator.b(0.0F, -1.0F, 0.0F);

      for (int var16 = 0; var16 < par6; var16++) {
         float f7 = (float)var16 / par6;
         float f8 = par4 + (par2 - par4) * f7 - f6;
         par0Tessellator.a(1.0, f7, 0.0, par3, f8);
         par0Tessellator.a(0.0, f7, 0.0, par1, f8);
         par0Tessellator.a(0.0, f7, 0.0F - par7, par1, f8);
         par0Tessellator.a(1.0, f7, 0.0F - par7, par3, f8);
      }

      par0Tessellator.a();
   }

   public void a(float par1) {
      float f1 = this.h + (this.g - this.h) * par1;
      bdi entityclientplayermp = this.e.h;
      float f2 = entityclientplayermp.D + (entityclientplayermp.B - entityclientplayermp.D) * par1;
      GL11.glPushMatrix();
      GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(entityclientplayermp.C + (entityclientplayermp.A - entityclientplayermp.C) * par1, 0.0F, 1.0F, 0.0F);
      att.b();
      GL11.glPopMatrix();
      float f3 = entityclientplayermp.j + (entityclientplayermp.h - entityclientplayermp.j) * par1;
      float f4 = entityclientplayermp.i + (entityclientplayermp.g - entityclientplayermp.i) * par1;
      GL11.glRotatef((entityclientplayermp.B - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef((entityclientplayermp.A - f4) * 0.1F, 0.0F, 1.0F, 0.0F);
      ye itemstack = this.f;
      float f5 = this.e.f.q(ls.c(entityclientplayermp.u), ls.c(entityclientplayermp.v), ls.c(entityclientplayermp.w));
      f5 = 1.0F;
      int i = this.e.f.h(ls.c(entityclientplayermp.u), ls.c(entityclientplayermp.v), ls.c(entityclientplayermp.w), 0);
      int j = i % 65536;
      int k = i / 65536;
      bma.a(bma.b, j / 1.0F, k / 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (itemstack != null) {
         i = yc.g[itemstack.d].a(itemstack, 0);
         float f7 = (i >> 16 & 0xFF) / 255.0F;
         float f8 = (i >> 8 & 0xFF) / 255.0F;
         float f6 = (i & 0xFF) / 255.0F;
         GL11.glColor4f(2.95F - f5 * f7, 2.95F - f5 * f8, 2.95F - f5 * f6, 1.0F);
      } else {
         GL11.glColor4f(f5, f5, f5, 1.0F);
      }

      if (itemstack != null && itemstack.b() instanceof yh) {
         GL11.glPushMatrix();
         float f12 = 0.8F;
         float f7 = entityclientplayermp.k(par1);
         float f8 = ls.a(f7 * (float) Math.PI);
         float f6 = ls.a(ls.c(f7) * (float) Math.PI);
         GL11.glTranslatef(-f6 * 0.4F, ls.a(ls.c(f7) * (float) Math.PI * 2.0F) * 0.2F, -f8 * 0.2F);
         f7 = 1.0F - f2 / 45.0F + 0.1F;
         if (f7 < 0.0F) {
            f7 = 0.0F;
         }

         if (f7 > 1.0F) {
            f7 = 1.0F;
         }

         f7 = -ls.b(f7 * (float) Math.PI) * 0.5F + 0.5F;
         GL11.glTranslatef(0.0F, 0.0F * f12 - (1.0F - f1) * 1.2F - f7 * 0.5F + 0.04F, -0.9F * f12);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(f7 * -85.0F, 0.0F, 0.0F, 1.0F);
         GL11.glEnable(32826);
         this.e.J().a(entityclientplayermp.r());

         for (int var28 = 0; var28 < 2; var28++) {
            int l = var28 * 2 - 1;
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.0F, -0.6F, 1.1F * l);
            GL11.glRotatef(-45 * l, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-65 * l, 0.0F, 1.0F, 0.0F);
            bgm render = bgl.a.a(this.e.h);
            bhj renderplayer = (bhj)render;
            float f11 = 1.0F;
            GL11.glScalef(f11, f11, f11);
            renderplayer.a(this.e.h);
            GL11.glPopMatrix();
         }

         f8 = entityclientplayermp.k(par1);
         f6 = ls.a(f8 * f8 * (float) Math.PI);
         float f9 = ls.a(ls.c(f8) * (float) Math.PI);
         GL11.glRotatef(-f6 * 20.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-f9 * 20.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(-f9 * 80.0F, 1.0F, 0.0F, 0.0F);
         float f10 = 0.38F;
         GL11.glScalef(f10, f10, f10);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
         float f11 = 0.015625F;
         GL11.glScalef(f11, f11, f11);
         this.e.J().a(c);
         bfq tessellator = bfq.a;
         GL11.glNormal3f(0.0F, 0.0F, -1.0F);
         tessellator.b();
         byte b0 = 7;
         tessellator.a(0 - b0, 128 + b0, 0.0, 0.0, 1.0);
         tessellator.a(128 + b0, 128 + b0, 0.0, 1.0, 1.0);
         tessellator.a(128 + b0, 0 - b0, 0.0, 1.0, 0.0);
         tessellator.a(0 - b0, 0 - b0, 0.0, 0.0, 0.0);
         tessellator.a();
         IItemRenderer custom = MinecraftForgeClient.getItemRenderer(itemstack, ItemRenderType.FIRST_PERSON_MAP);
         ali mapdata = ((yh)itemstack.b()).a(itemstack, this.e.f);
         if (custom == null) {
            if (mapdata != null) {
               this.a.a(this.e.h, this.e.J(), mapdata);
            }
         } else {
            custom.renderItem(ItemRenderType.FIRST_PERSON_MAP, itemstack, new Object[]{this.e.h, this.e.J(), mapdata});
         }

         GL11.glPopMatrix();
      } else if (itemstack != null) {
         GL11.glPushMatrix();
         float f12x = 0.8F;
         if (entityclientplayermp.bq() > 0) {
            zj enumaction = itemstack.o();
            if (enumaction == zj.b || enumaction == zj.c) {
               float f8x = entityclientplayermp.bq() - par1 + 1.0F;
               float f6x = 1.0F - f8x / itemstack.n();
               float f9 = 1.0F - f6x;
               f9 = f9 * f9 * f9;
               f9 = f9 * f9 * f9;
               f9 = f9 * f9 * f9;
               float f10 = 1.0F - f9;
               GL11.glTranslatef(0.0F, ls.e(ls.b(f8x / 4.0F * (float) Math.PI) * 0.1F) * (f6x > 0.2 ? 1 : 0), 0.0F);
               GL11.glTranslatef(f10 * 0.6F, -f10 * 0.5F, 0.0F);
               GL11.glRotatef(f10 * 90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(f10 * 10.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(f10 * 30.0F, 0.0F, 0.0F, 1.0F);
            }
         } else {
            float f7x = entityclientplayermp.k(par1);
            float f8x = ls.a(f7x * (float) Math.PI);
            float f6x = ls.a(ls.c(f7x) * (float) Math.PI);
            GL11.glTranslatef(-f6x * 0.4F, ls.a(ls.c(f7x) * (float) Math.PI * 2.0F) * 0.2F, -f8x * 0.2F);
         }

         GL11.glTranslatef(0.7F * f12x, -0.65F * f12x - (1.0F - f1) * 0.6F, -0.9F * f12x);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glEnable(32826);
         float f7x = entityclientplayermp.k(par1);
         float f8x = ls.a(f7x * f7x * (float) Math.PI);
         float f6x = ls.a(ls.c(f7x) * (float) Math.PI);
         GL11.glRotatef(-f8x * 20.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-f6x * 20.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(-f6x * 80.0F, 1.0F, 0.0F, 0.0F);
         float f9 = 0.4F;
         GL11.glScalef(f9, f9, f9);
         if (entityclientplayermp.bq() > 0) {
            zj enumaction1 = itemstack.o();
            if (enumaction1 == zj.d) {
               GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
               GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
            } else if (enumaction1 == zj.e) {
               GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
               GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
               GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
               float f11 = itemstack.n() - (entityclientplayermp.bq() - par1 + 1.0F);
               float f13 = f11 / 20.0F;
               f13 = (f13 * f13 + f13 * 2.0F) / 3.0F;
               if (f13 > 1.0F) {
                  f13 = 1.0F;
               }

               if (f13 > 0.1F) {
                  GL11.glTranslatef(0.0F, ls.a((f11 - 0.1F) * 1.3F) * 0.01F * (f13 - 0.1F), 0.0F);
               }

               GL11.glTranslatef(0.0F, 0.0F, f13 * 0.1F);
               GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
               GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
               GL11.glTranslatef(0.0F, 0.5F, 0.0F);
               float f14 = 1.0F + f13 * 0.2F;
               GL11.glScalef(1.0F, 1.0F, f14);
               GL11.glTranslatef(0.0F, -0.5F, 0.0F);
               GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            }
         }

         if (itemstack.b().o_()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }

         if (itemstack.b().b()) {
            this.renderItem(entityclientplayermp, itemstack, 0, ItemRenderType.EQUIPPED_FIRST_PERSON);

            for (int x = 1; x < itemstack.b().getRenderPasses(itemstack.k()); x++) {
               int i1 = yc.g[itemstack.d].a(itemstack, x);
               float f11x = (i1 >> 16 & 0xFF) / 255.0F;
               float f13x = (i1 >> 8 & 0xFF) / 255.0F;
               float f14 = (i1 & 0xFF) / 255.0F;
               GL11.glColor4f(f5 * f11x, f5 * f13x, f5 * f14, 1.0F);
               this.renderItem(entityclientplayermp, itemstack, x, ItemRenderType.EQUIPPED_FIRST_PERSON);
            }
         } else {
            this.renderItem(entityclientplayermp, itemstack, 0, ItemRenderType.EQUIPPED_FIRST_PERSON);
         }

         GL11.glPopMatrix();
      } else if (!entityclientplayermp.aj()) {
         GL11.glPushMatrix();
         float f12xx = 0.8F;
         float f7xx = entityclientplayermp.k(par1);
         float f8xx = ls.a(f7xx * (float) Math.PI);
         float f6xx = ls.a(ls.c(f7xx) * (float) Math.PI);
         GL11.glTranslatef(-f6xx * 0.3F, ls.a(ls.c(f7xx) * (float) Math.PI * 2.0F) * 0.4F, -f8xx * 0.4F);
         GL11.glTranslatef(0.8F * f12xx, -0.75F * f12xx - (1.0F - f1) * 0.6F, -0.9F * f12xx);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glEnable(32826);
         f7xx = entityclientplayermp.k(par1);
         f8xx = ls.a(f7xx * f7xx * (float) Math.PI);
         f6xx = ls.a(ls.c(f7xx) * (float) Math.PI);
         GL11.glRotatef(f6xx * 70.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-f8xx * 20.0F, 0.0F, 0.0F, 1.0F);
         this.e.J().a(entityclientplayermp.r());
         GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
         GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glTranslatef(5.6F, 0.0F, 0.0F);
         bgm render = bgl.a.a(this.e.h);
         bhj renderplayer = (bhj)render;
         float f11x = 1.0F;
         GL11.glScalef(f11x, f11x, f11x);
         renderplayer.a(this.e.h);
         GL11.glPopMatrix();
      }

      GL11.glDisable(32826);
      att.a();
   }

   public void b(float par1) {
      GL11.glDisable(3008);
      if (this.e.h.af()) {
         this.d(par1);
      }

      if (this.e.h.U()) {
         int i = ls.c(this.e.h.u);
         int j = ls.c(this.e.h.v);
         int k = ls.c(this.e.h.w);
         int l = this.e.f.a(i, j, k);
         if (this.e.f.u(i, j, k)) {
            this.a(par1, aqz.s[l].m(2));
         } else {
            for (int i1 = 0; i1 < 8; i1++) {
               float f1 = ((i1 >> 0) % 2 - 0.5F) * this.e.h.O * 0.9F;
               float f2 = ((i1 >> 1) % 2 - 0.5F) * this.e.h.P * 0.2F;
               float f3 = ((i1 >> 2) % 2 - 0.5F) * this.e.h.O * 0.9F;
               int j1 = ls.d(i + f1);
               int k1 = ls.d(j + f2);
               int l1 = ls.d(k + f3);
               if (this.e.f.u(j1, k1, l1)) {
                  l = this.e.f.a(j1, k1, l1);
               }
            }
         }

         if (aqz.s[l] != null) {
            this.a(par1, aqz.s[l].m(2));
         }
      }

      if (this.e.h.a(akc.h)) {
         this.c(par1);
      }

      GL11.glEnable(3008);
   }

   private void a(float par1, ms par2Icon) {
      this.e.J().a(bik.b);
      bfq tessellator = bfq.a;
      float f1 = 0.1F;
      GL11.glColor4f(f1, f1, f1, 0.5F);
      GL11.glPushMatrix();
      float f2 = -1.0F;
      float f3 = 1.0F;
      float f4 = -1.0F;
      float f5 = 1.0F;
      float f6 = -0.5F;
      float f7 = par2Icon.c();
      float f8 = par2Icon.d();
      float f9 = par2Icon.e();
      float f10 = par2Icon.f();
      tessellator.b();
      tessellator.a(f2, f4, f6, f8, f10);
      tessellator.a(f3, f4, f6, f7, f10);
      tessellator.a(f3, f5, f6, f7, f9);
      tessellator.a(f2, f5, f6, f8, f9);
      tessellator.a();
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void c(float par1) {
      this.e.J().a(d);
      bfq tessellator = bfq.a;
      float f1 = this.e.h.d(par1);
      GL11.glColor4f(f1, f1, f1, 0.5F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glPushMatrix();
      float f2 = 4.0F;
      float f3 = -1.0F;
      float f4 = 1.0F;
      float f5 = -1.0F;
      float f6 = 1.0F;
      float f7 = -0.5F;
      float f8 = -this.e.h.A / 64.0F;
      float f9 = this.e.h.B / 64.0F;
      tessellator.b();
      tessellator.a(f3, f5, f7, f2 + f8, f2 + f9);
      tessellator.a(f4, f5, f7, 0.0F + f8, f2 + f9);
      tessellator.a(f4, f6, f7, 0.0F + f8, 0.0F + f9);
      tessellator.a(f3, f6, f7, f2 + f8, 0.0F + f9);
      tessellator.a();
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
   }

   private void d(float par1) {
      bfq tessellator = bfq.a;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      float f1 = 1.0F;

      for (int i = 0; i < 2; i++) {
         GL11.glPushMatrix();
         ms icon = aqz.aw.c(1);
         this.e.J().a(bik.b);
         float f2 = icon.c();
         float f3 = icon.d();
         float f4 = icon.e();
         float f5 = icon.f();
         float f6 = (0.0F - f1) / 2.0F;
         float f7 = f6 + f1;
         float f8 = 0.0F - f1 / 2.0F;
         float f9 = f8 + f1;
         float f10 = -0.5F;
         GL11.glTranslatef(-(i * 2 - 1) * 0.24F, -0.3F, 0.0F);
         GL11.glRotatef((i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
         tessellator.b();
         tessellator.a(f6, f8, f10, f3, f5);
         tessellator.a(f7, f8, f10, f2, f5);
         tessellator.a(f7, f9, f10, f2, f4);
         tessellator.a(f6, f9, f10, f3, f4);
         tessellator.a();
         GL11.glPopMatrix();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
   }

   public void a() {
      this.h = this.g;
      bdi entityclientplayermp = this.e.h;
      ye itemstack = entityclientplayermp.bn.h();
      boolean flag = this.j == entityclientplayermp.bn.c && itemstack == this.f;
      if (this.f == null && itemstack == null) {
         flag = true;
      }

      if (itemstack != null && this.f != null && itemstack != this.f && itemstack.d == this.f.d && itemstack.k() == this.f.k()) {
         this.f = itemstack;
         flag = true;
      }

      float f = 0.4F;
      float f1 = flag ? 1.0F : 0.0F;
      float f2 = f1 - this.g;
      if (f2 < -f) {
         f2 = -f;
      }

      if (f2 > f) {
         f2 = f;
      }

      this.g += f2;
      if (this.g < 0.1F) {
         this.f = itemstack;
         this.j = entityclientplayermp.bn.c;
      }
   }

   public void b() {
      this.g = 0.0F;
   }

   public void c() {
      this.g = 0.0F;
   }
}
