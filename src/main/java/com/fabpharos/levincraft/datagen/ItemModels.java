package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.Levincraft;
import com.fabpharos.levincraft.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator output, ExistingFileHelper existingFileHelper) {
        super(output, Levincraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(Registration.ALUMINUM_INGOT.get());
        basicItem(Registration.ALUMINUM_NUGGET.get());
        basicItem(Registration.DURALUMIN_INGOT.get());
        basicItem(Registration.DURALUMIN_NUGGET.get());
        basicItem(Registration.DURALUMIN_RAW.get());
        basicItem(Registration.ALUMINUM_RAW.get());
        basicItem(Registration.VOID_INGOT.get());
        basicItem(Registration.VOID_NUGGET.get());
        basicItem(Registration.SOLAR_INGOT.get());

        withExistingParent(Registration.PYLON_BLOCK.getId().getPath(), modLoc("block/pylon_block"));
        withExistingParent(Registration.ALUMINUM_BLOCK.getId().getPath(), modLoc("block/aluminum_block"));
        withExistingParent(Registration.DURALUMIN_BLOCK.getId().getPath(), modLoc("block/duralumin_block"));
        withExistingParent(Registration.ALUMINUM_RAW_BLOCK.getId().getPath(), modLoc("block/raw_bauxite_block"));
        withExistingParent(Registration.ALUMINUM_ORE_BLOCK.getId().getPath(), modLoc("block/bauxite_ore"));
        withExistingParent(Registration.ASH_BLOCK.getId().getPath(), modLoc("block/ash_block"));
        withExistingParent(Registration.CHARGED_AMETHYST.getId().getPath(), modLoc("block/amethyst_charged"));
    }
}
