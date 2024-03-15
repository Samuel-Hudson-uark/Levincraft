package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.Levincraft;
import com.fabpharos.levincraft.Registration;
import net.minecraft.data.DataGenerator;

public class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider {

    public LanguageProvider(DataGenerator output, String locale) {
        super(output, Levincraft.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        //Blocks
        add(Registration.PYLON_BLOCK.get(), "Pylon");
        add(Registration.ALUMINUM_ORE_BLOCK.get(), "Aluminum Ore");
        add(Registration.ALUMINUM_BLOCK.get(), "Block of Aluminum");
        add(Registration.ALUMINUM_RAW_BLOCK.get(), "Block of Raw Aluminum");
        add(Registration.ASH_BLOCK.get(), "Ash");
        add(Registration.CHARGED_AMETHYST.get(), "Charged Amethyst");
        add(Registration.DURALUMIN_BLOCK.get(), "Block of Duralumin");

        //Items
        add(Registration.ATTUNEMENT_WAND.get(), "Attunement Wand");
        add(Registration.ALUMINUM_RAW.get(), "Raw Aluminum");
        add(Registration.ALUMINUM_INGOT.get(), "Aluminum Ingot");
        add(Registration.ALUMINUM_NUGGET.get(), "Aluminum Nugget");
        add(Registration.DURALUMIN_RAW.get(), "Raw Duralumin");
        add(Registration.DURALUMIN_INGOT.get(), "Duralumin Ingot");
        add(Registration.DURALUMIN_NUGGET.get(), "Duralumin Nugget");
        add(Registration.SOLAR_INGOT.get(), "Solar Ingot");
        add(Registration.VOID_INGOT.get(), "Void Ingot");
        add(Registration.VOID_NUGGET.get(), "Void Nugget");
        add(Registration.CONFIGURATOR.get(), "Configurator");
        add(Registration.DECONFIGURATOR.get(), "Deconfigurator");
        add(Registration.METEOR.get(), "Meteor");
        add(Registration.BOLTCASTER.get(), "Boltcaster");
    }
}
