package ru.stalcraft.client.player;

import ru.stalcraft.clans.IClan;
import ru.stalcraft.client.ClientContamination;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.particles.PlayerParticleEmitter;
import ru.stalcraft.entity.PlayerJumpHelper;
import ru.stalcraft.entity.PlayerMoveHelper;
import ru.stalcraft.entity.PlayerPathNavigator;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.tile.IParticleEmmiter;

public class PlayerClientInfo extends PlayerInfo implements IParticleEmmiter {
   private int forceCooldown = -1;
   public boolean hasQuitted;
   public PlayerMoveHelper moveHelper;
   public PlayerJumpHelper jumpHelper;
   public PlayerPathNavigator navigator;
   public boolean shouldMove;
   public boolean shouldJump;
   public int yesNoTick;
   public int[] position = new int[3];
   private int greenCaseValue = 0;
   public PlayerParticleEmitter particleEmitter;
   public int[] clientPlayerParameters = new int[1];
   public String inviterName;
   public int inviteTimeValid;
   public int teleportCoolDown;

   public PlayerClientInfo(uf par1) {
      super(par1, new ClientWeaponInfo(par1), new ClientContamination(par1));
      this.navigator = new PlayerPathNavigator(par1, par1.q);
      this.moveHelper = new PlayerMoveHelper(par1);
      this.jumpHelper = new PlayerJumpHelper(par1);
      this.player.v().a(20, 0.0F);
      this.player.v().a(21, 60.0F);
      this.clonePlayer();
      this.particleEmitter = new PlayerParticleEmitter(this);
      EffectsEngine.instance.addParticleEmitter(this.particleEmitter);
   }

   private void clonePlayer() {
      PlayerInfo info = PlayerUtils.getInfo(atv.w().h);
      if (info != null) {
         this.setReputation(info.getReputation());
         this.setDeathScore(info.getDeathScore());
      }
   }

   @Override
   public void tick() {
      this.weight = this.player.v().d(20);
      this.maxWeight = this.player.v().d(21);
      this.player.am = this.attribs.leashingPlayer != null;
      this.shouldMove = false;
      if (this.weaponInfo != null) {
         super.weaponInfo.tick();
      }

      if (this.weight <= 20.0F) {
         this.weightSpeed = 1.0F;
      } else if (this.weight > this.maxWeight) {
         this.weightSpeed = 0.15F;
      } else {
         this.weightSpeed = Math.max(1.0F - this.weight / this.maxWeight / 2.0F, 0.01F);
      }

      if (this.attribs.leashingPlayer != null && !this.attribs.leashingPlayer.M && this.attribs.leashingPlayer.e(this.player) > 6.5) {
         this.navigator.tryMoveToEntityLiving(this.attribs.leashingPlayer, 32.0);
      } else {
         this.navigator.clearPathEntity();
      }

      if (this.yesNoTick > 0) {
         this.yesNoTick--;
      } else {
         this.position[0] = 0;
         this.position[1] = 0;
         this.position[2] = 0;
      }

      if (this.inviteTimeValid > 0) {
         this.yesNoTick = 0;
         this.inviteTimeValid--;
      } else {
         this.inviterName = null;
      }

      this.navigator.onUpdateNavigation();
      this.moveHelper.onUpdateMoveHelper();
      this.jumpHelper.doJump();
   }

   @Override
   protected by getPersistedTag() {
      by data = this.player.getEntityData();
      if (!data.b("PlayerPersisted")) {
         data.a("PlayerPersisted", new by());
      }

      return data.l("PlayerPersisted");
   }

   public void setForceCooldown(int par1) {
      this.forceCooldown = par1;
   }

   @Override
   public int getForceCooldown() {
      return this.forceCooldown;
   }

   @Override
   public void itemInteractionForEntity(uf par1) {
   }

   @Override
   public void setLeahingPlayer(uf par1) {
      this.attribs.leashingPlayer = par1;
   }

   @Override
   public void setBackpackId(int par1) {
      this.attribs.backpack = par1;
   }

   @Override
   public void setHandcuffs(boolean par1) {
      this.attribs.handcuffs = par1;
   }

   public void setRespawn(int x, int y, int z) {
      this.yesNoTick = 100;
      this.position[0] = x;
      this.position[1] = y;
      this.position[2] = z;
   }

   public void deny() {
      this.yesNoTick = 0;
   }

   @Override
   public void onAgression() {
   }

   @Override
   public void setRespawnPoint(int par1, int par2, int par3, int par4) {
   }

   public boolean isNoDrop(ye par1) {
      return par1.e != null && par1.e.e("no_drop") != 0;
   }

   public boolean isClan(ye par1) {
      return par1.e != null && (!par1.e.i("clan").equals("") || par1.e.i("clan").equals("0"));
   }

   public boolean isPersonal(ye par1) {
      return par1.e != null && par1.e.e("personal") != 0 && par1.e.i("personalOwner").equals("");
   }

   public boolean isPlayerOwner(ye par1) {
      return par1.e != null && !par1.e.i("personalOwner").equals("");
   }

   public boolean isOwner(ye par1) {
      return par1.e != null && par1.e.i("owner") != "";
   }

   @Override
   public IClan getClan() {
      return null;
   }

   @Override
   public void setClan(IClan par1) {
   }

   public int getGreenCaseValue() {
      return this.greenCaseValue;
   }

   @Override
   public double getPosX() {
      return super.player.u;
   }

   @Override
   public double getPosY() {
      return super.player.v;
   }

   @Override
   public double getPosZ() {
      return super.player.w;
   }

   @Override
   public abw getWorld() {
      return super.player.q;
   }

   public void setClientParam(int param, int duration) {
      this.clientPlayerParameters[param] = duration;
   }

   public void setTradeInvite(String inviterName) {
      this.inviterName = inviterName;
      this.inviteTimeValid = 200;
   }
}
