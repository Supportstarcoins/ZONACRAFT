package ru.stalcraft.entity;

import ru.stalcraft.Config;
import ru.stalcraft.StalkerMain;

public class EntityTurrel1 extends EntityTurrel {
   public EntityTurrel1(abw world) {
      super(
         world,
         Config.turrel1Cooldown,
         Config.turrel1Damage,
         20.0F,
         -90.0F,
         45.0F,
         StalkerMain.turrel1.cv,
         "stalker:lightturrel_hit",
         "stalker:lightturrel_shoot"
      );
      this.a(0.7F, 0.8F);
      super.health = Config.turrel1Health;
   }

   public EntityTurrel1(abw world, String clanName, asx agroZone) {
      super(
         world,
         Config.turrel1Cooldown,
         Config.turrel1Damage,
         20.0F,
         -90.0F,
         45.0F,
         StalkerMain.turrel1.cv,
         "stalker:lightturrel_hit",
         "stalker:lightturrel_shoot",
         clanName,
         agroZone
      );
      this.a(0.7F, 0.8F);
      super.health = Config.turrel1Health;
   }

   @Override
   public void l_() {
      super.prevGunRoll = super.gunRoll;
      if (super.lastShot < Config.turrel2Cooldown) {
         super.gunRoll = super.gunRoll + 360 / Config.turrel1Cooldown / 8;
         if (super.gunRoll > 360.0F) {
            super.gunRoll -= 360.0F;
         }
      }

      super.l_();
   }

   @Override
   public String getSleeveModelName() {
      return "turrel1";
   }

   @Override
   public float getLightDistance() {
      return 1.5F;
   }

   @Override
   public float getSleeveDistance() {
      return 0.1F;
   }

   @Override
   public float f() {
      return 0.425F;
   }

   @Override
   public float getRotationPointZ() {
      return 0.115F;
   }
}
