#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
STALCRAFT GLB Armor Exporter 0.4.3 automatic full-file fixer.

Place this script next to blender_stalcraft_glb_export.py and run it.
It creates a COMPLETE corrected file:
    blender_stalcraft_glb_export_0.4.3_FIXED.py

The original exporter is not overwritten. A backup is also created.
No Blender import is required to run this fixer.
"""

from __future__ import print_function

import ast
import os
import re
import shutil
import sys

SOURCE_NAME = "blender_stalcraft_glb_export.py"
OUTPUT_NAME = "blender_stalcraft_glb_export_0.4.3_FIXED.py"
BACKUP_SUFFIX = ".before_0.4.3_fix.bak"


class FixError(RuntimeError):
    pass


def find_source():
    candidates = []

    if len(sys.argv) > 1 and sys.argv[1].strip():
        candidates.append(os.path.abspath(sys.argv[1]))

    here = os.path.dirname(os.path.abspath(__file__))
    candidates.append(os.path.join(here, SOURCE_NAME))
    candidates.append(os.path.join(os.getcwd(), SOURCE_NAME))

    seen = set()
    for candidate in candidates:
        candidate = os.path.normpath(candidate)
        if candidate in seen:
            continue
        seen.add(candidate)
        if os.path.isfile(candidate):
            return candidate

    raise FixError(
        "Не найден %s. Положи fixer рядом с исходным exporter-файлом "
        "или перетащи исходный .py на fixer." % SOURCE_NAME
    )


def replace_once(text, old, new, description):
    count = text.count(old)
    if count != 1:
        raise FixError(
            "%s: ожидалось одно совпадение, найдено %d" % (description, count)
        )
    return text.replace(old, new, 1)


def insert_before_once(text, marker, insertion, description):
    count = text.count(marker)
    if count != 1:
        raise FixError(
            "%s: ожидалось одно место вставки, найдено %d" %
            (description, count)
        )
    return text.replace(marker, insertion + marker, 1)


def patch_source(source):
    text = source

    # Version shown by Blender's add-on manager.
    text = replace_once(
        text,
        '"version": (0, 4, 2),',
        '"version": (0, 4, 3),',
        "Не удалось обновить версию аддона",
    )

    # Safely quote names containing spaces, commas, quotes or Cyrillic text.
    quote_helper = r'''

def forge_config_string(value):
    """Return a Forge 1.6.4-safe quoted string value."""
    value = str(value if value is not None else "")
    value = value.replace("\\", "\\\\").replace('"', '\\"')
    value = value.replace("\r", " ").replace("\n", " ")
    return '"' + value + '"'


'''
    text = insert_before_once(
        text,
        "def config_block(index, settings):\n",
        quote_helper,
        "Не удалось добавить безопасное экранирование displayName",
    )

    text = replace_once(
        text,
        "    S:displayName={display_name}\n",
        "    S:displayName={display_name_cfg}\n",
        "Не удалось исправить displayName в config_block",
    )

    text = replace_once(
        text,
        '        display_name=settings["display_name"],\n',
        '        display_name_cfg=forge_config_string(settings["display_name"]),\n',
        "Не удалось подключить quoted displayName",
    )

    # Robust runtime path resolver and post-write verification.
    helpers = r'''

def resolve_runtime_config_path(raw_path):
    """
    Accept any of these selections:
      1) Minecraft instance root (.../classic1.6.4)
      2) its config directory (.../classic1.6.4/config)
      3) the exact stalcraftglb.cfg file

    The old 0.4.2 code always appended config/stalcraftglb.cfg and therefore
    wrote to config/config/stalcraftglb.cfg when the config folder was selected.
    """
    if not (raw_path or "").strip():
        raise ArmorExportError("Minecraft/config path is empty")

    selected = os.path.normpath(
        os.path.abspath(bpy.path.abspath(raw_path.strip()))
    )
    lower = selected.lower()

    if lower.endswith(".cfg"):
        if os.path.basename(lower) != "stalcraftglb.cfg":
            raise ArmorExportError(
                "Select stalcraftglb.cfg, the config folder, or the Minecraft instance root"
            )
        parent = os.path.dirname(selected)
        if parent:
            os.makedirs(parent, exist_ok=True)
        return selected

    if not os.path.isdir(selected):
        raise ArmorExportError(
            "Selected Minecraft/config folder does not exist: " + selected
        )

    if os.path.basename(selected).lower() == "config":
        return os.path.join(selected, "stalcraftglb.cfg")

    direct = os.path.join(selected, "stalcraftglb.cfg")
    if os.path.isfile(direct):
        return direct

    config_dir = os.path.join(selected, "config")
    if os.path.isdir(config_dir):
        return os.path.join(config_dir, "stalcraftglb.cfg")

    raise ArmorExportError(
        "The selected folder is neither a Minecraft instance root nor its config folder: "
        + selected
    )


def verify_written_config(filepath, settings, expected_index):
    """Fail instead of reporting success when the requested item was not saved."""
    if not os.path.isfile(filepath):
        raise ArmorExportError("Config was not created: " + filepath)

    with open(filepath, "r", encoding="utf-8-sig") as handle:
        saved = handle.read()

    blocks = find_category_blocks(saved)
    matching = []
    for index, start, end, block in blocks:
        name_match = re.search(r"(?m)^\\s*S:name=(.*?)\\s*$", block)
        item_match = re.search(r"(?m)^\\s*I:itemId=(-?\\d+)\\s*$", block)
        name = name_match.group(1).strip().strip('"') if name_match else None
        item_id = int(item_match.group(1)) if item_match else None
        if name == settings["name"]:
            matching.append((index, item_id))

    if len(matching) != 1:
        raise ArmorExportError(
            "Config verification failed for '%s': found %d matching sections in %s"
            % (settings["name"], len(matching), filepath)
        )

    actual_index, actual_id = matching[0]
    if actual_index != expected_index or actual_id != settings["item_id"]:
        raise ArmorExportError(
            "Config verification failed: expected armor_%d / ID %d, got armor_%d / ID %s"
            % (
                expected_index,
                settings["item_id"],
                actual_index,
                str(actual_id),
            )
        )

    count_match = re.search(r"(?m)^\\s*I:armorCount=(\\d+)\\s*$", saved)
    armor_count = int(count_match.group(1)) if count_match else -1
    if armor_count <= expected_index:
        raise ArmorExportError(
            "Config verification failed: armorCount=%d does not include armor_%d"
            % (armor_count, expected_index)
        )


'''
    text = insert_before_once(
        text,
        "def export_and_install(props):\n",
        helpers,
        "Не удалось добавить исправленный выбор runtime-конфига",
    )

    old_runtime = '''        minecraft_root = os.path.abspath(bpy.path.abspath(props.minecraft_root))
        runtime_cfg = os.path.join(minecraft_root, "config", "stalcraftglb.cfg")
        upsert_config(runtime_cfg, settings)'''

    new_runtime = '''        runtime_cfg = resolve_runtime_config_path(props.minecraft_root)
        upsert_config(runtime_cfg, settings)
        verify_written_config(runtime_cfg, settings, config_index)
        print(ADDON_PREFIX, "Verified runtime config:", runtime_cfg)'''

    text = replace_once(
        text,
        old_runtime,
        new_runtime,
        "Не удалось заменить ошибочный путь config/config",
    )

    # Verify generated project config too.
    text = replace_once(
        text,
        "    config_index = upsert_config(generated_cfg, settings)\n",
        "    config_index = upsert_config(generated_cfg, settings)\n"
        "    verify_written_config(generated_cfg, settings, config_index)\n",
        "Не удалось добавить проверку generated_config",
    )

    # Make a backup of an existing config before atomic replacement.
    text = replace_once(
        text,
        '''    temp_path = filepath + ".tmp"
    with open(temp_path, "w", encoding="utf-8", newline="\\n") as handle:
        handle.write(text.rstrip() + "\\n")
    os.replace(temp_path, filepath)
    return target_index''',
        '''    if os.path.isfile(filepath):
        try:
            shutil.copy2(filepath, filepath + ".bak")
        except Exception as backup_error:
            print(ADDON_PREFIX, "Warning: config backup failed:", backup_error)

    temp_path = filepath + ".tmp"
    with open(temp_path, "w", encoding="utf-8", newline="\\n") as handle:
        handle.write(text.rstrip() + "\\n")
    os.replace(temp_path, filepath)
    return target_index''',
        "Не удалось добавить резервную копию конфига",
    )

    # Clearer UI: the field accepts root/config/cfg, not just a Minecraft root.
    text = replace_once(
        text,
        '        description="Also update .minecraft/config/stalcraftglb.cfg",',
        '        description="Update stalcraftglb.cfg. Select the Minecraft root, its config folder, or the cfg file itself.",',
        "Не удалось обновить описание Write Minecraft Config",
    )
    text = replace_once(
        text,
        '        name="Minecraft Folder",',
        '        name="Minecraft / config Path",',
        "Не удалось обновить название поля пути",
    )
    text = replace_once(
        text,
        '        description="Folder containing config and mods",',
        '        description="Minecraft instance root, the config folder itself, or stalcraftglb.cfg",',
        "Не удалось обновить подсказку поля пути",
    )

    text = text.replace("v0.4.2", "v0.4.3")
    text = text.replace("(v0.4.2)", "(v0.4.3)")

    # Syntax verification without importing bpy.
    ast.parse(text, filename=OUTPUT_NAME)
    return text


def main():
    source_path = find_source()
    output_path = os.path.join(os.path.dirname(source_path), OUTPUT_NAME)
    backup_path = source_path + BACKUP_SUFFIX

    with open(source_path, "r", encoding="utf-8-sig") as handle:
        original = handle.read()

    if not os.path.isfile(backup_path):
        shutil.copy2(source_path, backup_path)

    fixed = patch_source(original)
    with open(output_path, "w", encoding="utf-8", newline="\n") as handle:
        handle.write(fixed.rstrip() + "\n")

    # Read it back and verify the critical fix really exists.
    with open(output_path, "r", encoding="utf-8") as handle:
        check = handle.read()

    required = (
        '"version": (0, 4, 3),',
        "def resolve_runtime_config_path(raw_path):",
        "runtime_cfg = resolve_runtime_config_path(props.minecraft_root)",
        "verify_written_config(runtime_cfg, settings, config_index)",
        'name="Minecraft / config Path"',
    )
    missing = [marker for marker in required if marker not in check]
    if missing:
        raise FixError("Финальная проверка не пройдена: " + ", ".join(missing))

    print()
    print("ГОТОВО")
    print("Исходник:   " + source_path)
    print("Резервная:  " + backup_path)
    print("Исправлен:  " + output_path)
    print()
    print("Установи в Blender именно:")
    print("  " + OUTPUT_NAME)
    print()
    print("В поле Minecraft / config Path теперь можно выбирать:")
    print("  ...\\classic1.6.4")
    print("  ...\\classic1.6.4\\config")
    print("  ...\\classic1.6.4\\config\\stalcraftglb.cfg")


if __name__ == "__main__":
    try:
        main()
    except Exception as error:
        print()
        print("ОШИБКА: " + str(error))
        print()
        try:
            input("Нажми Enter для выхода...")
        except Exception:
            pass
        raise
