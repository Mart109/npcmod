package com.example.npcmod.entity;

import com.example.npcmod.NpcMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<CustomNpcEntity> CUSTOM_NPC = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(NpcMod.MOD_ID, "custom_npc"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CustomNpcEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .trackRangeBlocks(64)
                    .build()
    );

    public static void register() {}
}