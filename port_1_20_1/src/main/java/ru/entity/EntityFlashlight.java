package ru.stalcraft.entity;

import atomicstryker.dynamiclights.client.DynamicLights;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.WeaponInfo;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.FlashlightLight;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.player.PlayerUtils;

@SideOnly(Side.CLIENT)
public class EntityFlashlight extends nn {
   private uf player;

   public EntityFlashlight(uf player) {
      super(player.q);
      this.player = player;
      this.a(0.0F, 0.0F);
      super.Z = true;
      ((ClientWeaponInfo)PlayerUtils.getInfo(player).weaponInfo).spawnedEntityFlashlight = true;
      this.updatePos();
      DynamicLights.addLightSource(new FlashlightLight(this));
   }

   public void l_() {
      super.l_();
      WeaponInfo wi = PlayerUtils.getInfo(this.player).weaponInfo;
      if (!this.player.M && this.player.aN() > 0.0F && GuiSettingsStalker.dynamicLights && wi.isFlashlightEnabled()) {
         this.updatePos();
      } else {
         this.x();
      }
   }

   public void x() {
      super.x();
      ((ClientWeaponInfo)PlayerUtils.getInfo(this.player).weaponInfo).spawnedEntityFlashlight = false;
   }

   private void updatePos() {
      ata obj = ((ClientWeaponInfo)PlayerUtils.getInfo(this.player).weaponInfo).getLightPos();
      boolean x = false;
      boolean y = false;
      boolean z = false;
      if (obj != null) {
         int var5 = obj.b;
         int var6 = obj.c;
         int var7 = obj.d;
         if (obj.e == 0) {
            var6--;
         }

         if (obj.e == 1) {
            var6++;
         }

         if (obj.e == 2) {
            var7--;
         }

         if (obj.e == 3) {
            var7++;
         }

         if (obj.e == 4) {
            var5--;
         }

         if (obj.e == 5) {
            var5++;
         }

         this.b(var5 + 0.5, var6 + 0.5, var7 + 0.5);
      } else {
         this.x();
      }
   }

   protected void a() {
   }

   protected void a(by nbttagcompound) {
   }

   protected void b(by nbttagcompound) {
   }
}
