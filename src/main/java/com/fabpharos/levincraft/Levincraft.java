package com.fabpharos.levincraft;

import com.fabpharos.levincraft.setup.ClientSetup;
import com.fabpharos.levincraft.setup.ModSetup;
import com.fabpharos.levincraft.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(Levincraft.MODID)
public class Levincraft {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "levincraft";

    public Levincraft() {

        // Register the deferred registry
        Registration.init();
        ModSetup.setup();
        GeckoLib.initialize();

        // Register the setup method for modloading
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register 'ModSetup::init' to be called at mod setup time (server and client)
        modbus.addListener(ModSetup::init);
        //MinecraftForge.EVENT_BUS.register(new ForgeEvents());
        // Register 'ClientSetup::init' to be called at mod setup time (client only)
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(ClientSetup::init));
    }
}
