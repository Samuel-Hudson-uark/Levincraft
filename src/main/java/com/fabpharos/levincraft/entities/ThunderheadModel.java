package com.fabpharos.levincraft.entities;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.fabpharos.levincraft.Levincraft.MODID;

public class ThunderheadModel extends AnimatedGeoModel<ThunderheadEntity> {

    private static final ResourceLocation MODEL = new ResourceLocation(MODID, "geo/thunderhead.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/thunderhead.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(MODID, "animations/thunderhead.animation.json");
    @Override
    public ResourceLocation getModelLocation(ThunderheadEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(ThunderheadEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ThunderheadEntity animatable) {
        return ANIMATION;
    }
}
