package com.example.npcmod.command;

import com.example.npcmod.entity.CustomNpcEntity;
import com.example.npcmod.entity.ModEntities;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class NpcCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("npc")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("create")
                        .then(CommandManager.argument("index", IntegerArgumentType.integer(0, 61))
                                .executes(ctx -> {
                                    int index = IntegerArgumentType.getInteger(ctx, "index");
                                    ServerWorld world = ctx.getSource().getWorld();
                                    BlockPos pos = BlockPos.ofFloored(ctx.getSource().getPosition());

                                    CustomNpcEntity npc = new CustomNpcEntity(ModEntities.CUSTOM_NPC, world);
                                    npc.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                                    npc.setNpcInfo(index);
                                    world.spawnEntity(npc);

                                    ctx.getSource().sendFeedback(() ->
                                                    Text.literal("§a✅ Создан NPC #" + index + ": " +
                                                            com.example.npcmod.npcdata.NpcData.NPC_NAMES[index]),
                                            true);
                                    return 1;
                                })
                        )
                )
                .then(CommandManager.literal("list")
                        .executes(ctx -> {
                            ctx.getSource().sendFeedback(() ->
                                            Text.literal("§eВсего NPC: 62. Используй /npc create <0-61>"),
                                    false);
                            return 1;
                        })
                )
        );
    }
}