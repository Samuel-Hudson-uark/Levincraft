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
        add(Registration.BEAMBLOCK.get(), "Basalt beam");
        add(Registration.RUNEBLOCK.get(), "Amethyst rune");
        add(Registration.PYLONBLOCK.get(), "Pylon");
        add(Registration.GLASSMATRIXBLOCK.get(), "Glass matrix");
        add(Registration.GENERATORBLOCK.get(), "Lightning generator");
        //Items
        add(Registration.CRYSTALBATTERY_ITEM.get(), "Crystal battery");
        add(Registration.CRYSTALTUNER_ITEM.get(), "Crystal tuner");
        add(Registration.RAILGUNAMMO_ITEM.get(), "Railgun ammo");
        add(Registration.EXPLOSIVERAILGUNAMMO_ITEM.get(), "Explosive railgun ammo");
        add(Registration.RAILGUN_ITEM.get(), "Railgun");
        //Messages
        add("message.chargableitem", "Charge: %s/%s");
    }


}
