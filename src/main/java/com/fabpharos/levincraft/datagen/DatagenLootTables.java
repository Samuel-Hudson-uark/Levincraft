package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.setup.Registration;
import net.minecraft.data.DataGenerator;

public class DatagenLootTables extends BaseLootTableProvider {
    public DatagenLootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addTables() {

        lootTables.put(Registration.PYLONBLOCK.get(), createStandardTable("pylon", Registration.PYLONBLOCK.get(), Registration.PYLONBLOCK_TILE.get()));
        lootTables.put(Registration.GENERATORBLOCK.get(), createStandardTable("generator", Registration.GENERATORBLOCK.get(), Registration.GENERATORBLOCK_TILE.get()));
        lootTables.put(Registration.GLASSMATRIXBLOCK.get(), createStandardTable("glass_matrix", Registration.GLASSMATRIXBLOCK.get(), Registration.GLASSMATRIXBLOCK_TILE.get()));
        lootTables.put(Registration.BEAMBLOCK.get(), createSimpleTable("beamblock", Registration.BEAMBLOCK.get()));
        lootTables.put(Registration.RUNEBLOCK.get(), createSimpleTable("runeblock", Registration.RUNEBLOCK.get()));

        lootTables.put(Registration.ALUMINUM_BLOCK.get(), createSimpleTable("aluminum_block", Registration.ALUMINUM_BLOCK.get()));
        lootTables.put(Registration.DURALUMIN_BLOCK.get(), createSimpleTable("duralumin_block", Registration.DURALUMIN_BLOCK.get()));

        lootTables.put(Registration.BAUXITE_ORE.get(), createSilkTouchTable("bauxite_ore", Registration.BAUXITE_ORE.get(), Registration.RAW_BAUXITE.get(), 1, 3));
    }
}
