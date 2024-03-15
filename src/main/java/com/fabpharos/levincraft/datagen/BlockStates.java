package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.Levincraft;
import com.fabpharos.levincraft.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {


    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Levincraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.ALUMINUM_BLOCK.get());
        simpleBlock(Registration.ALUMINUM_RAW_BLOCK.get());
        simpleBlock(Registration.DURALUMIN_BLOCK.get());
        simpleBlock(Registration.CHARGED_AMETHYST.get());
        simpleBlock(Registration.ASH_BLOCK.get());
        simpleBlock(Registration.ALUMINUM_ORE_BLOCK.get());
    }
}

