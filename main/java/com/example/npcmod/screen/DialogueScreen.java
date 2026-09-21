package com.example.npcmod.screen;

import com.example.npcmod.NpcMod;
import com.example.npcmod.reputation.ReputationManager;
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

public class DialogueScreen extends HandledScreen<DialogueScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(NpcMod.MOD_ID, "textures/gui/dialogue_bg.png");

    private String[] dialogueOptions = {"", "", ""};
    private int[] reputationDeltas = {0, 0, 0};
    private String npcDialogueText = "";

    public DialogueScreen(DialogueScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 400;
        this.backgroundHeight = 180;
        this.playerInventoryTitleY = 1000;

        setupDialogue();
    }

    private void setupDialogue() {
        var npc = handler.getNpc();
        if (npc == null || npc.getNpcInfo() == null) {
            npcDialogueText = "Привет, путник.";
            dialogueOptions = new String[]{"Поговорить", "Уйти", ""};
            return;
        }

        String name = npc.getNpcInfo().name;

        if (name.contains("Серафима")) {
            npcDialogueText = "Привет, путник. Я вижу в тебе отголоски прошлого. Ты пришёл узнать правду о Чёрном Когте?";
            dialogueOptions = new String[]{"§aДа, расскажи мне всё", "§eНет, я ищу кое-что другое", "§cКто ты такая?"};
            reputationDeltas = new int[]{10, 0, 5};
        } else if (name.contains("Рейн")) {
            npcDialogueText = "Чего желаешь? Выпить или поговорить? Слышал, в порту опять неспокойно...";
            dialogueOptions = new String[]{"§eЧто слышно в порту?", "§6Купить выпить (5💰)", "§7Ничего"};
            reputationDeltas = new int[]{5, 0, 0};
        } else if (name.contains("Кракен")) {
            npcDialogueText = "Аррр... Ты напоминаешь мне твоего отца. Чёрный Коготь пал, но мы ещё живы.";
            dialogueOptions = new String[]{"§aРасскажи об отце", "§eКак возродить Коготь?", "§7Прощай"};
            reputationDeltas = new int[]{15, 20, 0};
        } else if (name.contains("Лис")) {
            npcDialogueText = "Тсс... У меня есть кое-что интересное. Документы Панциря. Интересует?";
            dialogueOptions = new String[]{"§6Купить (50💰)", "§eЧто за документы?", "§7Уйти"};
            reputationDeltas = new int[]{-5, 0, 0};
        } else if (name.contains("Молчальник")) {
            npcDialogueText = "...";
            dialogueOptions = new String[]{"§8Ты Молчальник?", "§5Я ищу Клинок", "§7Уйти"};
            reputationDeltas = new int[]{10, 15, 0};
        } else if (name.contains("Люциус")) {
            npcDialogueText = "А, наследник Чёрного Когтя. Я ждал тебя. Твой отец был... упрям.";
            dialogueOptions = new String[]{"§4Это ты заказал резню?!", "§eЧего ты хочешь?", "§7Уйти"};
            reputationDeltas = new int[]{-20, -10, 0};
        } else if (name.contains("Морте")) {
            npcDialogueText = "Ты ещё жив? Хм. Молчальник велел не трогать тебя... пока.";
            dialogueOptions = new String[]{"§4Это ты сбросил меня с крыши!", "§8Чего хочет Молчальник?", "§7Уйти"};
            reputationDeltas = new int[]{-10, 5, 0};
        } else if (name.contains("Эхо")) {
            npcDialogueText = "...Молчальник говорит: 'Тени помнят всё'. Иди к маяку.";
            dialogueOptions = new String[]{"§5Что в маяке?", "§8Кто такой Молчальник?", "§7Уйти"};
            reputationDeltas = new int[]{10, 5, 0};
        } else if (name.contains("Бреск")) {
            npcDialogueText = "Ха! Наследник Когтя! Ярый Рог помнит союз с твоим отцом. Хочешь отомстить Панцирю?";
            dialogueOptions = new String[]{"§cДа, помоги мне", "§eЧто ты можешь предложить?", "§7Подумаю"};
            reputationDeltas = new int[]{25, 10, 0};
        } else if (name.contains("Стрелка")) {
            npcDialogueText = "Орлиное Око видит всё. Твой отец был другом. Панцирь — наш общий враг.";
            dialogueOptions = new String[]{"§bСоюз против Панциря?", "§eЧто ты знаешь о резне?", "§7Уйти"};
            reputationDeltas = new int[]{20, 10, 0};
        } else {
            npcDialogueText = "Привет, путник. Чего желаешь?";
            dialogueOptions = new String[]{"§aПоговорить", "§eЗадать вопрос", "§7Уйти"};
            reputationDeltas = new int[]{0, 0, 0};
        }
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - backgroundWidth) / 2;
        int y = height - backgroundHeight - 10;

        for (int i = 0; i < 3; i++) {
            if (!dialogueOptions[i].isEmpty()) {
                final int index = i;
                this.addDrawableChild(ButtonWidget.builder(
                                Text.literal(dialogueOptions[i]),
                                btn -> {
                                    if (reputationDeltas[index] != 0) {
                                        PacketByteBuf buf = PacketByteBufs.create();
                                        buf.writeInt(reputationDeltas[index]);
                                        buf.writeString(handler.getNpc().getNpcInfo().faction);
                                        ClientPlayNetworking.send(NpcMod.id("dialogue_choice"), buf);
                                    }
                                    close();
                                })
                        .dimensions(x + 20, y + backgroundHeight - 35 - (2 - i) * 25, 360, 20)
                        .build());
            }
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        int x = (width - backgroundWidth) / 2;
        int y = height - backgroundHeight - 10;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.x = (width - backgroundWidth) / 2;
        this.y = height - backgroundHeight - 10;

        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        context.drawText(textRenderer, handler.getNpc().getDisplayName(), x + 80, y + 15, 0xFFD700, false);

        List<String> wrappedLines = textRenderer.wrapLines(Text.literal(npcDialogueText), 300);
        for (int i = 0; i < wrappedLines.size() && i < 3; i++) {
            context.drawText(textRenderer, wrappedLines.get(i), x + 80, y + 35 + i * 12, 0xFFFFFF, false);
        }

        var info = handler.getNpc().getNpcInfo();
        if (info != null && info.texture != null) {
            context.drawTexture(info.texture, x + 15, y + 15, 0, 0, 50, 80, 50, 80);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}