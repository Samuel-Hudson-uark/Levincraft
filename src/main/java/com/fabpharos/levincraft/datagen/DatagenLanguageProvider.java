package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static com.fabpharos.levincraft.Levincraft.MODID;
import static com.fabpharos.levincraft.setup.ModSetup.TAB_NAME;

public class DatagenLanguageProvider extends LanguageProvider {
    public DatagenLanguageProvider(DataGenerator generator, String locale) {
        super(generator, MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + TAB_NAME, "Levincraft");
        //Blocks
        add(Registration.BEAMBLOCK.get(), "Basalt Beam");
        add(Registration.RUNEBLOCK.get(), "Amethyst Rune");
        add(Registration.PYLONBLOCK.get(), "Pylon");
        add(Registration.GLASSMATRIXBLOCK.get(), "Glass Matrix");
        add(Registration.GENERATORBLOCK.get(), "Lightning Generator");

        add(Registration.ALUMINUM_BLOCK.get(), "Block of Aluminum");
        add(Registration.RAW_BAUXITE_BLOCK.get(), "Block of Raw Bauxite");
        add(Registration.BAUXITE_ORE.get(), "Bauxite Ore");
        //Items
        add(Registration.CRYSTALBATTERY_ITEM.get(), "Crystal Battery");
        add(Registration.CRYSTALTUNER_ITEM.get(), "Crystal Tuner");
        add(Registration.RAILGUNAMMO_ITEM.get(), "Railgun Ammo");
        add(Registration.EXPLOSIVERAILGUNAMMO_ITEM.get(), "Explosive Railgun Ammo");
        add(Registration.RAILGUN_ITEM.get(), "Railgun");

        add(Registration.ALUMINUM_INGOT.get(), "Aluminum Ingot");
        add(Registration.ALUMINUM_NUGGET.get(), "Aluminum Nugget");
        add(Registration.RAW_BAUXITE.get(), "Raw Bauxite");
        //Messages
        add("message.chargableitem", "Charge: %s/%s");
    }


}
