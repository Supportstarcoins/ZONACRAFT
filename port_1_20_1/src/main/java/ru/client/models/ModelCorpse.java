package ru.stalcraft.client.models;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.entity.EntityCorpse;

public class ModelCorpse extends bbj {
   public float rotationFall;
   public float rotationRightHand;
   public float rotationLeftHand;

   public ModelCorpse(float par1, float par2, int par3, int par4) {
      super(par1, par2, par3, par4);
   }

   public void a(nn par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      EntityCorpse corpse = (EntityCorpse)par1Entity;
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 1.35F, 0.0F);
      GL11.glRotatef(this.rotationFall, 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(0.0F, -1.35F, 0.0F);
      if (corpse.isFallingFinished) {
         GL11.glTranslatef(0.0F, 0.9F, 0.0F);
      }

      super.a(par1Entity, par2, par3, par4, par5, par6, par7);
      GL11.glPopMatrix();
   }

   public void a(float par1, float par2, float par3, float par4, float par5, float par6, nn par7Entity) {
      super.a(par1, par2, par3, par4, par5, par6, par7Entity);
      super.f.f = 0.0F;
      super.f.g = 0.0F;
      super.f.h = this.rotationRightHand / 180.0F * (float) Math.PI;
      super.g.f = 0.0F;
      super.g.g = 0.0F;
      super.g.h = this.rotationLeftHand / 180.0F * (float) Math.PI;
   }
}
