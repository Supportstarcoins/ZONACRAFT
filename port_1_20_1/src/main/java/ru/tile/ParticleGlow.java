package ru.stalcraft.tile;

public class ParticleGlow {
   public boolean isDead = false;
   public float prevAlpha = 1.0F;
   public float alpha = 1.0F;
   public double prevRotation;
   public double rotation;
   public double scale;
   public double prevScale;

   public ParticleGlow() {
      this.prevRotation = this.rotation = (float)Math.random() * 360.0F;
      this.prevScale = this.scale = 200.2F;
      this.alpha = 0.85F;
   }

   public void tick() {
      this.prevScale = this.scale;
      this.prevRotation = this.rotation;
      this.prevAlpha = this.alpha;
      this.scale += 0.05F;
      this.alpha -= 0.1F;
      if (this.alpha <= 0.0F) {
         this.setDead();
      }
   }

   private void setDead() {
      this.isDead = true;
   }
}
