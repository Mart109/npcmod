package com.example.npcmod;

import com.example.npcmod.command.NpcCommand;
import com.example.npcmod.command.QuestCommand;
import com.example.npcmod.entity.ModEntities;
import com.example.npcmod.item.ModItems;
import com.example.npcmod.money.MoneyManager;
import com.example.npcmod.network.ModPackets;
import com.example.npcmod.quest.QuestManager;
import com.example.npcmod.reputation.ReputationManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

public class NpcMod implements ModInitializer {
    public static final String MOD_ID = "npcmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("§6✨ Запуск NPC Kingdom... ✨");
        GeckoLib.initialize();
        
        ModItems.register();
        ModEntities.register();
        ModPackets.register();
        
        // Регистрация квестов
        QuestManager.registerQuests();
        LOGGER.info("§a📜 Квесты зарегистрированы");
        
        FabricDefaultAttributeRegistry.register(ModEntities.CUSTOM_NPC, 
                com.example.npcmod.entity.CustomNpcEntity.createMobAttributes());
        
        // Регистрация команд
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            NpcCommand.register(dispatcher);
            QuestCommand.register(dispatcher);
        });
        
        // Загрузка данных при старте сервера
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ReputationManager.load(server);
            MoneyManager.load(server);
            QuestManager.load(server);
            LOGGER.info("§a✅ Данные репутации, монет и квестов загружены!");
        });
        
        // Сохранение данных при остановке сервера
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            ReputationManager.save(server);
            MoneyManager.save(server);
            QuestManager.save(server);
            LOGGER.info("§e💾 Данные сохранены!");
        });
        
        LOGGER.info("§a✅ NPC Kingdom загружен! Используй §6/npc create <0-61>");
        LOGGER.info("§a📜 Используй §6/quest give <id> §aдля выдачи квестов");
    }
    
    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}