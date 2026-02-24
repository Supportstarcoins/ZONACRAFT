package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.tile.TileEntityKissel;

public class KisselParticleEmitter extends ParticleEmitter {
   private static ParticleIcon bubbleIcon;

   public KisselParticleEmitter(TileEntityKissel kissel) {
      super(kissel);
      super.setCenter(super.centerX + 0.5, (float)super.centerY + 0.1F, super.centerZ + 0.5);
      super.setSize(-1.0, -1.0, -1.0, 1.0, 2.0, 1.0);
   }

   @Override
   public void tick() {
      super.tick();
      super.particles.add(new KisselBubbleParticle(this, 0.05F + super.world.s.nextFloat() * 0.05F, bubbleIcon));
      super.renderDistanceSq = GuiSettingsStalker.particleRenderDistance * GuiSettingsStalker.particleRenderDistance;
   }

   public void spawnActiveParticles() {
      KisselBubbleParticle particle = null;

      for (int i = 0; i < 25; i++) {
         particle = new KisselBubbleParticle(this, 0.05F + this.world.s.nextFloat() * 0.05F, bubbleIcon);
         super.particles.add(particle);
         particle.motionY = particle.motionY + (0.02F + this.world.s.nextFloat() * 0.01F);
      }
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.mc.f.g.contains(super.emmiter);
   }

   @Override
   public void reset() {
      super.reset();
   }

   public static void registerIcons(mt ir) {
      bubbleIcon = (ParticleIcon)ir.a("stalker:bubble");
   }

   public void onActivate() {
   }
}
