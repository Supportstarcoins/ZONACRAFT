package ru.stalcraft.client.effects.particles;

import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.effects.particles.attributes.AlphaAttribute;
import ru.stalcraft.client.effects.particles.attributes.Attribute;
import ru.stalcraft.client.effects.particles.attributes.BurnAttribute;
import ru.stalcraft.client.effects.particles.attributes.LightmaskCoordAttribute;
import ru.stalcraft.client.effects.particles.attributes.PositionAttribute;
import ru.stalcraft.client.effects.particles.attributes.RotationAttribute;
import ru.stalcraft.client.effects.particles.attributes.SizeAttribute;
import ru.stalcraft.client.effects.particles.attributes.TextureCoordsIdAttribute;

public class ParticlesRenderer {
   private boolean isTessellatorRenderingEnabled = false;
   private FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(16)
      .put(new float[]{-0.5F, 0.5F, 0.0F, 0.0F, -0.5F, -0.5F, 0.0F, 0.0F, 0.5F, -0.5F, 0.0F, 0.0F, 0.5F, 0.5F, 0.0F, 0.0F});
   private final float PI = (float) Math.PI;
   private final float sin45 = (float)Math.sin(Math.PI / 4);
   private final String rotVecUniform = "rotationVec";
   private final String billboardRotMatUniform = "billboardRotMatrix";
   private final String textureCoordsUniform = "textureCoords";
   private final String textureUniform = "texture";
   private final String vertexPosAttrib = "vertexPosition";
   private int vaoId;
   private int vertexVboId;
   private int vertexPosLocation;
   private FloatBuffer coordsListBuffer;
   private int rotationVecLocation;
   private int billboardRotMatLocation;
   private int textureCoordsLocation;
   private int textureLocation;
   private int energyEffectLocation;
   private List attributes = new ArrayList();
   private Matrix4f billboardRotationMatrix = new Matrix4f();
   private bfv frustrum = new bfv();
   private FloatBuffer brmBuffer = BufferUtils.createFloatBuffer(16);
   private ArrayList particles1;
   private ArrayList particle2;
   private int renderPass = 0;

   public ParticlesRenderer() {
      ((Buffer)this.verticesBuffer).flip();
      this.attributes.add(new PositionAttribute());
      this.attributes.add(new RotationAttribute());
      this.attributes.add(new TextureCoordsIdAttribute());
      this.attributes.add(new LightmaskCoordAttribute());
      this.attributes.add(new AlphaAttribute());
      this.attributes.add(new SizeAttribute());
      this.attributes.add(new BurnAttribute());
      this.billboardRotationMatrix.m33 = 1.0F;
   }

   public void render(float frame) {
      if (this.renderPass == 2) {
         this.renderPass = 0;
      }

      if (this.renderPass == 0) {
         DoubleBuffer mdl = BufferUtils.createDoubleBuffer(16);
         GL11.glGetDouble(2982, mdl);
         double cameraX = -(mdl.get(0) * mdl.get(12) + mdl.get(1) * mdl.get(13) + mdl.get(2) * mdl.get(14)) + bgl.b;
         double cameraY = -(mdl.get(4) * mdl.get(12) + mdl.get(5) * mdl.get(13) + mdl.get(6) * mdl.get(14)) + bgl.c;
         double cameraZ = -(mdl.get(8) * mdl.get(12) + mdl.get(9) * mdl.get(13) + mdl.get(10) * mdl.get(14)) + bgl.d;
         boolean invertXParticleRotation = atv.w().u.aa == 2;
         Matrix3f rotationMatrixY = EffectsEngine.rotationMatrix(-180.0F + bgl.a.j, 0.0F, 1.0F, 0.0F);
         Matrix3f rotationMatrixX = EffectsEngine.rotationMatrix(invertXParticleRotation ? -bgl.a.k : bgl.a.k, 1.0F, 0.0F, 0.0F);
         Matrix3f matrix = Matrix3f.mul(rotationMatrixY, rotationMatrixX, (Matrix3f)null);
         this.billboardRotationMatrix.m00 = matrix.m00;
         this.billboardRotationMatrix.m01 = matrix.m01;
         this.billboardRotationMatrix.m02 = matrix.m02;
         this.billboardRotationMatrix.m10 = matrix.m10;
         this.billboardRotationMatrix.m11 = matrix.m11;
         this.billboardRotationMatrix.m12 = matrix.m12;
         this.billboardRotationMatrix.m20 = matrix.m20;
         this.billboardRotationMatrix.m21 = matrix.m21;
         this.billboardRotationMatrix.m22 = matrix.m22;
         ((Buffer)this.brmBuffer).clear();
         this.billboardRotationMatrix.store(this.brmBuffer);
         ((Buffer)this.brmBuffer).flip();
         ArrayList systemsToRender = new ArrayList();
         int particlesNumber = 0;
         this.frustrum.a(cameraX, cameraY, cameraZ);
         Iterator it = EffectsEngine.instance.particleEmitters.iterator();
         ParticleEmitter particleEmitter = null;
         Particle particle = null;

         while (it.hasNext()) {
            particleEmitter = (ParticleEmitter)it.next();
            particleEmitter.updateDistance(cameraX, cameraY, cameraZ);
            if (particleEmitter.lastDistanceSq < particleEmitter.renderDistanceSq
               && (particleEmitter.ignoreFrustrumCheck() || this.frustrum.a(particleEmitter.getBoundingBox()))) {
               systemsToRender.add(particleEmitter);
               particlesNumber += particleEmitter.particles.size();
            }
         }

         EffectsEngine.instance.particlesRendered = particlesNumber;
         EffectsEngine.instance.emittersRendered = systemsToRender.size();
         this.particles1 = new ArrayList(particlesNumber);
         this.particle2 = new ArrayList(particlesNumber);
         it = systemsToRender.iterator();
         Iterator it2 = null;

         while (it.hasNext()) {
            particleEmitter = (ParticleEmitter)it.next();

            for (Particle var23 : particleEmitter.particles) {
               var23.updateDistance(cameraX, cameraY, cameraZ, frame);
               if (var23.blendFunc == 0) {
                  this.particles1.add(var23);
               } else if (var23.blendFunc == 1) {
                  this.particle2.add(var23);
               }
            }
         }

         if (this.particles1.size() > 0) {
            Collections.sort(this.particles1);
         }

         if (this.particle2.size() > 0) {
            Collections.sort(this.particle2);
         }
      }

      GL11.glEnable(3042);
      GL11.glBlendFunc(1, 771);
      ParticlesTextureMap var10001 = EffectsEngine.instance.particlesTextureMap;
      atv.w().N.a(ParticlesTextureMap.particlesTexture);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glDepthMask(false);
      GL11.glDisable(3008);
      if (this.isTessellatorRenderingEnabled) {
         this.switchToTessellatorRendering();
      }

      this.renderWithTessellator(this.particles1, frame);
      if (this.particle2.size() > 0) {
         Collections.sort(this.particle2);
      }

      if (this.isTessellatorRenderingEnabled) {
         this.switchToTessellatorRendering();
      }

      this.renderWithTessellatorBlendAttributeOne(this.particle2, frame);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
      this.renderPass++;
   }

   private void switchToShaderRendering() {
      if (!this.isTessellatorRenderingEnabled) {
         this.isTessellatorRenderingEnabled = true;
         this.vaoId = GL30.glGenVertexArrays();
         GL30.glBindVertexArray(this.vaoId);
         this.vertexVboId = GL15.glGenBuffers();
         this.vertexPosLocation = GL20.glGetAttribLocation(EffectsEngine.instance.programId, "vertexPosition");
         this.rotationVecLocation = GL20.glGetUniformLocation(EffectsEngine.instance.programId, "rotationVec");
         this.billboardRotMatLocation = GL20.glGetUniformLocation(EffectsEngine.instance.programId, "billboardRotMatrix");
         this.textureCoordsLocation = GL20.glGetUniformLocation(EffectsEngine.instance.programId, "textureCoords");
         this.textureLocation = GL20.glGetUniformLocation(EffectsEngine.instance.programId, "texture");
         GL15.glBindBuffer(34962, this.vertexVboId);
         GL15.glBufferData(34962, this.verticesBuffer, 35044);
         GL20.glVertexAttribPointer(this.vertexPosLocation, 4, 5126, false, 0, 0L);
         Iterator it = this.attributes.iterator();
         Attribute attribute = null;

         while (it.hasNext()) {
            attribute = (Attribute)it.next();
            attribute.location = GL20.glGetAttribLocation(EffectsEngine.instance.programId, attribute.name);
            attribute.vboId = GL15.glGenBuffers();
            GL15.glBindBuffer(34962, attribute.vboId);
            GL20.glVertexAttribPointer(attribute.location, attribute.floatsPerParticle, 5126, false, 0, 0L);
         }

         GL15.glBindBuffer(34962, 0);
         GL30.glBindVertexArray(0);
      }
   }

   private void switchToTessellatorRendering() {
      if (this.isTessellatorRenderingEnabled) {
         this.isTessellatorRenderingEnabled = false;
         this.coordsListBuffer = null;

         for (Attribute attribute : this.attributes) {
            attribute.buffer = null;
            GL15.glDeleteBuffers(attribute.vboId);
            attribute.location = 0;
            attribute.vboId = 0;
         }

         GL15.glDeleteBuffers(this.vertexVboId);
         GL30.glDeleteVertexArrays(this.vaoId);
      }
   }

   protected void renderWithTessellator(List particles, float frame) {
      bfq t = bfq.a;
      atv.w().p.b(frame);
      float par3 = atp.d;
      float par5 = atp.f;
      float par6 = atp.g;
      float par7 = atp.h;
      float par4 = atp.e;
      Iterator it = particles.iterator();
      Particle particle = null;
      t.b();

      while (it.hasNext()) {
         particle = (Particle)it.next();
         if (particle.shouldRender(frame)) {
            particle.onRender(t, atp.d, atp.f, atp.g, atp.h, atp.e, this.sin45, frame);
         }
      }

      t.a();
      atv.w().p.a(frame);
   }

   protected void renderWithShader(List particles, float frame) {
      ARBShaderObjects.glUseProgramObjectARB(EffectsEngine.instance.programId);
      Iterator it = this.attributes.iterator();
      Attribute attribute = null;
      Particle particle = null;
      ParticleEmitter particleEmitter = null;
      ParticleIcon particleIcon = null;

      while (it.hasNext()) {
         attribute = (Attribute)it.next();
         attribute.prepareBuffer(particles.size());
      }

      it = particles.iterator();
      Iterator it2 = null;

      while (it.hasNext()) {
         particle = (Particle)it.next();
         if (particle.shouldRender(frame)) {
            for (Attribute var15 : this.attributes) {
               var15.writeToBuffer(particle, frame);
            }
         }
      }

      for (Attribute var16 : this.attributes) {
         ((Buffer)var16.buffer).flip();
      }

      GL30.glBindVertexArray(this.vaoId);
      GL20.glUniform3f(this.rotationVecLocation, 0.0F, 0.0F, 1.0F);
      GL20.glUniformMatrix4(this.billboardRotMatLocation, false, this.brmBuffer);
      GL20.glUniform1i(this.textureLocation, 0);
      if (this.coordsListBuffer == null) {
         this.coordsListBuffer = BufferUtils.createFloatBuffer(EffectsEngine.instance.particlesTextureMap.icons.size() * 4);

         for (ParticleIcon var20 : EffectsEngine.instance.particlesTextureMap.icons) {
            this.coordsListBuffer.put(var20.c());
            this.coordsListBuffer.put(var20.e());
            this.coordsListBuffer.put(var20.d());
            this.coordsListBuffer.put(var20.f());
         }

         ((Buffer)this.coordsListBuffer).flip();
      }

      GL20.glUniform1(this.textureCoordsLocation, this.coordsListBuffer);

      for (Attribute var17 : this.attributes) {
         GL15.glBindBuffer(34962, var17.vboId);
         GL15.glBufferData(34962, var17.buffer, 35048);
         GL20.glEnableVertexAttribArray(var17.location);
         GL33.glVertexAttribDivisor(var17.location, 1);
      }

      GL20.glEnableVertexAttribArray(this.vertexPosLocation);
      GL33.glVertexAttribDivisor(this.vertexPosLocation, 0);
      GL31.glDrawArraysInstanced(7, 0, 4, particles.size());
      GL20.glDisableVertexAttribArray(this.vertexPosLocation);

      for (Attribute var18 : this.attributes) {
         GL20.glDisableVertexAttribArray(var18.location);
      }

      GL15.glBindBuffer(34962, 0);
      GL30.glBindVertexArray(0);
      ARBShaderObjects.glUseProgramObjectARB(0);
   }

   protected void renderWithTessellatorBlendAttributeOne(List particles, float frame) {
      bfq t = bfq.a;
      atv.w().p.b(frame);
      GL11.glBlendFunc(770, 1);
      Iterator it = particles.iterator();
      Particle particle = null;
      t.b();

      while (it.hasNext()) {
         particle = (Particle)it.next();
         if (particle.shouldRender(frame)) {
            particle.onRender(t, atp.d, atp.f, atp.g, atp.h, atp.e, this.sin45, frame);
         }
      }

      t.a();
      atv.w().p.a(frame);
   }

   protected void renderWithShaderBlendAttributeOne(List particles, float frame) {
      ARBShaderObjects.glUseProgramObjectARB(EffectsEngine.instance.programId);
      Iterator it = this.attributes.iterator();
      GL11.glBlendFunc(770, 1);
      Attribute attribute = null;
      Particle particle = null;
      ParticleEmitter particleEmitter = null;
      ParticleIcon particleIcon = null;

      while (it.hasNext()) {
         attribute = (Attribute)it.next();
         attribute.prepareBuffer(particles.size());
      }

      it = particles.iterator();
      Iterator it2 = null;

      while (it.hasNext()) {
         particle = (Particle)it.next();
         if (particle.shouldRender(frame)) {
            for (Attribute var15 : this.attributes) {
               var15.writeToBuffer(particle, frame);
            }
         }
      }

      for (Attribute var16 : this.attributes) {
         ((Buffer)var16.buffer).flip();
      }

      GL30.glBindVertexArray(this.vaoId);
      GL20.glUniform3f(this.rotationVecLocation, 0.0F, 0.0F, 1.0F);
      GL20.glUniformMatrix4(this.billboardRotMatLocation, false, this.brmBuffer);
      GL20.glUniform1i(this.textureLocation, 0);
      if (this.coordsListBuffer == null) {
         this.coordsListBuffer = BufferUtils.createFloatBuffer(EffectsEngine.instance.particlesTextureMap.icons.size() * 4);

         for (ParticleIcon var20 : EffectsEngine.instance.particlesTextureMap.icons) {
            this.coordsListBuffer.put(var20.c());
            this.coordsListBuffer.put(var20.e());
            this.coordsListBuffer.put(var20.d());
            this.coordsListBuffer.put(var20.f());
         }

         ((Buffer)this.coordsListBuffer).flip();
      }

      GL20.glUniform1(this.textureCoordsLocation, this.coordsListBuffer);

      for (Attribute var17 : this.attributes) {
         GL15.glBindBuffer(34962, var17.vboId);
         GL15.glBufferData(34962, var17.buffer, 35048);
         GL20.glEnableVertexAttribArray(var17.location);
         GL33.glVertexAttribDivisor(var17.location, 1);
      }

      GL20.glEnableVertexAttribArray(this.vertexPosLocation);
      GL33.glVertexAttribDivisor(this.vertexPosLocation, 0);
      GL31.glDrawArraysInstanced(7, 0, 4, particles.size());
      GL20.glDisableVertexAttribArray(this.vertexPosLocation);

      for (Attribute var18 : this.attributes) {
         GL20.glDisableVertexAttribArray(var18.location);
      }

      GL15.glBindBuffer(34962, 0);
      GL30.glBindVertexArray(0);
      ARBShaderObjects.glUseProgramObjectARB(0);
   }

   public static float interpolateRotation(float prev, float current, float frame) {
      float f3 = current - prev;

      while (f3 < -180.0F) {
         f3 += 360.0F;
      }

      while (f3 >= 180.0F) {
         f3 -= 360.0F;
      }

      return prev + frame * f3;
   }
}
