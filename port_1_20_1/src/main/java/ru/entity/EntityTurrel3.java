package ru.stalcraft.entity;

import ru.stalcraft.Config;
import ru.stalcraft.StalkerMain;

public class EntityTurrel3 extends EntityTurrel {
   public EntityTurrel3(abw world) {
      super(
         world,
         Config.turrel3Cooldown,
         Config.turrel3Damage,
         5.0F,
         -90.0F,
         5.0F,
         StalkerMain.turrel2.cv,
         "stalker:heavyturrel_hit",
         "stalker:heavyturrel_shoot"
      );
      this.a(2.8F, 1.2F);
      super.health = Config.turrel3Health;
   }

   public EntityTurrel3(abw world, String clanName, asx agroZone) {
      super(
         world,
         Config.turrel3Cooldown,
         Config.turrel3Damage,
         5.0F,
         -90.0F,
         5.0F,
         StalkerMain.turrel2.cv,
         "stalker:heavyturrel_hit",
         "stalker:heavyturrel_shoot",
         clanName,
         agroZone
      );
      this.a(2.8F, 1.2F);
      super.health = Config.turrel3Health;
   }

   @Override
   public String getSleeveModelName() {
      return "turrel3";
   }

   @Override
   public float getLightDistance() {
      return 5.5F;
   }

   @Override
   public float getSleeveDistance() {
      return 0.5F;
   }

   @Override
   public float f() {
      return 1.12F;
   }

   @Override
   public float getRotationPointZ() {
      return -1.12F;
   }
}
