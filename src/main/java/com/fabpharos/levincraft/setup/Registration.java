package com.fabpharos.levincraft.setup;

import com.fabpharos.levincraft.blocks.*;
import com.fabpharos.levincraft.entities.ExplosiveRailgunArrow;
import com.fabpharos.levincraft.entities.GeneratorEntity;
import com.fabpharos.levincraft.entities.RailgunArrow;
import com.fabpharos.levincraft.entities.ThunderheadEntity;
import com.fabpharos.levincraft.items.ChargableItem;
import com.fabpharos.levincraft.items.CrystalTuner;
import com.fabpharos.levincraft.items.RailgunItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.fabpharos.levincraft.Levincraft.MODID;

public class Registration {
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILES.register(bus);
        ENTITIES.register(bus);
    }

    //BLOCKS
    public static final RegistryObject<PylonBlock> PYLONBLOCK = BLOCKS.register("pylonblock", PylonBlock::new);
    public static final RegistryObject<Item> PYLONBLOCK_ITEM = fromBlock(PYLONBLOCK);
    public static final RegistryObject<BlockEntityType<PylonTile>> PYLONBLOCK_TILE = TILES.register("pylonblock", () -> BlockEntityType.Builder.of(PylonTile::new, PYLONBLOCK.get()).build(null));

    public static final RegistryObject<RuneBlock> RUNEBLOCK = BLOCKS.register("runeblock", RuneBlock::new);
    public static final RegistryObject<Item> RUNEBLOCK_ITEM = fromBlock(RUNEBLOCK);

    public static final RegistryObject<BeamBlock> BEAMBLOCK = BLOCKS.register("beamblock", BeamBlock::new);
    public static final RegistryObject<Item> BEAMBLOCK_ITEM = fromBlock(BEAMBLOCK);

    public static final RegistryObject<GeneratorBlock> GENERATORBLOCK = BLOCKS.register("generatorblock", GeneratorBlock::new);
    public static final RegistryObject<Item> GENERATORBLOCK_ITEM = fromBlock(GENERATORBLOCK);
    public static final RegistryObject<BlockEntityType<GeneratorTile>> GENERATORBLOCK_TILE = TILES.register("generatorblock", () -> BlockEntityType.Builder.of(GeneratorTile::new, GENERATORBLOCK.get()).build(null));

    public static final RegistryObject<GlassMatrixBlock> GLASSMATRIXBLOCK = BLOCKS.register("glassmatrixblock", GlassMatrixBlock::new);
    public static final RegistryObject<Item> GLASSMATRIXBLOCK_ITEM = fromBlock(GLASSMATRIXBLOCK);
    public static final RegistryObject<BlockEntityType<GlassMatrixTile>> GLASSMATRIXBLOCK_TILE = TILES.register("glassmatrixblock", () -> BlockEntityType.Builder.of(GlassMatrixTile::new, GLASSMATRIXBLOCK.get()).build(null));

    //Aluminum
    public static final BlockBehaviour.Properties BAUXITE_PROPERTIES =
            BlockBehaviour.Properties.of(Material.STONE).strength(2f).requiresCorrectToolForDrops();

    public static final RegistryObject<Block> BAUXITE_ORE = BLOCKS.register("bauxite_ore", () ->
            new Block(BAUXITE_PROPERTIES));
    public static final RegistryObject<Item> BAUXITE_ORE_ITEM = fromBlock(BAUXITE_ORE);

    public static final RegistryObject<Block> ALUMINUM_BLOCK = BLOCKS.register("aluminum_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.METAL).strength(2f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Item> ALUMINUM_BLOCK_ITEM = fromBlock(ALUMINUM_BLOCK);

    public static final RegistryObject<Block> RAW_BAUXITE_BLOCK = BLOCKS.register("raw_bauxite_block", () ->
            new Block(BAUXITE_PROPERTIES));
    public static final RegistryObject<Item> RAW_BAUXITE_BLOCK_ITEM = fromBlock(RAW_BAUXITE_BLOCK);

    public static final RegistryObject<Item> RAW_BAUXITE = ITEMS.register("raw_bauxite", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> ALUMINUM_NUGGET = ITEMS.register("aluminum_nugget", () -> new Item(ITEM_PROPERTIES));

    //DURALUMIN

    public static final RegistryObject<Block> DURALUMIN_BLOCK = BLOCKS.register("duralumin_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.METAL).strength(4f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Item> DURALUMIN_BLOCK_ITEM = fromBlock(DURALUMIN_BLOCK);

    public static final RegistryObject<Item> DURALUMIN_BLEND = ITEMS.register("duralumin_blend", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> DURALUMIN_INGOT = ITEMS.register("duralumin_ingot", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> DURALUMIN_NUGGET = ITEMS.register("duralumin_nugget", () -> new Item(ITEM_PROPERTIES));

    //ITEMS
    public static final RegistryObject<Item> RAILGUN_ITEM = ITEMS.register("railgun", RailgunItem::new);
    public static final RegistryObject<Item> RAILGUNAMMO_ITEM = ITEMS.register("railgunammo", () ->
            new ArrowItem(ITEM_PROPERTIES) {
                @Override
                public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
                    return new RailgunArrow(pLevel, pShooter);
                }
            });
    public static final RegistryObject<Item> EXPLOSIVERAILGUNAMMO_ITEM = ITEMS.register("explosiverailgunammo", () ->
            new ArrowItem(ITEM_PROPERTIES) {
                @Override
                public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
                    return new ExplosiveRailgunArrow(pLevel, pShooter);
                }
            });
    public static final RegistryObject<Item> CRYSTALTUNER_ITEM = ITEMS.register("crystaltuner", CrystalTuner::new);
    public static final RegistryObject<Item> CRYSTALBATTERY_ITEM = ITEMS.register("crystalbattery", () ->
            new ChargableItem(() -> 60000, () -> 200, ITEM_PROPERTIES));


    //ENTITIES
    public static final RegistryObject<EntityType<GeneratorEntity>> GENERATOR_ENTITY = ENTITIES.register("generator", () -> EntityType.Builder.of(GeneratorEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(false)
            .fireImmune()
            .build("generator"));

    public static final RegistryObject<EntityType<ThunderheadEntity>> THUNDERHEAD = ENTITIES.register("thunderhead", () ->
            EntityType.Builder.of(ThunderheadEntity::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(70)
                    .setShouldReceiveVelocityUpdates(false)
                    .build("thunderhead"));

    public static final RegistryObject<Item> THUNDERHEAD_EGG = ITEMS.register("thunderhead", () ->
            new ForgeSpawnEggItem(THUNDERHEAD, 0x7a7a7a, 0x731568, ITEM_PROPERTIES));

    // Conveniance function: Take a RegistryObject<Block> and make a corresponding RegistryObject<Item> from it
    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }
}
