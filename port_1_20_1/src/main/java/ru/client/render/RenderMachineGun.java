package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.tile.TileEntityMachineGun;

public class RenderMachineGun extends bje {
   private IModelCustom model = AdvancedModelLoader.loadModel("/assets/stalker/models/machinegun.obj");
   private bjo texture = new bjo("stalker", "models/machinegun.png");

   public void a(asp tileentity, double x, double y, double z, float f) {
      GL11.glPushMatrix();
      atv.w().N.a(this.texture);
      TileEntityMachineGun tile = (TileEntityMachineGun)tileentity;
      int direction = tile.p();
      float rotationYaw = tile.prevYaw + (tile.yaw - tile.prevYaw) * f;
      float rotationPitch = tile.prevPitch + (tile.pitch - tile.prevPitch) * f;
      GL11.glTranslatef((float)x + 0.5F, (float)y + 0.35F, (float)z + 0.5F);
      GL11.glRotatef(-direction * 90 + 180, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(1.2F, 1.2F, 1.2F);
      GL11.glRotatef(-rotationYaw, 0.0F, 1.0F, 0.0F);
      this.model.renderPart("Mesh2");
      GL11.glTranslatef(0.0F, -0.15F, 0.15F);
      GL11.glRotatef(-rotationPitch, 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(0.0F, 0.15F, -0.15F);
      this.model.renderPart("Mesh1");
      GL11.glPopMatrix();
   }
}
