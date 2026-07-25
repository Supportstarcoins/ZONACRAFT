package ru.zonecraft.armorbootfix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

final class BootFixSettings {
    private static final int CONFIG_VERSION = 2;
    private static final float OLD_DEFAULT_LIFT = 0.09375F;

    static boolean enabled = true;
    static float baseLiftBlocks = 0.1875F;
    static float sneakingExtraBlocks = 0.0625F;
    static boolean applyWhileAirborne = true;

    private BootFixSettings() {
    }

    static void load(File configDirectory) {
        if (configDirectory == null) {
            return;
        }

        File file = new File(configDirectory, "zonecraft_armor_boot_ground_fix.properties");
        Properties values = new Properties();

        if (file.isFile()) {
            FileInputStream input = null;
            try {
                input = new FileInputStream(file);
                values.load(input);
            } catch (Throwable error) {
                System.out.println("[Zonecraft Armor Boot Fix] Could not read config: " + error);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Throwable ignored) {
                    }
                }
            }
        }

        int version = readInt(values, "configVersion", 1);
        enabled = readBoolean(values, "enabled", true);
        baseLiftBlocks = clamp(readFloat(values, "baseLiftBlocks", 0.1875F), 0.0F, 0.75F);
        sneakingExtraBlocks = clamp(readFloat(values, "sneakingExtraBlocks", 0.0625F), 0.0F, 0.25F);
        applyWhileAirborne = readBoolean(values, "applyWhileAirborne", true);

        // Version 1 used 0.09375, which was too small for the heavy suits.
        // Migrate only untouched old defaults; deliberate user values stay intact.
        if (version < CONFIG_VERSION
                && Math.abs(baseLiftBlocks - OLD_DEFAULT_LIFT) < 0.00001F) {
            baseLiftBlocks = 0.1875F;
            sneakingExtraBlocks = Math.max(sneakingExtraBlocks, 0.0625F);
            System.out.println("[Zonecraft Armor Boot Fix] Migrated default lift "
                    + "from 0.09375 to 0.1875 blocks.");
        }

        values.setProperty("configVersion", String.valueOf(CONFIG_VERSION));
        values.setProperty("enabled", String.valueOf(enabled));
        values.setProperty("baseLiftBlocks", String.valueOf(baseLiftBlocks));
        values.setProperty("sneakingExtraBlocks", String.valueOf(sneakingExtraBlocks));
        values.setProperty("applyWhileAirborne", String.valueOf(applyWhileAirborne));
        values.setProperty("description",
                "Raises every ItemStalcraftSuit render so boots do not sink into the floor. "
                + "0.0625 = one Minecraft model pixel. Default 0.1875 = three pixels.");

        FileOutputStream output = null;
        try {
            if (!configDirectory.exists()) {
                configDirectory.mkdirs();
            }
            output = new FileOutputStream(file);
            values.store(output, "Zonecraft Universal Armor Boot Ground Fix 1.1.0");
        } catch (Throwable error) {
            System.out.println("[Zonecraft Armor Boot Fix] Could not write config: " + error);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Throwable ignored) {
                }
            }
        }
    }

    private static boolean readBoolean(Properties values, String key, boolean fallback) {
        String value = values.getProperty(key);
        return value == null ? fallback : Boolean.parseBoolean(value.trim());
    }

    private static int readInt(Properties values, String key, int fallback) {
        String value = values.getProperty(key);
        if (value == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (Throwable ignored) {
            return fallback;
        }
    }

    private static float readFloat(Properties values, String key, float fallback) {
        String value = values.getProperty(key);
        if (value == null) {
            return fallback;
        }
        try {
            return Float.parseFloat(value.trim().replace(',', '.'));
        } catch (Throwable ignored) {
            return fallback;
        }
    }

    private static float clamp(float value, float minimum, float maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
