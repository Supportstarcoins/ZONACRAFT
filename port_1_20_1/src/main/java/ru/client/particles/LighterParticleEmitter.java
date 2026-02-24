package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.tile.TileEntityLighter;

public class LighterParticleEmitter extends ParticleEmitter {
   private static final int MAX_PARTICLES_ACTIVE = 3000;
   public static final int PARTICLE_LIFE = 20;
   private static ParticleIcon[] icons;
   private static ParticleIcon sparkIcon;

   public LighterParticleEmitter(TileEntityLighter lighter) {
      super(lighter);
      super.setCenter(super.centerX + 0.5, (float)super.centerY + 0.25, super.centerZ + 0.5);
      super.setSize(-2.0, -3.0, -2.0, 2.0, 7.0, 2.0);
   }

   @Override
   public void tick() {
      super.tick();
      if (((TileEntityLighter)super.emmiter).ticksSleep > 0) {
         int particlesCount = 12;
         int index = super.world.s.nextInt(4);

         for (int i = 0; i < particlesCount; i++) {
            super.particles.add(new LighterParticle(this, icons[index]));
         }
      } else if (super.world.s.nextInt(3) == 0) {
         super.particles.add(new LightSparkParticle(this, sparkIcon));
      }

      super.renderDistanceSq = GuiSettingsStalker.particleRenderDistance * GuiSettingsStalker.particleRenderDistance;
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.mc.f.g.contains(super.emmiter);
   }

   public static void registerIcons(mt ir) {
      icons = new ParticleIcon[4];

      for (int i = 0; i < 4; i++) {
         icons[i] = (ParticleIcon)ir.a("stalker:lighter/newfire" + (i + 1));
      }

      sparkIcon = (ParticleIcon)ir.a("stalker:lighter/spark");
   }
}
