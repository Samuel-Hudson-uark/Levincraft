package com.fabpharos.levincraft.setup;

import com.fabpharos.levincraft.Levincraft;
import com.fabpharos.levincraft.blocks.PylonRenderer;
import com.fabpharos.levincraft.entities.GeneratorRenderer;
import com.fabpharos.levincraft.entities.ThunderheadRenderer;
import com.fabpharos.levincraft.items.RailgunItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.example.GeckoLibMod;

@Mod.EventBusSubscriber(modid = Levincraft.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        PylonRenderer.register();
        ItemProperties.register(Registration.RAILGUN_ITEM.get(), new ResourceLocation("pull"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null || !RailgunItem.isCharged(p_174635_)) {
                return 0.0F;
            } else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float) (p_174635_.getUseDuration() - p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(Registration.RAILGUN_ITEM.get(), new ResourceLocation("pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) ->
                p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ && RailgunItem.isCharged(p_174630_) ? 1.0F : 0.0F);
        ItemProperties.register(Registration.RAILGUN_ITEM.get(), new ResourceLocation("charged"), (p_174610_, p_174611_, p_174612_, p_174613_) ->
                p_174612_ != null && RailgunItem.isCharged(p_174610_) ? 1.0F : 0.0F);
        ItemProperties.register(Registration.RAILGUN_ITEM.get(), new ResourceLocation("charging"), (p_174615_, p_174616_, p_174617_, p_174618_) -> p_174617_ != null && p_174617_.isUsingItem() && p_174617_.getUseItem() == p_174615_ && !RailgunItem.isCharged(p_174615_) ? 1.0F : 0.0F);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Registration.GENERATOR_ENTITY.get(), GeneratorRenderer::new);
        if (!FMLEnvironment.production && !GeckoLibMod.DISABLE_IN_DEV) {
            event.registerEntityRenderer(Registration.THUNDERHEAD.get(), ThunderheadRenderer::new);
        }
    }
}
