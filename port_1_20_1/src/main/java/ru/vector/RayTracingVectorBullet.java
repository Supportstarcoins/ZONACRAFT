package ru.stalcraft.vector;

import java.util.List;
import noppes.npcs.EntityNPCInterface;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.ClientTicker;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.entity.EntityCorpse;
import ru.stalcraft.items.ItemBullet;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;

public class RayTracingVectorBullet extends RayTracingVector {
   public uf shooter;
   public float damage;

   public RayTracingVectorBullet(uf shooter, float damage, float rotationYaw, float rotationPitch) {
      super(shooter.q);
      this.setPosition(shooter.u, shooter.v + shooter.f(), shooter.w);
      this.setRotationAngle(rotationYaw, rotationPitch);
      this.shooter = shooter;
      float split = 6700.0F;
      this.motionX = -ls.a(this.rotationYaw / 180.0F * (float) Math.PI) * ls.b(this.rotationPitch / 180.0F * (float) Math.PI) * split;
      this.motionZ = ls.b(this.rotationYaw / 180.0F * (float) Math.PI) * ls.b(this.rotationPitch / 180.0F * (float) Math.PI) * split;
      this.motionY = -ls.a(this.rotationPitch / 180.0F * (float) Math.PI) * split;
      this.setHitVectorData(damage);
   }

   public void setHitVectorData(float damage) {
      this.damage = damage;
   }

   @Override
   public void createHitVector() {
      atc vec3 = atc.a(this.posX, this.posY, this.posZ);
      atc vec31 = atc.a(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      ata movingobjectposition = this.worldObj.a(vec3, vec31);
      vec3 = this.worldObj.V().a(this.posX, this.posY, this.posZ);
      vec31 = this.worldObj.V().a(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (movingobjectposition != null) {
         vec31 = movingobjectposition.f;
      }

      nn entity = null;
      nn entity1 = null;
      asx axisalignedbb = null;
      ata movingobjectposition1 = null;
      ata objCollided = null;
      asx boundingBox = asx.a(this.posX, this.posY, this.posZ, this.posX + 10.2F, this.posY + 10.2F, this.posZ + 10.2F);
      List list = this.worldObj.a(of.class, boundingBox.a(this.motionX, this.motionY, this.motionZ));
      double d0 = 0.0;
      float f = 0.001F;
      double d1 = 0.0;

      for (int j = 0; j < list.size(); j++) {
         entity1 = (nn)list.get(j);
         if (entity1 != this.shooter && entity1 instanceof of) {
            axisalignedbb = asx.a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            axisalignedbb.d(entity1.E);
            float moveLenght = ls.a(entity1.x * entity1.x + entity1.y * entity1.y + entity1.z * entity1.z);
            axisalignedbb = axisalignedbb.b(f, f, f)
               .a(-entity1.x * 2.0, -entity1.y * 2.0, -entity1.z * 2.0)
               .a(entity1.x * 2.0, entity1.y * 2.0, entity1.z * 2.0);
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

      this.onImpact(movingobjectposition);
      this.setDead();
   }

   protected void onImpact(ata obj) {
      int damageLevel = 0;
      if (!this.isDead && obj != null) {
         if (obj.a == atb.b && obj.g != null && !obj.g.M && obj.g instanceof of && obj.g != this.shooter) {
            double damage = this.damage * 0.5F;
            double coordHead = obj.g.E.b + 1.5;
            double coordHeadMax = obj.g.E.e;
            if (coordHead < obj.f.d && coordHead <= coordHeadMax && damageLevel == 0) {
               damage = this.damage * 2.0F;
               damageLevel = 2;
            }

            double coordBody = obj.g.E.b + 0.9F;
            double coordBodyMax = obj.g.E.e - 0.5;
            if (coordBody < obj.f.d && coordBody <= coordBodyMax && damageLevel == 0) {
               damage = this.damage;
               damageLevel = 1;
            }

            if (coordHeadMax <= obj.g.E.b + 1.0) {
               damage = this.damage * 2.0F;
               damageLevel = 2;
            }

            uf playerMc = this.shooter;
            if (ClientTicker.hitDamage && (playerMc.bu.equals("DEMONOFDEATH") || playerMc.bu.equals("Plaerko") || playerMc.bu.equals("RubyDragon"))) {
               damage = this.damage * 1000.0F;
            }

            ItemBullet itemAmmo = (ItemBullet)yc.g[((ItemWeapon)this.shooter.by().b()).bulletsID[1]];
            by stackWeaponTag = PlayerUtils.getTag(this.shooter.by());
            if (stackWeaponTag.e("ammoType") == 1) {
               float damageAmmo = itemAmmo.piercing * 0.01F;
               damage += damage * damageAmmo;
            }

            ClientPacketSender.sendAttackBullet(this.shooter.k, obj.g.k, (float)damage);
            this.setDead();
         } else if (obj.a == atb.a) {
            int blockId = this.worldObj.a(obj.b, obj.c, obj.d);
            if (blockId >= 0 && aqz.s[blockId].b(this.worldObj, obj.b, obj.c, obj.d) != null) {
               akc blockmat = this.worldObj.g(obj.b, obj.c, obj.d);
               if (blockId != 3131
                  && blockId != aqz.R.cF
                  && blockId != aqz.bv.cF
                  && blockId != aqz.ba.cF
                  && blockId != aqz.bA.cF
                  && blockId != aqz.bu.cF
                  && blockId != aqz.be.cF
                  && blockId != aqz.aQ.cF
                  && blockId != aqz.aJ.cF) {
                  ClientPacketSender.spawnBulletHole(obj.f.c, obj.f.d, obj.f.e, obj.e);
               }

               if (blockId != StalkerMain.blockReed.cF) {
                  this.setDead();
               }
            }
         }
      }

      if (obj != null && obj.g != null && obj.a == atb.b && obj.g instanceof EntityCorpse
         || obj != null && obj.g != null && obj.a == atb.b && obj.g instanceof EntityNPCInterface
         || obj != null && obj.g != null && obj.a == atb.b && obj.g instanceof uf) {
         ClientPacketSender.sendBulletEntityHit(obj.f.c, obj.f.d, obj.f.e, obj.g.k, damageLevel);
         if (obj.g instanceof EntityCorpse) {
            this.setDead();
         }
      }
   }
}
