package ru.stalcraft.entity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import java.util.List;
import ru.stalcraft.server.network.ServerPacketSender;

public class EntityGrenade extends nn implements IEntityAdditionalSpawnData {
   private static final float MOTION_FACTOR = 0.999F;
   private static final float ROTATION_FACTOR = 0.999F;
   private static final float ENTITY_SIZE = 0.1F;
   public float xRotation = 0.0F;
   public float yRotation = 0.0F;
   public float zRotation = 0.0F;
   public float xRotationSpeed = 0.0F;
   public float yRotationSpeed = 0.0F;
   public float zRotationSpeed = 0.0F;
   protected boolean collided = false;
   protected double prevY;
   private float explosionSize;
   public uf shooter;
   public String modelName;
   public String textureName;
   private int lifetime;
   private boolean explosionOnCollide;

   public EntityGrenade(abw par1World) {
      super(par1World);
      this.xRotationSpeed = ((float)Math.random() - 0.5F) * 10.0F;
      this.yRotationSpeed = (float)Math.random() * 10.0F + 10.0F;
      this.zRotationSpeed = ((float)Math.random() - 0.5F) * 10.0F;
      this.a(0.1F, 0.1F);
      super.l = 1000.0;
      super.N = 0.15F;
   }

   public EntityGrenade(
      abw par1World, uf shooter, float speedFactor, float explosionSize, String modelName, String textureName, int lifetime, boolean explosionOnCollide
   ) {
      this(par1World);
      this.shooter = shooter;
      this.explosionSize = explosionSize;
      this.b(shooter.u, shooter.v + shooter.f(), shooter.w, shooter.A, shooter.B);
      super.u = super.u - ls.b(super.A / 180.0F * (float) Math.PI) * 0.16F;
      super.v -= 0.1F;
      super.w = super.w - ls.a(super.A / 180.0F * (float) Math.PI) * 0.16F;
      this.b(super.u, super.v, super.w);
      super.x = -ls.a(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI) * speedFactor;
      super.z = ls.b(super.A / 180.0F * (float) Math.PI) * ls.b(super.B / 180.0F * (float) Math.PI) * speedFactor;
      super.y = -ls.a(super.B / 180.0F * (float) Math.PI) * speedFactor;
      this.lifetime = lifetime;
      this.explosionOnCollide = explosionOnCollide;
      this.modelName = modelName;
      this.textureName = textureName;
   }

   public void l_() {
      super.l_();
      this.updatePos();
      if (super.q.I) {
         float rotationFactor = super.v == this.prevY ? 0.94905F : 0.999F;
         this.xRotationSpeed *= rotationFactor;
         this.yRotationSpeed *= rotationFactor;
         this.zRotationSpeed *= rotationFactor;
         this.xRotation = (this.xRotation + this.xRotationSpeed) % 360.0F;
         this.yRotation = (this.yRotation + this.yRotationSpeed) % 360.0F;
         this.zRotation = (this.zRotation + this.zRotationSpeed) % 360.0F;
      }

      this.prevY = super.v;
      if (!super.q.I && super.ac > this.lifetime) {
         this.x();
      }
   }

   public void updatePos() {
      super.U = super.u;
      super.V = super.v;
      super.W = super.w;
      if (this.D == 0.0F && this.C == 0.0F) {
         float f = ls.a(this.x * this.x + this.z * this.z);
         this.C = this.A = (float)(Math.atan2(this.x, this.z) * 180.0 / Math.PI);
         this.D = this.B = (float)(Math.atan2(this.y, f) * 180.0 / Math.PI);
      }

      if (!super.q.I) {
         atc vec3 = super.q.V().a(super.u, super.v, super.w);
         atc vec31 = super.q.V().a(super.u + super.x, super.v + super.y, super.w + super.z);
         ata movingobjectposition = super.q.a(vec3, vec31, false, true);
         vec3 = super.q.V().a(super.u, super.v, super.w);
         vec31 = super.q.V().a(super.u + super.x, super.v + super.y, super.w + super.z);
         if (movingobjectposition != null) {
            vec31 = super.q.V().a(movingobjectposition.f.c, movingobjectposition.f.d, movingobjectposition.f.e);
         }

         nn entity = null;
         List list = super.q.b(this, super.E.a(super.x, super.y, super.z).b(1.0, 1.0, 1.0));
         double d0 = 0.0;
         uf entitylivingbase = this.shooter;

         for (int prevMotionX = 0; prevMotionX < list.size(); prevMotionX++) {
            nn entity1 = (nn)list.get(prevMotionX);
            if (entity1.L() && (entity1 != entitylivingbase || super.ac >= 5)) {
               float prevMotionY = 0.3F;
               asx axisalignedbb = entity1.E.b(prevMotionY, prevMotionY, prevMotionY);
               ata prevMotionZ = axisalignedbb.a(vec3, vec31);
               if (prevMotionZ != null) {
                  double d1 = vec3.d(prevMotionZ.f);
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

         if (movingobjectposition != null && movingobjectposition.a == atb.a) {
            this.onImpact(movingobjectposition);
         }

         double var20 = super.x;
         double var21 = super.y;
         double var22 = super.z;
         this.d(super.x, super.y, super.z);
         if (this.explosionOnCollide && (var20 != super.x || var21 != super.y || var22 != super.z)) {
            this.x();
         }

         if (super.F) {
            if (this.explosionOnCollide) {
               this.x();
            } else {
               super.x *= 0.95;
               super.z *= 0.95;
            }
         }

         float f2 = ls.a(this.x * this.x + this.z * this.z);
         this.A = (float)(Math.atan2(this.x, this.z) * 180.0 / Math.PI);
         this.B = (float)(Math.atan2(this.y, f2) * 180.0 / Math.PI);

         while (this.B - this.D < -180.0F) {
            this.D -= 360.0F;
         }

         while (this.B - this.D >= 180.0F) {
            this.D += 360.0F;
         }

         while (this.A - this.C < -180.0F) {
            this.C -= 360.0F;
         }

         while (this.A - this.C >= 180.0F) {
            this.C += 360.0F;
         }

         this.B = this.D + (this.B - this.D) * 0.2F;
         this.A = this.C + (this.A - this.C) * 0.2F;
         f2 = 0.99F;
         float f3 = 0.05F;
         if (this.H()) {
            for (int k = 0; k < 4; k++) {
               float f4 = 0.25F;
               super.q.a("bubble", super.u - super.x * f4, super.v - super.y * f4, super.w - super.z * f4, super.x, super.y, super.z);
            }

            f2 = 0.8F;
         }

         super.x *= f2;
         super.y *= f2;
         super.z *= f2;
         super.y -= f3;
         ServerPacketSender.sendEntityPos(this);
      }
   }

   public void x() {
      if (!super.M && !super.q.I) {
         super.q.a(this.shooter, super.u, super.v, super.w, this.explosionSize, false);
      }

      super.x();
   }

   protected void onImpact(ata mop) {
      if (!super.q.I) {
         if (this.explosionOnCollide) {
            this.x();
         } else {
            if (mop.a.ordinal() == 0) {
               this.pushOff(mop.b, mop.c, mop.d, mop.e);
            } else {
               this.hitEntity(mop.g);
            }

            this.calculateNewImpact();
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
   }

   protected void pushOff(int blockX, int blockY, int blockZ, int side) {
      boolean setY = Math.abs(super.y) > 0.1F;
      if (side != 0 && side != 1) {
         if (side != 2 && side != 3) {
            super.x *= -0.5;
            super.y *= 0.5;
            super.z *= 0.5;
         } else {
            super.x *= 0.5;
            super.y *= 0.5;
            super.z *= -0.5;
         }
      } else if (setY) {
         super.x *= 0.8F;
         super.y *= -0.5;
         super.z *= 0.8F;
      } else {
         super.x *= 0.95F;
         super.y = 0.0;
         super.y *= -0.5;
         super.z *= 0.95F;
      }
   }

   protected void calculateNewImpact() {
      atc vec3 = super.q.V().a(super.u, super.v, super.w);
      atc vec31 = super.q.V().a(super.u + super.x, super.v + super.y, super.w + super.z);
      ata movingobjectposition = super.q.a(vec3, vec31, false, true);
      vec3 = super.q.V().a(super.u, super.v, super.w);
      vec31 = super.q.V().a(super.u + super.x, super.v + super.y, super.w + super.z);
      if (movingobjectposition != null) {
         vec31 = super.q.V().a(movingobjectposition.f.c, movingobjectposition.f.d, movingobjectposition.f.e);
      }

      if (!super.q.I) {
         nn entity = null;
         List list = super.q.b(this, super.E.a(super.x, super.y, super.z).b(1.0, 1.0, 1.0));
         double d0 = 0.0;
         uf entityliving = this.shooter;

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

      if (movingobjectposition != null && movingobjectposition.a == atb.a) {
         this.onImpact(movingobjectposition);
      }
   }

   protected void a(by tag) {
      this.explosionSize = tag.g("explosion_size");
      this.modelName = tag.i("model_name");
      this.textureName = tag.i("texture_name");
      this.lifetime = tag.e("lifetime");
      this.explosionOnCollide = tag.n("explosion_on_collide");
   }

   protected void b(by tag) {
      tag.a("explosion_size", this.explosionSize);
      tag.a("model_name", this.modelName);
      tag.a("texture_name", this.textureName);
      tag.a("lifetime", this.lifetime);
      tag.a("explosion_on_collide", this.explosionOnCollide);
   }

   protected void a() {
   }

   public void writeSpawnData(ByteArrayDataOutput data) {
      data.writeUTF(this.modelName);
      data.writeUTF(this.textureName);
   }

   public void readSpawnData(ByteArrayDataInput data) {
      this.modelName = data.readUTF();
      this.textureName = data.readUTF();
   }
}
