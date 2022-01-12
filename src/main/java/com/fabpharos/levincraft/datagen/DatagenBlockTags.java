package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.fabpharos.levincraft.Levincraft.MODID;

public class DatagenBlockTags extends BlockTagsProvider {
    public DatagenBlockTags(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.PYLONBLOCK.get())
                .add(Registration.BEAMBLOCK.get())
                .add(Registration.GENERATORBLOCK.get())
                .add(Registration.GLASSMATRIXBLOCK.get())
                .add(Registration.RUNEBLOCK.get());
        tag(BlockTags.CRYSTAL_SOUND_BLOCKS).add(Registration.RUNEBLOCK.get());
    }

    @Override
    public String getName() {
        return "Levincraft Tags";
    }
}
