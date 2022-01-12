package com.fabpharos.levincraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.fabpharos.levincraft.Levincraft.MODID;

public class DatagenItemTags extends ItemTagsProvider {
    public DatagenItemTags(DataGenerator generator, DatagenBlockTags blockTags, ExistingFileHelper existingFileHelper) {
        super(generator, blockTags, MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {

    }

    @Override
    public String getName() {
        return "Levincraft Tags";
    }
}
