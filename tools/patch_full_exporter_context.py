from pathlib import Path

path = Path('tools/blender_stalcraft_glb_export_0.4.3_FULL_FIXED.py')
text = path.read_text(encoding='utf-8')

text = text.replace('"version": (0, 4, 4)', '"version": (0, 4, 5)', 1)
text = text.replace('ADDON_PREFIX = "[STALCRAFT GLB 0.4.4]"', 'ADDON_PREFIX = "[STALCRAFT GLB 0.4.5]"', 1)

marker = '''def clear_collection(collection):\n    for obj in list(collection.objects):\n        bpy.data.objects.remove(obj, do_unlink=True)\n\n\n'''
helper = '''def clear_collection(collection):\n    for obj in list(collection.objects):\n        bpy.data.objects.remove(obj, do_unlink=True)\n\n\ndef ensure_object_mode():\n    active = bpy.context.view_layer.objects.active\n    if active is None:\n        return\n    if getattr(active, "mode", "OBJECT") == "OBJECT":\n        return\n    try:\n        with bpy.context.temp_override(\n            active_object=active,\n            object=active,\n            selected_objects=[active],\n            selected_editable_objects=[active],\n        ):\n            bpy.ops.object.mode_set(mode='OBJECT')\n    except Exception as error:\n        raise ArmorExportError(\n            "Cannot switch to Object Mode automatically. Press Tab to leave Edit Mode and run export again: "\n            + str(error)\n        )\n\n\ndef deselect_all_objects():\n    # bpy.ops.object.select_all depends on the active editor/context and can fail\n    # when the operator is launched from a sidebar, Text Editor, Properties, or File Browser.\n    # Direct object selection is context-independent.\n    for obj in list(bpy.context.view_layer.objects):\n        try:\n            obj.select_set(False)\n        except Exception:\n            pass\n\n\n'''
if 'def deselect_all_objects():' not in text:
    if marker not in text:
        raise SystemExit('clear_collection marker not found')
    text = text.replace(marker, helper, 1)

text = text.replace("bpy.ops.object.select_all(action='DESELECT')", 'deselect_all_objects()')

old = '''def export_armor(filepath):\n    if not filepath.lower().endswith(".glb"):\n'''
new = '''def export_armor(filepath):\n    ensure_object_mode()\n    if not filepath.lower().endswith(".glb"):\n'''
if old in text:
    text = text.replace(old, new, 1)
elif 'def export_armor(filepath):\n    ensure_object_mode()' not in text:
    raise SystemExit('export_armor marker not found')

if 'bpy.ops.object.select_all' in text:
    raise SystemExit('blocking context-sensitive select_all remains')
if 'def deselect_all_objects():' not in text:
    raise SystemExit('helper missing')
if '"version": (0, 4, 5)' not in text:
    raise SystemExit('version bump missing')

path.write_text(text, encoding='utf-8', newline='\n')
print('Applied Blender context-safe selection patch')
