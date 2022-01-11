package com.fabpharos.levincraft.setup;

import com.fabpharos.levincraft.blocks.PylonBlock;
import com.fabpharos.levincraft.blocks.PylonTile;
import com.fabpharos.levincraft.blocks.RuneBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.fabpharos.levincraft.Levincraft.MODID;

public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    public static final RegistryObject<PylonBlock> PYLONBLOCK = BLOCKS.register("pylonblock", PylonBlock::new);
    public static final RegistryObject<Item> PYLONBLOCK_ITEM = ITEMS.register("pylonblock", () -> new BlockItem(PYLONBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<PylonTile>> PYLONBLOCK_TILE = TILES.register("pylonblock", () -> BlockEntityType.Builder.of(PylonTile::new, PYLONBLOCK.get()).build(null));

    public static final RegistryObject<RuneBlock> RUNEBLOCK = BLOCKS.register("runeblock", RuneBlock::new);
    public static final RegistryObject<Item> RUNEBLOCK_ITEM = ITEMS.register("runeblock", () -> new BlockItem(RUNEBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));
}
