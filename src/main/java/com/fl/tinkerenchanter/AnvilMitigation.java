package com.fl.tinkerenchanter;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Main event listener class. Its only job is to catch the anvil update event
 * and pass it along to our logic handler.
 */
@Mod.EventBusSubscriber
public class AnvilMitigation {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        EnchantmentLogic.handleAnvilUpdate(event);
    }
}
