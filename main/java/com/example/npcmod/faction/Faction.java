package com.example.npcmod.faction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Faction {
    
    public enum FactionType {
        BLACK_CLAW("§5Чёрный Коготь", "black_claw"),
        GOLDEN_SHELL("§6Золотой Панцирь", "golden_shell"),
        FIERCE_HORN("§cЯрый Рог", "fierce_horn"),
        EAGLE_EYE("§bОрлиное Око", "eagle_eye"),
        MIDNIGHT_BLADE("§8Полуночный Клинок", "midnight_blade"),
        BANDITS("§4Разбойники", "bandits"),
        NEUTRAL("§7Нейтралы", "neutral");
        
        public final String displayName;
        public final String id;
        
        FactionType(String displayName, String id) {
            this.displayName = displayName;
            this.id = id;
        }
        
        public static FactionType fromId(String id) {
            for (FactionType type : values()) {
                if (type.id.equals(id)) return type;
            }
            return NEUTRAL;
        }
    }
    
    // Репутация игрока с каждой фракцией
    private static final Map<UUID, Map<String, Integer>> REPUTATIONS = new HashMap<>();
    
    public static int getReputation(UUID playerId, String factionId) {
        return REPUTATIONS
            .getOrDefault(playerId, new HashMap<>())
            .getOrDefault(factionId, 0);
    }
    
    public static void setReputation(UUID playerId, String factionId, int value) {
        REPUTATIONS
            .computeIfAbsent(playerId, k -> new HashMap<>())
            .put(factionId, Math.max(-100, Math.min(100, value)));
    }
    
    public static void changeReputation(UUID playerId, String factionId, int delta) {
        int current = getReputation(playerId, factionId);
        setReputation(playerId, factionId, current + delta);
    }
    
    public static Map<String, Integer> getAllReputations(UUID playerId) {
        return REPUTATIONS.getOrDefault(playerId, new HashMap<>());
    }
}