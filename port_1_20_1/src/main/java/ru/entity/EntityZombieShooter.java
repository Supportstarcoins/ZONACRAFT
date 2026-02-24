package ru.stalcraft.entity;

import ru.stalcraft.ItemsConfig;
import ru.stalcraft.items.ItemWeapon;

public class EntityZombieShooter extends tm implements IEntityShooter, IEntityFighter, IEntityWalker {
   public EntityAIMobAgroTask agroTask;
   protected qa collideTask;
   protected EntityAIShellAttack shootTask;
   protected pp swimmingTask;
   protected qm wanderTask;
   protected ql lookIdleTask;
   public int shootCooldown = -1;
   protected float meleeDistanceSq = 4.0F;
   public int lastShot = 0;

   public EntityZombieShooter(abw par1World) {
      super(par1World);
      super.e[0] = 0.0F;
      this.collideTask = new qa(this, uf.class, this.getRunSpeed(), false);
      this.shootTask = new EntityAIShellAttack(this, this.getRunSpeed(), this.getStartMoveDistance(), 20.0F, -90.0F, 90.0F);
      super.c.a(0, new pp(this));
      super.c.a(3, new qm(this, this.getWalkSpeed()));
      super.c.a(4, new px(this, uf.class, 24.0F));
      super.c.a(4, new ql(this));
      this.agroTask = new EntityAIMobAgroTask(this, uf.class, this.getAgroDistance(), this.getMaxDistance());
      super.d.a(1, this.agroTask);
      if (par1World != null && !par1World.I) {
         ye stack = ItemsConfig.getRandomZombieWeapon();
         if (stack != null) {
            this.c(0, stack);
            this.shootCooldown = ((ItemWeapon)stack.b()).cooldown;
         }
      }

      if (par1World != null && !par1World.I) {
         this.setCombatTask();
      }
   }

   public void a(by par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      if (this.aZ() != null && this.aZ().b() instanceof ItemWeapon) {
         this.shootCooldown = ((ItemWeapon)this.aZ().b()).cooldown;
      }
   }

   protected boolean bf() {
      return true;
   }

   protected void az() {
      super.az();
      this.a(tp.b).a(this.getMaxDistance());
      this.a(tp.d).a(0.23F);
      this.a(tp.e).a(3.0);
      this.a(tp.a).a(20.0);
   }

   public void l_() {
      if (super.q != null && !super.q.I) {
         this.setCombatTask();
      }

      this.lastShot++;
      super.l_();
   }

   protected void setCombatTask() {
      if (this.aZ() == null && super.c.a.contains(this.shootTask)) {
         if (!super.c.a.contains(this.collideTask)) {
            super.c.a(1, this.collideTask);
         }

         super.c.a(this.shootTask);
      } else if (this.agroTask.targetEntity != null) {
         double distancesq = this.e(this.agroTask.targetEntity);
         if (distancesq > this.meleeDistanceSq && this.shootCooldown >= 0) {
            if (!super.c.a.contains(this.shootTask)) {
               super.c.a(1, this.shootTask);
            }

            super.c.a(this.collideTask);
         } else {
            if (!super.c.a.contains(this.collideTask)) {
               super.c.a(1, this.collideTask);
            }

            super.c.a(this.shootTask);
         }
      }
   }

   protected void bw() {
   }

   public boolean a(nb par1DamageSource, float damage) {
      if (this.ar()) {
         return false;
      } else {
         nn damager = par1DamageSource.i();
         if (damager instanceof og && !(damager instanceof EntityZombieShooter)) {
            for (nn helper : super.q.b(this, super.E.b(16.0, 16.0, 16.0))) {
               if (helper instanceof EntityZombieShooter) {
                  ((EntityZombieShooter)helper).onAllyAttack((of)damager, damage);
               }
            }

            this.agroTask.onOwnerAttack((of)damager, damage);
         }

         return super.a(par1DamageSource, damage);
      }
   }

   public void onAllyAttack(of enemy, float damage) {
      this.agroTask.onAllyAttack(enemy, damage);
   }

   protected float getWalkSpeed() {
      return 0.5F;
   }

   protected float getRunSpeed() {
      return 1.0F;
   }

   protected float getAgroDistance() {
      return 16.0F;
   }

   protected float getMaxDistance() {
      return 32.0F;
   }

   protected float getStartMoveDistance() {
      return 16.0F;
   }

   @Override
   public void shoot() {
      ye weapon = this.n(0);
      if (weapon != null) {
         this.lastShot = 0;
         ((ItemWeapon)weapon.b()).shoot(this, weapon, false, true);
      }
   }

   @Override
   public int getShootCooldown() {
      return this.shootCooldown;
   }

   protected String r() {
      return "mob.zombie.say";
   }

   protected String aO() {
      return "mob.zombie.hurt";
   }

   protected String aP() {
      return "mob.zombie.death";
   }

   public oj aY() {
      return oj.b;
   }

   @Override
   public boolean canShoot() {
      return this.lastShot > this.shootCooldown;
   }

   public String ay() {
      return "Зомби-стрелок";
   }

   public String an() {
      return "Зомби-стрелок";
   }

   @Override
   public void setLookPositionWithEntity(nn entity, float yawRotSpeed, float pitchRotSpeed) {
      this.h().a(entity, yawRotSpeed, pitchRotSpeed);
   }

   @Override
   public float getRotationYaw() {
      return super.aP;
   }

   @Override
   public boolean canSee(nn entity) {
      return this.l().a(entity);
   }

   @Override
   public rf getPathNavigator() {
      return this.k();
   }

   @Override
   public of getTarget() {
      return this.m();
   }

   @Override
   public void setTarget(of newTarget) {
      this.d(newTarget);
   }
}
