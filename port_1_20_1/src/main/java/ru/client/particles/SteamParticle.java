package ru.stalcraft.client.particles;

import java.util.Random;
import org.lwjgl.util.vector.Vector2f;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class SteamParticle extends Particle {
   private static final float Y_MOTION = 0.2F;
   private static final float SIZE_FACTOR = 1.07F;
   private int removingStart;
   private float renderStartFrame;
   private int lifetime;
   private float baseAlpha;

   public SteamParticle(SteamParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.3F, 0.5F, icon);
      this.parent = parent;
      Random rand = parent.world.s;
      Vector2f vec = new Vector2f(rand.nextFloat() - 0.5F, rand.nextFloat() - 0.5F);
      vec.normalise();
      float distance = rand.nextFloat() * 0.2F;
      this.setPosition(parent.centerX + vec.x * distance, parent.centerY - 0.25 + rand.nextFloat() * 0.2F, parent.centerZ + vec.y * distance);
      this.motionY = 0.2F * (rand.nextFloat() / 2.0F + 0.5F);
      this.move(0.0, this.motionY, 0.0);
      this.removingStart = 10 + rand.nextInt(3);
      this.renderStartFrame = rand.nextFloat();
      this.lifetime = (int)(39.0F * (rand.nextFloat() / 2.0F + 0.5F));
      this.prevRotation = this.rotation = rand.nextInt(360);
      this.baseAlpha = 0.3F + rand.nextFloat() * 0.2F;
      super.textureSize = 0.2F;
   }

   @Override
   public void tick() {
      super.tick();
      this.textureSize += 0.058F;
      this.alpha = (1.0F - (float)this.ticksExisted / this.lifetime) * this.baseAlpha;
      this.motionX = this.motionX + (EffectsEngine.instance.xWind + (this.parent.world.s.nextFloat() - 0.5F) * 0.01F);
      this.motionY = 0.2F;
      this.motionZ = this.motionZ + (EffectsEngine.instance.zWind + (this.parent.world.s.nextFloat() - 0.5F) * 0.01F);
      if (this.ticksExisted >= this.lifetime) {
         this.isDead = true;
      }
   }
}
