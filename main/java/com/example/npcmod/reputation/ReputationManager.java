package com.example.npcmod.reputation;

import com.example.npcmod.NpcMod;
import com.example.npcmod.faction.Faction.FactionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReputationManager {
    private static final Map<UUID, Map<String, Integer>> REPUTATIONS = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type TYPE = new TypeToken<Map<UUID, Map<String, Integer>>>(){}.getType();

    public static int getReputation(UUID playerId, String factionId) {
        return REPUTATIONS
                .getOrDefault(playerId, new HashMap<>())
                .getOrDefault(factionId, 0);
    }

    public static void changeReputation(MinecraftServer server, UUID playerId, String factionId, int delta) {
        Map<String, Integer> playerReps = REPUTATIONS.computeIfAbsent(playerId, k -> new HashMap<>());
        int current = playerReps.getOrDefault(factionId, 0);
        int newValue = Math.max(-100, Math.min(100, current + delta));
        playerReps.put(factionId, newValue);
        save(server);
    }

    public static Map<String, Integer> getAllReputations(UUID playerId) {
        return REPUTATIONS.getOrDefault(playerId, new HashMap<>());
    }

    public static void load(MinecraftServer server) {
        REPUTATIONS.clear();
        Path path = getSavePath(server);
        File file = path.toFile();
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Map<UUID, Map<String, Integer>> loaded = GSON.fromJson(reader, TYPE);
                if (loaded != null) REPUTATIONS.putAll(loaded);
                NpcMod.LOGGER.info("§aЗагружена репутация для {} игроков", REPUTATIONS.size());
            } catch (IOException e) {
                NpcMod.LOGGER.error("Ошибка загрузки репутации", e);
            }
        }
    }

    public static void save(MinecraftServer server) {
        Path path = getSavePath(server);
        path.getParent().toFile().mkdirs();
        try (Writer writer = new FileWriter(path.toFile())) {
            GSON.toJson(REPUTATIONS, writer);
        } catch (IOException e) {
            NpcMod.LOGGER.error("Ошибка сохранения репутации", e);
        }
    }

    private static Path getSavePath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("npcmod").resolve("reputations.json");
    }
}