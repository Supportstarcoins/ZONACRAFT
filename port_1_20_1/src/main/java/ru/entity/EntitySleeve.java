package ru.stalcraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;

public class EntitySleeve extends uq {
   private static final float MOTION_FACTOR = 0.999F;
   private static final float ROTATION_FACTOR = 0.999F;
   private static final float ENTITY_SIZE = 0.05F;
   public float xRotation = 0.0F;
   public float yRotation = 0.0F;
   public float zRotation = 0.0F;
   public float xRotationSpeed = 0.0F;
   public float yRotationSpeed = 0.0F;
   public float zRotationSpeed = 0.0F;
   protected boolean collided = false;
   protected double prevY;
   public boolean renderOnGround;
   public final String model;
   public final bjo texture;
   private static final int LIFETIME = 250;

   public EntitySleeve(abw par1World, of shooter, boolean isShooterPlayer, ItemWeapon weapon) {
      super(par1World);
      boolean canAim = shooter == atv.w().h && ((ClientWeaponInfo)PlayerUtils.getInfo(atv.w().h).weaponInfo).isAiming();
      this.b(shooter.u, shooter.v + shooter.f() - (isShooterPlayer ? 0.3 : 0.1), shooter.w, shooter.aP, shooter.B);
      if (!canAim) {
         super.u = super.u - ls.b(super.A / 180.0F * (float) Math.PI) * 0.35F;
         super.w = super.w - ls.a(super.A / 180.0F * (float) Math.PI) * 0.35F;
      }

      double lookX = -ls.a(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI);
      double lookY = -ls.a(super.B / 180.0F * (float) Math.PI);
      double lookZ = ls.b(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI);
      double ySpeed = ls.a((super.B + 90.0F) / 180.0F * (float) Math.PI);
      atc look = atc.a(lookX, lookY, lookZ).a();
      super.u = super.u + look.c * 0.4;
      super.v = super.v + look.d * 0.4;
      super.w = super.w + look.e * 0.4;
      if (canAim) {
         super.u = super.u + (weapon.aimPosZ + 1.0F) / 2.0F * look.c;
         super.v = super.v + (weapon.aimPosZ + 1.0F) / 2.0F * look.d;
         super.w = super.w + (weapon.aimPosZ + 1.0F) / 2.0F * look.e;
      } else if (shooter == atv.w().h) {
         super.u = super.u + look.c * 0.5;
         super.v = super.v + look.d * 0.5;
         super.w = super.w + look.e * 0.5;
      }

      look.b((float)Math.toRadians(-90.0));
      super.y = ySpeed * 0.25;
      super.x = look.c * 0.15;
      super.z = look.e * 0.15;
      super.y = super.y * (1.0 + (Math.random() - 0.5) / 2.0);
      super.x = super.x * (1.0 + (Math.random() - 0.5) / 2.0);
      super.z = super.z * (1.0 + (Math.random() - 0.5) / 2.0);
      super.u = super.u + super.x / 2.0;
      super.w = super.w + super.z / 2.0;
      this.xRotationSpeed = ((float)Math.random() - 0.5F) * 10.0F;
      this.yRotationSpeed = (float)Math.random() * 10.0F + 10.0F;
      this.zRotationSpeed = ((float)Math.random() - 0.5F) * 10.0F;
      this.a(0.05F, 0.05F);
      super.P = 0.05F;
      this.model = weapon.sleeveModel;
      this.texture = weapon.sleeveTexture;
   }

   public EntitySleeve(abw par1World, double posX, double posY, double posZ, float yaw, float pitch, float distanceFactor, String sleeve) {
      super(par1World);
      this.b(posX, posY - 0.3, posZ, yaw, pitch);
      yaw += 90.0F;
      double ySpeed = ls.a((super.B + 90.0F) / 180.0F * (float) Math.PI);
      super.y = ySpeed * 0.25;
      super.x = -ls.a(yaw / 180.0F * (float) Math.PI) * ls.b(pitch / 180.0F * (float) Math.PI) * 0.15;
      super.z = ls.b(yaw / 180.0F * (float) Math.PI) * ls.b(pitch / 180.0F * (float) Math.PI) * 0.15;
      super.u = super.u + (double)(-ls.a(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI)) * distanceFactor;
      super.w = super.w + (double)(ls.b(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI)) * distanceFactor;
      super.v = super.v + ((double)(-ls.a(super.B / 180.0F * (float) Math.PI)) * distanceFactor + 0.1);
      super.u = super.u + super.x / 2.0;
      super.v = super.v + super.y / 2.0;
      super.w = super.w + super.z / 2.0;
      super.y = super.y * (1.0 + (Math.random() - 0.5) / 2.0);
      super.x = super.x * (1.0 + (Math.random() - 0.5) / 2.0);
      super.z = super.z * (1.0 + (Math.random() - 0.5) / 2.0);
      this.xRotationSpeed = ((float)Math.random() - 0.5F) * 10.0F;
      this.yRotationSpeed = (float)Math.random() * 10.0F + 10.0F;
      this.zRotationSpeed = ((float)Math.random() - 0.5F) * 10.0F;
      this.a(0.05F, 0.05F);
      super.P = 0.05F;
      this.model = sleeve;
      this.texture = new bjo("stalker", "models/sleeves/" + sleeve + ".png");
   }

   protected float e() {
      return 0.07F;
   }

   public void l_() {
      super.l_();
      float rotationFactor = super.v == this.prevY ? 0.94905F : 0.999F;
      this.xRotationSpeed *= rotationFactor;
      this.yRotationSpeed *= rotationFactor;
      this.zRotationSpeed *= rotationFactor;
      this.xRotation = (this.xRotation + this.xRotationSpeed) % 360.0F;
      this.yRotation = (this.yRotation + this.yRotationSpeed) % 360.0F;
      this.zRotation = (this.zRotation + this.zRotationSpeed) % 360.0F;
      this.prevY = super.v;
      if (super.ac > 250) {
         this.x();
      }
   }

   protected void a(ata mop) {
      if (mop.g != null || aqz.s[super.q.a(mop.b, mop.c, mop.d)].b(super.q, mop.b, mop.c, mop.d) != null) {
         if (mop.a.ordinal() == 0) {
            this.pushOff(mop.b, mop.c, mop.d, mop.e);
         } else {
            this.hitEntity(mop.g);
         }
      }
   }

   protected void hitEntity(nn entity) {
      super.x *= -0.5;
      super.y *= -0.5;
      super.z *= -0.5;
      this.xRotationSpeed *= 0.5F;
      this.yRotationSpeed *= 0.5F;
      this.zRotationSpeed *= 0.5F;
      this.calculateNewImpact();
   }

   protected void pushOff(int blockX, int blockY, int blockZ, int side) {
      boolean setY = Math.abs(super.y) > 0.2F || side == 0;
      if (side != 0 && side != 1) {
         if (side != 2 && side != 3) {
            super.x *= -0.5;
            super.y *= 0.8F;
            super.z *= 0.5;
         } else {
            super.x *= 0.5;
            super.y *= 0.8F;
            super.z *= -0.5;
         }
      } else if (setY) {
         super.x *= 0.8F;
         super.y *= -0.5;
         super.z *= 0.8F;
         this.renderOnGround = false;
      } else {
         super.x *= 0.8F;
         super.y = 0.0;
         super.z *= 0.8F;
         this.xRotationSpeed = 0.0F;
         this.zRotationSpeed = 0.0F;
         this.xRotation = 0.0F;
         this.zRotation = 0.0F;
         this.renderOnGround = true;
      }

      if (!setY) {
         this.xRotationSpeed *= 0.5F;
         this.yRotationSpeed *= 0.5F;
         this.zRotationSpeed *= 0.5F;
      } else {
         this.xRotationSpeed = ((float)Math.random() - 0.5F) * 10.0F;
         this.yRotationSpeed = (float)Math.random() * 10.0F + 10.0F;
         this.zRotationSpeed = ((float)Math.random() - 0.5F) * 10.0F;
      }

      this.calculateNewImpact();
   }

   protected void calculateNewImpact() {
      atc vec3 = super.q.V().a(super.u, super.v, super.w);
      atc vec31 = super.q.V().a(super.u + super.x, super.v + super.y, super.w + super.z);
      ata movingobjectposition = super.q.a(vec3, vec31);
      vec3 = super.q.V().a(super.u, super.v, super.w);
      vec31 = super.q.V().a(super.u + super.x, super.v + super.y, super.w + super.z);
      if (movingobjectposition != null) {
         vec31 = super.q.V().a(movingobjectposition.f.c, movingobjectposition.f.d, movingobjectposition.f.e);
      }

      if (!super.q.I) {
         nn entity = null;
         List list = super.q.b(this, super.E.a(super.x, super.y, super.z).b(1.0, 1.0, 1.0));
         double d0 = 0.0;
         of entityliving = this.h();

         for (int j = 0; j < list.size(); j++) {
            nn entity1 = (nn)list.get(j);
            if (entity1.L() && (entity1 != entityliving || super.ac >= 5)) {
               float f = 0.3F;
               asx axisalignedbb = entity1.E.b(f, f, f);
               ata movingobjectposition1 = axisalignedbb.a(vec3, vec31);
               if (movingobjectposition1 != null) {
                  double d1 = vec3.d(movingobjectposition1.f);
                  if (d1 < d0 || d0 == 0.0) {
                     entity = entity1;
                     d0 = d1;
                  }
               }
            }
         }

         if (entity != null) {
            movingobjectposition = new ata(entity);
         }
      }

      if (movingobjectposition != null) {
         if (movingobjectposition.a == atb.a && super.q.a(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d) == aqz.bj.cF) {
            this.ab();
         } else {
            this.a(movingobjectposition);
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public void a(double par1, double par3, double par5, float par7, float par8, int par9) {
   }
}
