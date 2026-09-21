package com.example.npcmod.screen;

import com.example.npcmod.entity.CustomNpcEntity;
import com.example.npcmod.money.MoneyManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TransporterScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final CustomNpcEntity npc;

    public static class Destination {
        public final String name;
        public final String description;
        public final int price;
        public final BlockPos coords;

        public Destination(String name, String description, int price, BlockPos coords) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.coords = coords;
        }
    }

    public TransporterScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, null);
    }

    public TransporterScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CustomNpcEntity npc) {
        super(ModScreenHandlers.TRANSPORTER_SCREEN_HANDLER, syncId);
        this.context = context;
        this.npc = npc;
    }

    public List<Destination> getDestinations() {
        List<Destination> dests = new ArrayList<>();

        if (npc == null || npc.getNpcInfo() == null) return dests;

        String npcName = npc.getNpcInfo().name;

        if (npcName.contains("Эдмон")) {
            dests.add(new Destination("§6🏛️ Золотой престол", "Столица, гильдии, торговцы", 50, new BlockPos(200, 68, 100)));
            dests.add(new Destination("§5🌑 Теневой базар", "Чёрный рынок, редкие товары", 75, new BlockPos(-300, 64, -200)));
        } else if (npcName.contains("Лука")) {
            dests.add(new Destination("§8⚓ Порт Теней", "Портовый район, таверна", 50, new BlockPos(-1200, 64, 500)));
            dests.add(new Destination("§c🏔️ Цитадель Рога", "Крепость Ярого Рога", 60, new BlockPos(-800, 70, 800)));
            dests.add(new Destination("§b🦅 Орлиный пик", "Башни Орлиного Ока", 70, new BlockPos(-500, 120, 300)));
        }

        return dests;
    }

    public boolean teleport(ServerPlayerEntity player, int destIndex) {
        List<Destination> dests = getDestinations();
        if (destIndex < 0 || destIndex >= dests.size()) return false;

        Destination dest = dests.get(destIndex);

        if (!MoneyManager.spendMoney(player.getUuid(), dest.price)) {
            player.sendMessage(Text.literal("§c❌ Недостаточно монет! Нужно " + dest.price + "💰"), false);
            return false;
        }

        player.teleport((ServerWorld) player.getWorld(),
                dest.coords.getX() + 0.5,
                dest.coords.getY(),
                dest.coords.getZ() + 0.5,
                player.getYaw(),
                player.getPitch());

        player.sendMessage(Text.literal("§a✨ Вы телепортировались в " + dest.name), false);
        return true;
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