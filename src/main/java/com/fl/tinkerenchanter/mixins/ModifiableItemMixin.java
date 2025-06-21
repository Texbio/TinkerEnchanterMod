package com.fl.tinkerenchanter.mixins;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

/**
 * This Mixin modifies the ModifiableItem class from Tinkers' Construct.
 * It injects code into the isFoil() method (the method that determines the enchantment glint).
 */
@Mixin(ModifiableItem.class)
public abstract class ModifiableItemMixin {

    /**
     * This injected method runs at the beginning of the original isFoil method.
     * It checks if the tool has any vanilla enchantments. If it does, it immediately
     * sets the return value to 'true' (which shows the glint) and cancels the original method.
     *
     * @param stack The ItemStack being checked.
     * @param cir The Mixin callback info, used to set the return value.
     */
    @Inject(method = "isFoil", at = @At("HEAD"), cancellable = true, remap = false)
    private void onIsFoil(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        // --- THIS IS THE DEBUGGING LINE ---
        // It will print a message to your console every time you hover over a Tinkers' tool.
        System.out.println("[TinkerEnchanter] Mixin is running for item: " + stack.getItem().toString());

        // Corrected for 1.19.2: Check if the enchantment map is not empty.
        // If the Tinkers' tool has any enchantments from our mod, force the glint.
        if (!EnchantmentHelper.getEnchantments(stack).isEmpty()) {
            cir.setReturnValue(true);
        }
    }
}
