package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.Levincraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(DataGenerator packOutput, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, blockTags, Levincraft.MODID, helper);
    }

    @Override
    protected void addTags() {
    }
}
