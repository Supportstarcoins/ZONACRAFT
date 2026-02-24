package ru.stalcraft.client;

import java.util.HashMap;

public class ClientWeaponManager {
   public HashMap weaponsData = new HashMap();
   public atv mc;

   public ClientWeaponManager(atv mc) {
      this.mc = mc;
   }

   public void tick() {
      if (this.mc.f != null) {
         for (uf player : this.mc.f.h) {
            if (!this.weaponsData.containsKey(player)) {
               this.weaponsData.put(player, new ClientWeaponData());
            } else {
               ((ClientWeaponData)this.weaponsData.get(player)).tick();
            }
         }
      }
   }
}
