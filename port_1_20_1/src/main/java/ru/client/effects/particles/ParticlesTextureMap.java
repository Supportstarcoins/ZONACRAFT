package ru.stalcraft.client.effects.particles;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ParticlesTextureMap extends bia implements bir, mt {
   public static final bjo particlesTexture = new bjo("textures/atlas/particles.png");
   private final List listAnimatedSprites = Lists.newArrayList();
   private final Map mapRegisteredSprites = Maps.newHashMap();
   private final Map mapUploadedSprites = Maps.newHashMap();
   private final int textureType;
   private final String basePath;
   private List emitterClasses;
   public List icons = new ArrayList();

   public ParticlesTextureMap(List emitterClasses) {
      this.basePath = "textures/particles";
      this.textureType = 347;
      this.emitterClasses = emitterClasses;
      this.registerIcons();
   }

   public void a(bjp par1ResourceManager) throws IOException {
      this.loadTextureAtlas(par1ResourceManager);
   }

   public void loadTextureAtlas(bjp par1) {
      int par2 = atv.y();
      big par3 = new big(par2, par2, true);
      this.mapUploadedSprites.clear();
      this.listAnimatedSprites.clear();

      for (Entry par5 : this.mapRegisteredSprites.entrySet()) {
         bjo par6 = new bjo((String)par5.getKey());
         ParticleIcon par7 = (ParticleIcon)par5.getValue();
         bjo par8 = new bjo(par6.b(), String.format("%s/%s%s", this.basePath, par6.a(), ".png"));

         try {
            if (!par7.load(par1, par8)) {
               continue;
            }
         } catch (RuntimeException var12) {
            atv.w().an().c(String.format("Unable to parse animation metadata from %s: %s", par8, var12.getMessage()));
            continue;
         } catch (IOException var13) {
            atv.w().an().c("Using missing texture, unable to load: " + par8);
            continue;
         }

         par3.a(par7);
      }

      try {
         par3.c();
      } catch (bij var11) {
         throw var11;
      }

      bip.a(this.b(), par3.a(), par3.b());

      for (ParticleIcon par5 : par3.d()) {
         String par6 = par5.g();
         this.mapUploadedSprites.put(par6, par5);

         try {
            bip.a(par5.a(0), par5.a(), par5.b(), par5.h(), par5.i(), false, false);
         } catch (Throwable var10) {
            b par7 = b.a(var10, "Stitching texture atlas");
            m par8 = par7.a("Texture being stitched together");
            par8.a("Atlas path", this.basePath);
            par8.a("Sprite", par5);
            throw new u(par7);
         }

         if (par5.m()) {
            this.listAnimatedSprites.add(par5);
         } else {
            par5.l();
         }
      }
   }

   private void registerIcons() {
      for (Class clazz : this.emitterClasses) {
         try {
            Method e = clazz.getMethod("registerIcons", mt.class);
            e.invoke(null, this);
         } catch (Exception var41) {
            var41.printStackTrace();
         }
      }
   }

   public int getTextureType() {
      return this.textureType;
   }

   public void d() {
      this.updateAnimations();
   }

   public void updateAnimations() {
   }

   public ms a(String par1Str) {
      if (par1Str == null) {
         new RuntimeException("Don't register null!").printStackTrace();
         par1Str = "null";
      }

      ParticleIcon object = (ParticleIcon)this.mapRegisteredSprites.get(par1Str);
      if (object == null) {
         object = new ParticleIcon(par1Str, this.icons.size());
         this.mapRegisteredSprites.put(par1Str, object);
      }

      this.icons.add(object);
      return object;
   }
}
