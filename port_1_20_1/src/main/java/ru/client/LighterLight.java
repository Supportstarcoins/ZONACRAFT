package ru.stalcraft.client;

import atomicstryker.dynamiclights.client.IDynamicLightSource;

public class LighterLight implements IDynamicLightSource {
   private nn entity;

   public LighterLight(nn entity) {
      this.entity = entity;
   }

   public nn getAttachmentEntity() {
      return this.entity;
   }

   public int getLightLevel() {
      return 15;
   }
}
