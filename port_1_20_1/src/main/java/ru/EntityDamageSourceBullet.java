package ru.stalcraft;

public class EntityDamageSourceBullet extends nc {
   public EntityDamageSourceBullet(String name, nn shooter) {
      super(name, shooter);
      this.b();
   }

   public cv b(of entity) {
      return new cv().a(entity.an() + " был застрелен " + (this.i() == null ? "неизвестным" : this.i().an()));
   }
}
