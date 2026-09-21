package com.example.npcmod.command;

import com.example.npcmod.quest.QuestManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class QuestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("quest")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("give")
                .then(CommandManager.argument("questId", StringArgumentType.word())
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayer();
                        String questId = StringArgumentType.getString(ctx, "questId");
                        if (QuestManager.startQuest(player, questId)) {
                            ctx.getSource().sendFeedback(() -> 
                                Text.literal("§aКвест " + questId + " выдан"), false);
                        } else {
                            ctx.getSource().sendFeedback(() -> 
                                Text.literal("§cНе удалось выдать квест"), false);
                        }
                        return 1;
                    })
                )
            )
            .then(CommandManager.literal("complete")
                .then(CommandManager.argument("questId", StringArgumentType.word())
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayer();
                        String questId = StringArgumentType.getString(ctx, "questId");
                        var quest = QuestManager.getPlayerQuest(player, questId);
                        if (quest != null) {
                            quest.addProgress(999);
                            QuestManager.completeQuest(player, questId);
                            ctx.getSource().sendFeedback(() -> 
                                Text.literal("§aКвест " + questId + " завершён"), false);
                        }
                        return 1;
                    })
                )
            )
            .then(CommandManager.literal("list")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    var quests = QuestManager.getActiveQuests(player);
                    ctx.getSource().sendFeedback(() -> 
                        Text.literal("§eАктивных квестов: " + quests.size()), false);
                    for (var q : quests) {
                        ctx.getSource().sendFeedback(() -> 
                            Text.literal("  §7- " + q.getName() + " (" + q.getCurrentAmount() + "/" + q.getTargetAmount() + ")"), false);
                    }
                    return 1;
                })
            )
        );
    }
}