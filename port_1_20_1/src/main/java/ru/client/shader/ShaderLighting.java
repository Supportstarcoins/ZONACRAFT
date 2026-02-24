package ru.stalcraft.client.shader;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderLighting extends Shader {
   public ShaderLighting() throws Exception {
      super("");
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
