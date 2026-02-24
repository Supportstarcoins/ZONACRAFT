package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.entity.EntityTurrel;

public class RenderTurrel extends bgm {
   private IModelCustom model;
   private bjo texture;
   private float scale;

   public RenderTurrel(String modelName, String textureName, float scale) {
      this.model = AdvancedModelLoader.loadModel("/assets/stalker/models/" + modelName + ".obj");
      this.texture = new bjo("stalker", "models/" + textureName + ".png");
      this.scale = scale;
   }

   public void doRenderTurrel(EntityTurrel entity, double xPos, double yPos, double zPos, float par8, float frame) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      atv.w().N.a(this.texture);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)xPos, (float)yPos, (float)zPos);
      GL11.glRotatef(180.0F - RenderUtils.interpolateRotation(entity.C, entity.A, frame), 0.0F, 1.0F, 0.0F);
      GL11.glScalef(this.scale, this.scale, this.scale);
      this.model.renderPart("bashnya");
      GL11.glTranslatef(0.0F, entity.f() / this.scale, -entity.getRotationPointZ() / this.scale);
      float angle = -RenderUtils.interpolateRotation(entity.D, entity.B, frame);
      GL11.glRotatef(angle, 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(0.0F, -entity.f() / this.scale, entity.getRotationPointZ() / this.scale);
      this.model.renderPart("stvol");
      angle = RenderUtils.interpolateRotation(entity.prevGunRoll, entity.gunRoll, frame);
      GL11.glTranslatef(0.0F, entity.f() / this.scale, -entity.getRotationPointZ() / this.scale);
      GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, -entity.f() / this.scale, entity.getRotationPointZ() / this.scale);
      this.model.renderPart("rotating");
      GL11.glPopMatrix();
      GL11.glEnable(2896);
   }

   public void a(nn par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderTurrel((EntityTurrel)par1Entity, par2, par4, par6, par8, par9);
   }

   protected bjo a(nn entity) {
      return null;
   }
}
