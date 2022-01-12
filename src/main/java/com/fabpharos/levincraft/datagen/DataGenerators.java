package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.Levincraft;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Levincraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new DatagenRecipes(generator));
            generator.addProvider(new DatagenLootTables(generator));
            DatagenBlockTags blockTags = new DatagenBlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new DatagenItemTags(generator, blockTags, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
            generator.addProvider(new DatagenBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new DatagenItemModels(generator, event.getExistingFileHelper()));
            generator.addProvider(new DatagenLanguageProvider(generator, "en_us"));
        }
    }
}
