package ru.stalcraft.client;

import atomicstryker.dynamiclights.client.IDynamicLightSource;
import ru.stalcraft.entity.EntityFlashlight;

public class FlashlightLight implements IDynamicLightSource {
   private EntityFlashlight entity;

   public FlashlightLight(EntityFlashlight shot) {
      this.entity = shot;
   }

   public nn getAttachmentEntity() {
      return this.entity;
   }

   public int getLightLevel() {
      return 15;
   }
}
