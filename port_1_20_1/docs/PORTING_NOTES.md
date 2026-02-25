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
