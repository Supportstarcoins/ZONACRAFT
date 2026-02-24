package ru.stalcraft.entity;

import java.util.HashSet;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class EntityAIClanGuardAgroTask extends ps {
   private HashSet agressiveTargets = new HashSet();
   public of targetEntity;
   private boolean isCurrentTargetAttacker;
   private float minPitch;
   private float maxPitch;
   private nn taskOwner;

   public EntityAIClanGuardAgroTask(nn entity, float minPitch, float maxPitch) {
      this.taskOwner = entity;
      this.a(3);
      this.minPitch = minPitch;
      this.maxPitch = maxPitch;
   }

   public boolean a() {
      return true;
   }

   public boolean b() {
      return true;
   }

   public void d() {
      this.targetEntity = null;
   }

   public void onOwnerAttack(of enemy, float damage) {
      this.agressiveTargets.add(enemy.k);
      if (((IEntityClanGuard)this.taskOwner).isEnabled()) {
         if (this.targetEntity == null && enemy instanceof uf) {
            uf player = (uf)enemy;
            PlayerInfo info = PlayerUtils.getInfo(player);
            if (info.getClan() == null || !info.getClan().getName().equals(((IEntityClanGuard)this.taskOwner).getClanName())) {
               this.targetEntity = enemy;
               this.isCurrentTargetAttacker = true;
            }
         } else if (this.targetEntity == null && !(enemy instanceof IEntityClanGuard)) {
            this.targetEntity = enemy;
            this.isCurrentTargetAttacker = true;
         }
      }
   }

   public void e() {
      if (((IEntityClanGuard)this.taskOwner).isEnabled()
         && !((IEntityClanGuard)this.taskOwner).getClanName().isEmpty()
         && ((IEntityClanGuard)this.taskOwner).getFlagZone() != null) {
         if (this.targetEntity != null
            && (
               this.targetEntity.M
                  || this.targetEntity.aN() <= 0.0F
                  || this.targetEntity.e(this.taskOwner) > 10000.0
                  || this.taskOwner.ar != this.targetEntity.ar
            )) {
            this.targetEntity = null;
         }

         boolean isTargetReacheble = false;
         if (this.targetEntity != null) {
            isTargetReacheble = ((IEntityFighter)this.taskOwner).canSee(this.targetEntity) && this.canShootTarget(this.targetEntity);
         }

         int currentTargetPriority = (isTargetReacheble ? 2 : 0) + (this.isCurrentTargetAttacker ? 1 : 0) + (this.targetEntity instanceof uf ? 1 : 0);
         if (!isTargetReacheble || !this.isCurrentTargetAttacker) {
            for (of entity : this.taskOwner.q.a(of.class, ((IEntityClanGuard)this.taskOwner).getFlagZone())) {
               if (entity instanceof uf) {
                  uf isNewTargetReachable1 = (uf)entity;
                  PlayerInfo isNewTargetAgressive2 = PlayerUtils.getInfo(isNewTargetReachable1);
                  if ((
                        isNewTargetAgressive2.getClan() == null
                           || !isNewTargetAgressive2.getClan().getName().equals(((IEntityClanGuard)this.taskOwner).getClanName())
                     )
                     && (!this.isCurrentTargetAttacker || !isTargetReacheble)) {
                     boolean newTargetPriority2 = ((IEntityFighter)this.taskOwner).canSee(isNewTargetReachable1)
                        && this.canShootTarget(isNewTargetReachable1)
                        && !isNewTargetReachable1.bG.d;
                     boolean isNewTargetAgressive1 = this.agressiveTargets.contains(isNewTargetReachable1.k);
                     int newTargetPriority1 = (newTargetPriority2 ? 2 : 0) + (isNewTargetAgressive1 ? 1 : 0) + 1;
                     if (newTargetPriority1 > currentTargetPriority && newTargetPriority1 > 1) {
                        currentTargetPriority = newTargetPriority1;
                        isTargetReacheble = newTargetPriority2;
                        this.isCurrentTargetAttacker = isNewTargetAgressive1;
                        this.targetEntity = isNewTargetReachable1;
                        if (newTargetPriority1 == 4) {
                           break;
                        }
                     }
                  }
               } else if (entity instanceof tm) {
                  boolean isNewTargetReachable = ((IEntityFighter)this.taskOwner).canSee(entity) && this.canShootTarget(entity);
                  boolean isNewTargetAgressive = this.agressiveTargets.contains(entity.k);
                  int newTargetPriority = (isNewTargetReachable ? 2 : 0) + (isNewTargetAgressive ? 1 : 0);
                  if (newTargetPriority > currentTargetPriority) {
                     currentTargetPriority = newTargetPriority;
                     isTargetReacheble = isNewTargetReachable;
                     this.isCurrentTargetAttacker = isNewTargetAgressive;
                     this.targetEntity = entity;
                     if (newTargetPriority == 3) {
                        break;
                     }
                  }
               }
            }
         }

         if (((IEntityFighter)this.taskOwner).getTarget() != this.targetEntity) {
            ((IEntityFighter)this.taskOwner).setTarget(this.targetEntity);
         }
      } else {
         this.targetEntity = null;
         if (((IEntityFighter)this.taskOwner).getTarget() != this.targetEntity) {
            ((IEntityFighter)this.taskOwner).setTarget(this.targetEntity);
         }
      }
   }

   private boolean canShootTarget(nn entity) {
      double xLook = entity.u - this.taskOwner.u;
      double yLook = entity.v + entity.f() - this.taskOwner.v - this.taskOwner.f();
      double zLook = entity.w - this.taskOwner.w;
      float f = ls.a(xLook * xLook + zLook * zLook);
      float pitch = -((float)(Math.atan2(yLook, f) * 180.0 / Math.PI));
      return pitch > this.minPitch && pitch < this.maxPitch;
   }
}
