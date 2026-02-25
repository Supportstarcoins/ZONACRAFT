# PORTING_NOTES

## Phase 2 — registry foundation (аномалии: electra/steam)

### Что найдено
- В коде аномалий используются legacy-ключи `stalker:electra`, `stalker:electra_hit`, `stalker:steam`, `stalker:steam_hit`:
  - `BlockElectra` -> `stalker:electra`
  - `BlockSteam` -> `stalker:steam`
  - `ClientEvents` -> `stalker:electra.ogg`, `stalker:electra_hit.ogg`, `stalker:steam.ogg`, `stalker:steam_hit.ogg`
  - `StalkerSounds` (константы звуков)
- Ассеты звуков физически лежат в `assets/stalker/sound/*.ogg` (папка `sound`, единственное число).
- В `assets/stalker/sounds.json` присутствуют минимальные записи для `electra`, `electra_hit`, `steam`, `steam_hit`.

### Какие пути несовместимы с 1.20.1
- Для 1.20.1 файлы должны резолвиться из `assets/<namespace>/sounds/**`, а не из `assets/<namespace>/sound/**`.
- Legacy-вызовы в стиле прямой строки `stalker:*.ogg` (например, в `ClientEvents`) не соответствуют modern-подходу с `SoundEvent` registry и `sounds.json`.
- Текущие записи `sounds.json` пока описывают legacy-идентификаторы, но фактические `.ogg` находятся в legacy-структуре каталогов, что не даст полноценной загрузки в strict 1.20.1 без следующего шага миграции путей.

### Что осталось на следующий шаг
- Перенести/дублировать нужные `.ogg` в `assets/stalker/sounds/...` (без массового рефакторинга ассетов; точечно для аномалий).
- Подключить `DeferredRegister<SoundEvent>` + `RegistryObject<SoundEvent>` в точку инициализации Forge 1.20.1.
- Заменить прямые строковые воспроизведения звуков на обращения к зарегистрированным `SoundEvent`.
- После переноса путей проверить загрузку через dev-клиент и лог `Missing sound event` / `Unable to play empty soundEvent`.

## Phase 3 — live anomaly blocks (Forge 1.20.1 MVP+)

### Что сделано
- Сохранены `modid = stalker_port`, entrypoint `StalkerPortMod`, `ModBlocks`, `ModItems` и существующие IDs блоков аномалий.
- Блоки `carousel`, `trampoline`, `black_hole`, `lighter`, `electra` переведены с обычного `Block` на `BaseAnomalyBlock` + `BlockEntity`-тикер.
- Добавлен `ModBlockEntities` с `BlockEntityType` для всех зарегистрированных anomaly-блоков.
- Реализована общая логика `BaseAnomalyBlockEntity`:
  - server/client тик;
  - state-поля (`cooldownTicks`, `activeTicks`, `animationTick`, `fxSeed`);
  - синхронизация (`save/load`, `getUpdateTag`, `getUpdatePacket` + `sendBlockUpdated`).
- Для каждого типа добавлено рабочее поведение:
  - **Electra**: магический урон, замедление, отталкивание/подброс;
  - **Black Hole**: постоянное притяжение в радиусе + урон/дебафф при триггере;
  - **Trampoline**: подброс вверх, антиспам-кулдаун на сущности через `PersistentData`;
  - **Lighter**: поджог + огненный урон + дебафф голода;
  - **Carousel**: тангенциальный импульс (закрутка), подброс и дезориентация.
- Добавлен side-safe слой FX:
  - `AnomalyFxDispatcher` в common (без импортов `net.minecraft.client.*`);
  - `AnomalyClientFxHooks` в client-пакете для частиц/локальных звуков.

### Быстрый тест в мире
```mcfunction
/give @p stalker_port:electra
/give @p stalker_port:black_hole
/give @p stalker_port:trampoline
/give @p stalker_port:lighter
/give @p stalker_port:carousel
```

```mcfunction
/setblock ~ ~ ~ stalker_port:electra
/setblock ~1 ~ ~ stalker_port:black_hole
/setblock ~2 ~ ~ stalker_port:trampoline
/setblock ~3 ~ ~ stalker_port:lighter
/setblock ~4 ~ ~ stalker_port:carousel
```

### Ожидаемое поведение
- **electra**: при входе в зону — вспышка/искры, урон, короткий контроль импульсом.
- **black_hole**: тянет мобов/игрока к центру, при близком контакте дополнительно бьёт.
- **trampoline**: подбрасывает вверх и не спамит каждый тик за счёт entity cooldown.
- **lighter**: зажигает, наносит периодический огненный урон при контакте.
- **carousel**: закручивает вокруг центра и накладывает лёгкую дезориентацию.
