package ru.stalcraft.client.particles;

public class ParticleFlashSmoke {
   public atv mc = atv.w();
   public boolean tick;
   public float alpha = 1.0F;
   public float size;
   public boolean isDead;
   public float rotation;
   public float prevRotation;

   public ParticleFlashSmoke(float size, boolean tick) {
      this.prevRotation = this.rotation = (float)Math.random() * 360.0F;
      this.size = size;
      this.tick = tick;
   }

   public void tick() {
      if (this.tick) {
         this.alpha -= 0.065F;
         this.size += 0.005F;
         if (this.alpha <= 0.0F) {
            this.isDead = true;
         }
      } else if (this.mc.n == null) {
         this.isDead = true;
      }
   }
}
