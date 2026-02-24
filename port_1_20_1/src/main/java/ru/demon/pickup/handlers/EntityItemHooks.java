package ru.demon.pickup.handlers;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class EntityItemHooks {
   @ForgeSubscribe
   public void resetPickupEvent(EntityItemPickupEvent ev) {
      ev.setCanceled(true);
   }

   @ForgeSubscribe
   public void itemHooks(EntityJoinWorldEvent ev) {
      if (ev.entity instanceof ss) {
         ((ss)ev.entity).lifespan = 10000;
      }
   }
}
