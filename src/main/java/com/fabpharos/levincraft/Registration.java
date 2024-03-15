package com.fabpharos.levincraft;

import com.fabpharos.levincraft.blocks.PylonBlock;
import com.fabpharos.levincraft.blocks.PylonBlockEntity;
import com.fabpharos.levincraft.items.Configurator;
import com.fabpharos.levincraft.items.LightningGunItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {

    //Deferred Registers
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Levincraft.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Levincraft.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Levincraft.MODID);
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(Levincraft.ClientSetup.ITEM_GROUP);

    //Items
    public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> ALUMINUM_NUGGET = ITEMS.register("aluminum_nugget", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> DURALUMIN_INGOT = ITEMS.register("duralumin_ingot", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> DURALUMIN_NUGGET = ITEMS.register("duralumin_nugget", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> DURALUMIN_RAW = ITEMS.register("duralumin_blend", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> ALUMINUM_RAW = ITEMS.register("raw_bauxite", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> SOLAR_INGOT = ITEMS.register("solar_ingot", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> VOID_INGOT = ITEMS.register("void_ingot", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> VOID_NUGGET = ITEMS.register("void_nugget", () -> new Item(ITEM_PROPERTIES));

    //Tools
    public static final RegistryObject<Item> CONFIGURATOR = ITEMS.register("configurator", Configurator::new);
    public static final RegistryObject<Item> DECONFIGURATOR = ITEMS.register("deconfigurator", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> BOLTCASTER = ITEMS.register("boltcaster", () -> new LightningGunItem(ITEM_PROPERTIES));
    public static final RegistryObject<Item> METEOR = ITEMS.register("meteor", () -> new LightningGunItem(ITEM_PROPERTIES));


    //Blocks
    public static final RegistryObject<Block> ALUMINUM_BLOCK = BLOCKS.register("aluminum_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)));
    public static final RegistryObject<Item> ALUMINUM_BLOCK_ITEM = ITEMS.register("aluminum_block", () -> new BlockItem(ALUMINUM_BLOCK.get(), ITEM_PROPERTIES));
    public static final RegistryObject<Block> CHARGED_AMETHYST = BLOCKS.register("amethyst_charged", () -> new Block(BlockBehaviour.Properties.of(Material.AMETHYST).lightLevel(BlockState -> 15)));
    public static final RegistryObject<Item> CHARGED_AMETHYST_ITEM = ITEMS.register("amethyst_charged", () -> new BlockItem(CHARGED_AMETHYST.get(), ITEM_PROPERTIES));
    public static final RegistryObject<Block> ASH_BLOCK = BLOCKS.register("ash_block", () -> new Block(BlockBehaviour.Properties.of(Material.GRASS)));
    public static final RegistryObject<Item> ASH_BLOCK_ITEM = ITEMS.register("ash_block", () -> new BlockItem(ASH_BLOCK.get(), ITEM_PROPERTIES));
    public static final RegistryObject<Block> DURALUMIN_BLOCK = BLOCKS.register("duralumin_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)));
    public static final RegistryObject<Item> DURALUMIN_BLOCK_ITEM = ITEMS.register("duralumin_block", () -> new BlockItem(DURALUMIN_BLOCK.get(), ITEM_PROPERTIES));
    public static final RegistryObject<Block> ALUMINUM_RAW_BLOCK = BLOCKS.register("raw_bauxite_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)));
    public static final RegistryObject<Item> ALUMINUM_RAW_BLOCK_ITEM = ITEMS.register("raw_bauxite_block", () -> new BlockItem(ALUMINUM_RAW_BLOCK.get(), ITEM_PROPERTIES));
    public static final RegistryObject<Block> ALUMINUM_ORE_BLOCK = BLOCKS.register("bauxite_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Item> ALUMINUM_ORE_BLOCK_ITEM = ITEMS.register("bauxite_ore", () -> new BlockItem(ALUMINUM_ORE_BLOCK.get(), ITEM_PROPERTIES));

    public static final RegistryObject<PylonBlock> PYLON_BLOCK = BLOCKS.register("pylon_block", PylonBlock::new);
    public static final RegistryObject<Item> PYLON_BLOCK_ITEM = ITEMS.register("pylon_block", () -> new BlockItem(PYLON_BLOCK.get(), ITEM_PROPERTIES));
    public static final RegistryObject<BlockEntityType<PylonBlockEntity>> PYLON_BLOCK_ENTITY = BLOCK_ENTITIES.register("pylon_block",
            () -> BlockEntityType.Builder.of(PylonBlockEntity::new, PYLON_BLOCK.get()).build(null));

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
