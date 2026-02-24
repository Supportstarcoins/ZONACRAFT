package ru.stalcraft.client.effects.particles;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import ru.stalcraft.Util;

public class ParticleIcon extends bil {
   public final int index;

   public ParticleIcon(String par1Str, int index) {
      super(par1Str);
      this.index = index;
   }

   public void a(bjn par1Resource) throws IOException {
      try {
         ReflectionHelper.findMethod(bil.class, this, new String[]{"resetSprite", "func_130102_n", "n"}, new Class[]{(Class)null}).invoke(null, null);
      } catch (Exception var12) {
      }

      InputStream inputstream = par1Resource.b();
      bko animationmetadatasection = (bko)par1Resource.a("animation");
      BufferedImage bufferedimage = ImageIO.read(inputstream);
      super.f = bufferedimage.getHeight();
      super.e = bufferedimage.getWidth();
      int[] aint = new int[super.f * super.e];
      bufferedimage.getRGB(0, 0, super.e, super.f, aint, 0, super.e);

      for (int i = 0; i < aint.length; i++) {
         int j = aint[i];
         int k = j >> 24 & 0xFF;
         int l = (int)((j >> 16 & 0xFF) * (k / 255.0F));
         int arraylist = (int)((j >> 8 & 0xFF) * (k / 255.0F));
         int b = (int)((j & 0xFF) * (k / 255.0F));
         aint[i] = (k & 0xFF) << 24 | (l & 0xFF) << 16 | (arraylist & 0xFF) << 8 | b & 0xFF;
      }

      if (animationmetadatasection == null) {
         if (super.f != super.e) {
            throw new RuntimeException("broken aspect ratio and not an animation");
         }

         super.a.add(aint);
      } else {
         int var131 = super.f / super.e;
         int j = super.e;
         int k = super.e;
         super.f = super.e;
         if (animationmetadatasection.c() > 0) {
            for (int l : animationmetadatasection.e()) {
               if (l >= var131) {
                  throw new RuntimeException("invalid frameindex " + l);
               }

               this.d(l);
               super.a.set(l, a(aint, j, k, l));
            }

            Util.setPrivateValue(ParticleIcon.class, this, animationmetadatasection, "animationMetadata", "field_110982_k", "j");
         } else {
            ArrayList var13x = Lists.newArrayList();

            for (int l = 0; l < var131; l++) {
               super.a.add(a(aint, j, k, l));
               var13x.add(new bkn(l, -1));
            }

            Util.setPrivateValue(
               ParticleIcon.class, this, new bko(var13x, super.e, super.f, animationmetadatasection.d()), "animationMetadata", "field_110982_k", "j"
            );
         }
      }
   }

   protected static int[] a(int[] par0ArrayOfInteger, int par1, int par2, int par3) {
      int[] aint1 = new int[par1 * par2];
      System.arraycopy(par0ArrayOfInteger, par3 * aint1.length, aint1, 0, aint1.length);
      return aint1;
   }

   protected void d(int par1) {
      if (this.a.size() <= par1) {
         for (int j = this.a.size(); j <= par1; j++) {
            this.a.add(null);
         }
      }
   }
}
