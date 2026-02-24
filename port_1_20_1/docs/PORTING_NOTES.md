# PORTING_NOTES (1.6.4 -> 1.20.1)

## Итерация 1 — минимальное ядро

### Что уже перенесено
- Поднят рабочий Forge 1.20.1/FG6 Gradle skeleton в `port_1_20_1`.
- Добавлен минимальный мод `stalker` с `DeferredRegister`.
- Добавлены базовые регистры:
  - `stalker:anomaly_debug_block`
  - `stalker:anomaly_core`
- Добавлены базовые data-driven ресурсы (blockstate/model/lang/loot/recipe).
- Существующие legacy ассеты в `assets/stalker/**` сохранены без удаления.

### Временные заглушки (TODO)
- `anomaly_debug_block` использует vanilla текстуру (`minecraft:block/obsidian`) как debug-совместимость.
- `anomaly_core` использует vanilla иконку (`minecraft:item/ender_eye`) как debug-совместимость.
- Legacy Java-код 1.6.4-подобной архитектуры не участвует в сборке: сборка ограничена пакетом `ru/zonacraft/port/**`.

### Отличия от 1.6.4
- Регистрация перенесена на `DeferredRegister/RegistryObject`.
- Ресурсы переведены на современную структуру JSON (`blockstates`, `models`, `loot_tables`, `recipes`).
- Отказ от прямых legacy API-паттернов (`GameRegistry.registerBlock`, старые .lang как основной формат и т.д.).

### Нужен ручной тест в игре
- `/give @p stalker:anomaly_core`
- `/give @p stalker:anomaly_debug_block`
- Проверка отображения предметов/блока и дропа блока.

## Инвентаризация систем (карта переноса)

Подсчёты выполнены по текущему дереву `src/main/java/ru` и `src/main/resources/assets/stalker`.

- Блоки: `src/main/java/ru/blocks` (~31 Java файлов).
- Предметы: `src/main/java/ru/items` (~49 Java файлов).
- Сущности: `src/main/java/ru/entity` (~33 Java файлов).
- Block Entities (legacy TileEntity): `src/main/java/ru/tile` (~22 Java файлов).
- GUI/экраны: `src/main/java/ru/client/gui` (~51 Java файлов).
- Сеть/пакеты: `src/main/java/ru/network` (~8 Java файлов).
- Инвентари/контейнеры: `src/main/java/ru/inventory` (~37 Java файлов).
- Текстуры: `assets/stalker/textures` (~969 файлов, включая `.png` и legacy `.mic`).
- Модели/звуки/частицы в современном JSON-формате: в большинстве отсутствуют или legacy-структуры, требуется миграция.

## Mapping старое -> новое (начальный)
- Legacy main class `ru.StalkerMain` -> `ru.zonacraft.port.ZonaCraftPortMod`.
- Legacy registry manager (`BlockManager` и аналогичные) -> `ru.zonacraft.port.registry.ModBlocks/ModItems`.
- Legacy аномалии (`BlockElectra/Steam/Trampoline/Funnel/Carousel` + `TileEntity*`) -> будущие `Block + BlockEntity + ticker/entityInside` реализации в `ru.zonacraft.port.anomaly.*`.
- Legacy Kissel (`BlockKisselFluid`, `TileEntityKissel`, `MaterialKissel`) -> `FluidType + ForgeFlowingFluid (source/flowing) + LiquidBlock + BucketItem`.

## Предложенный план миграции (Phase)

1. **Phase 1: Boot ядро (текущая)**
   - Forge 1.20.1 skeleton, загрузка мода, базовые регистрации, минимальные JSON ресурсы.
2. **Phase 2: Registry Foundation**
   - Каркас регистров Blocks/Items/Sounds/Particles/BlockEntities/Menus/Entities.
   - Первичный namespace-аудит ассетов.
3. **Phase 3: Аномалии (MVP)**
   - Electra/Steam/Trampoline/Funnel/Carousel: серверная логика урона/эффектов + базовые звуки.
4. **Phase 4: Kissel Fluid**
   - Полная жидкость: `FluidType`, source/flowing, block, bucket, render layer, эффекты/урон.
5. **Phase 5: Клиент и визуал**
   - Рендеры, particles JSON, модели, экраны, слои рендера.
6. **Phase 6: Data-driven контент**
   - Рецепты, лут, теги, (при необходимости) datagen.
7. **Phase 7: Полировка и parity**
   - Ручные тесты в игре, выравнивание различий с 1.6.4, косметический проход ассетов.
