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
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class MerchantScreen extends HandledScreen<MerchantScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(NpcMod.MOD_ID, "textures/gui/merchant_bg.png");

    public MerchantScreen(MerchantScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 220;
        this.backgroundHeight = 220;
        this.playerInventoryTitleY = 1000;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        List<MerchantScreenHandler.TradeOffer> offers = handler.getOffers();

        for (int i = 0; i < offers.size(); i++) {
            final int index = i;
            MerchantScreenHandler.TradeOffer offer = offers.get(i);

            int col = i % 4;
            int row = i / 4;
            int btnX = x + 30 + col * 45;
            int btnY = y + 55 + row * 35;

            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal("§6" + offer.price + "💰"),
                            btn -> {
                                PacketByteBuf buf = PacketByteBufs.create();
                                buf.writeInt(index);
                                ClientPlayNetworking.send(NpcMod.id("merchant_buy"), buf);
                            })
                    .dimensions(btnX, btnY, 40, 15)
                    .build());
        }

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("§7✖ Закрыть"),
                        btn -> close())
                .dimensions(x + backgroundWidth - 60, y + 5, 50, 15)
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

        // Заголовок
        context.drawText(textRenderer, title, x + 10, y + 8, 0xFFD700, false);

        // Монеты игрока
        var player = MinecraftClient.getInstance().player;
        if (player != null) {
            int money = MoneyManager.getMoney(player.getUuid());
            context.drawText(textRenderer, "§6💰 " + money, x + backgroundWidth - 60, y + 8, 0xFFFFFF, false);
        }

        // Товары с ценами
        List<MerchantScreenHandler.TradeOffer> offers = handler.getOffers();
        for (int i = 0; i < offers.size(); i++) {
            MerchantScreenHandler.TradeOffer offer = offers.get(i);
            int col = i % 4;
            int row = i / 4;

            int itemX = x + 35 + col * 45;
            int itemY = y + 30 + row * 35;

            // Иконка предмета
            context.drawItem(offer.item, itemX, itemY);
            context.drawItemInSlot(textRenderer, offer.item, itemX, itemY);

            // Название (сокращённое)
            String shortName = offer.name.length() > 8 ? offer.name.substring(0, 6) + ".." : offer.name;
            context.drawText(textRenderer, "§7" + shortName, itemX - 5, itemY - 12, 0xFFFFFF, false);
        }

        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}