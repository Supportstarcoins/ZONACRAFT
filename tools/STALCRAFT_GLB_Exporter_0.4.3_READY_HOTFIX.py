bl_info = {
    "name": "STALCRAFT GLB Armor Exporter 0.4.3 Ready Hotfix",
    "author": "OpenAI",
    "version": (0, 4, 3),
    "blender": (3, 6, 0),
    "location": "View3D > Sidebar > GLB Armor",
    "description": "Fixes runtime config registration without generators or blocking input",
    "category": "Import-Export",
}

import bpy
import importlib.util
import os
import re
import shutil
import sys

ADDON_PREFIX = "[STALCRAFT GLB 0.4.3 HOTFIX]"
ORIGINAL_FILE_NAME = "blender_stalcraft_glb_export.py"
ORIGINAL_MODULE_ALIAS = "_stalcraft_glb_export_original_043"

_original_module = None
_registered_original_here = False
_original_config_block = None
_original_upsert_config = None
_original_export_and_install = None


class HotfixError(RuntimeError):
    pass


def _find_loaded_original():
    for module in list(sys.modules.values()):
        try:
            info = getattr(module, "bl_info", None)
            if isinstance(info, dict) and info.get("name") == "STALCRAFT GLB Armor Exporter":
                if module is not sys.modules.get(__name__):
                    return module
        except Exception:
            pass
    return None


def _candidate_original_paths():
    result = []

    try:
        current_dir = os.path.dirname(os.path.abspath(__file__))
        result.append(os.path.join(current_dir, ORIGINAL_FILE_NAME))
    except Exception:
        pass

    try:
        addons_dir = bpy.utils.user_resource("SCRIPTS", path="addons")
        if addons_dir:
            result.append(os.path.join(addons_dir, ORIGINAL_FILE_NAME))
    except Exception:
        pass

    try:
        for scripts_dir in bpy.utils.script_paths():
            result.append(os.path.join(scripts_dir, "addons", ORIGINAL_FILE_NAME))
    except Exception:
        pass

    unique = []
    seen = set()
    for path in result:
        normalized = os.path.normcase(os.path.abspath(path))
        if normalized not in seen:
            seen.add(normalized)
            unique.append(path)
    return unique


def _load_original_module():
    loaded = _find_loaded_original()
    if loaded is not None:
        print(ADDON_PREFIX, "Using already loaded original addon:", loaded.__name__)
        return loaded

    for path in _candidate_original_paths():
        if not os.path.isfile(path):
            continue

        if os.path.normcase(os.path.abspath(path)) == os.path.normcase(os.path.abspath(__file__)):
            continue

        spec = importlib.util.spec_from_file_location(ORIGINAL_MODULE_ALIAS, path)
        if spec is None or spec.loader is None:
            continue

        module = importlib.util.module_from_spec(spec)
        sys.modules[ORIGINAL_MODULE_ALIAS] = module
        spec.loader.exec_module(module)
        print(ADDON_PREFIX, "Loaded original addon:", path)
        return module

    raise HotfixError(
        "Не найден исходный %s. Положите этот hotfix-файл рядом с оригинальным аддоном."
        % ORIGINAL_FILE_NAME
    )


def _cfg_string(value):
    value = str(value if value is not None else "")
    value = value.replace("\\", "\\\\")
    value = value.replace('"', '\\"')
    value = value.replace("\r", " ").replace("\n", " ")
    return '"' + value + '"'


def _resolve_runtime_config_path(raw_path):
    if not (raw_path or "").strip():
        raise HotfixError("Minecraft/config path is empty")

    selected = os.path.normpath(
        os.path.abspath(bpy.path.abspath(raw_path.strip()))
    )
    lower = selected.lower()

    if lower.endswith(".cfg"):
        if os.path.basename(lower) != "stalcraftglb.cfg":
            raise HotfixError(
                "Выберите stalcraftglb.cfg, папку config или корень Minecraft-сборки."
            )
        parent = os.path.dirname(selected)
        if not os.path.isdir(parent):
            raise HotfixError("Папка конфига не существует: " + parent)
        return selected

    if not os.path.isdir(selected):
        raise HotfixError("Выбранная папка не существует: " + selected)

    if os.path.basename(selected).lower() == "config":
        return os.path.join(selected, "stalcraftglb.cfg")

    direct = os.path.join(selected, "stalcraftglb.cfg")
    if os.path.isfile(direct):
        return direct

    config_dir = os.path.join(selected, "config")
    if os.path.isdir(config_dir):
        return os.path.join(config_dir, "stalcraftglb.cfg")

    raise HotfixError(
        "Путь не является корнем Minecraft-сборки, папкой config или stalcraftglb.cfg: "
        + selected
    )


def _runtime_root_from_config(config_path):
    config_dir = os.path.dirname(config_path)
    if os.path.basename(config_dir).lower() != "config":
        raise HotfixError("Файл stalcraftglb.cfg должен находиться в папке config")
    return os.path.dirname(config_dir)


def _find_armor_blocks(text):
    pattern = re.compile(r"(?ms)^armor_(\d+)\s*\{.*?^\}")
    return [
        (int(match.group(1)), match.group(0))
        for match in pattern.finditer(text)
    ]


def _block_name(block):
    match = re.search(r"(?m)^\s*S:name=(.*?)\s*$", block)
    if match is None:
        return None
    value = match.group(1).strip()
    if len(value) >= 2 and value[0] == '"' and value[-1] == '"':
        value = value[1:-1]
    return value


def _block_item_id(block):
    match = re.search(r"(?m)^\s*I:itemId=(-?\d+)\s*$", block)
    return int(match.group(1)) if match else None


def _verify_written_config(filepath, settings, expected_index):
    if not os.path.isfile(filepath):
        raise HotfixError("Конфиг не был создан: " + filepath)

    with open(filepath, "r", encoding="utf-8-sig") as handle:
        text = handle.read()

    matches = []
    for index, block in _find_armor_blocks(text):
        if _block_name(block) == settings["name"]:
            matches.append((index, block))

    if len(matches) != 1:
        raise HotfixError(
            "Проверка записи '%s' не пройдена: найдено секций %d в %s"
            % (settings["name"], len(matches), filepath)
        )

    index, block = matches[0]
    item_id = _block_item_id(block)
    if index != expected_index or item_id != settings["item_id"]:
        raise HotfixError(
            "Ожидались armor_%d и ID %d, записались armor_%d и ID %s"
            % (expected_index, settings["item_id"], index, str(item_id))
        )

    count_match = re.search(r"(?m)^\s*I:armorCount=(\d+)\s*$", text)
    armor_count = int(count_match.group(1)) if count_match else -1
    if armor_count <= expected_index:
        raise HotfixError(
            "armorCount=%d не включает armor_%d" % (armor_count, expected_index)
        )


def _patched_config_block(index, settings):
    block = _original_config_block(index, settings)
    safe_name = _cfg_string(settings.get("display_name", ""))
    pattern = re.compile(r"(?m)^(\s*S:displayName=).*$")
    if pattern.search(block):
        block = pattern.sub(lambda match: match.group(1) + safe_name, block, count=1)
    return block


def _patched_upsert_config(filepath, settings):
    parent = os.path.dirname(filepath)
    if parent:
        os.makedirs(parent, exist_ok=True)

    if os.path.isfile(filepath):
        with open(filepath, "r", encoding="utf-8-sig") as handle:
            old_text = handle.read()

        for index, block in _find_armor_blocks(old_text):
            existing_id = _block_item_id(block)
            existing_name = _block_name(block)
            if existing_id == settings["item_id"] and existing_name != settings["name"]:
                raise HotfixError(
                    "Item ID %d уже занят костюмом '%s' (armor_%d)"
                    % (settings["item_id"], existing_name or "?", index)
                )

        backup = filepath + ".bak"
        shutil.copy2(filepath, backup)
        print(ADDON_PREFIX, "Config backup:", backup)

    index = _original_upsert_config(filepath, settings)
    _verify_written_config(filepath, settings, index)
    print(ADDON_PREFIX, "Verified config:", filepath)
    print(ADDON_PREFIX, "Registered armor_%d / item ID %d" % (index, settings["item_id"]))
    return index


def _patched_export_and_install(props):
    original_selected_path = props.minecraft_root
    resolved_cfg = None

    try:
        if bool(props.write_runtime_config):
            resolved_cfg = _resolve_runtime_config_path(original_selected_path)
            props.minecraft_root = _runtime_root_from_config(resolved_cfg)
            print(ADDON_PREFIX, "Resolved runtime config:", resolved_cfg)

        result = _original_export_and_install(props)

        if resolved_cfg is not None:
            armor_name = result[0]
            settings = {
                "name": armor_name,
                "item_id": int(props.item_id),
            }

            with open(resolved_cfg, "r", encoding="utf-8-sig") as handle:
                saved = handle.read()

            matching = [
                (index, block)
                for index, block in _find_armor_blocks(saved)
                if _block_name(block) == armor_name
            ]
            if len(matching) != 1 or _block_item_id(matching[0][1]) != settings["item_id"]:
                raise HotfixError(
                    "После экспорта костюм не найден в настоящем runtime-конфиге: "
                    + resolved_cfg
                )

            print(ADDON_PREFIX, "Runtime registration verified:", resolved_cfg)

        return result
    finally:
        props.minecraft_root = original_selected_path


def _patch_original(module):
    global _original_config_block
    global _original_upsert_config
    global _original_export_and_install

    if getattr(module, "_stalcraft_043_hotfix_applied", False):
        return

    _original_config_block = module.config_block
    _original_upsert_config = module.upsert_config
    _original_export_and_install = module.export_and_install

    module.config_block = _patched_config_block
    module.upsert_config = _patched_upsert_config
    module.export_and_install = _patched_export_and_install
    module._stalcraft_043_hotfix_applied = True

    try:
        module.bl_info["version"] = (0, 4, 3)
    except Exception:
        pass

    print(ADDON_PREFIX, "Hotfix applied successfully.")


def register():
    global _original_module
    global _registered_original_here

    _original_module = _load_original_module()
    _patch_original(_original_module)

    if not hasattr(bpy.types.Scene, "stalcraft_glb_props"):
        _original_module.register()
        _registered_original_here = True
        print(ADDON_PREFIX, "Original exporter UI registered by hotfix.")
    else:
        _registered_original_here = False
        print(ADDON_PREFIX, "Original exporter UI was already registered.")


def unregister():
    global _registered_original_here

    if _registered_original_here and _original_module is not None:
        try:
            _original_module.unregister()
        except Exception as error:
            print(ADDON_PREFIX, "Unregister warning:", error)

    _registered_original_here = False


if __name__ == "__main__":
    register()
