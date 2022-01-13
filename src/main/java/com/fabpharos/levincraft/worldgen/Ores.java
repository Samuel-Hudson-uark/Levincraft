package com.fabpharos.levincraft.worldgen;

import com.fabpharos.levincraft.setup.Registration;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class Ores {
    public static final int BAUXITE_VEINSIZE = 16;
    public static final int BAUXITE_AMOUNT = 6;

    public static PlacedFeature BAUXITE_OREGEN;

    public static void registerConfiguredFeatures() {
        OreConfiguration bauxiteConfig = new OreConfiguration(
                OreFeatures.STONE_ORE_REPLACEABLES,
                Registration.BAUXITE_ORE.get().defaultBlockState(),
                BAUXITE_VEINSIZE);
        BAUXITE_OREGEN = registerPlacedFeature("bauxite_ore",
                Feature.ORE.configured(bauxiteConfig),
                CountPlacement.of(BAUXITE_AMOUNT),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(96)));
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> PlacedFeature registerPlacedFeature(String registryName,
                                                                                                              ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
        PlacedFeature placed = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(registryName), feature)
                .placed(placementModifiers);
        return PlacementUtils.register(registryName, placed);
    }

    public static void onBiomeLoadEvent(BiomeLoadingEvent event) {
        if(event.getCategory() != Biome.BiomeCategory.NETHER || event.getCategory() != Biome.BiomeCategory.THEEND) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, BAUXITE_OREGEN);
        }
    }
}
