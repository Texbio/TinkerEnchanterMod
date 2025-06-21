package com.fl.tinkerenchanter;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("tinkerenchanter")
public class TinkerEnchanterMod {

    // Directly reference the mod id so that it's consistent everywhere.
    public static final String MOD_ID = "tinkerenchanter";
    // Create a logger instance for our mod. This is what was missing.
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public TinkerEnchanterMod() {
        // Register our event handler to the event bus
        MinecraftForge.EVENT_BUS.register(new AnvilMitigation());

        LOGGER.info("TinkerEnchanterMod has been loaded!");
    }
}
