package com.fabpharos.levincraft.setup;

import com.fabpharos.levincraft.blocks.*;
import com.fabpharos.levincraft.entities.ExplosiveRailgunArrow;
import com.fabpharos.levincraft.entities.GeneratorEntity;
import com.fabpharos.levincraft.entities.RailgunArrow;
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
    public static final RegistryObject<Item> PYLONBLOCK_ITEM = ITEMS.register("pylonblock", () -> new BlockItem(PYLONBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<PylonTile>> PYLONBLOCK_TILE = TILES.register("pylonblock", () -> BlockEntityType.Builder.of(PylonTile::new, PYLONBLOCK.get()).build(null));

    public static final RegistryObject<RuneBlock> RUNEBLOCK = BLOCKS.register("runeblock", RuneBlock::new);
    public static final RegistryObject<Item> RUNEBLOCK_ITEM = ITEMS.register("runeblock", () -> new BlockItem(RUNEBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<BeamBlock> BEAMBLOCK = BLOCKS.register("beamblock", BeamBlock::new);
    public static final RegistryObject<Item> BEAMBLOCK_ITEM = ITEMS.register("beamblock", () -> new BlockItem(BEAMBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<GeneratorBlock> GENERATORBLOCK = BLOCKS.register("generatorblock", GeneratorBlock::new);
    public static final RegistryObject<Item> GENERATORBLOCK_ITEM = ITEMS.register("generatorblock", () -> new BlockItem(GENERATORBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<GeneratorTile>> GENERATORBLOCK_TILE = TILES.register("generatorblock", () -> BlockEntityType.Builder.of(GeneratorTile::new, GENERATORBLOCK.get()).build(null));

    public static final RegistryObject<GlassMatrixBlock> GLASSMATRIXBLOCK = BLOCKS.register("glassmatrixblock", GlassMatrixBlock::new);
    public static final RegistryObject<Item> GLASSMATRIXBLOCK_ITEM = ITEMS.register("glassmatrixblock", () -> new BlockItem(GLASSMATRIXBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<GlassMatrixTile>> GLASSMATRIXBLOCK_TILE = TILES.register("glassmatrixblock", () -> BlockEntityType.Builder.of(GlassMatrixTile::new, GLASSMATRIXBLOCK.get()).build(null));

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
}
