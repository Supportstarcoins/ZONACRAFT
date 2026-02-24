package ru.stalcraft.entity;

public interface IEntityShooter {
   void shoot();

   int getShootCooldown();

   boolean canShoot();
}
