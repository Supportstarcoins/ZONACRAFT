package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.blocks.BlockConfig;
import ru.stalcraft.client.ClientProxy;

public class RenderDecorative extends bje {
   public IModelCustom modelCube = AdvancedModelLoader.loadModel("/assets/stalker/models/cube.obj");
   public atv mc;
   private int modelSpectater = -1;
   public ClientProxy proxy;

   public RenderDecorative(ClientProxy proxy) {
      this.proxy = proxy;
      this.mc = atv.w();
      this.modelSpectater = GL11.glGenLists(1);
      GL11.glNewList(this.modelSpectater, 4864);
      this.modelCube.renderAll();
      GL11.glEndList();
   }

   public void a(asp tile, double x, double y, double z, float f) {
      int blockId = tile.k.a(tile.l, tile.m, tile.n);
      if (blockId != -1 && aqz.s[blockId] instanceof BlockConfig) {
         this.proxy.rendererManager.renderersBlock.get(blockId).renderDecorative(tile, x, y, z, f);
      }

      if (this.mc.c.h()
         && (aqz.s[blockId] instanceof BlockConfig && !((BlockConfig)aqz.s[blockId]).isModelBlock && this.mc.c.h() || !(aqz.s[blockId] instanceof BlockConfig))
         )
       {
         GL11.glPushMatrix();
         GL11.glColor4d(0.0, 0.5, 0.9F, 1.0);
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         GL11.glEnable(3042);
         GL11.glBlendFunc(768, 768);
         GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         if (this.modelSpectater != -1) {
            GL11.glCallList(this.modelSpectater);
         }

         GL11.glDisable(3042);
         GL11.glEnable(2896);
         GL11.glEnable(3553);
         GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
         GL11.glPopMatrix();
      }
   }
}
