# Zonecraft NPC Stalker Compatibility Bridge 1.2.0

Отдельный Forge-мост для Minecraft 1.6.4. Он ставится в `mods` и не заменяет классы внутри `stalker-01.jar`, `CustomNPCs` или `stalkerarmor.jar`.

## Что исправлено

- Предмет из слота **Projectile** больше не вылетает из оружия. Ящики с патронами, магазины и другие предметы не превращаются в снаряды.
- Projectile-слот остаётся только техническим переключателем штатного дальнего AI CustomNPC.
- Серверная стрельба управляется характеристиками настоящего `ru.stalcraft.items.ItemWeapon`:
  - `cooldown` — задержка между выстрелами;
  - `reload_time` — продолжительность перезарядки;
  - `cage_size` — ёмкость магазина;
  - `bullets_count` — число дробин/пуль за один выстрел;
  - `autoShooting` и текущий `FireMode` — одиночный, автоматический или очередной режим.
- Для режима очереди мост читает активный `FireMode`; при неизвестной структуре использует очередь по 3 выстрела.
- После опустошения магазина NPC действительно ждёт штатное время перезарядки оружия, затем продолжает стрелять.
- Автоматы стреляют непрерывно по своему cooldown, винтовки и дробовики сохраняют длинную задержку, оружие с burst-режимом стреляет очередями.
- Урон наносится на сервере projectile-источником от самого NPC. Для дробовиков учитывается `damage × bullets_count`.
- Сломанные NPC `EntityBullet`, которые могли попадать в ориентированный на игрока сетевой путь StalkerMod, подавляются.
- Воронка одинаково притягивает дружественных, нейтральных и враждебных CustomNPC.
- На клиенте сохраняется исправленный рендер оружия в руке GLB-брони.

## Установка

1. Удалить старые версии:
   - `Zonecraft-NPC-GLB-Compatibility-Bridge-1.6.4-1.0.0.jar`;
   - `Zonecraft-NPC-Stalker-Compatibility-Bridge-1.6.4-1.1.0.jar`.
2. Положить `Zonecraft-NPC-Stalker-Compatibility-Bridge-1.6.4-1.2.0.jar` в папку `mods`.
3. Оставить без изменений:
   - `stalker-01.jar`;
   - CustomNPCs 1.6.4;
   - `stalkerarmor.jar` 0.5.19;
   - Armor Stats Bridge и Armor Zone Patch.
4. В Projectile-слоте NPC должен оставаться любой предмет, чтобы CustomNPC включил дальний AI. Этот предмет больше не будет физически выстреливаться.
5. Полностью перезапустить Minecraft.

## Проверка

В логе должна появиться строка:

```text
[Zonecraft NPC GLB Compat] 1.2.0 initialized: real ItemWeapon cadence/reload + no ammo-box projectiles + all-faction vortex physics + held weapon render.
```

При первом обнаружении оружия мост выводит профиль:

```text
[Weapon] profile ... mode=auto cooldown=2 reload=75 magazine=30 ...
```

Во время работы будут строки `shot=... applied=true`, `reload=...` и `cancelled item projectile ...`.
