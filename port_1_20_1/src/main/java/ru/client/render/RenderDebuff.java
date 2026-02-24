package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.StalkerMain;

public class RenderDebuff extends bje {
   public IModelCustom modelCube = AdvancedModelLoader.loadModel("/assets/stalker/models/cube.obj");

   public void a(asp tile, double x, double y, double z, float f) {
      if (atv.w().c.h()) {
         int blockID = tile.k.a(tile.l, tile.m, tile.n);
         if (blockID == StalkerMain.radiation1.cF || blockID == StalkerMain.radiation2.cF || blockID == StalkerMain.radiation3.cF) {
            GL11.glPushMatrix();
            GL11.glColor4d(0.7F, 0.7F, 0.0, 1.0);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(768, 768);
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.modelCube.renderAll();
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glPopMatrix();
         } else if (blockID == StalkerMain.biological1.cF || blockID == StalkerMain.biological2.cF || blockID == StalkerMain.biological3.cF) {
            GL11.glPushMatrix();
            GL11.glColor4d(0.0, 0.7F, 0.0, 1.0);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(768, 768);
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.modelCube.renderAll();
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glPopMatrix();
         } else if (blockID == StalkerMain.chemical1.cF || blockID == StalkerMain.chemical2.cF || blockID == StalkerMain.chemical3.cF) {
            GL11.glPushMatrix();
            GL11.glColor4d(0.6F, 0.0, 0.7F, 1.0);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(768, 768);
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.modelCube.renderAll();
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glPopMatrix();
         } else if (blockID == StalkerMain.psycho.cF) {
            GL11.glPushMatrix();
            GL11.glColor4d(0.7F, 0.7F, 0.7F, 1.0);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(768, 768);
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.modelCube.renderAll();
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glPopMatrix();
         }
      }
   }
}
