package com.example.npcmod.entity.client;

import com.example.npcmod.NpcMod;
import com.example.npcmod.entity.CustomNpcEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CustomNpcModel extends GeoModel<CustomNpcEntity> {
    @Override
    public Identifier getModelResource(CustomNpcEntity animatable) {
        return new Identifier(NpcMod.MOD_ID, "geo/custom_npc.geo.json");
    }

    @Override
    public Identifier getTextureResource(CustomNpcEntity animatable) {
        var info = animatable.getNpcInfo();
        return info != null ? info.texture : new Identifier(NpcMod.MOD_ID, "textures/entity/merchant1.png");
    }

    @Override
    public Identifier getAnimationResource(CustomNpcEntity animatable) {
        return new Identifier(NpcMod.MOD_ID, "animations/custom_npc.animation.json");
    }
}