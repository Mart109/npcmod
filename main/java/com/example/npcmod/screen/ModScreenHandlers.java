package com.example.npcmod.screen;

import com.example.npcmod.NpcMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<DialogueScreenHandler> DIALOGUE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier(NpcMod.MOD_ID, "dialogue"),
                    new ScreenHandlerType<>(DialogueScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    public static final ScreenHandlerType<MerchantScreenHandler> MERCHANT_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier(NpcMod.MOD_ID, "merchant"),
                    new ScreenHandlerType<>((syncId, inv) -> new MerchantScreenHandler(syncId, inv), FeatureFlags.VANILLA_FEATURES));

    public static final ScreenHandlerType<TransporterScreenHandler> TRANSPORTER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier(NpcMod.MOD_ID, "transporter"),
                    new ScreenHandlerType<>((syncId, inv) -> new TransporterScreenHandler(syncId, inv), FeatureFlags.VANILLA_FEATURES));
}