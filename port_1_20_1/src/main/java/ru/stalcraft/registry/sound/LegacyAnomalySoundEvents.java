package ru.stalcraft.registry.sound;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Phase 2 (registry foundation): legacy anomaly sound IDs prepared for 1.20.1 migration.
 *
 * <p>This class is intentionally dependency-free (no Forge imports yet), so it can be reused
 * during incremental porting before full registry wiring is introduced.</p>
 */
public final class LegacyAnomalySoundEvents {
   public static final String MOD_ID = "stalker";

   // Legacy keys currently referenced in anomaly-related code.
   public static final String ELECTRA = "electra";
   public static final String ELECTRA_HIT = "electra_hit";
   public static final String STEAM = "steam";
   public static final String STEAM_HIT = "steam_hit";

   /**
    * Mapping: legacy 1.6.x string IDs -> future 1.20.1 registry paths.
    */
   public static final Map<String, String> LEGACY_TO_PATH = createLegacyToPath();

   private LegacyAnomalySoundEvents() {
   }

   public static String id(String path) {
      return MOD_ID + ":" + path;
   }

   private static Map<String, String> createLegacyToPath() {
      LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
      map.put(id(ELECTRA), ELECTRA);
      map.put(id(ELECTRA_HIT), ELECTRA_HIT);
      map.put(id(STEAM), STEAM);
      map.put(id(STEAM_HIT), STEAM_HIT);
      return Collections.unmodifiableMap(map);
   }
}
