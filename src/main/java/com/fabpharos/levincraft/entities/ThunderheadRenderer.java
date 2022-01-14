package com.fabpharos.levincraft.entities;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ThunderheadRenderer extends GeoEntityRenderer<ThunderheadEntity> {

    public ThunderheadRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ThunderheadModel());
        this.shadowRadius = 0.5f;
    }
}
