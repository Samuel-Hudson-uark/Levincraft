package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.fabpharos.levincraft.Levincraft.MODID;

public class DatagenBlockStates extends BlockStateProvider {
    public DatagenBlockStates(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.BAUXITE_ORE.get());
        simpleBlock(Registration.RAW_BAUXITE_BLOCK.get());
        simpleBlock(Registration.ALUMINUM_BLOCK.get());

        simpleBlock(Registration.DURALUMIN_BLOCK.get());
    }
}
