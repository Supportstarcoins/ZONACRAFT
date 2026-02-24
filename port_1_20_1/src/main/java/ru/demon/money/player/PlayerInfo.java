package ru.demon.money.player;

public abstract class PlayerInfo {
   public uf player;
   public int money;

   public PlayerInfo(uf player) {
      this.player = player;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public abstract void tick();

   public abstract void addMoneyValue(int var1);

   public abstract by getPersistedTag();
}
