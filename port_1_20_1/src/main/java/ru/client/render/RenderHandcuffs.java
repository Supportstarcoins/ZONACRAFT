package ru.stalcraft.client.render;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;
import ru.stalcraft.client.models.ModelHand;
import ru.stalcraft.player.PlayerUtils;

public class RenderHandcuffs {
   private static float[] translation = new float[]{-0.457F, -0.454F, -1.241F};
   private static float[] rotation = new float[]{45.0F, 0.0F, 0.0F};
   private static float[] temp = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
   private static IModelCustom modelHandcuffs = AdvancedModelLoader.loadModel("/assets/stalker/models/handcuffs.obj");
   private static ModelHand modelHand = new ModelHand();
   private static bjo texture = new bjo("stalker", "models/handcuffs.png");
   private static atv mc = atv.w();

   @SideOnly(Side.CLIENT)
   public static boolean renderHandcuffs(bfe renderer, float frame, int anaglyph) {
      if (atv.w().h.aN() <= 0.0F) {
         return true;
      } else if (PlayerUtils.getInfo(atv.w().h).getHandcuffs() && renderer.n <= 0 && !mc.i.aj() && mc.u.aa == 0) {
         setupTranslations(renderer, frame, anaglyph);
         if (mc.u.aa == 0 && !mc.i.bh() && !mc.u.Z && !mc.c.a()) {
            doRenderHandcuffs(frame, renderer.c);
         }

         reloadOldTranslations(renderer, frame, anaglyph);
         return true;
      } else {
         return false;
      }
   }

   private static void doRenderHandcuffs(float frame, bfj renderer) {
      float equippedProgress = (Float)ReflectionHelper.getPrivateValue(bfj.class, renderer, new String[]{"equippedProgress", "field_78454_c", "g"});
      float prevEquippedProgress = (Float)ReflectionHelper.getPrivateValue(bfj.class, renderer, new String[]{"prevEquippedProgress", "field_78451_d", "h"});
      ye itemToRender = (ye)ReflectionHelper.getPrivateValue(bfj.class, renderer, new String[]{"itemToRender", "field_78453_b", "f"});
      float var10000 = prevEquippedProgress + (equippedProgress - prevEquippedProgress) * frame;
      bdi entityclientplayermp = atv.w().h;
      float f2 = entityclientplayermp.D + (entityclientplayermp.B - entityclientplayermp.D) * frame;
      GL11.glPushMatrix();
      GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(entityclientplayermp.C + (entityclientplayermp.A - entityclientplayermp.C) * frame, 0.0F, 1.0F, 0.0F);
      att.b();
      GL11.glPopMatrix();
      float f3 = entityclientplayermp.j + (entityclientplayermp.h - entityclientplayermp.j) * frame;
      float f4 = entityclientplayermp.i + (entityclientplayermp.g - entityclientplayermp.i) * frame;
      GL11.glRotatef((entityclientplayermp.B - f3) * 0.05F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef((entityclientplayermp.A - f4) * 0.05F, 0.0F, 1.0F, 0.0F);
      float f5 = atv.w().f.q(ls.c(entityclientplayermp.u), ls.c(entityclientplayermp.v), ls.c(entityclientplayermp.w));
      f5 = 1.0F;
      int i = atv.w().f.h(ls.c(entityclientplayermp.u), ls.c(entityclientplayermp.v), ls.c(entityclientplayermp.w), 0);
      int j = i % 65536;
      int k = i / 65536;
      bma.a(bma.b, j / 1.0F, k / 1.0F);
      GL11.glColor4f(f5, f5, f5, 1.0F);
      bhj renderplayer = (bhj)bgl.a.a(mc.h);
      GL11.glEnable(32826);
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      mc.J().a(entityclientplayermp.r());
      GL11.glPushMatrix();
      GL11.glTranslatef(0.18F, -0.785F, -0.87F);
      GL11.glRotatef(-45.0F, 1.0F, 0.0F, 0.0F);
      modelHand.render(entityclientplayermp, 2);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef(-0.43F, -0.785F, -0.87F);
      GL11.glRotatef(-45.0F, 1.0F, 0.0F, 0.0F);
      modelHand.render(entityclientplayermp, 1);
      GL11.glPopMatrix();
      mc.J().a(texture);
      GL11.glPushMatrix();
      GL11.glTranslatef(translation[0], translation[1], translation[2]);
      GL11.glRotatef(rotation[0], 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(rotation[1], 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(rotation[2], 0.0F, 0.0F, 1.0F);
      GL11.glScalef(0.017F, 0.017F, 0.017F);
      modelHandcuffs.renderAll();
      GL11.glPopMatrix();
      GL11.glDisable(32826);
      att.a();
   }

   private static void setupTranslations(bfe renderer, float frame, float anaglyph) {
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      float f1 = 0.07F;
      if (mc.u.g) {
         GL11.glTranslatef(-(anaglyph * 2.0F - 1.0F) * f1, 0.0F, 0.0F);
      }

      double cameraZoom = (Double)ReflectionHelper.getPrivateValue(bfe.class, renderer, new String[]{"cameraZoom", "field_78503_V", "Y"});
      double cameraYaw = (Double)ReflectionHelper.getPrivateValue(bfe.class, renderer, new String[]{"cameraYaw", "field_78502_W", "Z"});
      double cameraPitch = (Double)ReflectionHelper.getPrivateValue(bfe.class, renderer, new String[]{"cameraPitch", "field_78509_X", "aa"});
      if (cameraZoom != 1.0) {
         GL11.glTranslatef((float)cameraYaw, (float)(-cameraPitch), 0.0F);
         GL11.glScaled(cameraZoom, cameraZoom, 1.0);
      }

      float farPlaneDistance = (Float)ReflectionHelper.getPrivateValue(bfe.class, renderer, new String[]{"farPlaneDistance", "field_78530_s", "r"});
      float getFOVModifier = 0.0F;

      try {
         getFOVModifier = (Float)ReflectionHelper.findMethod(
               bfe.class, renderer, new String[]{"getFOVModifier", "func_78481_a", "a"}, new Class[]{Float.class, Boolean.class}
            )
            .invoke(renderer, frame, false);
      } catch (Exception var15) {
      }

      Project.gluPerspective(getFOVModifier, (float)mc.d / mc.e, 0.05F, farPlaneDistance * 2.0F);
      if (mc.c.a()) {
         float f2 = 0.6666667F;
         GL11.glScalef(1.0F, f2, 1.0F);
      }

      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      if (mc.u.g) {
         GL11.glTranslatef((anaglyph * 2.0F - 1.0F) * 0.1F, 0.0F, 0.0F);
      }

      GL11.glPushMatrix();

      try {
         ReflectionHelper.findMethod(bfe.class, renderer, new String[]{"hurtCameraEffect", "func_78482_e", "e"}, new Class[]{Float.class})
            .invoke(renderer, frame);
      } catch (Exception var14) {
         var14.printStackTrace();
      }

      if (mc.u.f) {
         try {
            ReflectionHelper.findMethod(bfe.class, renderer, new String[]{"setupViewBobbing", "func_78475_f", "f"}, new Class[]{Float.class})
               .invoke(renderer, frame);
         } catch (Exception var13) {
            var13.printStackTrace();
         }
      }

      renderer.b(frame);
   }

   private static void reloadOldTranslations(bfe renderer, float frame, float anaglyph) {
      GL11.glPopMatrix();
      if (mc.u.aa == 0 && !mc.i.bh()) {
         renderer.c.b(frame);

         try {
            ReflectionHelper.findMethod(bfe.class, renderer, new String[]{"hurtCameraEffect", "func_78482_e", "e"}, new Class[]{Float.class})
               .invoke(renderer, frame);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      if (mc.u.f) {
         try {
            ReflectionHelper.findMethod(bfe.class, renderer, new String[]{"setupViewBobbing", "func_78475_f", "f"}, new Class[]{Float.class})
               .invoke(renderer, frame);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      renderer.a(frame);
   }

   public static void renderHandcuffsThirdPerson(bbj model) {
      if (Keyboard.isKeyDown(72)) {
         temp = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
      }

      if (Keyboard.isKeyDown(78)) {
         if (Keyboard.isKeyDown(79)) {
            temp[0] = temp[0] + 0.001F;
         }

         if (Keyboard.isKeyDown(80)) {
            temp[1] = temp[1] + 0.001F;
         }

         if (Keyboard.isKeyDown(81)) {
            temp[2] = temp[2] + 0.001F;
         }

         if (Keyboard.isKeyDown(75)) {
            temp[3] = temp[3] + 0.04F;
         }

         if (Keyboard.isKeyDown(76)) {
            temp[4] = temp[4] + 0.04F;
         }

         if (Keyboard.isKeyDown(77)) {
            temp[5] = temp[5] + 0.04F;
         }
      } else if (Keyboard.isKeyDown(12)) {
         if (Keyboard.isKeyDown(79)) {
            temp[0] = temp[0] - 0.001F;
         }

         if (Keyboard.isKeyDown(80)) {
            temp[1] = temp[1] - 0.001F;
         }

         if (Keyboard.isKeyDown(81)) {
            temp[2] = temp[2] - 0.001F;
         }

         if (Keyboard.isKeyDown(75)) {
            temp[3] = temp[3] - 0.04F;
         }

         if (Keyboard.isKeyDown(76)) {
            temp[4] = temp[4] - 0.04F;
         }

         if (Keyboard.isKeyDown(77)) {
            temp[5] = temp[5] - 0.04F;
         }
      }

      GL11.glPushMatrix();
      mc.J().a(texture);
      GL11.glPushMatrix();
      model.g.c(0.0625F);
      GL11.glTranslatef(-0.698F, 0.501F, 0.15F);
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(0.017F, 0.017F, 0.017F);
      modelHandcuffs.renderPart("Mesh4");
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      model.f.c(0.0625F);
      GL11.glTranslatef(-0.212F, 0.501F, 0.15F);
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(0.017F, 0.017F, 0.017F);
      modelHandcuffs.renderPart("Mesh5");
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      model.f.c(0.0625F);
      GL11.glTranslatef(-0.249F, 0.415F, 0.226F);
      GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(-10.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(9.24F, 0.0F, 0.0F, 1.0F);
      GL11.glScalef(0.02F, 0.02F, 0.02F);
      modelHandcuffs.renderOnly(new String[]{"Mesh1", "Mesh2", "Mesh3"});
      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }
}
