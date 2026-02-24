package ru.stalcraft.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class EntityAIMobAgroTask extends re {
   private static final float LOW_DISTANE_AGRO_PER_TICK = 2.5F;
   private static final float OWNER_DAMAGE_AGRO = 10.0F;
   private static final float AGRO_FACTOR_PER_TICK = 0.95F;
   private static final float ALLY_DAMAGE_AGRO = 2.0F;
   public of targetEntity;
   Class targetClass;
   float agroDistance;
   float maxDistance;
   private int checkDistance = 5;
   HashMap agro = new HashMap();

   public EntityAIMobAgroTask(on owner, float agroDistance, float maxDistance) {
      this(owner, uf.class, agroDistance, maxDistance);
   }

   public EntityAIMobAgroTask(on owner, Class targetClass, float agroDistance, float maxDistance) {
      super(owner, false, false);
      this.targetClass = targetClass;
      this.agroDistance = agroDistance;
      this.maxDistance = maxDistance;
      this.a(3);
   }

   public boolean a() {
      return true;
   }

   public boolean b() {
      return true;
   }

   public void d() {
      this.targetEntity = null;
      this.agro.clear();
   }

   public void onOwnerAttack(of enemy, float damage) {
      if (this.agro.containsKey(enemy)) {
         this.agro.put(enemy, (Float)this.agro.get(enemy) + 10.0F * damage);
      } else {
         this.agro.put(enemy, 10.0F * damage);
      }
   }

   public void onAllyAttack(of enemy, float damage) {
      if (this.agro.containsKey(enemy)) {
         this.agro.put(enemy, (Float)this.agro.get(enemy) + 2.0F * damage);
      } else {
         this.agro.put(enemy, 2.0F * damage);
      }
   }

   public void e() {
      this.removeUnsuitableTargets();
      if (--this.checkDistance <= 0) {
         float it = this.agroDistance * this.agroDistance;

         for (of it2 : super.c
            .q
            .a(
               this.targetClass,
               asx.a(
                  super.c.u - this.agroDistance,
                  super.c.v - this.agroDistance,
                  super.c.w - this.agroDistance,
                  super.c.u + this.agroDistance,
                  super.c.v + this.agroDistance,
                  super.c.w + this.agroDistance
               )
            )) {
            if (super.c.e(it2) <= it && (!(it2 instanceof uf) || !((uf)it2).bG.a)) {
               if (this.agro.containsKey(it2)) {
                  this.agro.put(it2, (Float)this.agro.get(it2) + 2.5F);
               } else {
                  this.agro.put(it2, 2.5F);
               }
            }
         }

         this.checkDistance = 5;
      }

      Iterator var6 = this.agro.entrySet().iterator();

      while (var6.hasNext()) {
         Entry var7 = (Entry)var6.next();
         var7.setValue((Float)var7.getValue() * 0.95F);
         if ((Float)var7.getValue() <= 0.0F) {
            var6.remove();
         }
      }

      of var8 = null;
      if (this.agro.size() > 0) {
         float var9 = -1.0F;

         for (Entry entry : this.agro.entrySet()) {
            if ((Float)entry.getValue() > var9) {
               var8 = (of)entry.getKey();
               var9 = (Float)entry.getValue();
            }
         }
      }

      this.targetEntity = var8;
      if (super.c.m() != this.targetEntity) {
         super.c.d(var8);
      }
   }

   private void removeUnsuitableTargets() {
      Iterator it = this.agro.entrySet().iterator();
      float distancesq = this.maxDistance * this.maxDistance;

      while (it.hasNext()) {
         Entry entry = (Entry)it.next();
         if ((Float)entry.getValue() == 0.0F
            || entry.getKey() == null
            || !((of)entry.getKey()).T()
            || super.c.e((nn)entry.getKey()) > distancesq
            || !super.c.a(((of)entry.getKey()).getClass())
            || entry.getKey() == super.c
            || ((of)entry.getKey()).ar != super.c.ar) {
            it.remove();
         }
      }
   }
}
