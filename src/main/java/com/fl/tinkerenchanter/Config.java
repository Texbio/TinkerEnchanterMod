package com.fl.tinkerenchanter;

// IMPORTANT: This file is part of the Forge MDK template.
// You do not need to modify it for this mod to work, but it should not be deleted.
// It is used for creating server and client configuration files.

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = "tinkerenchanter", bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static class Common {
        // You would define config values here. For example:
        // public final ForgeConfigSpec.BooleanValue myBooleanConfig;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("common");
            // myBooleanConfig = builder.comment("An example boolean config value.").define("myBooleanConfig", true);
            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event) {
        // This method is called when a config file is loaded.
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading event) {
        // This method is called when a config file is reloaded.
    }
}
