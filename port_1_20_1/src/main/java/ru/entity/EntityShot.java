package ru.stalcraft.entity;

import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.ShotLightManager;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.tile.IParticleEmmiter;

public class EntityShot extends nn implements IParticleEmmiter {
   public final of shooter;
   public final ItemWeapon weapon;
   public final float size;
   public final float distance;
   public final float rotationRoll;
   public boolean spawnedParticles = false;

   public EntityShot(of shooter, ItemWeapon weapon, boolean isShooterPlayer) {
      super(shooter.q);
      this.shooter = shooter;
      this.weapon = weapon;
      float distance = weapon.lightDistance;
      if (isShooterPlayer && shooter == atv.w().h && atv.w().u.aa != 0) {
      }

      float size = weapon.lightSize;
      super.a(0.5F, 0.5F);
      super.b(shooter.u, shooter.v + 0.05F + shooter.f() - (shooter.ah() ? 0.29 : 0.2) + (isShooterPlayer ? 0.0 : 0.27), shooter.w, shooter.aP, shooter.B);
      if (shooter == atv.w().h && ((ClientWeaponInfo)PlayerUtils.getInfo(atv.w().h).weaponInfo).isAiming()) {
         distance += weapon.aimPosZ;
         distance *= weapon.zoom;
      }

      super.u = super.u + -ls.a(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI) * distance;
      super.w = super.w + ls.b(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI) * distance;
      super.v = super.v + -ls.a(super.B / 180.0F * (float) Math.PI) * distance;
      if (shooter != atv.w().h || atv.w().u.aa != 0) {
         super.u = super.u - ls.b(super.A / 180.0F * (float) Math.PI) * (isShooterPlayer ? 0.16 : 0.25);
         super.w = super.w - ls.a(super.A / 180.0F * (float) Math.PI) * (isShooterPlayer ? 0.16 : 0.25);
      } else if (!((ClientWeaponInfo)PlayerUtils.getInfo(atv.w().h).weaponInfo).isAiming()) {
         super.u = super.u - ls.b(super.A / 180.0F * (float) Math.PI) * 0.16;
         super.w = super.w - ls.a(super.A / 180.0F * (float) Math.PI) * 0.16;
      }

      if (shooter != atv.w().h) {
         super.v -= 0.1;
      }

      this.size = size;
      super.b(super.u, super.v, super.w);
      this.rotationRoll = (float)Math.random() * 360.0F;
      this.distance = distance;
      ShotLightManager.addLight(this);
   }

   public EntityShot(abw w, double posX, double posY, double posZ, float yaw, float pitch, float size, float distance) {
      super(w);
      this.shooter = null;
      this.weapon = null;
      super.a(0.5F, 0.5F);
      super.b(posX, posY, posZ, yaw, pitch);
      super.u = super.u + -ls.a(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI) * distance;
      super.w = super.w + ls.b(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI) * distance;
      super.v = super.v + -ls.a(super.B / 180.0F * (float) Math.PI) * distance;
      this.size = size;
      this.rotationRoll = (float)Math.random() * 360.0F;
      this.distance = distance;
   }

   public void l_() {
      super.l_();
      float par1 = 0.0F;
      if (super.ac > 4 || !GuiSettingsStalker.advancedShot && super.ac > 2) {
         super.x();
      }
   }

   protected void a() {
      super.ah.a(10, 0);
   }

   protected void a(by nbttagcompound) {
   }

   protected void b(by nbttagcompound) {
   }

   public int c(float par1) {
      return 15728880;
   }

   public float d(float par1) {
      return 1.0F;
   }

   @Override
   public double getPosX() {
      return (int)super.u;
   }

   @Override
   public double getPosY() {
      return (int)super.v;
   }

   @Override
   public double getPosZ() {
      return (int)super.w;
   }

   @Override
   public abw getWorld() {
      return super.q;
   }
}
