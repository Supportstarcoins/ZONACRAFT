# Zonecraft Universal Armor Boot Ground Fix 1.1.0

Отдельный клиентский Forge-мост для Minecraft 1.6.4. Он не изменяет `stalkerarmor.jar`, `StalkerMod` или `CustomNPCs`.

## Что исправляет

- Автоматически определяет любой предмет `ru.stalcraft.glbarmor.item.ItemStalcraftSuit`.
- Приподнимает целиком GLB-рендер так, чтобы ботинки тяжёлой брони не проваливались в землю.
- Работает для всех зарегистрированных костюмов: старых и новых.
- Работает на игроках, CustomNPCs и других живых сущностях, которым надет GLB-костюм.
- Коррекция действует также на оружие, восстановленное NPC compatibility bridge, поэтому оружие и броня не расходятся по высоте.
- При приседании автоматически добавляет дополнительную компенсацию.

## Изменение 1.1.0

Стандартный подъём увеличен с `0.09375` до `0.1875` блока — с полутора до трёх пикселей модели Minecraft. Старый неизменённый конфиг версии 1.0.0 обновляется автоматически.

## Установка

1. Удалить `Zonecraft-Universal-Armor-Boot-Ground-Fix-1.6.4-1.0.0.jar`.
2. Положить `Zonecraft-Universal-Armor-Boot-Ground-Fix-1.6.4-1.1.0.jar` в папку `mods`.
3. Не удалять `stalkerarmor.jar`.
4. Полностью перезапустить Minecraft.

## Конфиг

Используется файл:

`config/zonecraft_armor_boot_ground_fix.properties`

Стандартные значения:

```properties
configVersion=2
enabled=true
baseLiftBlocks=0.1875
sneakingExtraBlocks=0.0625
applyWhileAirborne=true
```

`0.0625` блока соответствует одному пикселю модели Minecraft. Новый стандартный подъём равен трём пикселям.

Если после установки ботинки всё ещё слегка погружены, поставь `baseLiftBlocks=0.25`. Если они висят над землёй, уменьши до `0.15625`.
