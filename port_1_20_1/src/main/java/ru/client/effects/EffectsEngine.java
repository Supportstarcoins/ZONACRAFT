package ru.stalcraft.client.effects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import ru.stalcraft.Logger;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticlesRenderer;
import ru.stalcraft.client.effects.particles.ParticlesTextureMap;
import ru.stalcraft.client.particles.ParticleLivingEmitter;

public final class EffectsEngine {
   private Random rand = new Random();
   public static EffectsEngine instance;
   public List particleEmitters = new ArrayList();
   public int particlesRendered;
   public int emittersRendered;
   private List emitterClasses = new ArrayList();
   public int programId;
   public boolean invertXParticleRotation;
   public boolean canRenderOnGPU;
   public ParticlesTextureMap particlesTextureMap;
   private ParticlesRenderer particlesRenderer;
   public boolean shouldUseShaders;
   private static final float degToRad = (float) (Math.PI / 180.0);
   public float xWind;
   public float zWind;
   private int windChangeTimer = 0;
   public HashMap<Integer, ParticleLivingEmitter> emittersLiving = new HashMap<>();

   public EffectsEngine() throws Exception {
      this.canRenderOnGPU = GLContext.getCapabilities().OpenGL33;
      System.out
         .println(
            "[Effects API] OpenGL version is "
               + GL11.glGetString(7938)
               + " "
               + (this.canRenderOnGPU ? "(supports OpenGL 3.3.0)" : "(does not support OpenGL 3.3.0)")
         );
      boolean vertShader = false;
      boolean fragShader = false;
      int vertShader1 = this.createShader("/assets/effects/shaders/particle.vsh", 35633);
      int fragShader1 = this.createShader("/assets/effects/shaders/particle.fsh", 35632);
      if (vertShader1 != 0 && fragShader1 != 0) {
         this.programId = ARBShaderObjects.glCreateProgramObjectARB();
         if (this.programId == 0) {
            throw new Exception("[ERR][Effects API] Can't setup particle engine! (creating program)");
         } else {
            ARBShaderObjects.glAttachObjectARB(this.programId, vertShader1);
            ARBShaderObjects.glAttachObjectARB(this.programId, fragShader1);
            ARBShaderObjects.glLinkProgramARB(this.programId);
            if (ARBShaderObjects.glGetObjectParameteriARB(this.programId, 35714) == 0) {
               Logger.console(getLogInfo(this.programId));
               throw new Exception("[ERR][Effects API] Can't setup particle engine! (linking program)");
            } else {
               ARBShaderObjects.glValidateProgramARB(this.programId);
               if (ARBShaderObjects.glGetObjectParameteriARB(this.programId, 35715) == 0) {
                  Logger.console(getLogInfo(this.programId));
                  throw new Exception("[ERR][Effects API] Can't setup particle engine! (validating program)");
               } else {
                  this.particlesRenderer = new ParticlesRenderer();
                  Logger.console("[Effects API] Shaders have been loaded");
                  instance = this;
               }
            }
         }
      } else {
         throw new Exception("[ERR][Effects API] Can't setup particle engine! (loading shaders)");
      }
   }

   public static void renderStatic(float frame) {
      if (instance != null) {
         instance.render(frame);
      }
   }

   public void render(float frame) {
      this.particlesRenderer.render(frame);
   }

   public void tick() {
      this.tickParticles();
   }

   public void addParticleEmitter(ParticleEmitter s) {
      this.particleEmitters.add(s);
   }

   public void addParticleEmitter(ParticleEmitter s, boolean isContains) {
      if (!this.particleEmitters.contains(s)) {
         this.particleEmitters.add(s);
      }
   }

   private void tickParticles() {
      Iterator it = this.particleEmitters.iterator();
      ParticleEmitter particleEmitter = null;

      while (it.hasNext()) {
         particleEmitter = (ParticleEmitter)it.next();
         if (particleEmitter != null) {
            if (!particleEmitter.isValid()) {
               particleEmitter.onRemove();
               it.remove();
            } else {
               particleEmitter.updateDistance(bgl.b, bgl.c, bgl.d);
               if (particleEmitter.lastDistanceSq < (particleEmitter.renderDistanceSq + 16.0) * (particleEmitter.renderDistanceSq + 16.0)) {
                  particleEmitter.tick();
               }
            }
         }
      }

      if (--this.windChangeTimer <= 0) {
         this.windChangeTimer = 40 + this.rand.nextInt(60);
         Vector2f var5 = new Vector2f(this.rand.nextFloat() - 0.5F, this.rand.nextFloat() - 0.5F);
         var5.normalise();
         float var6 = this.rand.nextFloat() * 0.01F;
         this.xWind = var5.x * var6;
         this.zWind = var5.y * var6;
      }
   }

   private int createShader(String filename, int shaderType) throws Exception {
      byte shader = 0;

      try {
         int shader1 = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
         if (shader1 == 0) {
            return 0;
         } else {
            ARBShaderObjects.glShaderSourceARB(shader1, this.readResourceAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader1);
            if (ARBShaderObjects.glGetObjectParameteriARB(shader1, 35713) == 0) {
               throw new RuntimeException("Error creating shader: " + getLogInfo(shader1));
            } else {
               return shader1;
            }
         }
      } catch (Exception var51) {
         ARBShaderObjects.glDeleteObjectARB(shader);
         throw var51;
      }
   }

   private static String getLogInfo(int obj) {
      return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
   }

   private String readResourceAsString(String filename) throws Exception {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(EffectsEngine.class.getResourceAsStream(filename), "UTF-8"));
         StringBuffer buffer = new StringBuffer();
         boolean flag = false;

         while (!flag) {
            String str = e.readLine();
            if (str == null) {
               flag = true;
            } else {
               buffer.append(str).append("\n");
            }
         }

         return buffer.toString();
      } catch (Exception var61) {
         var61.printStackTrace();
         return null;
      }
   }

   public void registerEmitter(Class emitterClass) {
      this.emitterClasses.add(emitterClass);
   }

   public void loadIcons() {
      this.particlesTextureMap = new ParticlesTextureMap(this.emitterClasses);
      ParticlesTextureMap var10001 = this.particlesTextureMap;
      atv.w().N.a(ParticlesTextureMap.particlesTexture, this.particlesTextureMap);
   }

   public static Matrix3f rotationMatrix(float angle, float x, float y, float z) {
      angle *= (float) (Math.PI / 180.0);
      Vector3f axis = new Vector3f(x, y, z);
      axis.normalise();
      float s = ls.a(angle);
      float c = ls.b(angle);
      float oc = 1.0F - c;
      Matrix3f mat = new Matrix3f();
      FloatBuffer buff = BufferUtils.createFloatBuffer(9);
      buff.put(
         new float[]{
            oc * axis.x * axis.x + c,
            oc * axis.x * axis.y - axis.z * s,
            oc * axis.z * axis.x + axis.y * s,
            oc * axis.x * axis.y + axis.z * s,
            oc * axis.y * axis.y + c,
            oc * axis.y * axis.z - axis.x * s,
            oc * axis.z * axis.x - axis.y * s,
            oc * axis.y * axis.z + axis.x * s,
            oc * axis.z * axis.z + c
         }
      );
      ((Buffer)buff).flip();
      mat.load(buff);
      return mat;
   }
}
