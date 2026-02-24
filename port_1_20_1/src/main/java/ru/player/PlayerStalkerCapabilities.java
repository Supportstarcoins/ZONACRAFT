package ru.stalcraft.player;

public class PlayerStalkerCapabilities extends uc {
   private uf player;
   private PlayerInfo info;

   public PlayerStalkerCapabilities(uf player, PlayerInfo info) {
      this.player = player;
      this.info = info;
   }

   public float b() {
      return super.b();
   }

   public PlayerInfo getInfo() {
      return this.info;
   }
}
