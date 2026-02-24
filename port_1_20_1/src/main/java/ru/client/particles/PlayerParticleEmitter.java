package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.client.player.PlayerClientInfo;

public class PlayerParticleEmitter extends ParticleEmitter {
   public static ParticleIcon blood;
   public static ParticleIcon hitSmoke;
   private PlayerClientInfo playerInfo;

   public PlayerParticleEmitter(PlayerClientInfo playerInfo) {
      super(playerInfo);
      super.setCenter(super.centerX + 0.5, super.centerY + 4.5, super.centerZ + 0.5);
      super.setSize(-3.0, -5.0, -3.0, 3.0, 4.0, 3.0);
      this.playerInfo = playerInfo;
   }

   @Override
   public void tick() {
      super.setCenter(super.emmiter.getPosX(), super.emmiter.getPosY(), super.emmiter.getPosZ());
      super.tick();
   }

   public void addSplash(double posX, double posY, double posZ) {
      super.addParticle(new PlayerParticleBlood(this, blood, posX, posY, posZ));
   }

   public static void registerIcons(mt ir) {
      blood = (ParticleIcon)ir.a("stalker:blood");
      hitSmoke = (ParticleIcon)ir.a("stalker:smoke/smoke1");
   }

   @Override
   public boolean isValid() {
      return (!this.playerInfo.player.M || !this.playerInfo.player.q.e.contains(this.playerInfo.player)) && super.particles.size() <= 0;
   }
}
