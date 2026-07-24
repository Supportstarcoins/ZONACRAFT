# Zonecraft NPC Stalker Compatibility Bridge 1.1.0

Отдельный обычный Forge-мод для Minecraft 1.6.4. Ставится в `mods` и не заменяет классы внутри `stalker-01.jar`, `CustomNPCs` или `stalkerarmor.jar`.

## Что исправляет

- Перехватывает только `ru.stalcraft.entity.EntityBullet`, созданные CustomNPC с оружием `ru.stalcraft.items.ItemWeapon`.
- Не пропускает сломанную NPC-пулю в ориентированный на игрока сетевой путь StalkerMod.
- На сервере наносит текущей цели NPC реальный projectile-урон с владельцем-стрелком.
- Берёт урон из полей пули/оружия StalkerMod; при неизвестной структуре использует безопасный запасной урон `10`.
- Игроков и их Stalker-пули не изменяет.
- Воронка одинаково притягивает дружественных, нейтральных и враждебных CustomNPC.
- Наличие GLB-костюма больше не требуется для физики Воронки.
- Фракция NPC не проверяется и не влияет на силу притяжения.
- Игроки и обычные мобы остаются под штатной механикой StalkerMod.
- На клиенте повторно отрисовывает оружие StalkerMod после скрытия исходного тела GLB-костюмом.

## Установка

1. Удалить старый `Zonecraft-NPC-GLB-Compatibility-Bridge-1.6.4-1.0.0.jar`, если он был установлен.
2. Положить `Zonecraft-NPC-Stalker-Compatibility-Bridge-1.6.4-1.1.0.jar` в папку `mods`.
3. Оставить без изменений:
   - `stalker-01.jar`;
   - CustomNPCs 1.6.4;
   - `stalkerarmor.jar` 0.5.19;
   - Zonecraft Stalker Armor Stats Bridge и Armor Zone Patch.
4. Полностью перезапустить Minecraft.

После запуска в `ForgeModLoader-client-0.log` должна появиться строка:

```text
[Zonecraft NPC GLB Compat] 1.1.0 initialized: Stalker NPC damage + all-faction vortex physics + held weapon render.
```

При выстреле мост пишет строки `[Weapon] ... applied=true`. При срабатывании Воронки появляются строки `[Vortex] ...`.

## Важно

- Мост работает только когда у NPC в руке находится настоящий `ru.stalcraft.items.ItemWeapon` и NPC имеет текущую цель атаки.
- Стандартный Projectile-слот CustomNPC всё ещё должен быть заполнен, потому что он запускает штатный AI дальнего боя в версии 0.5.19.
- Старые патчи, которые заменяли Java-файлы внутри StalkerMod, устанавливать нельзя.
