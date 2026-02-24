package ru.stalcraft.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.ARBShaderObjects;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.shader.Shader;

public class ShaderManager {
   public Map<String, Shader> shaders = new HashMap<>();

   public int loadShader(String filename, int shaderType) throws Exception {
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

   public static String getLogInfo(int obj) {
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
}
