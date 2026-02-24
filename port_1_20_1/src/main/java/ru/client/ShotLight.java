package ru.stalcraft.client;

import atomicstryker.dynamiclights.client.IDynamicLightSource;
import ru.stalcraft.entity.EntityShot;

public class ShotLight implements IDynamicLightSource {
   private EntityShot entity;

   public ShotLight(EntityShot shot) {
      this.entity = shot;
   }

   public nn getAttachmentEntity() {
      return this.entity;
   }

   public int getLightLevel() {
      return 12;
   }
}
