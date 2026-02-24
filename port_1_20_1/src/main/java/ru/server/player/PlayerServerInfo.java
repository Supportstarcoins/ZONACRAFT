package ru.stalcraft.server.player;

import cpw.mods.fml.common.network.Player;
import java.util.Calendar;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import ru.stalcraft.Logger;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.WeaponInfo;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.entity.EntityBullet;
import ru.stalcraft.inventory.WeaponContainer;
import ru.stalcraft.items.IArtefakt;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemBackpack;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.IPlayerServerInfo;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.CommonProxy;
import ru.stalcraft.server.ServerContamination;
import ru.stalcraft.server.WeaponServerInfo;
import ru.stalcraft.server.clans.Clan;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.clans.Flag;
import ru.stalcraft.server.clans.FlagManager;
import ru.stalcraft.server.ejection.ServerEjection;
import ru.stalcraft.server.network.ServerPacketSender;
import ru.stalcraft.tile.TileEntityFlag;
import ru.stalcraft.tile.TileEntityMachineGun;

public class PlayerServerInfo extends PlayerInfo implements IPlayerServerInfo {
   private IClan clan;
   private float preMaxWeight;
   private float preWeight;
   private int tickPlayer;
   public boolean isEjectionSave;
   private boolean isPlayerCreativeAndOp;
   private boolean hasOpenInventory;
   private static final int MAX_REPUTATION_TIMER = 72000;
   private static final int MAX_DEATH_TIMER = 12000;
   private float prevSpeedModifier = 0.0F;
   private static final UUID speedModifierUUID = new UUID(482390457L, 547283457L);
   private double[] prevPosition = new double[]{0.0, 0.0, 0.0};
   private float[] prevRotation = new float[]{0.0F, 0.0F};
   public int fireTime;
   public int fireLevel;
   public int medicineUpdate;
   public float isPlayerRespawn;
   public int teleportCoolDown;

   public PlayerServerInfo(uf par1) {
      super(par1, new WeaponServerInfo(par1), new ServerContamination(par1));
      this.player.v().a(20, 0.0F);
      this.player.v().a(21, 60.0F);
      this.updatePrevPos();
      this.updateWeightSpeed();
      this.updateArtefaktStats();
      this.clan = ClanManager.instance().getPlayerClan(par1);
      by tag = this.getPersistedTag();
      if (!tag.b("reputation")) {
         tag.a("reputation", 0);
      }

      if (!tag.b("deathScore")) {
         tag.a("deathScore", 10);
      }

      if (!tag.b("reputationTimer")) {
         tag.a("reputationTimer", 72000);
      }

      if (!tag.b("deathScoreTimer")) {
         tag.a("deathScoreTimer", 12000);
      }

      if (this.clan != null && this.clan.getSpecialClan() != null && !tag.b("specialClanRepTimer")) {
         tag.a("specialClanRepTimer", this.clan.getSpecialClan().getMaxReputationTimer());
      }
   }

   @Override
   public void tick() {
      this.weaponInfo.tick();
      this.updatePrevPos();
      if (this.teleportCoolDown > 0) {
         this.teleportCoolDown--;
         ServerPacketSender.sendTeleportCoolDown(this.player, this.teleportCoolDown);
      }

      by tag = this.getPersistedTag();
      if (tag.e("caseValue") <= 0) {
         this.setCaseValue(0);
      }

      if (tag.e("duration") > 0 && !this.player.M && this.player.aN() > 0.001F) {
         this.medicineUpdate++;
         if (this.medicineUpdate >= 8) {
            int regeneration = tag.e("regeneration");
            int duration = tag.e("duration");
            float health = super.player.aN();
            ServerPacketSender.sendGuiParam(this.player, 0, duration);
            super.player.g(health + regeneration * 5.0E-4F);
            tag.a("duration", --duration);
            this.medicineUpdate = 0;
         }
      }

      if (tag.e("duration") <= 0) {
         tag.a("regeneration", 0);
         ServerPacketSender.sendGuiParam(this.player, 0, 0);
      }

      if (!this.player.bG.d) {
         for (int i = 0; i < this.player.bn.a.length; i++) {
            ye itemStack = this.player.bn.a[i];
            if (itemStack != null) {
               by itemStackNBT = PlayerUtils.getTag(itemStack);
               if (itemStackNBT.e("personal") != 0 && itemStackNBT.i("personalOwner").equals("")) {
                  itemStackNBT.a("personalOwner", this.player.bu);
               }
            }
         }
      }

      ye stack = null;
      IArtefakt artefakt = null;
      int ix = 0;
      if (super.player.bp != null && super.player.bp instanceof WeaponContainer) {
         WeaponContainer weaponContainer = (WeaponContainer)super.player.bp;
         if (weaponContainer != null) {
            ye updatedWeapon = weaponContainer.updatedWeapon;
            PlayerUtils.getTag(updatedWeapon).a("silencer", weaponContainer.silencer.d() != null);
            if (!((ItemWeapon)updatedWeapon.b()).integrateGrenade) {
               PlayerUtils.getTag(updatedWeapon).a("grenade_launcher", weaponContainer.grenadeLaunch.d() != null);
            }

            PlayerUtils.getTag(updatedWeapon).a("sight", weaponContainer.sight.d() != null);
         }
      }

      if (tag.g("isPlayerRespawn") > 0.0F || tag.g("isPlayerRespawn") < 0.0F) {
         asp tile = super.player.q.r(tag.e("respawnX"), tag.e("respawnY"), tag.e("respawnZ"));
         if (tile == null && !(tile instanceof TileEntityFlag)) {
            this.setRespawnPoint(this.player.q.t.i, tag.e("prevRespawnX"), tag.e("prevRespawnY"), tag.e("prevRespawnZ"));
         } else if (tile instanceof TileEntityFlag) {
            Flag flag = FlagManager.instance().getFlagByPos(super.player.q.t.i, tag.e("respawnX"), tag.e("respawnY"), tag.e("respawnZ"));
            Clan clan = (Clan)this.getClan();
            if (flag != null && flag.owner != clan) {
               this.setRespawnPoint(this.player.q.t.i, tag.e("prevRespawnX"), tag.e("prevRespawnY"), tag.e("prevRespawnZ"));
            }
         }
      }

      if (this.fireTime > 0) {
         this.fireTime--;
         super.player.d(this.fireLevel);
         super.player.a(nb.a, 0.5F);
      }

      if (!this.player.M && this.player.o(2) != null && this.player.o(2).b() instanceof ItemArmorArtefakt) {
         ItemArmorArtefakt armor = (ItemArmorArtefakt)this.player.o(2).b();
         by stackArmorNBT = PlayerUtils.getTag(this.player.o(2));
         if (armor.regeneration + stackArmorNBT.g("regenerationFactor") * 0.01F > 0.0F && this.player.aN() > 0.0F && this.player.aN() < 20.0F) {
            super.player.g(this.player.aN() + (armor.regeneration + stackArmorNBT.g("regenerationFactor") * 0.0025F));
         }
      }

      if (this.tickPlayer % 10 == 0) {
         if (super.player.bG.d) {
            for (int var25 = 0; var25 < 4; var25++) {
               super.protection[var25] = 100;
               super.immunity[var25] = false;
            }

            for (int var26 = 0; var26 < this.immunity.length; var26++) {
               if (!super.immunity[var26]) {
                  super.immunity[var26] = true;
               }
            }

            this.isPlayerCreativeAndOp = true;
         } else {
            if (this.isPlayerCreativeAndOp) {
               for (int var24 = 0; var24 < 4; var24++) {
                  super.protection[var24] = 0;
                  super.immunity[var24] = false;
               }

               this.isPlayerCreativeAndOp = false;
            }

            this.updateArtefaktStats();
         }

         stack = super.stInv.mainInventory[12];
         if (stack != null && !(tag = PlayerUtils.getTag(stack)).b("weightModifier")) {
            tag.a("weightModifier", ((ItemBackpack)stack.b()).weightModifier);
         }

         for (int var27 = 0; var27 < 9; var27++) {
            stack = var27 > 3 ? super.stInv.mainInventory[var27 - 3] : super.player.bn.b[var27];
            yc item;
            if (stack != null && (item = yc.g[stack.d]) instanceof IArtefakt) {
               tag = PlayerUtils.getTag(stack);
               artefakt = (IArtefakt)item;
               if (!tag.b("speedFactor")) {
                  tag.a("speedFactor", 0.1F);
               } else if (!tag.b("protection")) {
                  tag.a("protection", new int[]{artefakt.getProtection(0), artefakt.getProtection(1), artefakt.getProtection(2), artefakt.getProtection(3)});
               } else if (!tag.b("protection")) {
                  tag.a(
                     "immunity",
                     new int[]{
                        artefakt.getImmunity(0) ? 1 : 0, artefakt.getImmunity(1) ? 1 : 0, artefakt.getImmunity(2) ? 1 : 0, artefakt.getImmunity(3) ? 1 : 0
                     }
                  );
               } else if (!tag.b("bulletDamageFactor")) {
                  tag.a("bulletDamageFactor", artefakt.getBulletDamageFactor());
               } else if (!tag.b("jumpIncrease")) {
                  tag.a("jumpIncrease", artefakt.getSpeedFactor());
               } else if (!tag.b("fireResistance")) {
                  tag.a("fireResistance", artefakt.getFireResistance());
               } else if (!tag.b("waterWalking")) {
                  tag.a("waterWalking", artefakt.getWaterWalking());
               } else if (!tag.b("fallProtection")) {
                  tag.a("fallProtection", artefakt.getFallProtection());
               }
            }
         }

         this.updateWeightSpeed();
      }

      tag = this.getPersistedTag();
      if (tag.e("deathScore") < 10 && this.tickPlayer > 0 && this.tickPlayer % 2400 == 0) {
         tag.a("deathScore", ls.a(tag.e("deathScore") + 1, 0, 10));
      }

      if (this.activeEnergyEffect > 0) {
         this.activeEnergyEffect--;
      }

      if (super.weight <= 40.0F) {
         super.weightSpeed = 1.0F;
      } else if (super.weight > super.maxWeight) {
         super.weightSpeed = 0.15F;
      } else {
         super.weightSpeed = Math.max(1.0F - super.weight / super.maxWeight / 2.0F, 0.01F);
      }

      float speedModifier = super.weightSpeed;
      ItemArmorArtefakt info = null;
      ye stackArmor = this.player.o(2);
      if (!this.player.M && stackArmor != null && stackArmor.b() instanceof ItemArmorArtefakt) {
         by stackArmorNBT = PlayerUtils.getTag(stackArmor);
         info = (ItemArmorArtefakt)stackArmor.b();
         speedModifier += (info.speedModifier + stackArmorNBT.g("speedFactor")) * 0.01F;
      }

      if (speedModifier != this.prevSpeedModifier) {
         ot prevModifier = super.player.a(tp.d).a(speedModifierUUID);
         if (prevModifier != null) {
            super.player.a(tp.d).b(prevModifier);
         }

         if (speedModifier != 1.0F) {
            ot modifier = new ot(speedModifierUUID, "stalker_speed", speedModifier - 1.0F, 1);
            super.player.a(tp.d).a(modifier);
         }

         this.prevSpeedModifier = speedModifier;
      }

      this.tickPlayer++;
   }

   @Override
   public void readNBT(by tag) {
      try {
         ((ServerContamination)this.cont).readNBT(tag);
         ((WeaponServerInfo)this.weaponInfo).readNBT(tag);
         cg nbttaglist = tag.m("StalkerInventory");
         this.stInv.readFromNBT(nbttaglist);
         this.inventoryContainer.handleBackpackChanged(this.stInv.mainInventory[12] != null);
         this.attribs.handcuffs = tag.n("handcuffs");
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   @Override
   public void writeNBT(by tag) {
      try {
         ((ServerContamination)this.cont).writeNBT(tag);
         ((WeaponServerInfo)this.weaponInfo).writeNBT(tag);
         tag.a("StalkerInventory", this.stInv.writeToNBT(new cg()));
         tag.a("handcuffs", this.attribs.handcuffs);
         Logger.debug("Saving pos: " + this.player.bu + " (" + this.prevPosition[0] + ", " + this.prevPosition[1] + ", " + this.prevPosition[2]);
         tag.a("Pos", this.newDoubleNBTList(this.prevPosition));
         tag.a("Rotation", this.newFloatNBTList(this.prevRotation));
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   private cg newDoubleNBTList(double... par1ArrayOfDouble) {
      cg nbttaglist = new cg();

      for (int i = 0; i < par1ArrayOfDouble.length; i++) {
         nbttaglist.a(new cb((String)null, par1ArrayOfDouble[i]));
      }

      return nbttaglist;
   }

   private cg newFloatNBTList(float... par1ArrayOfFloat) {
      cg nbttaglist = new cg();

      for (int i = 0; i < par1ArrayOfFloat.length; i++) {
         nbttaglist.a(new cd((String)null, par1ArrayOfFloat[i]));
      }

      return nbttaglist;
   }

   @Override
   public void setRespawnPoint(int dimension, int x, int y, int z) {
      by tag = this.getPersistedTag();
      tag.a("respawnDimension", dimension);
      float respawn = x * y * z;
      if (respawn != tag.g("isPlayerRespawn")) {
         tag.a("isPlayerRespawn", respawn);
         if ((tag.e("respawnX") != x || tag.e("respawnY") != y || tag.e("respawnZ") != z) && FlagManager.instance().getFlagByPlayer(this.player) == null) {
            this.player.a("Теперь вы возрождаетесь здесь.");
         }

         tag.a("prevRespawnX", tag.e("respawnX"));
         tag.a("prevRespawnY", tag.e("respawnY"));
         tag.a("prevRespawnZ", tag.e("respawnZ"));
         tag.a("respawnX", x);
         tag.a("respawnY", y);
         tag.a("respawnZ", z);
      }
   }

   public void setPositionToPrevious() {
      this.player.a(this.prevPosition[0], this.prevPosition[1], this.prevPosition[2], this.prevRotation[0], this.prevRotation[1]);
   }

   private void updatePrevPos() {
      this.prevPosition[0] = this.player.r;
      this.prevPosition[1] = this.player.s + this.player.X;
      this.prevPosition[2] = this.player.t;
      this.prevRotation[0] = this.player.C;
      this.prevRotation[1] = this.player.D;
   }

   public void onDeath() {
      by tag = this.getPersistedTag();
      tag.a("deathScore", Math.max(-10, tag.e("deathScore") - 3));
      tag.a("deathScoreTimer", 12000);
      ServerPacketSender.sendDeathScore(this.player);
      tag.a("last_death", System.currentTimeMillis());
      int cooldown = Math.max(0, -tag.e("reputation") * 1200 - tag.e("deathScore") * 600);
      tag.a("last_cooldown", cooldown);
      ServerPacketSender.sendForceCooldown(this.player);
   }

   public void addReputation(int amount) {
      by tag = this.getPersistedTag();
      int oldReputation = tag.e("reputation");
      int reputation = oldReputation + amount;
      if (reputation > 10) {
         reputation = 10;
      }

      if (reputation < -10) {
         reputation = -10;
      }

      if (this.clan != null) {
         this.clan.addReputation(reputation - oldReputation);
      }

      tag.a("reputation", reputation);
      ServerPacketSender.sendReputation(this.player);
      ServerPacketSender.sendPlayerTag(this.player);
   }

   public void onWithdrawSalary() {
      this.getPersistedTag().a("last_salary", System.currentTimeMillis());
   }

   @Override
   public void onAgression() {
      if (this.attribs.agressionTimer == 0) {
         this.attribs.agressionTimer = 1200;
         ServerPacketSender.sendPlayerTag(this.player);
      } else {
         this.attribs.agressionTimer = 1200;
      }
   }

   @Override
   public void setHandcuffs(boolean handcuffs) {
      this.attribs.handcuffs = handcuffs;
      ServerPacketSender.sendHandcuffs(this.player, handcuffs);
      if (this.attribs.leashingPlayer != null && !handcuffs) {
         this.attribs.leashingPlayer.bn.a(new ye(StalkerMain.rope.cv, 1, 0));
         this.setLeahingPlayer((uf)null);
      }
   }

   @Override
   public void setLeahingPlayer(uf par1) {
      this.attribs.leashingPlayer = par1;
      ServerPacketSender.sendLeashing(this.player, this.attribs.leashingPlayer);
   }

   public void teleportToSpawnPoint() {
      Logger.debug("teleporting " + this.player.bu + " to spawn point");
      by tag = this.getPersistedTag();
      Flag flag = FlagManager.instance().getFlagByPlayer(this.player);
      int dimension;
      int respawnX;
      int respawnY;
      int respawnZ;
      if (flag != null) {
         dimension = flag.dimension;
         respawnX = flag.x;
         respawnY = flag.y;
         respawnZ = flag.z;
      } else {
         dimension = tag.e("respawnDimension");
         respawnX = tag.e("respawnX");
         respawnY = tag.e("respawnY");
         respawnZ = tag.e("respawnZ");
      }

      if (respawnX != 0 || respawnY != 0 || respawnZ != 0) {
         if (this.player.ar != dimension) {
            this.player.b(dimension);
         }

         this.player.a(respawnX + 0.5, respawnY + 0.5, respawnZ + 0.5);
      }
   }

   @Override
   public by getPersistedTag() {
      by data = this.player.getEntityData();
      if (!data.b("PlayerPersisted")) {
         data.a("PlayerPersisted", new by());
      }

      return data.l("PlayerPersisted");
   }

   @Override
   public int getForceCooldown() {
      if (!MinecraftServer.F().af().e(this.player.bu) && !this.player.bG.d) {
         by tag = this.getPersistedTag();
         if (tag.f("last_death") == 0L) {
            return 0;
         } else {
            int cooldown = Math.min(Math.max(0, tag.e("last_cooldown") - (int)((System.currentTimeMillis() - tag.f("last_death")) / 50L)), 36000);
            if (tag.n("deathTimer")) {
               cooldown = 0;
            }

            return cooldown;
         }
      } else {
         return 0;
      }
   }

   public boolean canGetSalary() {
      Calendar oldDate = Calendar.getInstance();
      Calendar newDate = Calendar.getInstance();
      oldDate.setTimeInMillis(this.getPersistedTag().f("last_salary"));
      newDate.setTimeInMillis(System.currentTimeMillis());
      return newDate.after(oldDate) && newDate.get(6) > oldDate.get(6) && newDate.get(1) > oldDate.get(1);
   }

   @Override
   public void setBackpackId(int newId) {
      this.attribs.backpack = newId;
   }

   public void resetInfo(WeaponInfo par1, uf par2) {
      this.weaponInfo = par1;
      this.cont = new ServerContamination(par2);
      this.attribs = new PlayerInfo.Attribs();
      this.updateArtefaktStats();
      this.updateWeightSpeed();
      this.teleportToSpawnPoint();
   }

   public void updateArtefaktStats() {
      this.speedFactor = 1.0F;
      int i = 0;

      for (int var6 = 0; var6 < 4; var6++) {
         this.protection[var6] = 0;
         this.immunity[var6] = false;
      }

      this.bulletDamageFactor = 1.0F;
      this.jumpIncrease = 0.0F;
      this.fallProtection = 0;
      this.fireResistance = false;
      this.waterWalking = false;
      ye stack = null;
      IArtefakt artefakt = null;

      for (int var7 = 0; var7 < 9; var7++) {
         stack = var7 > 3 ? this.stInv.mainInventory[var7 - 3] : this.player.bn.b[var7];
         yc item;
         if (stack != null && (item = yc.g[stack.d]) instanceof IArtefakt) {
            artefakt = (IArtefakt)item;
            this.speedFactor = this.speedFactor + (artefakt.getSpeedFactor() - 1.0F);

            for (int j = 0; j < 4; j++) {
               this.protection[j] = this.protection[j] + artefakt.getProtection(j);
               this.immunity[j] = this.immunity[j] | artefakt.getImmunity(j);
            }

            this.bulletDamageFactor = this.bulletDamageFactor + artefakt.getBulletDamageFactor();
            this.jumpIncrease = this.jumpIncrease + artefakt.getJumpIncrease();
            this.fireResistance = this.fireResistance | artefakt.getFireResistance();
            this.waterWalking = this.waterWalking | artefakt.getWaterWalking();
            this.fallProtection = this.fallProtection + artefakt.getFallProtection();
         }
      }

      if (this.speedFactor < 0.0F) {
         this.speedFactor = 0.0F;
      }
   }

   @Override
   public void itemInteractionForEntity(uf par1) {
      if (this.getHandcuffs() && this.getLeashingPlayer() == null) {
         this.setLeahingPlayer(this.player);
         par1.c(0, (ye)null);
         par1.a("Вы привязали игрока " + par1.bu);
         par1.a("Вас привязал игрок " + this.player.bu);
      } else if (!this.getHandcuffs()) {
         par1.a("Игрок должен сначала надеть наручники.");
      } else {
         par1.a("Игрок уже привязан.");
      }
   }

   @Override
   public IClan getClan() {
      return this.clan;
   }

   @Override
   public void setClan(IClan par1) {
      this.clan = par1;
   }

   @Override
   public void startEjection() {
      if (CommonProxy.serverEjectionManager.hasEjection()) {
         ServerEjection e = (ServerEjection)CommonProxy.serverEjectionManager.getEjection();
         ServerPacketSender.sendEjectionStartToPlayer((Player)this.player, e.id, e.age);
      }
   }

   @Override
   public void shooterMachineGun(TileEntityMachineGun par1) {
      if (par1.getShooter() == null
         && this.player.bn.h() == null
         && this.player.ar == par1.k.t.i
         && !this.player.M
         && this.player.aN() > 0.0F
         && this.player.f(par1.l, par1.m, par1.n) <= 2.0) {
         par1.updatePlayerPos();
         par1.updateRotation();
         this.weaponInfo.currentGun = par1;
         ServerPacketSender.sendMachinegunState(this.player, par1, true);
         ServerPacketSender.sendTileEntityEvent(par1, 1, par1.bulletsInCage);
         par1.setShooter(this.player);
      } else if (par1.getShooter() == this.player) {
         par1.removeShooter();
      }
   }

   public void sendUpdateStalkerContainer() {
      ((jv)this.player).bN();
      ServerPacketSender.sendUpdateStalkerInventory(this.player);
      ServerPacketSender.sendWindowId(this.player, ((jv)this.player).bY);
      this.inventoryContainer.d = ((jv)this.player).bY;
      ((jv)this.player).a(this.inventoryContainer);
   }

   @Override
   public void shootMachineGun(TileEntityMachineGun par1) {
      if (par1.getShooter() == this.player && par1.reloadTime == -1 && par1.bulletsInCage > 0 && par1.cooldown <= 0) {
         par1.bulletsInCage--;
         EntityBullet bullet = new EntityBullet(
            this.player, par1.l + 0.5, par1.m + 0.5, par1.n + 0.5, par1.yaw + par1.p * 90, par1.pitch, 10, 2.0F, "stalker:machinegun_hit", 0.995
         );
         ServerPacketSender.sendTileEntityEvent(par1, 1, par1.bulletsInCage);
         ServerPacketSender.sendTileEntityEvent(par1, 2, 0);
         par1.k.a(par1.l + 0.5, par1.m + 0.5, par1.n + 0.5, "stalker:machinegun_shoot", 5.0F, 0.9F + par1.k.s.nextFloat() * 0.1F);
         par1.k.d(bullet);
         par1.cooldown = 2;
      }
   }

   @Override
   public void reloadRequestMachineGun(TileEntityMachineGun par1) {
      if (par1.reloadTime == -1 && par1.getShooter() == this.player) {
         if (par1.bulletsInCage >= 300) {
            return;
         }

         if (!PlayerUtils.hasItem(this.player, 14955)) {
            return;
         }

         par1.reloadTime = 100;
         par1.k.a(par1.l + 0.5, par1.m + 0.5, par1.n + 0.5, "stalker:machinegun_reload", 1.0F, 0.9F + par1.k.s.nextFloat());
         ServerPacketSender.sendTileEntityEvent(par1, 3, 100);
      }
   }

   @Override
   public void reloadFinishMachineGun(TileEntityMachineGun par1) {
      if (par1.getShooter() == this.player && this.player.bn.e(14955)) {
         this.player.bn.d(14955);
         par1.bulletsInCage = 300;
         ((jv)this.player).a(this.player.bo);
         ServerPacketSender.sendTileEntityEvent(par1, 1, par1.bulletsInCage);
         ServerPacketSender.sendTileEntityEvent(par1, 3, -1);
      }
   }

   public void addDonateValue(int value) {
      by tag = this.getPersistedTag();
      int donateValue = tag.e("donateValue");
      int newDonateValue = donateValue + value;
      tag.a("donateValue", Integer.valueOf(newDonateValue));
      ServerPacketSender.sendUpdateDonateValue(this.player, tag.e("donateValue"));
   }

   public void addMoneyValue(int value) {
      by tag = this.getPersistedTag();
      int moneyValue = tag.e("moneyValue");
      int newMoneyValue = moneyValue + value;
      tag.a("moneyValue", Integer.valueOf(newMoneyValue));
      ServerPacketSender.sendUpdateMoneyValue(this.player, tag.e("moneyValue"));
   }

   public void addCaseValue(int value) {
      by tag = this.getPersistedTag();
      int caseValue = tag.e("caseValue");
      int newCaseValue = caseValue + value;
      tag.a("caseValue", Integer.valueOf(newCaseValue));
      ServerPacketSender.sendUpdateCaseDonateValue(this.player, tag.e("caseValue"));
   }

   @Override
   public void addItemSafe(uf par1EntityPlayer, ye updatedWeapon) {
   }

   @Override
   public void updateWeightSpeed() {
      this.updateWeight();
      this.updateMaxWeight();
   }

   private void updateWeight() {
      this.weight = 0.0F;
   }

   private void updateMaxWeight() {
      this.maxWeight = 60.0F;
      ye stack = super.stInv.mainInventory[12];
      if (stack != null) {
         super.maxWeight = super.maxWeight + ((ItemBackpack)stack.b()).weightModifier;
      }

      if (super.maxWeight > this.preMaxWeight || super.maxWeight < this.preMaxWeight) {
         super.player.v().b(21, super.maxWeight);
         this.preMaxWeight = super.maxWeight;
      }
   }

   @Override
   public void activeEffectEnergy() {
      this.activeEnergyEffect = 440;
   }

   public void setRespawn(int x, int y, int z) {
      by tag = this.getPersistedTag();
      float respawn = x * y * z;
      float isPlayerRespawn = this.isPlayerRespawn;
      if (isPlayerRespawn != respawn) {
         ServerPacketSender.sendPlayerRespawn(this.player, x, y, z);
      }
   }

   public void setFire(int fireTime, int fireLevel) {
      this.fireTime = fireTime;
      this.fireLevel = fireLevel;
   }

   public void setRegeneration(int regeneration, int duration) {
      int getReg = this.getPersistedTag().e("regeneration");
      int getDur = this.getPersistedTag().e("duration");
      this.getPersistedTag().a("regeneration", getReg + regeneration);
      this.getPersistedTag().a("duration", getDur + duration);
      ServerPacketSender.sendGuiParam(this.player, 0, getDur + duration);
   }

   public void setExtraRespawn(int x, int y, int z) {
      by tag = this.getPersistedTag();
      tag.a("prevRespawnX", x);
      tag.a("prevRespawnY", y);
      tag.a("prevRespawnZ", z);
   }
}
