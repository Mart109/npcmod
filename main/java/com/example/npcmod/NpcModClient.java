package com.example.npcmod;

import com.example.npcmod.client.KeyBindings;
import com.example.npcmod.entity.ModEntities;
import com.example.npcmod.entity.client.CustomNpcRenderer;
import com.example.npcmod.network.ModPackets;
import com.example.npcmod.screen.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class NpcModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Регистрация рендерера NPC
        EntityRendererRegistry.register(ModEntities.CUSTOM_NPC, CustomNpcRenderer::new);
        
        // Регистрация экранов GUI
        HandledScreens.register(ModScreenHandlers.DIALOGUE_SCREEN_HANDLER, DialogueScreen::new);
        HandledScreens.register(ModScreenHandlers.MERCHANT_SCREEN_HANDLER, MerchantScreen::new);
        HandledScreens.register(ModScreenHandlers.TRANSPORTER_SCREEN_HANDLER, TransporterScreen::new);
        
        // Регистрация клиентских пакетов
        ModPackets.registerClient();
        
        // Регистрация клавиш (J для журнала квестов)
        KeyBindings.register();
        
        com.example.npcmod.NpcMod.LOGGER.info("§a✅ Клиент NPC Kingdom загружен! Нажми §6J §aдля журнала квестов");
    }
}