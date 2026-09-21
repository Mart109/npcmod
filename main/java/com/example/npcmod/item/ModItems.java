package com.example.npcmod.item;

import com.example.npcmod.NpcMod;
import com.example.npcmod.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item NPC_SPAWN_EGG = registerItem("npc_spawn_egg",
            new SpawnEggItem(ModEntities.CUSTOM_NPC, 0x4B0082, 0xFFD700,
                    new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(NpcMod.MOD_ID, name), item);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries ->
                entries.add(NPC_SPAWN_EGG));
    }
}