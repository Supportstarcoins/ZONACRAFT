package ru.stalcraft.entity;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.network.ServerPacketSender;

public abstract class EntityTurrel extends nn implements IEntityShooter, IEntityClanGuard, IEntityFighter {
   public int shootCooldown = -1;
   public int lastShot = 10000;
   protected int damage;
   protected boolean enabled = true;
   protected asx agroZone;
   public final float minPitch;
   public final float maxPitch;
   protected EntityAIShellAttack shootTask;
   public EntityAIClanGuardAgroTask agroTask;
   private String hitSound;
   private String shootSound;
   private int dropId;
   protected String clanName;
   public float gunRoll = 0.0F;
   public float prevGunRoll = 0.0F;
   protected int health;
   private of target;
   public final pt tasks;
   public final pt targetTasks;
   private TurrelSenses senses;
   private TurrelLookHelper lookHelper;

   public EntityTurrel(abw world, int cooldown, int damage, float yawRotSpeed, float minPitch, float maxPitch, int dropId, String hitSound, String shootSound) {
      super(world);
      this.shootCooldown = cooldown;
      this.damage = damage;
      this.minPitch = minPitch;
      this.maxPitch = maxPitch;
      this.dropId = dropId;
      super.O = 1.0F;
      super.ag = true;
      super.m = true;
      this.hitSound = hitSound;
      this.shootSound = shootSound;
      this.senses = new TurrelSenses(this);
      this.lookHelper = new TurrelLookHelper(this);
      this.tasks = new pt(world != null && world.C != null ? world.C : null);
      this.targetTasks = new pt(world != null && world.C != null ? world.C : null);
      this.agroTask = new EntityAIClanGuardAgroTask(this, minPitch, maxPitch);
      this.targetTasks.a(1, this.agroTask);
      this.shootTask = new EntityAIShellAttack(this, 0.0F, 0.0F, yawRotSpeed, minPitch, maxPitch);
      this.tasks.a(1, this.shootTask);
   }

   protected void a() {
   }

   public EntityTurrel(
      abw world,
      int cooldown,
      int damage,
      float yawRotSpeed,
      float minPitch,
      float maxPitch,
      int dropId,
      String hitSound,
      String shootSound,
      String clanName,
      asx agroZone
   ) {
      this(world, cooldown, damage, yawRotSpeed, minPitch, maxPitch, dropId, hitSound, shootSound);
      this.clanName = clanName;
      this.agroZone = agroZone;
   }

   public void l_() {
      super.l_();
      this.lastShot++;
      if (this.enabled
         && !super.q.I
         && super.ac % 20 == 0
         && (StalkerMain.clanManager.getClan(this.clanName) == null || StalkerMain.flagManager.getFlagNearby(super.ar, (int)super.u, (int)super.w) == null)) {
         this.enabled = false;
         this.targetTasks.a(this.agroTask);
         this.tasks.a(this.shootTask);
         this.target = null;
      }

      if (!super.q.I) {
         this.senses.clearSensingCache();
         this.targetTasks.a();
         this.tasks.a();
         this.lookHelper.onUpdateLook();
         if (super.A != super.C || super.B != super.D) {
            ServerPacketSender.sendRotation(this, super.A, super.B);
         }
      }
   }

   @Override
   public void shoot() {
      this.lastShot = 0;
      EntityBullet bullet = new EntityBullet(this, super.u, super.v, super.w, super.A, super.B, this.damage, 2.0F, this.hitSound, 1.0);
      if (!super.q.I) {
         super.q.a(super.u, super.v, super.w, this.shootSound, 5.0F, 0.9F + super.q.s.nextFloat() * 0.1F);
         ServerPacketSender.sendTurrelShoot(this);
         super.q.d(bullet);
      }
   }

   @Override
   public int getShootCooldown() {
      return this.shootCooldown;
   }

   public void a(by tag) {
      this.agroZone = asx.a(tag.h("minX"), 0.0, tag.h("minZ"), tag.h("maxX"), 256.0, tag.h("maxZ"));
      this.enabled = tag.n("enabled");
      this.clanName = tag.i("clan");
      if (!this.enabled) {
         this.targetTasks.a(this.agroTask);
         this.tasks.a(this.shootTask);
         this.target = null;
      }
   }

   public void b(by tag) {
      tag.a("minX", this.agroZone.a);
      tag.a("minZ", this.agroZone.c);
      tag.a("maxX", this.agroZone.d);
      tag.a("maxZ", this.agroZone.f);
      tag.a("enabled", this.enabled);
      tag.a("clan", this.clanName);
   }

   @Override
   public boolean canShoot() {
      return this.lastShot > this.shootCooldown;
   }

   public boolean c(uf par1EntityPlayer) {
      if (!this.enabled && !super.q.I) {
         this.x();
         if (!super.q.I) {
            float f = 0.7F;
            ye stack = new ye(this.dropId, 1, 0);
            double d0 = super.q.s.nextFloat() * f + (1.0F - f) * 0.5;
            double d1 = super.q.s.nextFloat() * f + (1.0F - f) * 0.5;
            double d2 = super.q.s.nextFloat() * f + (1.0F - f) * 0.5;
            ss entityitem = new ss(super.q, super.u + d0, super.v + d1, super.w + d2, stack);
            entityitem.b = 10;
            super.q.d(entityitem);
         }
      }

      return true;
   }

   public boolean L() {
      return true;
   }

   public float f() {
      return super.P * 0.13F;
   }

   public boolean M() {
      return false;
   }

   public void setMoveForward(float par1) {
   }

   public asx g(nn par1Entity) {
      return par1Entity.E;
   }

   public asx E() {
      return super.E;
   }

   public void knockBack(nn par1Entity, float par2, double par3, double par5) {
   }

   public boolean isPotionApplicable(nj par1PotionEffect) {
      return false;
   }

   @Override
   public String getClanName() {
      return this.clanName;
   }

   @Override
   public asx getFlagZone() {
      return this.agroZone;
   }

   public abstract String getSleeveModelName();

   public abstract float getLightDistance();

   public abstract float getSleeveDistance();

   public abstract float getRotationPointZ();

   public String ay() {
      return "Турель";
   }

   public String an() {
      return "Турель";
   }

   @Override
   public of getTarget() {
      return this.target;
   }

   @Override
   public void setTarget(of newTarget) {
      this.target = newTarget;
   }

   @Override
   public void setLookPositionWithEntity(nn entity, float yawRotSpeed, float pitchRotSpeed) {
      this.lookHelper.setLookPositionWithEntity(entity, yawRotSpeed, pitchRotSpeed);
   }

   @Override
   public float getRotationYaw() {
      return super.A;
   }

   public boolean canEntityBeSeen(nn entity) {
      return super.q.a(super.q.V().a(super.u, super.v + this.f(), super.w), super.q.V().a(entity.u, entity.v + entity.f(), entity.w)) == null;
   }

   @Override
   public boolean canSee(nn entity) {
      return this.senses.canSee(entity);
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean ar() {
      return super.ar()
         || !super.q.I
            && this.clanName != null
            && ClanManager.instance().getClan(this.clanName) != null
            && ClanManager.instance().getClan(this.clanName).isAdminClan;
   }

   public boolean a(nb par1DamageSource, float par2) {
      if (this.ar()) {
         return false;
      } else if (super.q.I) {
         return false;
      } else if (this.health <= 0) {
         return false;
      } else {
         nn damager = par1DamageSource.i();
         if (damager instanceof of && this.agroTask != null) {
            this.agroTask.onOwnerAttack((of)damager, this.damage);
         }

         this.health = Math.max(this.health - (int)par2, 0);
         if (this.health == 0) {
            this.x();
         }

         return true;
      }
   }
}
