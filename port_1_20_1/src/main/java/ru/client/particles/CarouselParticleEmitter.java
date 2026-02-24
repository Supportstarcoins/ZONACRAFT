package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.items.EntityBolt;
import ru.stalcraft.tile.TileEntityCarousel;

public class CarouselParticleEmitter extends ParticleEmitter {
   private static final int TORNADO_PARTICLES_NUMBER = 100;
   private static final int DUST_PARTICLES_NUMBER = 750;
   private static final int LEAF_PARTICLES_NUMBER = 100;
   private int notSpawnedDustParticles = 750;
   private int notSpawnedLeafParticles = 100;
   private static ParticleIcon[] dustTextureCoords;
   private static ParticleIcon[] leafTextureCoords;
   private static ParticleIcon blastTextureCoords;
   private static ParticleIcon boltActive;
   public int leaf;
   public int boltIndex;

   public CarouselParticleEmitter(TileEntityCarousel carousel) {
      super(carousel);
      super.setCenter(super.centerX + 0.5, super.centerY + 4.5, super.centerZ + 0.5);
      super.setSize(-3.0, -5.0, -3.0, 3.0, 4.0, 3.0);
      this.leaf = 0;
   }

   @Override
   public void tick() {
      super.tick();
      TileEntityCarousel tile = (TileEntityCarousel)super.emmiter;
      if (tile.activeTimer >= 0) {
         int i = this.notSpawnedDustParticles / 15 + this.notSpawnedDustParticles % 15;

         for (int leafParticlesToSpawn = 0; leafParticlesToSpawn < i; leafParticlesToSpawn++) {
            super.particles.add(new CarouselParticle(this, dustTextureCoords[super.world.s.nextInt(dustTextureCoords.length)], false, 1.8F));
         }

         this.notSpawnedDustParticles -= i;
         int var7 = this.notSpawnedLeafParticles / 15 + this.notSpawnedLeafParticles % 15;

         for (int i1 = 0; i1 < var7; i1++) {
            this.particles.add(new CarouselParticle(this, leafTextureCoords[super.world.s.nextInt(leafTextureCoords.length)], true, 1.8F));
         }

         this.notSpawnedLeafParticles -= var7;
         this.leaf = 0;
      } else if (tile.ticksSinceActive == 80) {
         super.reset();
         this.leaf = 0;
      }

      if (tile.activeTimer < 0) {
         for (int i = 0; i < 20; i++) {
            this.particles.add(new CarouselTornadoParticle(this, dustTextureCoords[super.world.s.nextInt(dustTextureCoords.length)]));
         }

         if (this.leaf <= 3) {
            for (int var6 = 0; var6 < 2; var6++) {
               this.particles.add(new CarouselTornadeLeafParticle(this, leafTextureCoords[super.world.s.nextInt(leafTextureCoords.length)]));
            }

            this.leaf++;
         }
      }

      if (this.boltIndex > 0) {
         this.boltIndex--;
      }

      this.renderDistanceSq = GuiSettingsStalker.particleRenderDistance * GuiSettingsStalker.particleRenderDistance;
   }

   @Override
   public void reset() {
      super.reset();
      this.notSpawnedDustParticles = 750;
      this.notSpawnedLeafParticles = 100;
      this.leaf = 0;
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.mc.f.g.contains(super.emmiter);
   }

   public void spawnSplashParticle(of target) {
      GraviSplashParticle particle = new GraviSplashParticle(this, blastTextureCoords);
      super.particles.add(particle);
      particle.setPosition(target.u, target.v + target.P / 2.0F, target.w);
   }

   public void spawnSplashBoltParticle(EntityBolt bolt) {
      if (this.boltIndex == 0) {
         CarouselSplashParticle particle = new CarouselSplashParticle(this, boltActive);
         super.particles.add(particle);
         particle.setPosition(bolt.u, bolt.v, bolt.w);
         this.boltIndex = 6;
      }
   }

   public static void registerIcons(mt ir) {
      dustTextureCoords = new ParticleIcon[8];

      for (int i = 0; i < 8; i++) {
         dustTextureCoords[i] = (ParticleIcon)ir.a("stalker:dust/dust" + (i + 1));
      }

      leafTextureCoords = new ParticleIcon[2];
      leafTextureCoords[0] = (ParticleIcon)ir.a("stalker:leaf/leaf1");
      leafTextureCoords[1] = (ParticleIcon)ir.a("stalker:leaf/leaf2");
      blastTextureCoords = (ParticleIcon)ir.a("stalker:funnel/funnel_blast");
      boltActive = (ParticleIcon)ir.a("stalker:anomaly_active_bolt");
   }
}
