package ru.stalcraft.entity;

import ru.stalcraft.Config;
import ru.stalcraft.StalkerMain;

public class EntityTurrel2 extends EntityTurrel {
   public EntityTurrel2(abw world) {
      super(
         world,
         Config.turrel2Cooldown,
         Config.turrel2Damage,
         5.0F,
         -75.0F,
         5.0F,
         StalkerMain.turrel2.cv,
         "stalker:middleturrel_hit",
         "stalker:middleturrel_shoot"
      );
      this.a(2.8F, 0.8F);
      super.health = Config.turrel2Health;
   }

   public EntityTurrel2(abw world, String clanName, asx agroZone) {
      super(
         world,
         Config.turrel2Cooldown,
         Config.turrel2Damage,
         5.0F,
         -75.0F,
         5.0F,
         StalkerMain.turrel2.cv,
         "stalker:middleturrel_hit",
         "stalker:middleturrel_shoot",
         clanName,
         agroZone
      );
      this.a(2.8F, 0.8F);
      super.health = Config.turrel2Health;
   }

   @Override
   public String getSleeveModelName() {
      return "turrel2";
   }

   @Override
   public float getLightDistance() {
      return 2.7F;
   }

   @Override
   public float getSleeveDistance() {
      return 1.2F;
   }

   @Override
   public float f() {
      return 0.36F;
   }

   @Override
   public float getRotationPointZ() {
      return 1.0F;
   }
}
