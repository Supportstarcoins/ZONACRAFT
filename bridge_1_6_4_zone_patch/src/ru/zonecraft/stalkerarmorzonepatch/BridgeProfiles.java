package ru.zonecraft.stalkerarmorzonepatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class BridgeProfiles {
    private static final Pattern TRAILING_NUMBER = Pattern.compile("(\\d+)$");
    private static final String[] CONFIG_NAMES = new String[] {
        "zonecraftstalkerarmorbridge.cfg",
        "stalkerglbstatsbridge.cfg"
    };

    private static volatile List<ArmorProfile> profiles = Collections.emptyList();
    private static File configFile;
    private static long lastModified;
    private static long lastCheck;

    private BridgeProfiles() {
    }

    static synchronized void initialize(File configDirectory) {
        configFile = findConfig(configDirectory);
        reload();
    }

    static void reloadIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastCheck < 2000L) {
            return;
        }
        lastCheck = now;
        if (configFile == null || !configFile.isFile()) {
            File directory = ArmorZonePatchMod.getConfigDirectory();
            if (directory != null) {
                configFile = findConfig(directory);
            }
        }
        if (configFile != null && configFile.isFile() && configFile.lastModified() != lastModified) {
            reload();
        }
    }

    static ArmorProfile find(Object itemStack) {
        reloadIfNeeded();
        if (itemStack == null) {
            return null;
        }
        int id = ReflectionAccess.getItemId(itemStack);
        String identity = ReflectionAccess.getItemIdentity(itemStack);
        List<ArmorProfile> snapshot = profiles;
        int index;
        for (index = 0; index < snapshot.size(); index++) {
            ArmorProfile profile = snapshot.get(index);
            if (profile.matches(id, identity)) {
                return profile;
            }
        }
        return null;
    }

    static synchronized void reload() {
        if (configFile == null || !configFile.isFile()) {
            profiles = Collections.emptyList();
            return;
        }

        List<ArmorProfile> loaded = new ArrayList<ArmorProfile>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
            String category = null;
            Map<String, String> values = new HashMap<String, String>();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.length() == 0 || trimmed.startsWith("#") || trimmed.startsWith("//")) {
                    continue;
                }
                if (trimmed.endsWith("{")) {
                    if (category != null) {
                        addProfile(loaded, category, values);
                    }
                    category = stripQuotes(trimmed.substring(0, trimmed.length() - 1).trim());
                    values = new HashMap<String, String>();
                    continue;
                }
                if (trimmed.equals("}")) {
                    if (category != null) {
                        addProfile(loaded, category, values);
                    }
                    category = null;
                    values = new HashMap<String, String>();
                    continue;
                }
                if (category == null) {
                    continue;
                }
                int equals = trimmed.indexOf('=');
                if (equals < 0) {
                    continue;
                }
                String key = trimmed.substring(0, equals).trim();
                int colon = key.indexOf(':');
                if (colon >= 0) {
                    key = key.substring(colon + 1);
                }
                String value = stripQuotes(trimmed.substring(equals + 1).trim());
                values.put(key.toLowerCase(Locale.ROOT), value);
            }
            if (category != null) {
                addProfile(loaded, category, values);
            }
        } catch (Throwable t) {
            System.err.println("[Zonecraft Armor Zone Patch] Cannot parse bridge config: " + t);
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (Throwable ignored) { }
            }
        }

        profiles = Collections.unmodifiableList(loaded);
        lastModified = configFile.lastModified();
        System.out.println("[Zonecraft Armor Zone Patch] Loaded " + loaded.size() + " armor profiles from " + configFile.getName());
    }

    private static void addProfile(List<ArmorProfile> destination, String category, Map<String, String> values) {
        String lowerCategory = category.toLowerCase(Locale.ROOT);
        if (lowerCategory.indexOf("armor") < 0 && lowerCategory.indexOf("suit") < 0) {
            return;
        }

        ArmorProfile profile = new ArmorProfile(category);
        profile.enabled = bool(values, new String[] {"enabled", "active"}, true);
        profile.itemId = integer(values, new String[] {"itemid", "item_id", "armoritemid", "matchitemid", "id"}, -1);
        if (profile.itemId < 0) {
            Matcher matcher = TRAILING_NUMBER.matcher(category);
            if (matcher.find()) {
                try {
                    profile.itemId = Integer.parseInt(matcher.group(1));
                } catch (Throwable ignored) {
                    profile.itemId = -1;
                }
            }
        }
        profile.itemMatch = text(values, new String[] {"itemmatch", "itemname", "unlocalizedname", "registryname"}, "");
        profile.bulletResistance = number(values, new String[] {"bulletresistance", "bulletprotection", "bulletdamagefactor"}, 0.0D);
        profile.headProtection = number(values, new String[] {"headprotection", "headresistance", "helmetprotection"}, 0.0D);
        profile.limbProtection = number(values, new String[] {"limbprotection", "limbresistance", "armslegsprotection"}, 0.0D);

        if (profile.itemId >= 0 || profile.itemMatch.length() > 0) {
            destination.add(profile);
        }
    }

    private static File findConfig(File directory) {
        int index;
        for (index = 0; index < CONFIG_NAMES.length; index++) {
            File candidate = new File(directory, CONFIG_NAMES[index]);
            if (candidate.isFile()) {
                return candidate;
            }
        }
        return new File(directory, CONFIG_NAMES[0]);
    }

    private static boolean bool(Map<String, String> values, String[] keys, boolean fallback) {
        String value = first(values, keys);
        return value == null ? fallback : Boolean.parseBoolean(value);
    }

    private static int integer(Map<String, String> values, String[] keys, int fallback) {
        String value = first(values, keys);
        if (value == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (Throwable ignored) {
            return fallback;
        }
    }

    private static double number(Map<String, String> values, String[] keys, double fallback) {
        String value = first(values, keys);
        if (value == null) {
            return fallback;
        }
        try {
            return Double.parseDouble(value.trim().replace(',', '.'));
        } catch (Throwable ignored) {
            return fallback;
        }
    }

    private static String text(Map<String, String> values, String[] keys, String fallback) {
        String value = first(values, keys);
        return value == null ? fallback : value;
    }

    private static String first(Map<String, String> values, String[] keys) {
        int index;
        for (index = 0; index < keys.length; index++) {
            String value = values.get(keys[index]);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private static String stripQuotes(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
