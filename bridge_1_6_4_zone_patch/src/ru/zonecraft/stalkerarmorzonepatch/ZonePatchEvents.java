package ru.zonecraft.stalkerarmorzonepatch;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

final class ZonePatchEvents {
    @ForgeSubscribe(priority = EventPriority.LOWEST)
    public void onLivingHurt(LivingHurtEvent event) {
        PatchSettings.reloadIfNeeded();
        if (!PatchSettings.enabled || event == null || event.entityLiving == null || event.source == null || event.ammount <= 0.0F) {
            return;
        }
        if (!ReflectionAccess.isPlayerLike(event.entityLiving)) {
            return;
        }

        String damageType = ReflectionAccess.getDamageType(event.source);
        if (!isBulletDamage(event.source, damageType)) {
            return;
        }

        Object armorStack = ReflectionAccess.getChestArmor(event.entityLiving);
        ArmorProfile profile = BridgeProfiles.find(armorStack);
        if (profile == null || !profile.hasZoneProtection()) {
            return;
        }

        HitZone zone = HitZoneResolver.resolve(event.entityLiving, event.source, damageType);
        double zoneProtection;
        if (zone == HitZone.HEAD) {
            zoneProtection = profile.headProtection;
        } else if (zone == HitZone.LIMB) {
            zoneProtection = profile.limbProtection;
        } else {
            return;
        }
        if (zoneProtection <= 0.0D) {
            return;
        }

        double baseReduction = effectiveReduction(profile.bulletResistance);
        double zoneReduction = effectiveReduction(zoneProtection);
        double baseRemaining = Math.max(0.01D, 1.0D - baseReduction);
        double zoneRemaining = Math.max(0.0D, 1.0D - zoneReduction);
        double multiplier = zoneRemaining / baseRemaining;
        multiplier = clamp(multiplier, PatchSettings.minimumDamageMultiplier, PatchSettings.maximumDamageMultiplier);

        float original = event.ammount;
        event.ammount = (float) Math.max(0.0D, original * multiplier);
        if (PatchSettings.logDetectedHitZones) {
            System.out.println("[Zonecraft Armor Zone Patch] " + zone + " damage " + original + " -> " + event.ammount
                + " (base=" + profile.bulletResistance + ", zone=" + zoneProtection + ", type=" + damageType + ")");
        }
    }

    @ForgeSubscribe(priority = EventPriority.LOWEST)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onItemTooltip(ItemTooltipEvent event) {
        PatchSettings.reloadIfNeeded();
        if (!PatchSettings.enabled || event == null || event.itemStack == null || event.toolTip == null) {
            return;
        }
        ArmorProfile profile = BridgeProfiles.find(event.itemStack);
        if (profile == null) {
            return;
        }

        List tooltip = event.toolTip;
        int index;
        for (index = tooltip.size() - 1; index >= 0; index--) {
            Object raw = tooltip.get(index);
            String normalized = normalizeTooltip(raw == null ? "" : String.valueOf(raw));
            if (shouldHideLegacy(normalized)) {
                tooltip.remove(index);
            }
        }

        if (PatchSettings.deduplicateAllTooltipLines) {
            Set<String> seen = new HashSet<String>();
            for (index = tooltip.size() - 1; index >= 0; index--) {
                Object raw = tooltip.get(index);
                String normalized = normalizeTooltip(raw == null ? "" : String.valueOf(raw));
                if (normalized.length() == 0) {
                    continue;
                }
                if (seen.contains(normalized)) {
                    tooltip.remove(index);
                } else {
                    seen.add(normalized);
                }
            }
            removeRepeatedBlankLines(tooltip);
        }
    }

    private static boolean isBulletDamage(Object damageSource, String damageType) {
        String type = damageType == null ? "" : damageType.toLowerCase(Locale.ROOT);
        if (containsAny(type, new String[] {"bullet", "gun", "shot", "firearm", "rifle", "pistol", "sniper"})) {
            return true;
        }
        if (containsAny(type, new String[] {"explosion", "fire", "lava", "fall", "magic", "elect", "radiation", "chemical"})) {
            return false;
        }
        if (!PatchSettings.applyToAllProjectiles) {
            return false;
        }
        if (containsAny(type, new String[] {"projectile", "arrow"})) {
            return true;
        }
        Object direct = ReflectionAccess.getDamageSourceEntity(damageSource);
        return ReflectionAccess.isProjectileLike(direct);
    }

    private static double effectiveReduction(double configuredValue) {
        double reduction;
        if (PatchSettings.useStalkerScale) {
            reduction = (1.0D + Math.max(0.0D, configuredValue)) * 0.008D;
        } else {
            reduction = configuredValue / 100.0D;
        }
        return clamp(reduction, 0.0D, 0.95D);
    }

    private static boolean shouldHideLegacy(String normalized) {
        if (PatchSettings.hideLegacyGeneralProtection && startsWithAny(normalized, new String[] {
            "общая защита", "общая броня", "general protection", "overall protection"
        })) {
            return true;
        }
        if (PatchSettings.hideLegacyMovementSpeed && startsWithAny(normalized, new String[] {
            "скорость передвижения", "movement speed"
        })) {
            return true;
        }
        if (PatchSettings.hideLegacyMaxHealth && startsWithAny(normalized, new String[] {
            "макс. здоровье", "максимальное здоровье", "max health", "maximum health"
        })) {
            return true;
        }
        if (PatchSettings.hideLegacyKnockbackResistance && startsWithAny(normalized, new String[] {
            "сопротивление отбрасыванию", "knockback resistance"
        })) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    private static void removeRepeatedBlankLines(List tooltip) {
        boolean previousBlank = false;
        int index;
        for (index = tooltip.size() - 1; index >= 0; index--) {
            String normalized = normalizeTooltip(String.valueOf(tooltip.get(index)));
            boolean blank = normalized.length() == 0;
            if (blank && previousBlank) {
                tooltip.remove(index);
            }
            previousBlank = blank;
        }
    }

    private static String normalizeTooltip(String value) {
        StringBuilder builder = new StringBuilder();
        boolean colorMarker = false;
        int index;
        for (index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (colorMarker) {
                colorMarker = false;
                continue;
            }
            if (character == '\u00a7') {
                colorMarker = true;
                continue;
            }
            builder.append(character);
        }
        return builder.toString().trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    private static boolean startsWithAny(String text, String[] prefixes) {
        int index;
        for (index = 0; index < prefixes.length; index++) {
            if (text.startsWith(prefixes[index])) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsAny(String text, String[] values) {
        int index;
        for (index = 0; index < values.length; index++) {
            if (text.indexOf(values[index]) >= 0) {
                return true;
            }
        }
        return false;
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
