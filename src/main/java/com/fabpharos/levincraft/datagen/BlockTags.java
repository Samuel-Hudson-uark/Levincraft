package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.Levincraft;
import com.fabpharos.levincraft.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator output, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Levincraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.PYLON_BLOCK.get(),
                        Registration.ALUMINUM_BLOCK.get(),
                        Registration.ALUMINUM_ORE_BLOCK.get(),
                        Registration.ALUMINUM_RAW_BLOCK.get());
        tag(net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL)
                .add(Registration.ALUMINUM_BLOCK.get(),
                        Registration.ALUMINUM_ORE_BLOCK.get(),
                        Registration.ALUMINUM_RAW_BLOCK.get());
     }
}
