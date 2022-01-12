package com.fabpharos.levincraft.entities;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class GeneratorRenderer extends EntityRenderer<GeneratorEntity> {

    public GeneratorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(GeneratorEntity p_114482_) {
        return null;
    }
}
