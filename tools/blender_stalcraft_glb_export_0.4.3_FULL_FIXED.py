bl_info = {
    "name": "STALCRAFT GLB Armor Exporter",
    "author": "OpenAI",
    "version": (0, 4, 3),
    "blender": (3, 6, 0),
    "location": "View3D > Sidebar > GLB Armor",
    "description": "Standalone one-item GLB suit exporter with fixed Minecraft config registration",
    "category": "Import-Export",
}

import bpy
import bmesh
import os
import re
import shutil
from array import array
from mathutils import Matrix, Vector

REQUIRED_PARTS = (
    "head", "body", "right_arm", "left_arm", "right_leg", "left_leg",
)

PIVOTS = {
    "head": Vector((0.0, 0.0, 1.5000)),
    "body": Vector((0.0, 0.0, 1.5000)),
    "right_arm": Vector((-0.3125, 0.0, 1.3750)),
    "left_arm": Vector((0.3125, 0.0, 1.3750)),
    "right_leg": Vector((-0.1250, 0.0, 0.7500)),
    "left_leg": Vector((0.1250, 0.0, 0.7500)),
}

GUIDE_COLLECTION = "STALCRAFT_GLBA_GUIDE"
TEMP_COLLECTION = "STALCRAFT_GLBA_EXPORT_TEMP"
ADDON_PREFIX = "[STALCRAFT GLB 0.4.3]"


class ArmorExportError(RuntimeError):
    pass


def get_or_create_collection(name):
    collection = bpy.data.collections.get(name)
    if collection is None:
        collection = bpy.data.collections.new(name)
        bpy.context.scene.collection.children.link(collection)
    return collection


def clear_collection(collection):
    for obj in list(collection.objects):
        bpy.data.objects.remove(obj, do_unlink=True)


def material(name, rgba):
    mat = bpy.data.materials.get(name)
    if mat is None:
        mat = bpy.data.materials.new(name)
    mat.diffuse_color = rgba
    return mat


def add_cube(collection, name, center, size, mat):
    bpy.ops.mesh.primitive_cube_add(size=1.0, location=center)
    obj = bpy.context.object
    obj.name = name
    obj.scale = (size[0] * 0.5, size[1] * 0.5, size[2] * 0.5)
    bpy.ops.object.transform_apply(location=False, rotation=False, scale=True)
    for old_collection in list(obj.users_collection):
        old_collection.objects.unlink(obj)
    collection.objects.link(obj)
    obj.data.materials.append(mat)
    obj.display_type = 'WIRE'
    obj.show_in_front = True
    return obj


def add_pivot(collection, name, position):
    empty = bpy.data.objects.new("pivot_" + name, None)
    collection.objects.link(empty)
    empty.location = position
    empty.empty_display_type = 'SPHERE'
    empty.empty_display_size = 0.04
    empty.show_in_front = True
    return empty


def create_guide():
    collection = get_or_create_collection(GUIDE_COLLECTION)
    clear_collection(collection)
    colors = {
        "head": (0.95, 0.65, 0.25, 1.0),
        "body": (0.15, 0.75, 0.85, 1.0),
        "right_arm": (0.9, 0.2, 0.2, 1.0),
        "left_arm": (0.2, 0.9, 0.2, 1.0),
        "right_leg": (0.25, 0.35, 0.95, 1.0),
        "left_leg": (0.95, 0.9, 0.2, 1.0),
    }
    add_cube(collection, "guide_head", (0.0, 0.0, 1.75), (0.5, 0.5, 0.5), material("GuideHead", colors["head"]))
    add_cube(collection, "guide_body", (0.0, 0.0, 1.125), (0.5, 0.25, 0.75), material("GuideBody", colors["body"]))
    add_cube(collection, "guide_right_arm", (-0.375, 0.0, 1.0), (0.25, 0.25, 0.75), material("GuideRightArm", colors["right_arm"]))
    add_cube(collection, "guide_left_arm", (0.375, 0.0, 1.0), (0.25, 0.25, 0.75), material("GuideLeftArm", colors["left_arm"]))
    add_cube(collection, "guide_right_leg", (-0.125, 0.0, 0.375), (0.25, 0.25, 0.75), material("GuideRightLeg", colors["right_leg"]))
    add_cube(collection, "guide_left_leg", (0.125, 0.0, 0.375), (0.25, 0.25, 0.75), material("GuideLeftLeg", colors["left_leg"]))
    for part in REQUIRED_PARTS:
        add_pivot(collection, part, PIVOTS[part])
    print(ADDON_PREFIX, "Guide created.")


def find_parts():
    missing = []
    parts = {}
    for name in REQUIRED_PARTS:
        obj = bpy.data.objects.get(name)
        if obj is None or obj.type != 'MESH':
            missing.append(name)
        else:
            parts[name] = obj
    if missing:
        raise ArmorExportError("Missing mesh objects: " + ", ".join(missing))
    return parts


def validate_uvs(parts):
    missing = [name for name, obj in parts.items() if not obj.data.uv_layers]
    if missing:
        raise ArmorExportError("UV map is missing on: " + ", ".join(missing))


def evaluated_mesh_copy(source, temp_collection):
    depsgraph = bpy.context.evaluated_depsgraph_get()
    evaluated = source.evaluated_get(depsgraph)
    try:
        mesh = bpy.data.meshes.new_from_object(evaluated, depsgraph=depsgraph)
    except TypeError:
        mesh = bpy.data.meshes.new_from_object(evaluated)
    duplicate = bpy.data.objects.new(source.name, mesh)
    temp_collection.objects.link(duplicate)
    duplicate.matrix_world = source.matrix_world.copy()
    return duplicate


def recalculate_export_normals(mesh):
    bm = bmesh.new()
    try:
        bm.from_mesh(mesh)
        if bm.faces:
            bmesh.ops.recalc_face_normals(bm, faces=list(bm.faces))
            bm.normal_update()
        bm.to_mesh(mesh)
    finally:
        bm.free()
    mesh.validate(clean_customdata=False)
    mesh.update()


def prepare_export_copy(source, part_name, temp_collection):
    duplicate = evaluated_mesh_copy(source, temp_collection)
    duplicate.data.transform(duplicate.matrix_world.copy())
    duplicate.matrix_world = Matrix.Identity(4)
    duplicate.data.transform(Matrix.Translation(-PIVOTS[part_name]))
    recalculate_export_normals(duplicate.data)
    duplicate.name = part_name
    duplicate.data.name = part_name + "_mesh"
    duplicate["stalcraft_pivot_mode"] = "STRICT_GUIDE_PIVOTS"
    duplicate["stalcraft_axis_contract"] = "gltf_y_down_modelbiped_v2"
    return duplicate


def gltf_export_kwargs(filepath):
    wanted = {
        "filepath": filepath,
        "export_format": 'GLB',
        "use_selection": True,
        "export_apply": True,
        "export_yup": True,
        "export_normals": True,
        "export_tangents": True,
        "export_texcoords": True,
        "export_colors": False,
        "export_cameras": False,
        "export_lights": False,
        "export_animations": False,
        "export_materials": 'NONE',
    }
    try:
        available = set(bpy.ops.export_scene.gltf.get_rna_type().properties.keys())
        return {key: value for key, value in wanted.items() if key in available}
    except Exception:
        return wanted


def export_armor(filepath):
    if not filepath.lower().endswith(".glb"):
        filepath += ".glb"
    filepath = bpy.path.abspath(filepath)
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    parts = find_parts()
    validate_uvs(parts)
    temp = get_or_create_collection(TEMP_COLLECTION)
    clear_collection(temp)
    previous_selection = list(bpy.context.selected_objects)
    previous_active = bpy.context.view_layer.objects.active
    duplicates = []
    try:
        for part_name in REQUIRED_PARTS:
            duplicates.append(prepare_export_copy(parts[part_name], part_name, temp))
        bpy.ops.object.select_all(action='DESELECT')
        for obj in duplicates:
            obj.select_set(True)
        bpy.context.view_layer.objects.active = duplicates[0]
        bpy.ops.export_scene.gltf(**gltf_export_kwargs(filepath))
    finally:
        clear_collection(temp)
        bpy.ops.object.select_all(action='DESELECT')
        for obj in previous_selection:
            if obj and obj.name in bpy.data.objects:
                obj.select_set(True)
        if previous_active and previous_active.name in bpy.data.objects:
            bpy.context.view_layer.objects.active = previous_active
    print(ADDON_PREFIX, "Exported:", filepath)
    return filepath


def get_materials(parts):
    materials = []
    seen = set()
    for obj in parts.values():
        for mat in obj.data.materials:
            if mat is not None and mat.as_pointer() not in seen:
                seen.add(mat.as_pointer())
                materials.append(mat)
    if not materials:
        raise ArmorExportError("No material is assigned to the armor")
    return materials


def find_principled(material_obj):
    if not material_obj.use_nodes or not material_obj.node_tree:
        return None
    for node in material_obj.node_tree.nodes:
        if node.type == 'BSDF_PRINCIPLED':
            return node
    return None


def input_socket(node, names):
    if node is None:
        return None
    for name in names:
        socket = node.inputs.get(name)
        if socket is not None:
            return socket
    lowered = [name.lower() for name in names]
    for socket in node.inputs:
        if socket.name.lower() in lowered:
            return socket
    return None


def trace_image_from_socket(socket, visited=None):
    if socket is None or not socket.is_linked:
        return None
    if visited is None:
        visited = set()
    for link in socket.links:
        node = link.from_node
        key = node.as_pointer()
        if key in visited:
            continue
        visited.add(key)
        if node.type == 'TEX_IMAGE' and node.image is not None:
            return node.image
        for child_input in node.inputs:
            if child_input.is_linked:
                found = trace_image_from_socket(child_input, visited)
                if found is not None:
                    return found
    return None


def socket_scalar(socket, fallback):
    if socket is None:
        return float(fallback)
    try:
        return float(socket.default_value)
    except Exception:
        return float(fallback)


def socket_rgba(socket, fallback):
    if socket is None:
        return tuple(fallback)
    try:
        value = socket.default_value
        if len(value) >= 4:
            return tuple(float(value[i]) for i in range(4))
    except Exception:
        pass
    return tuple(fallback)


def unique_image(images):
    found = [image for image in images if image is not None]
    unique = []
    seen = set()
    for image in found:
        key = image.as_pointer()
        if key not in seen:
            seen.add(key)
            unique.append(image)
    if len(unique) > 1:
        raise ArmorExportError("Multiple textures found for one material channel. Bake one shared atlas.")
    return unique[0] if unique else None


def find_base_fallback(materials):
    candidates = []
    excluded = ("normal", "rough", "metal", "orm", "occlusion", "emiss", "height", "specular")
    for mat in materials:
        if not mat.use_nodes or not mat.node_tree:
            continue
        for node in mat.node_tree.nodes:
            if node.type != 'TEX_IMAGE' or node.image is None:
                continue
            haystack = " ".join((node.name or "", node.label or "", node.image.name or "", node.image.filepath or "")).lower()
            if not any(word in haystack for word in excluded):
                candidates.append(node.image)
    return unique_image(candidates)


def collect_material_channels(parts):
    materials = get_materials(parts)
    base_images = []
    normal_images = []
    rough_images = []
    metal_images = []
    emissive_images = []
    base_default = (0.8, 0.8, 0.8, 1.0)
    rough_default = 0.5
    metal_default = 0.0
    emissive_default = (0.0, 0.0, 0.0, 1.0)
    emissive_strength = 0.0
    found_principled = False
    for mat in materials:
        principled = find_principled(mat)
        if principled is None:
            continue
        found_principled = True
        base_socket = input_socket(principled, ("Base Color",))
        normal_socket = input_socket(principled, ("Normal",))
        rough_socket = input_socket(principled, ("Roughness",))
        metal_socket = input_socket(principled, ("Metallic",))
        emissive_socket = input_socket(principled, ("Emission Color", "Emission"))
        strength_socket = input_socket(principled, ("Emission Strength",))
        base_images.append(trace_image_from_socket(base_socket))
        normal_images.append(trace_image_from_socket(normal_socket))
        rough_images.append(trace_image_from_socket(rough_socket))
        metal_images.append(trace_image_from_socket(metal_socket))
        emissive_images.append(trace_image_from_socket(emissive_socket))
        base_default = socket_rgba(base_socket, base_default)
        rough_default = socket_scalar(rough_socket, rough_default)
        metal_default = socket_scalar(metal_socket, metal_default)
        emissive_default = socket_rgba(emissive_socket, emissive_default)
        emissive_strength = max(emissive_strength, socket_scalar(strength_socket, 0.0))
    if not found_principled:
        raise ArmorExportError("No Principled BSDF material found")
    base = unique_image(base_images) or find_base_fallback(materials)
    if emissive_strength <= 1.0e-6:
        emissive = None
        emissive_default = (0.0, 0.0, 0.0, 1.0)
        emissive_strength = 0.0
    else:
        emissive = unique_image(emissive_images)
    return {
        "base": base,
        "normal": unique_image(normal_images),
        "roughness": unique_image(rough_images),
        "metallic": unique_image(metal_images),
        "emissive": emissive,
        "base_default": base_default,
        "roughness_default": rough_default,
        "metallic_default": metal_default,
        "emissive_default": emissive_default,
        "emissive_strength": emissive_strength,
    }


def image_pixels(image):
    width, height = int(image.size[0]), int(image.size[1])
    if width <= 0 or height <= 0:
        raise ArmorExportError("Image has invalid size: " + image.name)
    pixels = list(image.pixels[:])
    if len(pixels) < width * height * 4:
        raise ArmorExportError("Incomplete pixel data: " + image.name)
    return pixels, width, height


def save_pixels_png(filepath, width, height, pixels, data_map=False):
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    image = bpy.data.images.new("__stalcraft_export_" + os.path.basename(filepath), width=width, height=height, alpha=True, float_buffer=False)
    try:
        if data_map:
            try:
                image.colorspace_settings.name = 'Non-Color'
            except Exception:
                pass
        image.pixels.foreach_set(pixels)
        image.filepath_raw = filepath
        image.file_format = 'PNG'
        image.save()
    finally:
        bpy.data.images.remove(image)


def flat_pixels(width, height, rgba):
    pixel = tuple(max(0.0, min(1.0, float(v))) for v in rgba)
    return array('f', pixel) * (width * height)


def export_image_or_default(image, filepath, size, default_rgba, data_map=False):
    target = max(16, min(4096, int(size)))
    if image is None:
        save_pixels_png(filepath, target, target, flat_pixels(target, target, default_rgba), data_map=data_map)
        return "generated default"
    temp = image.copy()
    try:
        if int(temp.size[0]) != target or int(temp.size[1]) != target:
            temp.scale(target, target)
        if data_map:
            try:
                temp.colorspace_settings.name = 'Non-Color'
            except Exception:
                pass
        os.makedirs(os.path.dirname(filepath), exist_ok=True)
        temp.filepath_raw = filepath
        temp.file_format = 'PNG'
        temp.save()
    finally:
        bpy.data.images.remove(temp)
    return image.name


def sample_image(image, x, y, out_size, default_value):
    if image is None:
        return float(default_value)
    pixels, width, height = image_pixels(image)
    sx = min(width - 1, int((x + 0.5) * width / out_size))
    sy = min(height - 1, int((y + 0.5) * height / out_size))
    base = (sy * width + sx) * 4
    return max(0.0, min(1.0, pixels[base] * 0.2126 + pixels[base + 1] * 0.7152 + pixels[base + 2] * 0.0722))


def export_orm(channels, filepath, size):
    size = max(16, min(4096, int(size)))
    output = array('f', [0.0]) * (size * size * 4)
    rough_image = channels["roughness"]
    metal_image = channels["metallic"]
    for y in range(size):
        for x in range(size):
            index = (y * size + x) * 4
            output[index] = 1.0
            output[index + 1] = sample_image(rough_image, x, y, size, channels["roughness_default"])
            output[index + 2] = sample_image(metal_image, x, y, size, channels["metallic_default"])
            output[index + 3] = 1.0
    save_pixels_png(filepath, size, size, output, data_map=True)
    return size


def export_item_icon(image, filepath, default_rgba, size=64):
    if image is None:
        save_pixels_png(filepath, size, size, flat_pixels(size, size, default_rgba), data_map=False)
        return "generated default"
    pixels, width, height = image_pixels(image)
    square = min(width, height)
    x0 = max(0, (width - square) // 2)
    y0 = max(0, (height - square) // 2)
    output = array('f', [0.0]) * (size * size * 4)
    for y in range(size):
        sy = min(height - 1, y0 + int((y + 0.5) * square / size))
        for x in range(size):
            sx = min(width - 1, x0 + int((x + 0.5) * square / size))
            src = (sy * width + sx) * 4
            dst = (y * size + x) * 4
            output[dst:dst + 4] = array('f', pixels[src:src + 4])
    save_pixels_png(filepath, size, size, output, data_map=False)
    return image.name


def sanitize_armor_name(value):
    text = (value or "").strip().lower()
    text = re.sub(r"\s+", "_", text)
    text = re.sub(r"[^a-z0-9_\-]", "", text)
    text = re.sub(r"_+", "_", text).strip("_-")
    if not text:
        raise ArmorExportError("Armor Name must contain Latin letters or digits")
    return text


def validate_item_id(value):
    item_id = int(value)
    if item_id < 256 or item_id > 31999:
        raise ArmorExportError("Item ID must be between 256 and 31999")
    return item_id


def cfg_string(value):
    value = str(value if value is not None else "")
    value = value.replace("\\", "\\\\").replace('"', '\\"').replace("\r", " ").replace("\n", " ")
    return '"' + value + '"'


def config_block(index, settings):
    return """armor_{index} {{
    B:enabled=true
    S:name={name}
    I:itemId={item_id}
    S:displayName={display_name}
    S:itemIcon=stalcraftglb:{name}
    I:maxDurability={durability}
    I:enchantability={enchantability}
    D:damageReductionPercent={damage_reduction}
    D:projectileReductionPercent={projectile_reduction}
    D:explosionReductionPercent={explosion_reduction}
    D:fireReductionPercent={fire_reduction}
    D:fallReductionPercent={fall_reduction}
    D:speedModifierPercent={speed_modifier}
    D:jumpBonus={jump_bonus}
    D:regenerationPerSecond={regeneration}
    D:knockbackResistance={knockback}
    B:fireImmunity={fire_immunity}
    B:nightVision={night_vision}
    S:model=stalcraftglb:models/armor/{name}/{name}.glb
    S:baseColor=stalcraftglb:textures/armor/{name}/basecolor.png
    S:normal=stalcraftglb:textures/armor/{name}/normal.png
    S:orm=stalcraftglb:textures/armor/{name}/orm.png
    S:emissive=stalcraftglb:textures/armor/{name}/emissive.png
    D:scale=1.0
    D:offsetX=0.0
    D:offsetY=0.0
    D:offsetZ=0.0
    D:normalStrength=1.0
    D:metallicFactor=1.0
    D:roughnessFactor=1.0
    D:emissiveStrength={emissive_strength}
    B:flipV=false
    B:pbrEnabled=true
    B:headYaw180=false
    B:bodyYaw180=false
    B:flipArmsVertical=false
    B:flipLegsVertical=false
    S:rarity=common
    S:titlePrefix=
    B:showOriginalHead=false
    B:showOriginalHeadwear=false
    B:showOriginalBody=false
    B:showOriginalArms=false
    B:showOriginalLegs=false
    B:showOriginalOtherParts=false
}}""".format(
        index=index,
        name=settings["name"],
        item_id=settings["item_id"],
        display_name=cfg_string(settings["display_name"]),
        durability=settings["durability"],
        enchantability=settings["enchantability"],
        damage_reduction=settings["damage_reduction"],
        projectile_reduction=settings["projectile_reduction"],
        explosion_reduction=settings["explosion_reduction"],
        fire_reduction=settings["fire_reduction"],
        fall_reduction=settings["fall_reduction"],
        speed_modifier=settings["speed_modifier"],
        jump_bonus=settings["jump_bonus"],
        regeneration=settings["regeneration"],
        knockback=settings["knockback"],
        fire_immunity=str(bool(settings["fire_immunity"])).lower(),
        night_vision=str(bool(settings["night_vision"])).lower(),
        emissive_strength=settings.get("emissive_strength", 0.0),
    )


def find_category_blocks(text):
    pattern = re.compile(r"(?ms)^armor_(\d+)\s*\{.*?^\}")
    return [(int(match.group(1)), match.start(), match.end(), match.group(0)) for match in pattern.finditer(text)]


def block_name(block):
    match = re.search(r"(?m)^\s*S:name=(.*?)\s*$", block)
    if not match:
        return None
    value = match.group(1).strip()
    if len(value) >= 2 and value[0] == '"' and value[-1] == '"':
        value = value[1:-1]
    return value


def block_item_id(block):
    match = re.search(r"(?m)^\s*I:itemId=(-?\d+)\s*$", block)
    return int(match.group(1)) if match else None


def verify_config(filepath, settings, expected_index):
    with open(filepath, "r", encoding="utf-8-sig") as handle:
        text = handle.read()
    matches = [(index, block) for index, start, end, block in find_category_blocks(text) if block_name(block) == settings["name"]]
    if len(matches) != 1:
        raise ArmorExportError("Config verification failed for " + settings["name"])
    index, block = matches[0]
    if index != expected_index or block_item_id(block) != settings["item_id"]:
        raise ArmorExportError("Wrong armor index or item ID after config write")
    count_match = re.search(r"(?m)^\s*I:armorCount=(\d+)\s*$", text)
    count = int(count_match.group(1)) if count_match else -1
    if count <= expected_index:
        raise ArmorExportError("armorCount does not include armor_%d" % expected_index)


def upsert_config(filepath, settings):
    parent = os.path.dirname(filepath)
    if parent:
        os.makedirs(parent, exist_ok=True)
    if os.path.isfile(filepath):
        with open(filepath, "r", encoding="utf-8-sig") as handle:
            text = handle.read()
    else:
        text = "general {\n    I:armorCount=0\n}\n"
    blocks = find_category_blocks(text)
    target = None
    for index, start, end, block in blocks:
        existing_name = block_name(block)
        existing_id = block_item_id(block)
        if existing_id == settings["item_id"] and existing_name != settings["name"]:
            raise ArmorExportError("Item ID %d is already used by %s" % (settings["item_id"], existing_name or ("armor_%d" % index)))
        if existing_name == settings["name"]:
            target = (index, start, end)
    if target is None:
        target_index = max([item[0] for item in blocks], default=-1) + 1
        text = text.rstrip() + "\n\n" + config_block(target_index, settings) + "\n"
    else:
        target_index, start, end = target
        text = text[:start] + config_block(target_index, settings) + text[end:]
    count = max([item[0] for item in find_category_blocks(text)], default=-1) + 1
    count_pattern = re.compile(r"(?m)^(\s*I:armorCount=)\d+\s*$")
    if count_pattern.search(text):
        text = count_pattern.sub(lambda match: match.group(1) + str(count), text, count=1)
    else:
        general_pattern = re.compile(r"(?ms)^general\s*\{.*?^\}")
        match = general_pattern.search(text)
        if match:
            old = match.group(0)
            replacement = old[:-1].rstrip() + "\n    I:armorCount=%d\n}" % count
            text = text[:match.start()] + replacement + text[match.end():]
        else:
            text = "general {\n    I:armorCount=%d\n}\n\n" % count + text
    if os.path.isfile(filepath):
        shutil.copy2(filepath, filepath + ".bak")
    temp_path = filepath + ".tmp"
    with open(temp_path, "w", encoding="utf-8", newline="\n") as handle:
        handle.write(text.rstrip() + "\n")
    os.replace(temp_path, filepath)
    verify_config(filepath, settings, target_index)
    print(ADDON_PREFIX, "Verified config:", filepath)
    return target_index


def resolve_runtime_config_path(raw_path):
    if not (raw_path or "").strip():
        raise ArmorExportError("Minecraft/config path is empty")
    selected = os.path.normpath(os.path.abspath(bpy.path.abspath(raw_path.strip())))
    lower = selected.lower()
    if lower.endswith(".cfg"):
        if os.path.basename(lower) != "stalcraftglb.cfg":
            raise ArmorExportError("Select stalcraftglb.cfg, the config folder, or the Minecraft instance root")
        if not os.path.isdir(os.path.dirname(selected)):
            raise ArmorExportError("Config folder does not exist: " + os.path.dirname(selected))
        return selected
    if not os.path.isdir(selected):
        raise ArmorExportError("Selected folder does not exist: " + selected)
    if os.path.basename(selected).lower() == "config":
        return os.path.join(selected, "stalcraftglb.cfg")
    direct = os.path.join(selected, "stalcraftglb.cfg")
    if os.path.isfile(direct):
        return direct
    config_dir = os.path.join(selected, "config")
    if os.path.isdir(config_dir):
        return os.path.join(config_dir, "stalcraftglb.cfg")
    raise ArmorExportError("Selected path is not a Minecraft root, config folder, or stalcraftglb.cfg: " + selected)


def export_and_install(props):
    project_root = os.path.abspath(bpy.path.abspath(props.project_root))
    resources = os.path.join(project_root, "src", "main", "resources")
    if not os.path.isdir(resources):
        raise ArmorExportError("Mod Project must contain src/main/resources")
    armor_name = sanitize_armor_name(props.armor_name)
    item_id = validate_item_id(props.item_id)
    display_name = (props.display_name or "").strip() or ("STALCRAFT Suit: " + armor_name)
    texture_size = int(props.texture_size)
    settings = {
        "name": armor_name,
        "item_id": item_id,
        "display_name": display_name,
        "durability": int(props.max_durability),
        "enchantability": int(props.enchantability),
        "damage_reduction": float(props.damage_reduction),
        "projectile_reduction": float(props.projectile_reduction),
        "explosion_reduction": float(props.explosion_reduction),
        "fire_reduction": float(props.fire_reduction),
        "fall_reduction": float(props.fall_reduction),
        "speed_modifier": float(props.speed_modifier),
        "jump_bonus": float(props.jump_bonus),
        "regeneration": float(props.regeneration),
        "knockback": float(props.knockback),
        "fire_immunity": bool(props.fire_immunity),
        "night_vision": bool(props.night_vision),
    }
    parts = find_parts()
    validate_uvs(parts)
    channels = collect_material_channels(parts)
    if props.require_base_texture and channels["base"] is None:
        raise ArmorExportError("Base Color texture was not found. Connect one shared atlas to Principled BSDF > Base Color.")
    settings["emissive_strength"] = channels["emissive_strength"]
    model_dir = os.path.join(resources, "assets", "stalcraftglb", "models", "armor", armor_name)
    texture_dir = os.path.join(resources, "assets", "stalcraftglb", "textures", "armor", armor_name)
    item_dir = os.path.join(resources, "assets", "stalcraftglb", "textures", "items")
    os.makedirs(model_dir, exist_ok=True)
    os.makedirs(texture_dir, exist_ok=True)
    os.makedirs(item_dir, exist_ok=True)
    glb_path = os.path.join(model_dir, armor_name + ".glb")
    base_path = os.path.join(texture_dir, "basecolor.png")
    normal_path = os.path.join(texture_dir, "normal.png")
    orm_path = os.path.join(texture_dir, "orm.png")
    emissive_path = os.path.join(texture_dir, "emissive.png")
    icon_path = os.path.join(item_dir, armor_name + ".png")
    export_armor(glb_path)
    base_origin = export_image_or_default(channels["base"], base_path, texture_size, channels["base_default"], data_map=False)
    normal_origin = export_image_or_default(channels["normal"], normal_path, texture_size, (0.5, 0.5, 1.0, 1.0), data_map=True)
    export_orm(channels, orm_path, texture_size)
    emissive_origin = export_image_or_default(channels["emissive"], emissive_path, texture_size, channels["emissive_default"], data_map=False)
    icon_origin = export_item_icon(channels["base"], icon_path, channels["base_default"], 64)
    generated_dir = os.path.join(project_root, "generated_config")
    generated_cfg = os.path.join(generated_dir, "stalcraftglb.cfg")
    config_index = upsert_config(generated_cfg, settings)
    runtime_cfg = None
    if props.write_runtime_config:
        runtime_cfg = resolve_runtime_config_path(props.minecraft_root)
        upsert_config(runtime_cfg, settings)
    os.makedirs(generated_dir, exist_ok=True)
    summary_path = os.path.join(generated_dir, armor_name + "_export_report.txt")
    with open(summary_path, "w", encoding="utf-8", newline="\n") as handle:
        handle.write("STALCRAFT GLB one-piece suit export 0.4.3\n")
        handle.write("armor=%s\n" % armor_name)
        handle.write("displayName=%s\n" % display_name)
        handle.write("itemId=%d\n" % item_id)
        handle.write("configIndex=%d\n" % config_index)
        handle.write("model=%s\n" % glb_path)
        handle.write("baseColor=%s (%s)\n" % (base_path, base_origin))
        handle.write("normal=%s (%s)\n" % (normal_path, normal_origin))
        handle.write("orm=%s\n" % orm_path)
        handle.write("emissive=%s (%s)\n" % (emissive_path, emissive_origin))
        handle.write("itemIcon=%s (%s)\n" % (icon_path, icon_origin))
        handle.write("generatedConfig=%s\n" % generated_cfg)
        if runtime_cfg:
            handle.write("runtimeConfig=%s\n" % runtime_cfg)
        handle.write("giveCommand=/glbsuit give %s\n" % armor_name)
        handle.write("next=Run gradlew clean build and restart Minecraft.\n")
    print(ADDON_PREFIX, "Installed standalone suit:", armor_name)
    if runtime_cfg:
        print(ADDON_PREFIX, "Runtime config:", runtime_cfg)
    return armor_name, glb_path, summary_path


class STALCRAFTGLBProperties(bpy.types.PropertyGroup):
    output_path: bpy.props.StringProperty(name="Legacy Output GLB", default="//example.glb", subtype='FILE_PATH')
    project_root: bpy.props.StringProperty(name="Mod Project", description="Root containing src/main/resources", default="", subtype='DIR_PATH')
    armor_name: bpy.props.StringProperty(name="Armor Name", default="my_armor")
    texture_size: bpy.props.EnumProperty(name="Generated Map Size", items=(("512", "512", "512x512"), ("1024", "1024", "1024x1024"), ("2048", "2048", "2048x2048"), ("4096", "4096", "4096x4096")), default="2048")
    require_base_texture: bpy.props.BoolProperty(name="Require real Base Color", default=True)
    item_id: bpy.props.IntProperty(name="New Item ID", default=12000, min=256, max=31999)
    display_name: bpy.props.StringProperty(name="Display Name", default="STALCRAFT Suit")
    max_durability: bpy.props.IntProperty(name="Durability", default=5000, min=1, max=1000000)
    enchantability: bpy.props.IntProperty(name="Enchantability", default=10, min=0, max=100)
    damage_reduction: bpy.props.FloatProperty(name="General Protection %", default=35.0, min=0.0, max=100.0)
    projectile_reduction: bpy.props.FloatProperty(name="Projectile Bonus %", default=25.0, min=0.0, max=100.0)
    explosion_reduction: bpy.props.FloatProperty(name="Explosion Bonus %", default=30.0, min=0.0, max=100.0)
    fire_reduction: bpy.props.FloatProperty(name="Fire Bonus %", default=50.0, min=0.0, max=100.0)
    fall_reduction: bpy.props.FloatProperty(name="Fall Protection %", default=75.0, min=0.0, max=100.0)
    speed_modifier: bpy.props.FloatProperty(name="Speed Modifier %", default=-5.0, min=-95.0, max=500.0)
    jump_bonus: bpy.props.FloatProperty(name="Jump Bonus", default=0.0, min=-0.5, max=3.0)
    regeneration: bpy.props.FloatProperty(name="Regeneration HP/s", default=0.0, min=0.0, max=20.0)
    knockback: bpy.props.FloatProperty(name="Knockback Resistance", default=0.2, min=0.0, max=1.0)
    fire_immunity: bpy.props.BoolProperty(name="Fire Immunity", default=False)
    night_vision: bpy.props.BoolProperty(name="Night Vision", default=False)
    write_runtime_config: bpy.props.BoolProperty(name="Write Minecraft Config", description="Update stalcraftglb.cfg in the selected instance/config path", default=False)
    minecraft_root: bpy.props.StringProperty(name="Minecraft / config Path", description="Minecraft root, config folder, or stalcraftglb.cfg", default="", subtype='DIR_PATH')


class STALCRAFTGLB_OT_create_guide(bpy.types.Operator):
    bl_idname = "stalcraft_glb.create_guide"
    bl_label = "Create Minecraft Guide"
    bl_options = {'REGISTER', 'UNDO'}
    def execute(self, context):
        try:
            create_guide()
            self.report({'INFO'}, "Minecraft guide created")
            return {'FINISHED'}
        except Exception as error:
            self.report({'ERROR'}, str(error))
            return {'CANCELLED'}


class STALCRAFTGLB_OT_analyze(bpy.types.Operator):
    bl_idname = "stalcraft_glb.analyze_materials"
    bl_label = "Analyze Material Maps"
    bl_options = {'REGISTER'}
    def execute(self, context):
        try:
            channels = collect_material_channels(find_parts())
            message = "; ".join("%s=%s" % (key, channels[key].name if channels[key] is not None else "default") for key in ("base", "normal", "roughness", "metallic", "emissive"))
            print(ADDON_PREFIX, message)
            self.report({'INFO'}, "Material maps analyzed; details in System Console")
            return {'FINISHED'}
        except Exception as error:
            self.report({'ERROR'}, str(error))
            print(ADDON_PREFIX, "Analysis failed:", error)
            return {'CANCELLED'}


class STALCRAFTGLB_OT_export(bpy.types.Operator):
    bl_idname = "stalcraft_glb.export_armor"
    bl_label = "Validate + Export GLB Only"
    bl_options = {'REGISTER'}
    def execute(self, context):
        try:
            export_armor(context.scene.stalcraft_glb_props.output_path)
            self.report({'INFO'}, "GLB armor exported")
            return {'FINISHED'}
        except Exception as error:
            self.report({'ERROR'}, str(error))
            print(ADDON_PREFIX, "Export failed:", error)
            return {'CANCELLED'}


class STALCRAFTGLB_OT_export_install(bpy.types.Operator):
    bl_idname = "stalcraft_glb.export_install"
    bl_label = "Create One-Piece Suit Item"
    bl_options = {'REGISTER'}
    def execute(self, context):
        try:
            armor_name, glb_path, report = export_and_install(context.scene.stalcraft_glb_props)
            self.report({'INFO'}, "Installed %s; run gradlew clean build" % armor_name)
            print(ADDON_PREFIX, "Report:", report)
            return {'FINISHED'}
        except Exception as error:
            self.report({'ERROR'}, str(error))
            print(ADDON_PREFIX, "Automatic export failed:", error)
            return {'CANCELLED'}


class STALCRAFTGLB_PT_panel(bpy.types.Panel):
    bl_label = "STALCRAFT GLB Armor"
    bl_idname = "STALCRAFTGLB_PT_panel"
    bl_space_type = 'VIEW_3D'
    bl_region_type = 'UI'
    bl_category = 'GLB Armor'
    def draw(self, context):
        layout = self.layout
        props = context.scene.stalcraft_glb_props
        layout.operator("stalcraft_glb.create_guide", icon='OUTLINER_OB_EMPTY')
        layout.separator()
        box = layout.box()
        box.label(text="One-click project export", icon='PACKAGE')
        box.prop(props, "project_root")
        box.prop(props, "armor_name")
        box.prop(props, "texture_size")
        box.prop(props, "require_base_texture")
        box.label(text="Real one-item suit (chest slot)", icon='ARMATURE_DATA')
        box.prop(props, "item_id")
        box.prop(props, "display_name")
        box.prop(props, "max_durability")
        box.prop(props, "enchantability")
        protection = box.box()
        protection.label(text="Unique gameplay properties")
        protection.prop(props, "damage_reduction")
        protection.prop(props, "projectile_reduction")
        protection.prop(props, "explosion_reduction")
        protection.prop(props, "fire_reduction")
        protection.prop(props, "fall_reduction")
        protection.prop(props, "speed_modifier")
        protection.prop(props, "jump_bonus")
        protection.prop(props, "regeneration")
        protection.prop(props, "knockback")
        protection.prop(props, "fire_immunity")
        protection.prop(props, "night_vision")
        box.prop(props, "write_runtime_config")
        if props.write_runtime_config:
            box.prop(props, "minecraft_root")
        box.operator("stalcraft_glb.analyze_materials", icon='VIEWZOOM')
        box.operator("stalcraft_glb.export_install", icon='EXPORT')
        layout.separator()
        legacy = layout.box()
        legacy.label(text="Legacy GLB-only export")
        legacy.prop(props, "output_path")
        legacy.operator("stalcraft_glb.export_armor", icon='EXPORT')
        layout.separator()
        layout.label(text="Required object names:")
        for name in REQUIRED_PARTS:
            layout.label(text=name)


CLASSES = (
    STALCRAFTGLBProperties,
    STALCRAFTGLB_OT_create_guide,
    STALCRAFTGLB_OT_analyze,
    STALCRAFTGLB_OT_export,
    STALCRAFTGLB_OT_export_install,
    STALCRAFTGLB_PT_panel,
)


def register():
    if hasattr(bpy.types.Scene, "stalcraft_glb_props"):
        try:
            unregister()
        except Exception:
            pass
    for cls in CLASSES:
        bpy.utils.register_class(cls)
    bpy.types.Scene.stalcraft_glb_props = bpy.props.PointerProperty(type=STALCRAFTGLBProperties)


def unregister():
    if hasattr(bpy.types.Scene, "stalcraft_glb_props"):
        del bpy.types.Scene.stalcraft_glb_props
    for cls in reversed(CLASSES):
        try:
            bpy.utils.unregister_class(cls)
        except RuntimeError:
            pass


if __name__ == "__main__":
    register()
