package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.fabpharos.levincraft.Levincraft.MODID;

public class DatagenItemTags extends ItemTagsProvider {
    public DatagenItemTags(DataGenerator generator, DatagenBlockTags blockTags, ExistingFileHelper existingFileHelper) {
        super(generator, blockTags, MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Tags.Items.ORES).add(Registration.BAUXITE_ORE_ITEM.get());
        tag(Tags.Items.INGOTS)
                .add(Registration.ALUMINUM_INGOT.get())
                .add(Registration.DURALUMIN_INGOT.get());
        tag(Tags.Items.NUGGETS)
                .add(Registration.ALUMINUM_NUGGET.get())
                .add(Registration.DURALUMIN_NUGGET.get());
        tag(Tags.Items.STORAGE_BLOCKS)
                .add(Registration.ALUMINUM_BLOCK_ITEM.get())
                .add(Registration.DURALUMIN_BLOCK_ITEM.get());
    }

    @Override
    public String getName() {
        return "Levincraft Tags";
    }
}
