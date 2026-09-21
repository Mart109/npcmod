package com.example.npcmod.network;

import com.example.npcmod.NpcMod;
import com.example.npcmod.reputation.ReputationManager;
import com.example.npcmod.screen.MerchantScreenHandler;
import com.example.npcmod.screen.TransporterScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier DIALOGUE_CHOICE = NpcMod.id("dialogue_choice");
    public static final Identifier TELEPORT_TO = NpcMod.id("teleport_to");
    public static final Identifier MERCHANT_BUY = NpcMod.id("merchant_buy");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(DIALOGUE_CHOICE, (server, player, handler, buf, responseSender) -> {
            int delta = buf.readInt();
            String faction = buf.readString();
            server.execute(() -> {
                ReputationManager.changeReputation(server, player.getUuid(), faction, delta);
                player.sendMessage(Text.literal("§aРепутация с фракцией изменена на " + delta), true);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(TELEPORT_TO, (server, player, handler, buf, responseSender) -> {
            int destIndex = buf.readInt();
            server.execute(() -> {
                ScreenHandler screenHandler = player.currentScreenHandler;
                if (screenHandler instanceof TransporterScreenHandler transporter) {
                    transporter.teleport(player, destIndex);
                }
                player.closeHandledScreen();
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(MERCHANT_BUY, (server, player, handler, buf, responseSender) -> {
            int offerIndex = buf.readInt();
            server.execute(() -> {
                ScreenHandler screenHandler = player.currentScreenHandler;
                if (screenHandler instanceof MerchantScreenHandler merchant) {
                    merchant.buyItem(player, offerIndex);
                }
            });
        });
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(DIALOGUE_CHOICE, (client, handler, buf, responseSender) -> {});
        ClientPlayNetworking.registerGlobalReceiver(TELEPORT_TO, (client, handler, buf, responseSender) -> {});
        ClientPlayNetworking.registerGlobalReceiver(MERCHANT_BUY, (client, handler, buf, responseSender) -> {});
    }
}