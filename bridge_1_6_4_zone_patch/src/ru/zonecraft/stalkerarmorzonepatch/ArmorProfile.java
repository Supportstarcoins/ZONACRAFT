package ru.zonecraft.stalkerarmorzonepatch;

final class ArmorProfile {
    final String category;
    boolean enabled = true;
    int itemId = -1;
    String itemMatch = "";
    double bulletResistance = 0.0D;
    double headProtection = 0.0D;
    double limbProtection = 0.0D;

    ArmorProfile(String category) {
        this.category = category;
    }

    boolean matches(int candidateItemId, String identity) {
        if (!enabled) {
            return false;
        }
        if (itemId >= 0 && itemId == candidateItemId) {
            return true;
        }
        if (itemMatch == null || itemMatch.length() == 0 || identity == null) {
            return false;
        }
        String expected = normalize(itemMatch);
        String actual = normalize(identity);
        return actual.equals(expected) || actual.endsWith(expected) || actual.indexOf(expected) >= 0;
    }

    boolean hasZoneProtection() {
        return headProtection > 0.0D || limbProtection > 0.0D;
    }

    private static String normalize(String text) {
        return text.toLowerCase().replace(' ', '_').replace('-', '_');
    }
}
