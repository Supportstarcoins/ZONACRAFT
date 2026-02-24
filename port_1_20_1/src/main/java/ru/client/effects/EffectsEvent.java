package ru.stalcraft.client.effects;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.asm.MethodsHelper;
import ru.stalcraft.client.ClientProxy;

public class EffectsEvent {
   @ForgeSubscribe
   public void onWorldRendering(RenderWorldLastEvent e) {
      MethodsHelper.shouldRenderRainShow = true;
      bfe entityRenderer = atv.w().p;
      EffectsEngine.renderStatic(e.partialTicks);

      try {
         ReflectionHelper.findMethod(bfe.class, entityRenderer, new String[]{"setupFog", "func_78468_a", "a"}, new Class[]{Integer.class, Float.class})
            .invoke(entityRenderer, 0, e.partialTicks);
      } catch (Exception var5) {
      }

      GL11.glEnable(2912);

      try {
         ReflectionHelper.findMethod(bfe.class, entityRenderer, new String[]{"renderRainSnow", "func_78474_d", "d"}, new Class[]{Float.class})
            .invoke(entityRenderer, e.partialTicks);
      } catch (Exception var4) {
      }

      MethodsHelper.shouldRenderRainShow = false;
      GL11.glDisable(2912);
      ClientProxy proxy = (ClientProxy)StalkerMain.getProxy();
      proxy.rendererVectorHandler.renderRayTracingVectors();
   }
}
