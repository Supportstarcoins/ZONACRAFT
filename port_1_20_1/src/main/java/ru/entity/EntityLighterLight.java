package ru.stalcraft.entity;

import atomicstryker.dynamiclights.client.DynamicLights;
import ru.stalcraft.Logger;
import ru.stalcraft.client.LighterLight;
import ru.stalcraft.tile.TileEntityLighter;

public class EntityLighterLight extends nn {
   public TileEntityLighter tile;

   public EntityLighterLight(TileEntityLighter tile) {
      super(tile.k);
      this.tile = tile;
      this.a(0.0F, 0.0F);
      super.Z = true;
      this.b(tile.l + 0.5, tile.m + 0.5, tile.n + 0.5);
      DynamicLights.addLightSource(new LighterLight(this));
   }

   public void l_() {
      super.l_();
      Logger.debug("tick");
      if (this.tile.r() || this.tile.activeTimer <= 0) {
         this.x();
      }
   }

   protected void a() {
   }

   protected void a(by nbttagcompound) {
   }

   protected void b(by nbttagcompound) {
   }
}
