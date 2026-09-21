package com.example.npcmod.screen;

import com.example.npcmod.entity.CustomNpcEntity;
import com.example.npcmod.money.MoneyManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MerchantScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final CustomNpcEntity npc;
    private final Inventory inventory;

    public static class TradeOffer {
        public final ItemStack item;
        public final int price;
        public final String name;

        public TradeOffer(ItemStack item, int price, String name) {
            this.item = item;
            this.price = price;
            this.name = name;
        }
    }

    private static List<TradeOffer> getOffersForNpc(String npcName) {
        List<TradeOffer> offers = new ArrayList<>();

        switch (npcName) {
            case "§6Бернардо":
                offers.add(new TradeOffer(new ItemStack(Items.PAPER, 1), 50, "Фальшивый документ"));
                offers.add(new TradeOffer(new ItemStack(Items.EMERALD, 1), 30, "Редкий изумруд"));
                offers.add(new TradeOffer(new ItemStack(Items.GOLDEN_APPLE, 1), 100, "Золотое яблоко"));
                offers.add(new TradeOffer(new ItemStack(Items.DIAMOND, 1), 75, "Алмаз"));
                break;
            case "§6Капитан Морган":
                offers.add(new TradeOffer(new ItemStack(Items.MAP, 1), 25, "Карта сокровищ"));
                offers.add(new TradeOffer(new ItemStack(Items.COMPASS, 1), 40, "Компас"));
                offers.add(new TradeOffer(new ItemStack(Items.OAK_BOAT, 1), 30, "Лодка"));
                offers.add(new TradeOffer(new ItemStack(Items.SPYGLASS, 1), 60, "Подзорная труба"));
                break;
            case "§fТорговец зельями":
                offers.add(new TradeOffer(new ItemStack(Items.POTION, 1), 40, "Зелье лечения"));
                offers.add(new TradeOffer(new ItemStack(Items.SPLASH_POTION, 1), 50, "Взрывное зелье"));
                offers.add(new TradeOffer(new ItemStack(Items.LINGERING_POTION, 1), 80, "Туманное зелье"));
                break;
            default:
                offers.add(new TradeOffer(new ItemStack(Items.IRON_INGOT, 1), 5, "Железо"));
                offers.add(new TradeOffer(new ItemStack(Items.GOLD_INGOT, 1), 15, "Золото"));
                offers.add(new TradeOffer(new ItemStack(Items.DIAMOND, 1), 50, "Алмаз"));
                offers.add(new TradeOffer(new ItemStack(Items.BREAD, 1), 3, "Хлеб"));
        }

        return offers;
    }

    public MerchantScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, null);
    }

    public MerchantScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CustomNpcEntity npc) {
        super(ModScreenHandlers.MERCHANT_SCREEN_HANDLER, syncId);
        this.context = context;
        this.npc = npc;

        List<TradeOffer> offers = npc != null && npc.getNpcInfo() != null
                ? getOffersForNpc(npc.getNpcInfo().name)
                : getOffersForNpc("");

        this.inventory = new SimpleInventory(offers.size());

        int slotIndex = 0;
        for (TradeOffer offer : offers) {
            this.addSlot(new Slot(inventory, slotIndex, 30 + (slotIndex % 4) * 40, 35 + (slotIndex / 4) * 30) {
                @Override
                public boolean canInsert(ItemStack stack) { return false; }
            });
            inventory.setStack(slotIndex, offer.item.copy());
            slotIndex++;
        }

        // Инвентарь игрока
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 120 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 178));
        }
    }

    public List<TradeOffer> getOffers() {
        return npc != null && npc.getNpcInfo() != null
                ? getOffersForNpc(npc.getNpcInfo().name)
                : getOffersForNpc("");
    }

    public boolean buyItem(ServerPlayerEntity player, int offerIndex) {
        List<TradeOffer> offers = getOffers();
        if (offerIndex < 0 || offerIndex >= offers.size()) return false;

        TradeOffer offer = offers.get(offerIndex);
        if (MoneyManager.spendMoney(player.getUuid(), offer.price)) {
            ItemStack bought = offer.item.copy();
            if (!player.getInventory().insertStack(bought)) {
                player.dropItem(bought, false);
            }
            player.sendMessage(Text.literal("§a✅ Куплено: " + offer.name + " за " + offer.price + "💰"), false);
            return true;
        } else {
            player.sendMessage(Text.literal("§c❌ Недостаточно монет! Нужно " + offer.price + "💰"), false);
            return false;
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}