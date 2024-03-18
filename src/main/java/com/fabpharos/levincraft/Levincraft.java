package com.fabpharos.levincraft;

import com.fabpharos.levincraft.blocks.PylonBlockRenderer;
import com.fabpharos.levincraft.datagen.DataGeneration;
import com.mojang.logging.LogUtils;
import com.mrcrayfish.guns.client.CustomGunManager;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Levincraft.MODID)
public class Levincraft {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "levincraft";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public Levincraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.init(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ClientSetup::onClientSetup);
        modEventBus.addListener(ClientSetup::registerRenderers);
        //modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(DataGeneration::generate);

        //ModTerrablender.registerBiomes();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        //event.enqueueWork(() -> {
        //    SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ModSurfaceRules.makeRules());
        //});
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSetup {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(Registration.PYLON_BLOCK.get(), RenderType.cutout());
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(Registration.PYLON_BLOCK_ENTITY.get(), PylonBlockRenderer::new);
        }
        public static final String TAB_NAME = "levincraft";

        public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(Registration.DECONFIGURATOR.get());
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> pItems) {
                super.fillItemList(pItems);
                CustomGunManager.fill(pItems);
            }
        };
    }
}
