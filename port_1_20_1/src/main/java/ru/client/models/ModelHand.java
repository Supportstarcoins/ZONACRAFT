package ru.stalcraft.client.models;

public class ModelHand extends bbo {
   public bcu RightArm = new bcu(this, 40, 16);
   public bcu LeftArm;

   public ModelHand() {
      this.RightArm.a(0.0F, 0.0F, 0.0F, 4, 12, 4);
      this.LeftArm = new bcu(this, 40, 16);
      this.LeftArm.i = true;
      this.LeftArm.a(0.0F, 0.0F, 0.0F, 4, 12, 4);
   }

   public void render(beu p, int f) {
      bim renderengine = atv.w().N;
      renderengine.a(p.r());
      super.a((nn)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      if (f == 2.0F) {
         this.RightArm.a(0.0625F);
      }

      if (f == 1.0F) {
         this.LeftArm.a(0.0625F);
      }
   }

   private void setRotation(bcu model, float x, float y, float z) {
      model.f = x;
      model.g = y;
      model.h = z;
   }

   public void a(float f, float f1, float f2, float f3, float f4, float f5, nn e) {
      super.a(f, f1, f2, f3, f4, f5, e);
   }
}
