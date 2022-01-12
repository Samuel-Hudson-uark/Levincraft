package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.fabpharos.levincraft.Levincraft.MODID;

public class DatagenItemModels extends ItemModelProvider {
    public DatagenItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Registration.PYLONBLOCK.get().getRegistryName().getPath(), modLoc("block/pylon"));
        withExistingParent(Registration.RUNEBLOCK.get().getRegistryName().getPath(), modLoc("block/runeblock"));
        withExistingParent(Registration.BEAMBLOCK.get().getRegistryName().getPath(), modLoc("block/beam_middle"));
        //withExistingParent(Registration.GENERATORBLOCK.get().getRegistryName().getPath(), modLoc("block/generator"));
        //withExistingParent(Registration.GLASSMATRIXBLOCK.get().getRegistryName().getPath(), modLoc("block/glass_matrix"));

        singleTexture(Registration.RAILGUNAMMO_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/railgun_ammo"));
        singleTexture(Registration.EXPLOSIVERAILGUNAMMO_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/explosive_railgun_ammo"));
        singleTexture(Registration.CRYSTALBATTERY_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", mcLoc("item/amethyst_shard"));
        /*singleTexture(Registration.CRYSTALTUNER_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/crystal_tuner"));*/
    }
}
