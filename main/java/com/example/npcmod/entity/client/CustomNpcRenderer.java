package com.example.npcmod.entity.client;

import com.example.npcmod.entity.CustomNpcEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CustomNpcRenderer extends GeoEntityRenderer<CustomNpcEntity> {
    public CustomNpcRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CustomNpcModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(CustomNpcEntity entity, float entityYaw, float partialTick,
                       MatrixStack poseStack, VertexConsumerProvider bufferSource,
                       int packedLight) {
        poseStack.scale(1.0f, 1.0f, 1.0f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}