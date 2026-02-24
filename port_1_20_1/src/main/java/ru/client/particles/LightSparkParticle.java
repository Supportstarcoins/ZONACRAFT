package ru.stalcraft.client.particles;

import java.util.Random;
import org.lwjgl.util.vector.Vector2f;
import ru.stalcraft.client.effects.particles.Particle;
import ru.stalcraft.client.effects.particles.ParticleIcon;

public class LightSparkParticle extends Particle {
   private static final float Y_VELOCITY = -0.03F;
   private static final float SIZE_FACTOR = 1.08F;
   private int removingStart;
   private float alphaFactor;
   private float renderStartFrame;
   private int lifetime;

   public LightSparkParticle(LighterParticleEmitter parent, ParticleIcon icon) {
      super(parent, 0.1F, 0.2F, icon);
      this.parent = parent;
      Random rand = parent.world.s;
      this.setPosition(parent.centerX, parent.centerY, parent.centerZ);
      Vector2f vec = new Vector2f(rand.nextFloat() - 0.5F, rand.nextFloat() - 0.5F);
      vec.normalise();
      float speed = rand.nextFloat() * 0.05F;
      this.motionX = vec.x * speed;
      this.motionZ = vec.y * speed;
      this.motionY = rand.nextFloat() * 0.15F + 0.3F;
      this.alphaFactor = 0.8F - rand.nextFloat() / 10.0F;
      this.removingStart = 10 + rand.nextInt(3);
      this.renderStartFrame = rand.nextFloat();
      this.lifetime = rand.nextInt(5) + 5;
      this.prevBurn = this.burn = 1.0F;
   }

   @Override
   public void tick() {
      super.tick();
      this.motionY += -0.03F;
      if (this.lifetime - this.ticksExisted < 5) {
         this.alpha = Math.max(0.0F, (this.lifetime - this.ticksExisted) * 0.25F);
      }

      if (this.ticksExisted >= this.lifetime || this.onGround) {
         this.isDead = true;
      }
   }
}
