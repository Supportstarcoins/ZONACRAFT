package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.items.EntityBolt;
import ru.stalcraft.tile.TileEntityFunnel;

public class FunnelParticleEmitter extends ParticleEmitter {
   private static final int DUST_PARTICLES_NUMBER = 750;
   private static final int LEAF_PARTICLES_NUMBER = 100;
   public static final float dustSize = 1.0F;
   private int notSpawnedDustParticles = 750;
   private int notSpawnedLeafParticles = 100;
   private FunnelEyeParticle eye;
   private static ParticleIcon[] dustTextureCoords;
   private static ParticleIcon[] leafTextureCoords;
   private static ParticleIcon eyeTextureCoords;
   private static ParticleIcon blastTextureCoords;
   public static ParticleIcon boltActive;
   public int tickSpawnEye;
   public int boltIndex;

   public FunnelParticleEmitter(TileEntityFunnel funnel) {
      super(funnel);
      super.setCenter(super.centerX + 0.5, super.centerY + 4.5, super.centerZ + 0.5);
      super.setSize(-3.0, -5.0, -3.0, 3.0, 4.0, 3.0);
      this.addEye();
   }

   @Override
   public void tick() {
      TileEntityFunnel tile = (TileEntityFunnel)super.emmiter;
      if (tile.activeTimer >= 0 && tile.activeTimer < 120) {
         int dustParticlesToSpawn = this.notSpawnedDustParticles / 15 + this.notSpawnedDustParticles % 15;

         for (int leafParticlesToSpawn = 0; leafParticlesToSpawn < dustParticlesToSpawn; leafParticlesToSpawn++) {
            super.particles.add(new FunnelParticle(this, dustTextureCoords[super.world.s.nextInt(dustTextureCoords.length)], false, 0.5F));
         }

         this.notSpawnedDustParticles -= dustParticlesToSpawn;
         int var6 = this.notSpawnedLeafParticles / 15 + this.notSpawnedLeafParticles % 15;

         for (int i = 0; i < var6; i++) {
            super.particles.add(new FunnelParticle(this, leafTextureCoords[super.world.s.nextInt(leafTextureCoords.length)], true, 0.5F));
         }

         this.notSpawnedLeafParticles -= var6;
      } else if (tile.activeTimer == 120) {
         this.particles.add(new GraviSplashParticle(this, blastTextureCoords));
      }

      if (this.boltIndex > 0) {
         this.boltIndex--;
      }

      this.tickSpawnEye++;
      if (tile.activeTimer <= 0 && this.tickSpawnEye >= 40) {
         this.addEye();
         this.tickSpawnEye = 0;
      }

      for (int i = 0; i < 2; i++) {
         super.addParticle(new FunnelParticleLeaf(this, leafTextureCoords[super.world.s.nextInt(leafTextureCoords.length)]));
      }

      this.renderDistanceSq = GuiSettingsStalker.particleRenderDistance * GuiSettingsStalker.particleRenderDistance;
      super.tick();
   }

   @Override
   public void reset() {
      super.reset();
      this.notSpawnedDustParticles = 750;
      this.notSpawnedLeafParticles = 100;
      super.particles.add(this.eye);
   }

   private void addEye() {
      this.eye = new FunnelEyeParticle(this, eyeTextureCoords);
      super.particles.add(this.eye);
   }

   public void spawnSplashBoltParticle(EntityBolt bolt) {
      if (this.boltIndex == 0) {
         CarouselSplashParticle particle = new CarouselSplashParticle(this, boltActive);
         super.particles.add(particle);
         particle.setPosition(bolt.u, bolt.v, bolt.w);
         this.boltIndex = 6;
      }
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.mc.f.g.contains(super.emmiter);
   }

   public static void registerIcons(mt ir) {
      dustTextureCoords = new ParticleIcon[8];

      for (int i = 0; i < 8; i++) {
         dustTextureCoords[i] = (ParticleIcon)ir.a("stalker:dust/dust" + (i + 1));
      }

      leafTextureCoords = new ParticleIcon[2];
      leafTextureCoords[0] = (ParticleIcon)ir.a("stalker:leaf/leaf1");
      leafTextureCoords[1] = (ParticleIcon)ir.a("stalker:leaf/leaf2");
      eyeTextureCoords = (ParticleIcon)ir.a("stalker:funnel/funnel_eye");
      blastTextureCoords = (ParticleIcon)ir.a("stalker:funnel/funnel_blast");
      boltActive = (ParticleIcon)ir.a("stalker:anomaly_active_bolt");
   }
}
