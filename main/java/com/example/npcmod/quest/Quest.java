package com.example.npcmod.quest;

import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Quest {
    public enum QuestType {
        FETCH,      // Принести предметы
        KILL,       // Убить существ
        TALK,       // Поговорить с NPC
        EXPLORE     // Найти локацию
    }
    
    public enum QuestStatus {
        NOT_STARTED,
        ACTIVE,
        COMPLETED
    }
    
    private final String id;
    private final String name;
    private final String description;
    private final String giverNpc;
    private final QuestType type;
    private final String targetId;
    private final int targetAmount;
    private final int requiredRep;
    private final String requiredFaction;
    private final List<QuestReward> rewards;
    private final String nextQuestId;
    private final boolean repeatable;
    
    private QuestStatus status = QuestStatus.NOT_STARTED;
    private int currentAmount = 0;
    
    public Quest(String id, String name, String description, String giverNpc,
                 QuestType type, String targetId, int targetAmount,
                 int requiredRep, String requiredFaction,
                 List<QuestReward> rewards, String nextQuestId, boolean repeatable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.giverNpc = giverNpc;
        this.type = type;
        this.targetId = targetId;
        this.targetAmount = targetAmount;
        this.requiredRep = requiredRep;
        this.requiredFaction = requiredFaction;
        this.rewards = rewards != null ? rewards : new ArrayList<>();
        this.nextQuestId = nextQuestId;
        this.repeatable = repeatable;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getGiverNpc() { return giverNpc; }
    public QuestType getType() { return type; }
    public String getTargetId() { return targetId; }
    public int getTargetAmount() { return targetAmount; }
    public int getRequiredRep() { return requiredRep; }
    public String getRequiredFaction() { return requiredFaction; }
    public List<QuestReward> getRewards() { return rewards; }
    public String getNextQuestId() { return nextQuestId; }
    public boolean isRepeatable() { return repeatable; }
    
    public QuestStatus getStatus() { return status; }
    public void setStatus(QuestStatus status) { this.status = status; }
    public int getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(int amount) { this.currentAmount = amount; }
    public void addProgress(int amount) { 
        this.currentAmount = Math.min(this.currentAmount + amount, targetAmount);
        if (this.currentAmount >= targetAmount) {
            this.status = QuestStatus.COMPLETED;
        }
    }
    public boolean isCompleted() { return status == QuestStatus.COMPLETED; }
    
    public Text toText() {
        return Text.literal("§6§l" + name + "\n§7" + description + 
            "\n§eПрогресс: " + currentAmount + "/" + targetAmount);
    }
    
    public static class QuestReward {
        public enum RewardType {
            ITEM, MONEY, REPUTATION, QUEST
        }
        
        private final RewardType type;
        private final String value;
        private final int amount;
        
        public QuestReward(RewardType type, String value, int amount) {
            this.type = type;
            this.value = value;
            this.amount = amount;
        }
        
        public RewardType getType() { return type; }
        public String getValue() { return value; }
        public int getAmount() { return amount; }
    }
}