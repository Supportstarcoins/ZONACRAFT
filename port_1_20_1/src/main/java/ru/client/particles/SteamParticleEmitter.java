package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.tile.TileEntitySteam;

public class SteamParticleEmitter extends ParticleEmitter {
   private static final int MAX_PARTICLES_ACTIVE = 500;
   public static final int PARTICLE_LIFE = 40;
   private static ParticleIcon[] icons;

   public SteamParticleEmitter(TileEntitySteam steam) {
      super(steam);
      super.setCenter(super.centerX + 0.5, super.centerY, super.centerZ + 0.5);
      super.setSize(-2.0, -3.0, -2.0, 2.0, 7.0, 2.0);
   }

   @Override
   public void tick() {
      super.tick();

      for (int i = 0; i < 12; i++) {
         super.particles.add(new SteamParticle(this, icons[super.world.s.nextInt(icons.length)]));
      }

      super.renderDistanceSq = GuiSettingsStalker.particleRenderDistance * GuiSettingsStalker.particleRenderDistance;
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.mc.f.g.contains(super.emmiter);
   }

   public static void registerIcons(mt ir) {
      icons = new ParticleIcon[1];
      icons[0] = (ParticleIcon)ir.a("stalker:steam/steam1");
   }
}
