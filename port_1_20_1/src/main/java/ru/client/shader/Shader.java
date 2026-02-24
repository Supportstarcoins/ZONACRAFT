package ru.stalcraft.client.shader;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import ru.stalcraft.Logger;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.ShaderManager;

public abstract class Shader {
   public ClientProxy proxy = (ClientProxy)StalkerMain.getProxy();
   private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
   public int shaderID;
   public int vertShader1;
   public int fragShader1;

   public Shader(String filename) throws Exception {
      boolean vertShader = false;
      boolean fragShader = false;
      this.vertShader1 = this.proxy.shaderManager.loadShader("/assets/stalker/shaders/" + filename + ".vsh", 35633);
      this.fragShader1 = this.proxy.shaderManager.loadShader("/assets/stalker/shaders/" + filename + ".fsh", 35632);
      if (this.vertShader1 != 0 && this.fragShader1 != 0) {
         this.shaderID = ARBShaderObjects.glCreateProgramObjectARB();
         if (this.shaderID == 0) {
            throw new Exception("[ERR][Effects API] Can't setup particle engine! (creating program)");
         } else {
            ARBShaderObjects.glAttachObjectARB(this.shaderID, this.vertShader1);
            ARBShaderObjects.glAttachObjectARB(this.shaderID, this.fragShader1);
            ARBShaderObjects.glLinkProgramARB(this.shaderID);
            if (ARBShaderObjects.glGetObjectParameteriARB(this.shaderID, 35714) == 0) {
               Logger.console(ShaderManager.getLogInfo(this.shaderID));
               throw new Exception("[ERR][Effects API] Can't setup particle engine! (linking program)");
            } else {
               ARBShaderObjects.glValidateProgramARB(this.shaderID);
               if (ARBShaderObjects.glGetObjectParameteriARB(this.shaderID, 35715) == 0) {
                  Logger.console(ShaderManager.getLogInfo(this.shaderID));
                  throw new Exception("[ERR][Effects API] Can't setup particle engine! (validating program)");
               }
            }
         }
      } else {
         throw new Exception("[ERR][Effects API] Can't setup particle engine! (loading shaders)");
      }
   }

   public void stop() {
      GL20.glUseProgram(0);
   }

   public void cleanUp() {
      this.stop();
      GL20.glDetachShader(this.shaderID, this.vertShader1);
      GL20.glDetachShader(this.shaderID, this.fragShader1);
      GL20.glDeleteShader(this.vertShader1);
      GL20.glDeleteShader(this.fragShader1);
      GL20.glDeleteProgram(this.shaderID);
   }

   protected abstract void bindAttributes();

   protected void bindAttribute(int attribute, String variableName) {
      GL20.glBindAttribLocation(this.shaderID, attribute, variableName);
   }

   protected void loadFloat(int location, float value) {
      GL20.glUniform1f(location, value);
   }

   protected void loadInt(int location, int value) {
      GL20.glUniform1i(location, value);
   }

   protected void loadVector(int location, Vector3f vector) {
      GL20.glUniform3f(location, vector.x, vector.y, vector.z);
   }

   protected void loadVector(int location, Vector4f vector) {
      GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
   }

   protected void load2DVector(int location, Vector2f vector) {
      GL20.glUniform2f(location, vector.x, vector.y);
   }

   protected void loadBoolean(int location, boolean value) {
      float toLoad = 0.0F;
      if (value) {
         toLoad = 1.0F;
      }

      GL20.glUniform1f(location, toLoad);
   }

   protected void loadMatrix(int location, Matrix4f matrix) {
      matrix.store(matrixBuffer);
      ((Buffer)matrixBuffer).flip();
      GL20.glUniformMatrix4(location, false, matrixBuffer);
   }
}
