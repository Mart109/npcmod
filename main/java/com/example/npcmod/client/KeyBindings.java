package com.example.npcmod.client;

import com.example.npcmod.NpcMod;
import com.example.npcmod.screen.QuestScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static KeyBinding questKey;
    
    public static void register() {
        questKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.npcmod.quests",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.npcmod.general"
        ));
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (questKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new QuestScreen());
            }
        });
    }
}