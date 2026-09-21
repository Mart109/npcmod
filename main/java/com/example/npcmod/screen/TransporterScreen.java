package com.example.npcmod.screen;

import com.example.npcmod.NpcMod;
import com.example.npcmod.money.MoneyManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class TransporterScreen extends HandledScreen<TransporterScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(NpcMod.MOD_ID, "textures/gui/transporter_bg.png");

    public TransporterScreen(TransporterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 300;
        this.backgroundHeight = 220;
        this.playerInventoryTitleY = 1000;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        List<TransporterScreenHandler.Destination> destinations = handler.getDestinations();

        for (int i = 0; i < destinations.size(); i++) {
            final int index = i;
            TransporterScreenHandler.Destination dest = destinations.get(i);

            int btnY = y + 55 + i * 40;

            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal("§a✨ В ПУТЬ"),
                            btn -> {
                                PacketByteBuf buf = PacketByteBufs.create();
                                buf.writeInt(index);
                                ClientPlayNetworking.send(NpcMod.id("teleport_to"), buf);
                                close();
                            })
                    .dimensions(x + 230, btnY + 5, 50, 20)
                    .build());
        }

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("§7✖ Закрыть"),
                        btn -> close())
                .dimensions(x + backgroundWidth - 50, y + 5, 40, 15)
                .build());
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawText(textRenderer, title, x + 10, y + 8, 0xFFD700, false);

        var player = MinecraftClient.getInstance().player;
        if (player != null) {
            int money = MoneyManager.getMoney(player.getUuid());
            context.drawText(textRenderer, "§6💰 " + money, x + backgroundWidth - 80, y + 8, 0xFFFFFF, false);
        }

        context.drawText(textRenderer, "§eВыберите пункт назначения:", x + 15, y + 30, 0xFFFFFF, false);

        List<TransporterScreenHandler.Destination> destinations = handler.getDestinations();
        for (int i = 0; i < destinations.size(); i++) {
            TransporterScreenHandler.Destination dest = destinations.get(i);
            int lineY = y + 55 + i * 40;

            context.drawText(textRenderer, dest.name, x + 15, lineY, 0xFFAA00, false);
            context.drawText(textRenderer, "§7" + dest.description, x + 15, lineY + 12, 0xCCCCCC, false);
            context.drawText(textRenderer, "§6Цена: " + dest.price + "💰", x + 160, lineY + 5, 0xFFD700, false);
        }
    }
}