package ru.stalcraft.client.render;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.loaders.StalkerModelManager;
import ru.stalcraft.client.models.ModelCorpse;
import ru.stalcraft.entity.EntityCorpse;

public class RenderCorpse extends bgu {
   public ClientProxy proxy;
   ModelCorpse modelCorpse = (ModelCorpse)this.a;
   ModelCorpse armor1;
   ModelCorpse armor2;

   public RenderCorpse(ClientProxy proxy) {
      super(new ModelCorpse(0.0F, 0.0F, 64, 32), 0.5F);
      this.proxy = proxy;
   }

   protected void b() {
      super.g = this.armor1 = new ModelCorpse(1.0F, 0.0F, 64, 32);
      super.h = this.armor2 = new ModelCorpse(0.5F, 0.0F, 64, 32);
   }

   protected bjo a(nn par1Entity) {
      return ((EntityCorpse)par1Entity).getTexture();
   }

   public void renderCorpse(EntityCorpse corpse, double par2, double par4, double par6, float par8, float frame) {
      this.armor1.rotationFall = this.armor2.rotationFall = this.modelCorpse.rotationFall = RenderUtils.interpolateRotation(
         corpse.prevRotationFall, corpse.rotationFall, frame
      );
      this.armor1.rotationRightHand = this.armor2.rotationRightHand = this.modelCorpse.rotationRightHand = RenderUtils.interpolateRotation(
         corpse.prevRightHandRotation, corpse.rightHandRotation, frame
      );
      this.armor1.rotationLeftHand = this.armor2.rotationLeftHand = this.modelCorpse.rotationLeftHand = RenderUtils.interpolateRotation(
         corpse.prevLeftHandRotation, corpse.leftHandRotation, frame
      );
      super.a(corpse, par2, par4, par6, par8, frame);
   }

   public void a(og par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderCorpse((EntityCorpse)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   protected void a(og par1EntityLiving, float par2) {
   }

   protected void c(of par1EntityLiving, float par2) {
      super.c(par1EntityLiving, par2);
      EntityCorpse corpse = (EntityCorpse)par1EntityLiving;
      StalkerModelManager m = ClientProxy.modelManager;
      GL11.glEnable(32826);
      GL11.glEnable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 1.35F, 0.0F);
      GL11.glRotatef(RenderUtils.interpolateRotation(corpse.prevRotationFall, corpse.rotationFall, par2), 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(0.0F, -1.35F, 0.0F);
      if (corpse.isFallingFinished) {
         GL11.glTranslatef(0.0F, 0.9F, 0.0F);
      }

      GL11.glPushMatrix();
      super.a.e.c(0.0625F);
      ye backpack1 = corpse.getBackpack();
      if (backpack1 != null) {
         RenderBackpack.renderBackpack(corpse, backpack1.d);
      }

      if (GuiSettingsStalker.renderEquippedWeapons) {
         this.renderEquippedWeapons(corpse, backpack1 != null);
      }

      GL11.glPopMatrix();
      GL11.glPopMatrix();
      GL11.glDisable(3042);
   }

   private void renderEquippedWeapons(EntityCorpse corpse, boolean backpack) {
      ye rifle = corpse.getRifle();
      ye pistol = corpse.getPistol();
      if (rifle != null && ClientProxy.weaponRenders.containsKey(rifle.d)) {
         ((RenderWeapon)ClientProxy.weaponRenders.get(rifle.d))
            .renderOnPlayer(corpse, backpack ? RenderWeapon.RenderType.BACKPACK_RIFLE : RenderWeapon.RenderType.RIFLE, rifle);
      }

      if (pistol != null && ClientProxy.weaponRenders.containsKey(pistol.d)) {
         ((RenderWeapon)ClientProxy.weaponRenders.get(pistol.d)).renderOnPlayer(corpse, RenderWeapon.RenderType.PISTOL, pistol);
      }
   }

   public void a(nn par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderCorpse((EntityCorpse)par1Entity, par2, par4, par6, par8, par9);
   }
}
