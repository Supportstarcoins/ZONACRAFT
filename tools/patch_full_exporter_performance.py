from pathlib import Path

path = Path("tools/blender_stalcraft_glb_export_0.4.3_FULL_FIXED.py")
text = path.read_text(encoding="utf-8")
start = text.index("def sample_image(")
end = text.index("def export_item_icon(", start)
replacement = r'''def build_image_sampler(image, default_value):
    default_value = max(0.0, min(1.0, float(default_value)))
    if image is None:
        return {
            "pixels": None,
            "width": 0,
            "height": 0,
            "default": default_value,
        }

    width = int(image.size[0])
    height = int(image.size[1])
    if width <= 0 or height <= 0:
        raise ArmorExportError("Image has invalid size: " + image.name)

    # Read the image only once. The previous implementation converted the
    # complete texture into a Python list again for every output pixel, which
    # made Blender appear permanently frozen at 1024/2048/4096 texture sizes.
    pixels = array('f', [0.0]) * (width * height * 4)
    try:
        image.pixels.foreach_get(pixels)
    except Exception:
        pixels = array('f', image.pixels[:])

    return {
        "pixels": pixels,
        "width": width,
        "height": height,
        "default": default_value,
    }


def sample_sampler(sampler, x, y, out_size):
    pixels = sampler["pixels"]
    if pixels is None:
        return sampler["default"]

    width = sampler["width"]
    height = sampler["height"]
    sx = min(width - 1, int((x + 0.5) * width / out_size))
    sy = min(height - 1, int((y + 0.5) * height / out_size))
    base = (sy * width + sx) * 4
    return max(
        0.0,
        min(
            1.0,
            pixels[base] * 0.2126
            + pixels[base + 1] * 0.7152
            + pixels[base + 2] * 0.0722,
        ),
    )


def export_orm(channels, filepath, size):
    size = max(16, min(4096, int(size)))
    rough_image = channels["roughness"]
    metal_image = channels["metallic"]
    rough_default = max(0.0, min(1.0, float(channels["roughness_default"])))
    metal_default = max(0.0, min(1.0, float(channels["metallic_default"])))

    # Fast path: no source data maps. Build one pixel and repeat it instead of
    # executing millions of Python sampling calls.
    if rough_image is None and metal_image is None:
        pixel = array('f', (1.0, rough_default, metal_default, 1.0))
        output = pixel * (size * size)
        save_pixels_png(filepath, size, size, output, data_map=True)
        return size

    rough_sampler = build_image_sampler(rough_image, rough_default)
    metal_sampler = build_image_sampler(metal_image, metal_default)
    output = array('f', [0.0]) * (size * size * 4)

    wm = getattr(bpy.context, "window_manager", None)
    if wm is not None:
        try:
            wm.progress_begin(0, size)
        except Exception:
            wm = None

    try:
        for y in range(size):
            row_start = y * size * 4
            for x in range(size):
                index = row_start + x * 4
                output[index] = 1.0
                output[index + 1] = sample_sampler(rough_sampler, x, y, size)
                output[index + 2] = sample_sampler(metal_sampler, x, y, size)
                output[index + 3] = 1.0

            if wm is not None and (y & 31) == 0:
                try:
                    wm.progress_update(y)
                except Exception:
                    pass
    finally:
        if wm is not None:
            try:
                wm.progress_end()
            except Exception:
                pass

    save_pixels_png(filepath, size, size, output, data_map=True)
    return size


'''
text = text[:start] + replacement + text[end:]
text = text.replace('"version": (0, 4, 3),', '"version": (0, 4, 4),', 1)
text = text.replace(
    '"description": "Standalone one-item GLB suit exporter with fixed Minecraft config registration",',
    '"description": "Standalone GLB suit exporter with fixed config registration and non-blocking ORM generation",',
    1,
)
path.write_text(text, encoding="utf-8", newline="\n")
print("Patched", path, "bytes=", path.stat().st_size)
