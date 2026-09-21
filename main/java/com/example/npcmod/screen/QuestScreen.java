package com.example.npcmod.screen;

import com.example.npcmod.NpcMod;
import com.example.npcmod.quest.QuestManager;
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

import java.util.List;

public class DialogueScreen extends HandledScreen<DialogueScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(NpcMod.MOD_ID, "textures/gui/dialogue_bg.png");
    
    private String[] dialogueOptions = {"", "", ""};
    private int[] reputationDeltas = {0, 0, 0};
    private String npcDialogueText = "";
    private String npcName = "";
    
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
            npcName = "NPC";
            dialogueOptions = new String[]{"Поговорить", "Уйти", ""};
            return;
        }
        
        npcName = npc.getNpcInfo().name;
        var player = MinecraftClient.getInstance().player;
        
        // ========== СЕРАФИМА ==========
        if (npcName.contains("Серафима")) {
            if (!QuestManager.hasQuest(player, "healing") && !QuestManager.isQuestCompleted(player, "healing")) {
                npcDialogueText = "Ты очнулся... Тебе нужно набраться сил. Принеси мне 5 целебных трав, они растут у реки за храмом.";
                dialogueOptions = new String[]{"§aХорошо, я принесу", "§7Позже", ""};
            } else if (QuestManager.hasQuest(player, "healing") && QuestManager.getPlayerQuest(player, "healing").isCompleted()) {
                npcDialogueText = "Ты принёс травы! Молодец. Теперь я могу рассказать о твоём прошлом...";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else if (QuestManager.isQuestCompleted(player, "healing") && !QuestManager.hasQuest(player, "first_choice")) {
                npcDialogueText = "Ты окреп. Теперь выбери свой путь: месть, восстановление семьи или поиск правды.";
                dialogueOptions = new String[]{"§4Месть", "§6Восстановление", "§bПравда"};
            } else {
                npcDialogueText = "Да благословит тебя Чёрный Коготь, дитя.";
                dialogueOptions = new String[]{"§aСпасибо", "§7Уйти", ""};
            }
        }
        
        // ========== РЕЙН ==========
        else if (npcName.contains("Рейн")) {
            if (!QuestManager.hasQuest(player, "rein_debt") && !QuestManager.isQuestCompleted(player, "rein_debt")) {
                npcDialogueText = "Ищешь информацию? Сначала верни долг Олдрича. 5 монет.";
                dialogueOptions = new String[]{"§aЯ верну долг", "§eКто такой Олдрич?", "§7Уйти"};
            } else if (QuestManager.hasQuest(player, "rein_debt") && QuestManager.getPlayerQuest(player, "rein_debt").isCompleted()) {
                npcDialogueText = "Долг вернул? Молодец. Теперь слушай...";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else {
                npcDialogueText = "Чего желаешь? Выпить или поговорить?";
                dialogueOptions = new String[]{"§6Купить эль (2💰)", "§eЧто слышно?", "§7Ничего"};
            }
        }
        
        // ========== БРЕСК ==========
        else if (npcName.contains("Бреск")) {
            if (!QuestManager.hasQuest(player, "smith_debt") && !QuestManager.isQuestCompleted(player, "smith_debt")) {
                npcDialogueText = "Союз? Докажи делом. Выбей долг с кузнеца Торгрима.";
                dialogueOptions = new String[]{"§aЯ выбью долг", "§eСколько он должен?", "§7Уйти"};
            } else if (QuestManager.hasQuest(player, "smith_debt") && QuestManager.getPlayerQuest(player, "smith_debt").isCompleted()) {
                npcDialogueText = "Долг вернул? Хорошо. Теперь о деле...";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else {
                npcDialogueText = "Ярый Рог помнит союз с твоим отцом. Хочешь отомстить Панцирю?";
                dialogueOptions = new String[]{"§cДа, помоги мне", "§eЧто ты можешь предложить?", "§7Подумаю"};
            }
        }
        
        // ========== СТРЕЛКА ==========
        else if (npcName.contains("Стрелка")) {
            if (!QuestManager.hasQuest(player, "fallen_crossbow") && !QuestManager.isQuestCompleted(player, "fallen_crossbow")) {
                npcDialogueText = "Мой арбалет упал с башни в порт. Найди его — и получишь информацию.";
                dialogueOptions = new String[]{"§aЯ найду", "§eГде искать?", "§7Уйти"};
            } else if (QuestManager.hasQuest(player, "fallen_crossbow") && QuestManager.getPlayerQuest(player, "fallen_crossbow").isCompleted()) {
                npcDialogueText = "Нашёл? Дай сюда.";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else {
                npcDialogueText = "Орлиное Око видит всё. Что тебе нужно?";
                dialogueOptions = new String[]{"§bИнформация", "§eЗадание", "§7Уйти"};
            }
        }
        
        // ========== ЭХО ==========
        else if (npcName.contains("Эхо")) {
            if (!QuestManager.hasQuest(player, "lost_messenger") && !QuestManager.isQuestCompleted(player, "lost_messenger")) {
                npcDialogueText = "Моя подруга Лира пропала в трущобах. Помоги найти её...";
                dialogueOptions = new String[]{"§aЯ помогу", "§eГде искать?", "§7Уйти"};
            } else if (QuestManager.hasQuest(player, "lost_messenger") && QuestManager.getPlayerQuest(player, "lost_messenger").isCompleted()) {
                npcDialogueText = "Ты спас Лиру! Спасибо...";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else {
                npcDialogueText = "Молчальник ждёт тебя на маяке.";
                dialogueOptions = new String[]{"§5Я готов", "§8Кто такой Молчальник?", "§7Уйти"};
            }
        }
        
        // ========== ЛИС ==========
        else if (npcName.contains("Лис")) {
            if (!QuestManager.hasQuest(player, "steal_debts") && !QuestManager.isQuestCompleted(player, "steal_debts")) {
                npcDialogueText = "Тсс... Нужно украсть расписки из банка Панциря. Поможешь — получишь клинок.";
                dialogueOptions = new String[]{"§aЯ помогу", "§eЧто за клинок?", "§7Уйти"};
            } else if (QuestManager.hasQuest(player, "steal_debts") && QuestManager.getPlayerQuest(player, "steal_debts").isCompleted()) {
                npcDialogueText = "Расписки у тебя? Отлично!";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else {
                npcDialogueText = "Есть работа? Или просто мимо проходишь?";
                dialogueOptions = new String[]{"§6Работа", "§eИнформация", "§7Уйти"};
            }
        }
        
        // ========== КРАКЕН ==========
        else if (npcName.contains("Кракен")) {
            if (!QuestManager.hasQuest(player, "kraken_box") && !QuestManager.isQuestCompleted(player, "kraken_box")) {
                npcDialogueText = "Аррр... В храме спрятан ящик с оружием. Принеси — расскажу про тайный ход.";
                dialogueOptions = new String[]{"§aЯ принесу", "§eЧто в ящике?", "§7Уйти"};
            } else if (QuestManager.hasQuest(player, "kraken_box") && QuestManager.getPlayerQuest(player, "kraken_box").isCompleted()) {
                npcDialogueText = "Ящик цел? Хорошо...";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else {
                npcDialogueText = "Чёрный Коготь пал, но мы ещё живы.";
                dialogueOptions = new String[]{"§5Расскажи об отце", "§eКак возродить Коготь?", "§7Прощай"};
            }
        }
        
        // ========== МОЛЧАЛЬНИК (через Эхо) ==========
        else if (npcName.contains("Молчальник")) {
            npcDialogueText = "...";
            dialogueOptions = new String[]{"§8Ты Молчальник?", "§5Я ищу Клинок", "§7Уйти"};
        }
        
        // ========== ЛЮЦИУС ==========
        else if (npcName.contains("Люциус")) {
            npcDialogueText = "А, наследник Чёрного Когтя. Я ждал тебя.";
            dialogueOptions = new String[]{"§4Это ты заказал резню!", "§eЧего ты хочешь?", "§7Уйти"};
        }
        
        // ========== МОРТЕ ==========
        else if (npcName.contains("Морте")) {
            npcDialogueText = "Ты ещё жив? Хм. Молчальник велел не трогать тебя... пока.";
            dialogueOptions = new String[]{"§4Это ты сбросил меня с крыши!", "§8Чего хочет Молчальник?", "§7Уйти"};
        }
        
        // ========== ПО УМОЛЧАНИЮ ==========
        else {
            npcDialogueText = "Привет, путник. Чего желаешь?";
            dialogueOptions = new String[]{"§aПоговорить", "§eЗадать вопрос", "§7Уйти"};
        }
    }
    
    @Override
    protected void init() {
        super.init();
        
        int x = (width - backgroundWidth) / 2;
        int y = height - backgroundHeight - 10;
        
        for (int i = 0; i < 3; i++) {
            if (dialogueOptions[i] != null && !dialogueOptions[i].isEmpty()) {
                final int index = i;
                this.addDrawableChild(ButtonWidget.builder(
                    Text.literal(dialogueOptions[i]),
                    btn -> handleDialogueChoice(index)
                ).dimensions(x + 20, y + backgroundHeight - 35 - (2 - i) * 25, 360, 20).build());
            }
        }
    }
    
    private void handleDialogueChoice(int index) {
        var player = MinecraftClient.getInstance().player;
        if (player == null) return;
        
        // ========== ВЫДАЧА И СДАЧА КВЕСТОВ ==========
        
        // СЕРАФИМА
        if (npcName.contains("Серафима")) {
            if (!QuestManager.hasQuest(player, "healing") && !QuestManager.isQuestCompleted(player, "healing") && index == 0) {
                QuestManager.startQuest(player, "healing");
            } else if (QuestManager.hasQuest(player, "healing") && QuestManager.getPlayerQuest(player, "healing").isCompleted() && index == 0) {
                QuestManager.completeQuest(player, "healing");
            }
        }
        
        // РЕЙН
        if (npcName.contains("Рейн")) {
            if (!QuestManager.hasQuest(player, "rein_debt") && !QuestManager.isQuestCompleted(player, "rein_debt") && index == 0) {
                QuestManager.startQuest(player, "rein_debt");
            } else if (QuestManager.hasQuest(player, "rein_debt") && QuestManager.getPlayerQuest(player, "rein_debt").isCompleted() && index == 0) {
                QuestManager.completeQuest(player, "rein_debt");
            }
        }
        
        // БРЕСК
        if (npcName.contains("Бреск")) {
            if (!QuestManager.hasQuest(player, "smith_debt") && !QuestManager.isQuestCompleted(player, "smith_debt") && index == 0) {
                QuestManager.startQuest(player, "smith_debt");
            } else if (QuestManager.hasQuest(player, "smith_debt") && QuestManager.getPlayerQuest(player, "smith_debt").isCompleted() && index == 0) {
                QuestManager.completeQuest(player, "smith_debt");
            }
        }
        
        // СТРЕЛКА
        if (npcName.contains("Стрелка")) {
            if (!QuestManager.hasQuest(player, "fallen_crossbow") && !QuestManager.isQuestCompleted(player, "fallen_crossbow") && index == 0) {
                QuestManager.startQuest(player, "fallen_crossbow");
            } else if (QuestManager.hasQuest(player, "fallen_crossbow") && QuestManager.getPlayerQuest(player, "fallen_crossbow").isCompleted() && index == 0) {
                QuestManager.completeQuest(player, "fallen_crossbow");
            }
        }
        
        // ЭХО
        if (npcName.contains("Эхо")) {
            if (!QuestManager.hasQuest(player, "lost_messenger") && !QuestManager.isQuestCompleted(player, "lost_messenger") && index == 0) {
                QuestManager.startQuest(player, "lost_messenger");
            } else if (QuestManager.hasQuest(player, "lost_messenger") && QuestManager.getPlayerQuest(player, "lost_messenger").isCompleted() && index == 0) {
                QuestManager.completeQuest(player, "lost_messenger");
            }
        }
        
        // ЛИС
        if (npcName.contains("Лис")) {
            if (!QuestManager.hasQuest(player, "steal_debts") && !QuestManager.isQuestCompleted(player, "steal_debts") && index == 0) {
                QuestManager.startQuest(player, "steal_debts");
            } else if (QuestManager.hasQuest(player, "steal_debts") && QuestManager.getPlayerQuest(player, "steal_debts").isCompleted() && index == 0) {
                QuestManager.completeQuest(player, "steal_debts");
            }
        }
        
        // КРАКЕН
        if (npcName.contains("Кракен")) {
            if (!QuestManager.hasQuest(player, "kraken_box") && !QuestManager.isQuestCompleted(player, "kraken_box") && index == 0) {
                QuestManager.startQuest(player, "kraken_box");
            } else if (QuestManager.hasQuest(player, "kraken_box") && QuestManager.getPlayerQuest(player, "kraken_box").isCompleted() && index == 0) {
                QuestManager.completeQuest(player, "kraken_box");
            }
        }
        
        close();
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
        
        // Имя NPC (золотой)
        context.drawText(textRenderer, Text.literal("§6" + npcName), x + 80, y + 15, 0xFFD700, false);
        
        // Текст диалога
        List<net.minecraft.text.OrderedText> lines = textRenderer.wrapLines(Text.literal(npcDialogueText), 300);
        for (int i = 0; i < lines.size() && i < 3; i++) {
            context.drawText(textRenderer, lines.get(i), x + 80, y + 35 + i * 12, 0xFFFFFF, false);
        }
        
        // Портрет NPC (заглушка)
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