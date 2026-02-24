package ru.stalcraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import ru.stalcraft.tile.IParticleEmmiter;
import ru.stalcraft.vector.RayTracingVectorBullet;

public class EntityTracer extends uh implements un, IParticleEmmiter {
   private int d = -1;
   private int e = -1;
   private int f = -1;
   private int au;
   private double av;
   private double damageFactor;
   private nn shooter;
   private String hitSound;
   private boolean isVelocity;
   private RayTracingVectorBullet bulletHitVector;

   public EntityTracer(abw par1World) {
      super(par1World);
      super.a(0.1F, 0.1F);
      super.N = 0.0F;
      this.l = 4096.0;
      this.isVelocity = false;
   }

   public EntityTracer(
      RayTracingVectorBullet bulletHitVector,
      nn shooter,
      int damage,
      boolean aim,
      float spread,
      float speedFactor,
      String hitSound,
      double damageFactor,
      float yaw,
      float pitch
   ) {
      this(shooter.q);
      float posX = 0.15F;
      float rotation = shooter.A;
      if (rotation > 290.0F) {
         posX = -(rotation * 0.001F);
      }

      if (rotation <= 90.0F) {
         posX = -0.15F;
      } else if (rotation <= 290.0F) {
         posX = 0.25F;
      }

      if (rotation > 240.0F && rotation <= 275.0F) {
         posX = 0.15F;
      }

      super.b(shooter.u + posX, shooter.v + shooter.f() - 0.075F, shooter.w, shooter.A, shooter.B);
      super.x = -ls.a(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI);
      super.z = ls.b(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI);
      super.y = -ls.a(super.B / 180.0F * (float) Math.PI);
      this.c(super.x, super.y, super.z, 3.55F, spread);
      this.bulletHitVector = bulletHitVector;
      this.shooter = shooter;
      this.av = damage;
      this.damageFactor = damageFactor;
      this.hitSound = hitSound;
   }

   public EntityTracer(
      nn shooter, double posX, double posY, double posZ, float yaw, float pitch, int damage, float speedFactor, String hitSound, double damageFactor
   ) {
      this(shooter.q);
      this.b(posX, posY, posZ, yaw, pitch);
      super.x = -ls.a(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI);
      super.z = ls.b(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI);
      super.y = -ls.a(super.B / 180.0F * (float) Math.PI);
      this.c(super.x, super.y, super.z, 10.0F, 0.0F);
      this.damageFactor = damageFactor;
      this.shooter = shooter;
      this.av = damage;
      this.hitSound = hitSound;
   }

   public void c(double par1, double par3, double par5, float par7, float par8) {
      float f2 = ls.a(par1 * par1 + par3 * par3 + par5 * par5);
      par1 /= f2;
      par3 /= f2;
      par5 /= f2;
      par1 += this.ab.nextGaussian() * (this.ab.nextBoolean() ? -1 : 1) * 0.0075F * par8;
      par3 += this.ab.nextGaussian() * (this.ab.nextBoolean() ? -1 : 1) * 0.0075F * par8;
      par5 += this.ab.nextGaussian() * (this.ab.nextBoolean() ? -1 : 1) * 0.0075F * par8;
      par1 *= par7;
      par3 *= par7;
      par5 *= par7;
      this.x = par1;
      this.y = par3;
      this.z = par5;
      float f3 = ls.a(par1 * par1 + par5 * par5);
      this.C = this.A = (float)(Math.atan2(par1, par5) * 180.0 / Math.PI);
      this.D = this.B = (float)(-Math.atan2(par3, f3) * 180.0 / Math.PI);
      this.j = 0;
   }

   public void a(by tag) {
      super.a(tag);

      try {
         this.hitSound = tag.i("hit_sound");
         this.av = tag.h("damage");
         this.damageFactor = tag.h("damage_factor");
      } catch (Exception var3) {
         this.x();
      }
   }

   public void b(by tag) {
      super.b(tag);
      tag.a("hit_sound", this.hitSound);
      tag.a("damage", this.av);
      tag.a("damage_factor", this.damageFactor);
   }

   public void l_() {
      super.y();
      if (this.bulletHitVector != null && this.bulletHitVector.isDead) {
         super.x();
      }

      int i = super.q.a(this.d, this.e, this.f);
      if (i > 0 && i != 20 && i != 30 && i != 89 && i != 102) {
         aqz.s[i].a(super.q, this.d, this.e, this.f);
         asx axisalignedbb = aqz.s[i].b(super.q, this.d, this.e, this.f);
         if (axisalignedbb != null && axisalignedbb.a(super.q.V().a(super.u, super.v, super.w))) {
            super.F = true;
         }
      }

      if (!super.F) {
         this.au++;
         atc vec3 = super.q.V().a(super.u, super.v, super.w);
         atc vec31 = super.q.V().a(this.u + super.x, super.v + super.y, super.w + super.z);
         ata movingobjectposition = super.q.a(vec3, vec31);
         vec3 = super.q.V().a(super.u, super.v, super.w);
         vec31 = super.q.V().a(super.u + super.x, super.v + super.y, super.w + super.z);
         if (movingobjectposition != null) {
            vec31 = super.q.V().a(movingobjectposition.f.c, movingobjectposition.f.d, movingobjectposition.f.e);
         }

         if (!super.q.I) {
            nn entity = null;
            nn entity1 = null;
            asx axisalignedbb = null;
            ata movingobjectposition1 = null;
            ata objCollided = null;
            List list = super.q.b(this, super.E.a(super.x, super.y, super.z));
            double d0 = 0.0;
            float f = 0.001F;
            double d1 = 0.0;

            for (int j = 0; j < list.size(); j++) {
               entity1 = (nn)list.get(j);
               if (entity1.L() && entity1 != this.shooter) {
                  axisalignedbb = entity1.E.b(f, f, f);
                  movingobjectposition1 = axisalignedbb.a(vec3, vec31);
                  if (movingobjectposition1 != null) {
                     d1 = vec3.d(movingobjectposition1.f);
                     if (d1 < d0 || d0 == 0.0) {
                        objCollided = movingobjectposition1;
                        entity = entity1;
                        d0 = d1;
                     }
                  }
               }
            }

            if (entity != null) {
               movingobjectposition = new ata(entity);
               movingobjectposition.f = objCollided.f;
            }
         }

         if (this.ac > 30) {
            super.x();
         }

         super.u = super.u + super.x;
         super.v = super.v + super.y;
         super.w = super.w + super.z;
         float f2 = ls.a(super.x * super.x + super.z * super.z);
         this.A = (float)(Math.atan2(this.x, this.z) * 180.0 / Math.PI);
         this.B = (float)(-Math.atan2(this.y, f2) * 180.0 / Math.PI);
         float f4 = 0.99F;
         float f1 = 0.05F;
         if (this.H()) {
            for (int j1 = 0; j1 < 4; j1++) {
               float f3 = 0.25F;
               super.q.a("bubble", super.u - super.x * f3, super.v - super.y * f3, super.w - super.z * f3, super.x, super.y, super.z);
            }

            f4 = 0.8F;
         }

         super.x *= f4;
         super.y *= f4;
         super.z *= f4;
         super.b(super.u, super.v, super.w);
         f2 = 0.99F;
         this.av = this.av * this.damageFactor;
         super.x /= f2;
         super.y /= f2;
         super.z /= f2;
      }
   }

   protected float getGravityVelocity() {
      return 0.0F;
   }

   private atc getHitPoint() {
      atc from = null;
      atc to = null;
      nn entity = null;
      List list = null;
      double d0 = 0.0;
      double d1 = 0.0;
      of entitylivingbase = (of)this.shooter;
      nn entity1 = null;
      asx axisalignedbb = null;
      ata movingobjectposition1 = null;
      float f = 0.3F;

      for (int i = 1; i <= 100; i++) {
         from = super.q.V().a(super.u + super.x * 0.01 * (i - 1), super.v * 0.01 * (i - 1), super.w * 0.01 * (i - 1));
         to = super.q.V().a(super.u + super.x * 0.01 * i, super.v + super.y * 0.01 * i, super.w + super.z * 0.01 * i);
         list = super.q.b(this, super.E.a(super.x, super.y, super.z).b(1.0, 1.0, 1.0));

         for (int j = 0; j < list.size(); j++) {
            entity1 = (nn)list.get(j);
            if (entity1.L() && (entity1 != entitylivingbase || super.ac >= 5)) {
               axisalignedbb = entity1.E.b(f, f, f);
               movingobjectposition1 = axisalignedbb.a(from, to);
               if (movingobjectposition1 != null) {
                  d1 = from.d(movingobjectposition1.f);
                  if (d1 < d0 || d0 == 0.0) {
                     entity = entity1;
                     d0 = d1;
                  }
               }
            }
         }

         if (entity != null) {
            return to;
         }
      }

      return null;
   }

   private atc getHeadCenter(uf player) {
      return player.ah()
         ? atc.a(player.u, player.v + 1.7F, player.w)
         : (
            !player.H() && !player.bG.b && player.P < 1.0F
               ? atc.a(player.u + Math.sin(Math.toRadians(player.A)) * 0.3, player.v + 0.25, player.w + Math.cos(Math.toRadians(player.A)))
               : atc.a(player.u, player.v + 1.75, player.w)
         );
   }

   @SideOnly(Side.CLIENT)
   public void addMotion(float x, float y, float z) {
   }

   @SideOnly(Side.CLIENT)
   public void h(double par1, double par3, double par5) {
      this.x = par1;
      this.y = par3;
      this.z = par5;
      float f = ls.a(par1 * par1 + par5 * par5);
      if (this.D == 0.0F && this.C == 0.0F) {
         this.C = this.A = (float)(Math.atan2(par1, par5) * 180.0 / Math.PI);
         this.D = this.B = (float)(-Math.atan2(par3, f) * 180.0 / Math.PI);
         this.b(this.u, this.v, this.w, this.A, this.B);
         this.j = 0;
      }
   }

   @SideOnly(Side.CLIENT)
   public void a(double par1, double par3, double par5, float par7, float par8, int par9) {
   }

   public void a(double par1, double par3, double par5, float par7, float par8) {
      this.r = this.u = par1;
      this.s = this.v = par3;
      this.t = this.w = par5;
      this.C = this.A = par7;
      this.D = this.B = par8;
      this.X = 0.0F;
      this.b(this.u, this.v, this.w);
      this.b(par7, par8);
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
