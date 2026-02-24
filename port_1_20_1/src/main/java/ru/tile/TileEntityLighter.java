package ru.stalcraft.tile;

import atomicstryker.dynamiclights.client.DynamicLights;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.LighterLight;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.particles.LighterParticleEmitter;
import ru.stalcraft.entity.EntityLighterLight;
import ru.stalcraft.items.EntityBolt;

public class TileEntityLighter extends TileEntityAnomaly {
   public int activeTimer = 0;
   public int ticksSleep;
   private String soundId = "";

   @Override
   public void h() {
      super.h();
      if (this.activeTimer > 0) {
         this.activeTimer--;
      }

      for (nn entity : this.az().a(nn.class, asx.a(this.l - 0.65, this.m - 0.65, this.n - 0.65, this.l + 0.65, this.m + 0.65, this.n + 0.65))) {
         if (entity instanceof of) {
            this.ticksSleep = 120;
            if (!this.k.I) {
               TileEntityAnomaly.damageEntityForce((of)entity, nb.a, 1.0F, true);
               entity.d(10);
            }
         } else if (entity instanceof EntityBolt) {
            this.ticksSleep = 120;
         }
      }

      if (this.ticksSleep > 0) {
         this.ticksSleep--;
      }

      if (super.k.I) {
         this.clientUpdate();
      }
   }

   @SideOnly(Side.CLIENT)
   public void clientUpdate() {
      if (this.activeTimer > 0) {
         this.activeTimer--;
      }
   }

   @SideOnly(Side.CLIENT)
   public void onClientCollide() {
      bln sndManager = atv.w().v;
      if (!sndManager.b.playing(this.soundId)) {
         sndManager.a(StalkerMain.lighter.sound, super.l + 0.5F, super.m + 0.5F, super.n + 0.5F, 1.0F, 1.0F);
         int latestSoundID = (Integer)ReflectionHelper.getPrivateValue(bln.class, atv.w().v, new String[]{"latestSoundID", "field_77378_e", "g"});
         this.soundId = "sound_" + latestSoundID;
      }

      if (this.activeTimer <= 0 && GuiSettingsStalker.dynamicLights) {
         EntityLighterLight entity = new EntityLighterLight(this);
         atv.w().f.d(entity);
         DynamicLights.addLightSource(new LighterLight(entity));
      }

      this.activeTimer = 5;
   }

   public void onServerCollide(of entity) {
      this.activeTimer = 5;
   }

   @Override
   public boolean canUpdate() {
      return true;
   }

   @Override
   protected Class getEmitterClass() {
      return LighterParticleEmitter.class;
   }
}
