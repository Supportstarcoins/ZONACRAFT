package ru.stalcraft.client.particles;

import java.util.Random;
import org.lwjgl.util.vector.Vector2f;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class LighterParticle extends Particle {
   private static final float Y_MOTION = 0.2F;
   private static final float SIZE_FACTOR = 1.08F;
   private int removingStart;
   private float alphaFactor;
   private float renderStartFrame;
   private int lifetime;

   public LighterParticle(LighterParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.3F, 0.2F, icon);
      this.parent = parent;
      Random rand = parent.world.s;
      Vector2f vec = new Vector2f(rand.nextFloat() - 0.5F, rand.nextFloat() - 0.5F);
      vec.normalise();
      float distance = rand.nextFloat() * 0.1F;
      this.setPosition(parent.centerX + vec.x * distance, parent.centerY - 0.25 + rand.nextFloat() * 0.2F, parent.centerZ + vec.y * distance);
      this.motionY = 0.2F * (rand.nextFloat() / 2.0F + 0.5F);
      this.move(0.0, this.motionY, 0.0);
      this.alphaFactor = 0.8F - rand.nextFloat() / 10.0F;
      this.removingStart = 10 + rand.nextInt(3);
      this.renderStartFrame = rand.nextFloat();
      this.lifetime = (int)(19.0F * (rand.nextFloat() / 2.0F + 0.5F));
      this.prevBurn = this.burn = 1.0F;
      super.textureSize = 0.25F;
   }

   @Override
   public void tick() {
      super.tick();
      this.textureSize *= 1.12F;
      this.motionY = 0.2F;
      if (this.lifetime - this.ticksExisted < 5) {
         this.alpha = Math.max(0.0F, (this.lifetime - this.ticksExisted) * 0.25F);
      }

      if (this.ticksExisted >= this.lifetime) {
         this.isDead = true;
      }
   }
}
