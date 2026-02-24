package ru.stalcraft.client;

public class ClientWeaponData {
   public int flashUpdate;

   public void tick() {
      if (this.flashUpdate > 0) {
         this.flashUpdate--;
      }
   }

   public void setFlash() {
      this.flashUpdate = 2;
   }
}
