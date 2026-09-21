package com.example.npcmod.screen;

import com.example.npcmod.entity.CustomNpcEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class DialogueScreenHandler extends ScreenHandler {
    protected final ScreenHandlerContext context;
    protected final CustomNpcEntity npc;

    public DialogueScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, null);
    }

    public DialogueScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CustomNpcEntity npc) {
        super(ModScreenHandlers.DIALOGUE_SCREEN_HANDLER, syncId);
        this.context = context;
        this.npc = npc;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public CustomNpcEntity getNpc() {
        return npc;
    }
}