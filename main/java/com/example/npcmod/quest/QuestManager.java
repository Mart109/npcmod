package com.example.npcmod.quest;

import com.example.npcmod.NpcMod;
import com.example.npcmod.money.MoneyManager;
import com.example.npcmod.reputation.ReputationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;

public class QuestManager {
    private static final Map<UUID, Map<String, Quest>> PLAYER_QUESTS = new HashMap<>();
    private static final Map<String, Quest> QUEST_TEMPLATES = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type TYPE = new TypeToken<Map<UUID, Map<String, Quest>>>(){}.getType();
    
    public static void registerQuests() {
        NpcMod.LOGGER.info("§a📜 Регистрация квестов...");
        
        // ==================== АКТ 1 ====================
        registerQuest(new Quest("healing", "Исцеление", "Принести Серафиме 5 целебных трав.", "Серафима", 
            Quest.QuestType.FETCH, "healing_herb", 5, 0, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.ITEM, "minecraft:potion", 1)), "memory", false));
        
        registerQuest(new Quest("help_loader", "Помочь грузчику", "Разгрузить ящики в порту.", "Олдрич", 
            Quest.QuestType.TALK, "oldrich", 1, 0, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.MONEY, "", 5)), null, false));
        
        registerQuest(new Quest("rein_debt", "Долг Рейна", "Вернуть долг Рейну.", "Рейн", 
            Quest.QuestType.TALK, "oldrich", 1, 0, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "fox_basement", 0)), null, false));
        
        registerQuest(new Quest("fox_basement", "Подвал Лиса", "Найти клинок в канализации.", "Лис", 
            Quest.QuestType.EXPLORE, "sewer_cache", 1, 0, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "first_choice", 0)), null, false));
        
        registerQuest(new Quest("first_choice", "Первый выбор", "Вернуться к Серафиме.", "Серафима", 
            Quest.QuestType.TALK, "serafima", 1, 0, "neutral", null, null, false));
        
        // ==================== АКТ 2 ====================
        registerQuest(new Quest("smith_debt", "Долг кузнеца", "Выбить долг с Торгрима.", "Бреск", 
            Quest.QuestType.TALK, "torgrim", 1, 10, "fierce_horn",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "barracks_spy", 0)), null, false));
        
        registerQuest(new Quest("barracks_spy", "Шпион в казармах", "Найти предателя.", "Бреск", 
            Quest.QuestType.TALK, "leif", 1, 15, "fierce_horn",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.ITEM, "minecraft:iron_sword", 1)), "caravan_attack", false));
        
        registerQuest(new Quest("fallen_crossbow", "Упавший арбалет", "Найти арбалет Стрелки.", "Стрелка", 
            Quest.QuestType.FETCH, "crossbow", 1, 0, "eagle_eye",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "bird_eye", 0)), null, false));
        
        registerQuest(new Quest("bird_eye", "Птичий глаз", "Наблюдать с башни 3 дня.", "Стрелка", 
            Quest.QuestType.EXPLORE, "watchtower", 3, 10, "eagle_eye",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.ITEM, "minecraft:map", 1)), "felix_drink", false));
        
        registerQuest(new Quest("lost_messenger", "Пропавшая посыльная", "Спасти Лиру.", "Эхо", 
            Quest.QuestType.KILL, "bandit", 3, 0, "midnight_blade",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "mute_witness", 0)), null, false));
        
        registerQuest(new Quest("mute_witness", "Немой свидетель", "Найти знахаря.", "Молчальник", 
            Quest.QuestType.TALK, "healer", 1, 10, "midnight_blade",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "memory_ritual", 0)), null, false));
        
        registerQuest(new Quest("steal_debts", "Кража долгов", "Украсть расписки.", "Лис", 
            Quest.QuestType.FETCH, "debt_papers", 1, 10, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.ITEM, "minecraft:iron_sword", 1)), null, false));
        
        registerQuest(new Quest("memory_ritual", "Ритуал памяти", "Принести 3 предмета.", "Молчальник", 
            Quest.QuestType.FETCH, "ritual_items", 3, 15, "midnight_blade",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "father_ruins", 0)), null, false));
        
        registerQuest(new Quest("father_ruins", "Руины отца", "Найти дневник Альдо.", "Кракен", 
            Quest.QuestType.EXPLORE, "ruins", 1, 0, "black_claw",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.ITEM, "minecraft:writable_book", 1)), null, false));
        
        registerQuest(new Quest("fake_docs", "Фальшивые документы", "Получить пропуск.", "Бернардо", 
            Quest.QuestType.TALK, "bernardo", 1, 0, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.ITEM, "minecraft:paper", 1)), null, false));
        
        registerQuest(new Quest("felix_safe", "Сейф Феликса", "Украсть чёрные книги.", "Лис", 
            Quest.QuestType.FETCH, "black_books", 1, 15, "neutral", null, null, false));
        
        registerQuest(new Quest("traitor_hawk", "Предатель в рядах", "Найти вора Верна.", "Стрелка", 
            Quest.QuestType.TALK, "vern", 1, 15, "eagle_eye", null, "caravan_attack", false));
        
        registerQuest(new Quest("caravan_attack", "Караван Панциря", "Ограбить караван.", "Бреск", 
            Quest.QuestType.KILL, "golden_guard", 5, 20, "fierce_horn",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.MONEY, "", 100)), null, false));
        
        registerQuest(new Quest("kraken_box", "Ящик Кракена", "Забрать ящик из храма.", "Кракен", 
            Quest.QuestType.FETCH, "kraken_box", 1, 0, "black_claw",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "kraken_mead", 0)), null, false));
        
        registerQuest(new Quest("kraken_mead", "Мёд для Кракена", "Найти медовуху.", "Кракен", 
            Quest.QuestType.FETCH, "mead", 1, 0, "black_claw", null, null, false));
        
        registerQuest(new Quest("felix_drink", "Подпоить Феликса", "Узнать о слабостях Панциря.", "Стрелка", 
            Quest.QuestType.TALK, "felix", 1, 10, "eagle_eye", null, null, false));
        
        // ==================== АКТ 3 ====================
        registerQuest(new Quest("marcello_debt", "Долг Марцелло", "Выбить 500 монет.", "Бреск", 
            Quest.QuestType.TALK, "marcello", 1, 25, "fierce_horn",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.MONEY, "", 500)), "forest_ambush", false));
        
        registerQuest(new Quest("forest_ambush", "Засада в лесу", "Отразить набег.", "Бреск", 
            Quest.QuestType.KILL, "bandit", 10, 30, "fierce_horn", null, null, false));
        
        registerQuest(new Quest("massacre_witness", "Свидетели резни", "Найти выживших.", "Кракен", 
            Quest.QuestType.TALK, "survivor", 1, 10, "black_claw", null, null, false));
        
        registerQuest(new Quest("secret_passage", "Тайный ход", "Найти карту в шахте.", "Кракен", 
            Quest.QuestType.EXPLORE, "mine", 1, 15, "black_claw",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.ITEM, "minecraft:map", 1)), null, false));
        
        registerQuest(new Quest("fox_amulet", "Амулет Лиса", "Украсть амулет.", "Лис", 
            Quest.QuestType.FETCH, "amulet", 1, 20, "bandits", null, null, false));
        
        registerQuest(new Quest("nightmare_cult", "Культ Кошмара", "Очистить храм.", "Молчальник", 
            Quest.QuestType.KILL, "cultist", 8, 25, "midnight_blade", null, null, false));
        
        registerQuest(new Quest("mansion_assault", "Штурм особняка", "Проникнуть к Люциусу.", "Бреск", 
            Quest.QuestType.EXPLORE, "mansion", 1, 35, "fierce_horn",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "lucius_key", 0)), null, false));
        
        // ==================== АКТ 4 ====================
        registerQuest(new Quest("lucius_key", "Ключ Люциуса", "Получить ключ.", "Люциус Вертиго", 
            Quest.QuestType.FETCH, "lucius_key", 1, 0, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "catacombs", 0)), null, false));
        
        registerQuest(new Quest("catacombs", "Катакомбы", "Добраться до Сердца.", "Люциус Вертиго", 
            Quest.QuestType.EXPLORE, "heart_room", 1, 0, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.QUEST, "three_paths", 0)), null, false));
        
        registerQuest(new Quest("three_paths", "Три пути", "Выбрать судьбу Сердца.", "Сердце города", 
            Quest.QuestType.TALK, "heart", 1, 0, "neutral", null, null, false));
        
        // ==================== ОПЦИОНАЛЬНЫЕ ====================
        registerQuest(new Quest("coal_for_smith", "Уголь для кузнеца", "Добыть 48 угля.", "Кузнец", 
            Quest.QuestType.FETCH, "minecraft:coal", 48, 0, "fierce_horn",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.MONEY, "", 20)), null, true));
        
        registerQuest(new Quest("iron_for_ship", "Железо для корабля", "Принести 16 слитков.", "Капитан Морган", 
            Quest.QuestType.FETCH, "minecraft:iron_ingot", 16, 0, "neutral", null, null, false));
        
        registerQuest(new Quest("diamonds_for_fox", "Алмазы для Лиса", "Найти 5 алмазов.", "Лис", 
            Quest.QuestType.FETCH, "minecraft:diamond", 5, 25, "bandits",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.MONEY, "", 50)), null, false));
        
        registerQuest(new Quest("cheese_for_rats", "Сыр для Крыс", "Принести 5 сыра.", "Главарь Чёрных Крыс", 
            Quest.QuestType.FETCH, "cheese", 5, 0, "bandits",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.ITEM, "minecraft:map", 1)), null, false));
        
        registerQuest(new Quest("black_claw_seal", "Печать Чёрного Когтя", "Найти печать на маяке.", "Кракен", 
            Quest.QuestType.EXPLORE, "lighthouse_secret", 1, 20, "black_claw", null, null, false));
        
        registerQuest(new Quest("shell_treasure", "Сокровищница Панциря", "Ограбить банк.", "Бернардо", 
            Quest.QuestType.EXPLORE, "bank_vault", 1, 10, "neutral",
            Arrays.asList(new Quest.QuestReward(Quest.QuestReward.RewardType.MONEY, "", 500)), null, false));
        
        NpcMod.LOGGER.info("§a✅ Зарегистрировано 37 квестов");
    }
    
    private static void registerQuest(Quest quest) {
        QUEST_TEMPLATES.put(quest.getId(), quest);
    }
    
    public static Quest getQuestTemplate(String questId) {
        return QUEST_TEMPLATES.get(questId);
    }
    
    public static boolean hasQuest(PlayerEntity player, String questId) {
        Map<String, Quest> playerQuests = PLAYER_QUESTS.get(player.getUuid());
        return playerQuests != null && playerQuests.containsKey(questId);
    }
    
    public static boolean isQuestCompleted(PlayerEntity player, String questId) {
        Quest quest = getPlayerQuest(player, questId);
        return quest != null && quest.isCompleted();
    }
    
    public static Quest getPlayerQuest(PlayerEntity player, String questId) {
        Map<String, Quest> playerQuests = PLAYER_QUESTS.get(player.getUuid());
        return playerQuests != null ? playerQuests.get(questId) : null;
    }
    
    public static Collection<Quest> getActiveQuests(PlayerEntity player) {
        Map<String, Quest> playerQuests = PLAYER_QUESTS.get(player.getUuid());
        if (playerQuests == null) return Collections.emptyList();
        
        List<Quest> active = new ArrayList<>();
        for (Quest q : playerQuests.values()) {
            if (q.getStatus() == Quest.QuestStatus.ACTIVE) {
                active.add(q);
            }
        }
        return active;
    }
    
    public static boolean startQuest(PlayerEntity player, String questId) {
        Quest template = QUEST_TEMPLATES.get(questId);
        if (template == null) return false;
        
        if (template.getRequiredRep() > 0) {
            int rep = ReputationManager.getReputation(player.getUuid(), template.getRequiredFaction());
            if (rep < template.getRequiredRep()) {
                player.sendMessage(Text.literal("§c❌ Требуется репутация " + template.getRequiredRep() + " с фракцией " + template.getRequiredFaction()), false);
                return false;
            }
        }
        
        Map<String, Quest> playerQuests = PLAYER_QUESTS.computeIfAbsent(player.getUuid(), k -> new HashMap<>());
        if (playerQuests.containsKey(questId) && !template.isRepeatable()) {
            return false;
        }
        
        Quest newQuest = new Quest(
            template.getId(), template.getName(), template.getDescription(),
            template.getGiverNpc(), template.getType(), template.getTargetId(),
            template.getTargetAmount(), template.getRequiredRep(), template.getRequiredFaction(),
            template.getRewards(), template.getNextQuestId(), template.isRepeatable()
        );
        newQuest.setStatus(Quest.QuestStatus.ACTIVE);
        playerQuests.put(questId, newQuest);
        
        player.sendMessage(Text.literal("§a✅ Квест начат: " + template.getName()), false);
        return true;
    }
    
    public static boolean completeQuest(PlayerEntity player, String questId) {
        Quest quest = getPlayerQuest(player, questId);
        if (quest == null || !quest.isCompleted()) return false;
        
        MinecraftServer server = player.getServer();
        if (server == null) return false;
        
        for (Quest.QuestReward reward : quest.getRewards()) {
            switch (reward.getType()) {
                case MONEY:
                    MoneyManager.addMoney(player.getUuid(), reward.getAmount());
                    player.sendMessage(Text.literal("§6+" + reward.getAmount() + "💰"), true);
                    break;
                case REPUTATION:
                    ReputationManager.changeReputation(server, player.getUuid(), 
                        reward.getValue(), reward.getAmount());
                    break;
                case ITEM:
                    Item item = Registries.ITEM.get(new Identifier(reward.getValue()));
                    if (item != null && item != Items.AIR) {
                        player.getInventory().offerOrDrop(new ItemStack(item, reward.getAmount()));
                    }
                    break;
                case QUEST:
                    if (reward.getValue() != null && !reward.getValue().isEmpty()) {
                        startQuest(player, reward.getValue());
                    }
                    break;
            }
        }
        
        if (quest.getNextQuestId() != null && !quest.getNextQuestId().isEmpty()) {
            startQuest(player, quest.getNextQuestId());
        }
        
        player.sendMessage(Text.literal("§a🎉 Квест завершён: " + quest.getName()), false);
        
        if (!quest.isRepeatable()) {
            quest.setStatus(Quest.QuestStatus.COMPLETED);
        } else {
            PLAYER_QUESTS.get(player.getUuid()).remove(questId);
        }
        
        return true;
    }
    
    public static void updateProgress(PlayerEntity player, String targetId, int amount) {
        Map<String, Quest> playerQuests = PLAYER_QUESTS.get(player.getUuid());
        if (playerQuests == null) return;
        
        for (Quest quest : playerQuests.values()) {
            if (quest.getStatus() == Quest.QuestStatus.ACTIVE && 
                quest.getType() == Quest.QuestType.FETCH &&
                quest.getTargetId().equals(targetId)) {
                quest.addProgress(amount);
                player.sendMessage(Text.literal("§e📋 " + quest.getName() + ": " + 
                    quest.getCurrentAmount() + "/" + quest.getTargetAmount()), true);
                if (quest.isCompleted()) {
                    player.sendMessage(Text.literal("§a✨ Квест готов к сдаче! Вернитесь к " + 
                        quest.getGiverNpc()), false);
                }
            }
        }
    }
    
    public static void resetPlayerQuests(PlayerEntity player) {
        PLAYER_QUESTS.remove(player.getUuid());
        player.sendMessage(Text.literal("§e🔄 Все квесты сброшены"), false);
    }
    
    public static void load(MinecraftServer server) {
        PLAYER_QUESTS.clear();
        registerQuests();
        
        Path path = getSavePath(server);
        File file = path.toFile();
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Map<UUID, Map<String, Quest>> loaded = GSON.fromJson(reader, TYPE);
                if (loaded != null) PLAYER_QUESTS.putAll(loaded);
                NpcMod.LOGGER.info("§aЗагружены квесты для {} игроков", PLAYER_QUESTS.size());
            } catch (IOException e) {
                NpcMod.LOGGER.error("Ошибка загрузки квестов", e);
            }
        }
    }
    
    public static void save(MinecraftServer server) {
        Path path = getSavePath(server);
        path.getParent().toFile().mkdirs();
        try (Writer writer = new FileWriter(path.toFile())) {
            GSON.toJson(PLAYER_QUESTS, writer);
        } catch (IOException e) {
            NpcMod.LOGGER.error("Ошибка сохранения квестов", e);
        }
    }
    
    private static Path getSavePath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("npcmod").resolve("quests.json");
    }
}