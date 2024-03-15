package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.Registration;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(Registration.PYLON_BLOCK.get(), createStandardTable("pylon_block", Registration.PYLON_BLOCK.get(), Registration.PYLON_BLOCK_ENTITY.get()));
    }

}
