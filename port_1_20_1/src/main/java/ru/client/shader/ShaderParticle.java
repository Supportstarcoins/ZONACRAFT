package ru.stalcraft.client.shader;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderParticle extends Shader {
   public ShaderParticle() throws Exception {
      super("particle");
   }

   public void start() {
   }

   @Override
   public void stop() {
      ARBShaderObjects.glUseProgramObjectARB(0);
   }

   @Override
   protected void bindAttributes() {
   }
}
