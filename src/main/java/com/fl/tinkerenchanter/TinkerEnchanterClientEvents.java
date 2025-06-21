package com.fl.tinkerenchanter;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

/**
 * This class handles client-side events. The glint logic has been moved to a Mixin.
 */
// The reference to TinkerEnchanter.MODID has been replaced with the actual mod ID string.
@Mod.EventBusSubscriber(modid = "tinkerenchanter", value = Dist.CLIENT)
public class TinkerEnchanterClientEvents {
    // The enchantment glint logic is now handled by ModifiableItemMixin,
    // as it requires modifying the behavior of the Tinkers' Construct item directly.
}
