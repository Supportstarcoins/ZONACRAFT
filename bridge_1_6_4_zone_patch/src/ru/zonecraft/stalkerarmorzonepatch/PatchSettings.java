package ru.zonecraft.stalkerarmorzonepatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class PatchSettings {
    static boolean enabled = true;
    static boolean autoReload = true;
    static boolean applyToAllProjectiles = true;
    static boolean useStalkerScale = true;
    static boolean deduplicateAllTooltipLines = true;
    static boolean hideLegacyGeneralProtection = true;
    static boolean hideLegacyMovementSpeed = true;
    static boolean hideLegacyMaxHealth = false;
    static boolean hideLegacyKnockbackResistance = false;
    static boolean logDetectedHitZones = false;

    static double headThreshold = 0.78D;
    static double legThreshold = 0.44D;
    static double armMinimumHeight = 0.48D;
    static double armMaximumHeight = 0.79D;
    static double armSideThreshold = 0.70D;
    static double rayBoxExpansion = 0.12D;
    static double minimumDamageMultiplier = 0.10D;
    static double maximumDamageMultiplier = 2.50D;

    private static File file;
    private static long lastModified;
    private static long lastCheck;

    private PatchSettings() {
    }

    static synchronized void initialize(File target) {
        file = target;
        if (!file.exists()) {
            writeDefaults();
        }
        reload();
    }

    static void reloadIfNeeded() {
        if (!autoReload || file == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastCheck < 2000L) {
            return;
        }
        lastCheck = now;
        if (file.lastModified() != lastModified) {
            reload();
        }
    }

    static synchronized void reload() {
        if (file == null || !file.isFile()) {
            return;
        }
        Map<String, String> values = new HashMap<String, String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#") || line.startsWith("//") || line.endsWith("{") || line.equals("}")) {
                    continue;
                }
                int equals = line.indexOf('=');
                if (equals < 0) {
                    continue;
                }
                String key = line.substring(0, equals).trim();
                int colon = key.indexOf(':');
                if (colon >= 0) {
                    key = key.substring(colon + 1);
                }
                String value = line.substring(equals + 1).trim();
                values.put(key.toLowerCase(Locale.ROOT), stripQuotes(value));
            }
        } catch (Throwable t) {
            System.err.println("[Zonecraft Armor Zone Patch] Cannot read settings: " + t);
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (Throwable ignored) { }
            }
        }

        enabled = bool(values, "enabled", enabled);
        autoReload = bool(values, "autoreload", autoReload);
        applyToAllProjectiles = bool(values, "applytoallprojectiles", applyToAllProjectiles);
        useStalkerScale = bool(values, "usestalkerscale", useStalkerScale);
        deduplicateAllTooltipLines = bool(values, "deduplicatealltooltiplines", deduplicateAllTooltipLines);
        hideLegacyGeneralProtection = bool(values, "hidelegacygeneralprotection", hideLegacyGeneralProtection);
        hideLegacyMovementSpeed = bool(values, "hidelegacymovementspeed", hideLegacyMovementSpeed);
        hideLegacyMaxHealth = bool(values, "hidelegacymaxhealth", hideLegacyMaxHealth);
        hideLegacyKnockbackResistance = bool(values, "hidelegacyknockbackresistance", hideLegacyKnockbackResistance);
        logDetectedHitZones = bool(values, "logdetectedhitzones", logDetectedHitZones);

        headThreshold = number(values, "headthreshold", headThreshold, 0.55D, 0.98D);
        legThreshold = number(values, "legthreshold", legThreshold, 0.10D, 0.65D);
        armMinimumHeight = number(values, "armminimumheight", armMinimumHeight, 0.20D, 0.85D);
        armMaximumHeight = number(values, "armmaximumheight", armMaximumHeight, 0.30D, 0.95D);
        armSideThreshold = number(values, "armsidethreshold", armSideThreshold, 0.10D, 1.00D);
        rayBoxExpansion = number(values, "rayboxexpansion", rayBoxExpansion, 0.00D, 0.50D);
        minimumDamageMultiplier = number(values, "minimumdamagemultiplier", minimumDamageMultiplier, 0.01D, 1.00D);
        maximumDamageMultiplier = number(values, "maximumdamagemultiplier", maximumDamageMultiplier, 1.00D, 10.00D);
        lastModified = file.lastModified();
        System.out.println("[Zonecraft Armor Zone Patch] Settings reloaded.");
    }

    private static void writeDefaults() {
        PrintWriter writer = null;
        try {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.println("# Zonecraft Armor Zone & Tooltip Patch");
            writer.println("# Values headProtection, limbProtection and bulletResistance are read from zonecraftstalkerarmorbridge.cfg.");
            writer.println("general {");
            writer.println("    B:enabled=true");
            writer.println("    B:autoReload=true");
            writer.println("    B:applyToAllProjectiles=true");
            writer.println("    B:useStalkerScale=true");
            writer.println("    B:logDetectedHitZones=false");
            writer.println("}");
            writer.println();
            writer.println("tooltip {");
            writer.println("    B:deduplicateAllTooltipLines=true");
            writer.println("    B:hideLegacyGeneralProtection=true");
            writer.println("    B:hideLegacyMovementSpeed=true");
            writer.println("    B:hideLegacyMaxHealth=false");
            writer.println("    B:hideLegacyKnockbackResistance=false");
            writer.println("}");
            writer.println();
            writer.println("hit_zones {");
            writer.println("    D:headThreshold=0.78");
            writer.println("    D:legThreshold=0.44");
            writer.println("    D:armMinimumHeight=0.48");
            writer.println("    D:armMaximumHeight=0.79");
            writer.println("    D:armSideThreshold=0.70");
            writer.println("    D:rayBoxExpansion=0.12");
            writer.println("    D:minimumDamageMultiplier=0.10");
            writer.println("    D:maximumDamageMultiplier=2.50");
            writer.println("}");
        } catch (Throwable t) {
            System.err.println("[Zonecraft Armor Zone Patch] Cannot create settings: " + t);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static boolean bool(Map<String, String> values, String key, boolean fallback) {
        String value = values.get(key);
        return value == null ? fallback : Boolean.parseBoolean(value);
    }

    private static double number(Map<String, String> values, String key, double fallback, double min, double max) {
        String value = values.get(key);
        if (value == null) {
            return fallback;
        }
        try {
            double parsed = Double.parseDouble(value.replace(',', '.'));
            return Math.max(min, Math.min(max, parsed));
        } catch (Throwable ignored) {
            return fallback;
        }
    }

    private static String stripQuotes(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
