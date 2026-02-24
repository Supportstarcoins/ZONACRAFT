package ru.stalcraft.client.particles;

public class ParticleTracer {
   public float alpha = 0.45F;
   public float motionX = 0.0F;
   public boolean isDead;

   public void tick() {
      this.alpha -= 0.2F;
      this.motionX += 2.9F;
      if (this.alpha <= 0.0F) {
         this.setDead();
      }
   }

   public void setDead() {
      this.isDead = true;
   }
}
