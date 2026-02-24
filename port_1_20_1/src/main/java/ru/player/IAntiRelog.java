package ru.stalcraft.player;

public interface IAntiRelog {
   boolean isPlayerRelogging(jv var1);

   void addReloggingPlayer(jv var1);

   void onDamage(jv var1);

   void onSaveAll();

   void tick();
}
