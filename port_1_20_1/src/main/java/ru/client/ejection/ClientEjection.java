package ru.stalcraft.client.ejection;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.ejection.Ejection;

public class ClientEjection extends Ejection {
   private String sound_id = null;
   private final ClientEjectionManager clientEjectionManager;
   public atv mc = atv.w();

   public ClientEjection(int id, int par2) {
      super(id, par2);
      this.clientEjectionManager = (ClientEjectionManager)StalkerMain.getProxy().getEjectionManager();
   }

   @Override
   public void tick() {
      super.tick();
      this.mc.u.l = false;
      float soundVolume;
      if (this.age >= 4000 && this.age < 8000) {
         soundVolume = 1.0F;
      } else if (this.age >= 8000) {
         soundVolume = Math.max(0.0F, (12000 - this.age) / 4000.0F);
      } else {
         soundVolume = Math.max(0.0F, this.age / 4000.0F);
      }

      this.mc.v.b.setVolume(this.sound_id, soundVolume * 0.25F * this.mc.u.b);
   }

   public float getColorWeight() {
      return this.age < 4000 ? this.age / 4000.0F : (this.age > 8000 ? (12000 - this.age) / 4000.0F : 1.0F);
   }

   public float[] getSkyColor() {
      if (this.age >= 4000 && this.age < 8000) {
         return new float[]{0.65F, 0.0F, 0.0F, 1.0F};
      } else {
         int renderAge = this.age > 4000 ? 12000 - this.age : this.age;
         if (renderAge < 2000) {
            float progress = renderAge / 2000.0F;
            return new float[]{0.5F + 0.5F * progress, 0.5F - 0.15F * progress, 0.5F - 0.5F * progress, 1.0F};
         } else {
            float progress = (renderAge - 2000) / 2000.0F;
            return new float[]{1.0F - 0.35F * progress, 0.35F - 0.35F * progress, 0.0F, 1.0F};
         }
      }
   }

   @Override
   public void start() {
      if (this.clientEjectionManager.hasEjection()) {
         this.clientEjectionManager.getEjection().end();
      }

      this.clientEjectionManager.setEjection(this);
   }

   @Override
   public void end() {
      this.mc.u.l = true;
      this.clientEjectionManager.setLastEjectionId(this.id);
      this.clientEjectionManager.setEjection(null);
   }
}
