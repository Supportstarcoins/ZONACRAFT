package ru.stalcraft.client.particles;

import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.tile.TileEntityFunnel;

public class FunnelEyeParticle extends Particle {
   private FunnelParticleEmitter parent;
   private boolean isEyeVisible = false;
   private int rotationFactor = 1;
   private int eyeStateChangeTimer = 0;
   private int eyeAngle = 0;
   private static final float DEG_TO_RAD = (float) (Math.PI / 180.0);
   public boolean rotationVector;

   public FunnelEyeParticle(FunnelParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.0F, 1.4F, icon);
      this.parent = parent;
      TileEntityFunnel funnel = (TileEntityFunnel)parent.emmiter;
      this.clip = false;
      this.setPosition(parent.centerX, parent.centerY - 2.0, parent.centerZ);
      this.alpha = 1.0F;
      super.textureSize = 3.35F;
      this.rotationVector = parent.world.s.nextBoolean();
      super.blendFunc = 0;
      super.rotation = (float)(Math.random() * 180.0);
   }

   @Override
   public void tick() {
      super.tick();
      super.motionY -= 0.018F;
      super.alpha -= 0.01F;
      if (this.textureSize > 0.4F) {
         super.textureSize -= 0.1815F;
      } else {
         super.alpha -= 0.045F;
      }

      if (this.alpha <= 0.0F) {
         super.isDead = true;
      }
   }
}
