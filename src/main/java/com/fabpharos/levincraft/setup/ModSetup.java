package com.fabpharos.levincraft.setup;

import com.fabpharos.levincraft.blocks.GeneratorTile;
import com.fabpharos.levincraft.entities.GeneratorEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {
    public static final String TAB_NAME = "levincraft";
    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.AMETHYST_SHARD);
        }
    };

    public static void init(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
        Level level = event.getEntity().level;
        if(!level.isClientSide) {
            Entity entity = event.getEntity();
            if(entity instanceof GeneratorEntity) {
                BlockEntity blockEntity = level.getBlockEntity(entity.getOnPos().above(1));
                if(blockEntity instanceof GeneratorTile) {
                    ((GeneratorTile) blockEntity).fillCounter();
                }
            }
        }
    }
}
