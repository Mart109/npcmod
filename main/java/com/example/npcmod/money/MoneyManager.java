package com.example.npcmod.money;

import com.example.npcmod.NpcMod;
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

public class MoneyManager {
    private static final Map<UUID, Integer> MONEY = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type TYPE = new TypeToken<Map<UUID, Integer>>(){}.getType();

    public static int getMoney(UUID playerId) {
        return MONEY.getOrDefault(playerId, 100);
    }

    public static boolean spendMoney(UUID playerId, int amount) {
        int current = getMoney(playerId);
        if (current >= amount) {
            MONEY.put(playerId, current - amount);
            return true;
        }
        return false;
    }

    public static void addMoney(UUID playerId, int amount) {
        MONEY.put(playerId, getMoney(playerId) + amount);
    }

    public static void load(MinecraftServer server) {
        MONEY.clear();
        Path path = getSavePath(server);
        if (path.toFile().exists()) {
            try (Reader reader = new FileReader(path.toFile())) {
                Map<UUID, Integer> loaded = GSON.fromJson(reader, TYPE);
                if (loaded != null) MONEY.putAll(loaded);
            } catch (IOException e) {}
        }
    }

    public static void save(MinecraftServer server) {
        Path path = getSavePath(server);
        path.getParent().toFile().mkdirs();
        try (Writer writer = new FileWriter(path.toFile())) {
            GSON.toJson(MONEY, writer);
        } catch (IOException e) {}
    }

    private static Path getSavePath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("npcmod").resolve("money.json");
    }
}