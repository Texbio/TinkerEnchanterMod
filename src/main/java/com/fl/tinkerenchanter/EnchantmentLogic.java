package com.fl.tinkerenchanter;

import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AnvilUpdateEvent;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableCrossbowItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all the logic for checking tool types, handling enchantment
 * conflicts, and calculating XP costs.
 */
public class EnchantmentLogic {

    public static void handleAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack tool = event.getLeft();
        ItemStack book = event.getRight();

        if (!tool.is(TinkerTags.Items.MODIFIABLE) || !(book.getItem() instanceof EnchantedBookItem)) {
            return;
        }

        Map<Enchantment, Integer> bookEnchantments = EnchantmentHelper.getEnchantments(book);
        if (bookEnchantments.isEmpty()) {
            return;
        }

        Map<Enchantment, Integer> outputEnchants = new HashMap<>(EnchantmentHelper.getEnchantments(tool));
        int totalCost = 0;
        boolean appliedAny = false;

        for (Map.Entry<Enchantment, Integer> bookEntry : bookEnchantments.entrySet()) {
            Enchantment enchantment = bookEntry.getKey();
            int bookLevel = bookEntry.getValue();

            if (!canEnchantmentApplyToTool(enchantment, tool)) {
                continue;
            }

            boolean isCompatible = true;
            for (Enchantment existingEnchant : outputEnchants.keySet()) {
                if (enchantment != existingEnchant && !enchantment.isCompatibleWith(existingEnchant)) {
                    isCompatible = false;
                    break;
                }
            }
            if (!isCompatible) continue;

            int currentLevel = outputEnchants.getOrDefault(enchantment, 0);
            int maxLevel = enchantment.getMaxLevel(); // Used ONLY for the combination rule.
            int finalLevel;

            // By default, the final level is the higher of the tool's level and the book's level.
            // This ensures applying a higher-level book always works as expected.
            finalLevel = Math.max(currentLevel, bookLevel);

            // Special case: If the levels are identical AND they are below the vanilla max level, they combine.
            // e.g., Efficiency IV + Efficiency IV = Efficiency V. This does not apply to Sharpness 10 + Sharpness 10.
            if (currentLevel == bookLevel && currentLevel < maxLevel) {
                finalLevel = currentLevel + 1;
            }

            // Allow the operation to proceed as long as the enchantments are compatible.
            outputEnchants.put(enchantment, finalLevel);
            totalCost += getRarityWeight(enchantment) * bookLevel;
            appliedAny = true;
        }

        if (!appliedAny) {
            return;
        }

        ItemStack output = tool.copy();
        EnchantmentHelper.setEnchantments(outputEnchants, output);

        totalCost += tool.getBaseRepairCost() + book.getBaseRepairCost();
        if (totalCost >= 40 && !event.getPlayer().getAbilities().instabuild) {
            totalCost = 39;
        }

        event.setOutput(output);
        event.setCost(totalCost);
        event.setMaterialCost(1);
    }

    /**
     * Checks if an enchantment's category is appropriate for the given tool using a hybrid
     * of Tinkers' Construct tags for broad categories and class checks for specific types.
     */
    private static boolean canEnchantmentApplyToTool(Enchantment enchantment, ItemStack tool) {
        EnchantmentCategory category = enchantment.category;

        if (category == EnchantmentCategory.BREAKABLE || category == EnchantmentCategory.VANISHABLE) return true;

        if (category == EnchantmentCategory.WEAPON && (tool.is(TinkerTags.Items.MELEE) || tool.is(TinkerTags.Items.HARVEST))) return true;
        if (category == EnchantmentCategory.DIGGER && tool.is(TinkerTags.Items.HARVEST)) return true;

        if (category == EnchantmentCategory.BOW && tool.getItem() instanceof ModifiableBowItem) return true;
        if (category == EnchantmentCategory.CROSSBOW && tool.getItem() instanceof ModifiableCrossbowItem) return true;

        if (tool.getItem() instanceof ModifiableArmorItem armor) {
            switch(armor.getSlot()) {
                case HEAD:
                    return category == EnchantmentCategory.ARMOR_HEAD || category == EnchantmentCategory.ARMOR || category == EnchantmentCategory.WEAPON;
                case CHEST:
                    return category == EnchantmentCategory.ARMOR_CHEST || category == EnchantmentCategory.ARMOR || category == EnchantmentCategory.WEARABLE;
                case LEGS:
                    return category == EnchantmentCategory.ARMOR_LEGS || category == EnchantmentCategory.ARMOR || category == EnchantmentCategory.WEARABLE;
                case FEET:
                    return category == EnchantmentCategory.ARMOR_FEET || category == EnchantmentCategory.ARMOR || category == EnchantmentCategory.WEARABLE;
            }
        }

        if (tool.is(Tags.Items.TOOLS_SHIELDS) && enchantment.canApplyAtEnchantingTable(new ItemStack(net.minecraft.world.item.Items.SHIELD))) return true;

        return false;
    }

    private static int getRarityWeight(Enchantment enchantment) {
        switch (enchantment.getRarity()) {
            case COMMON: return 1;
            case UNCOMMON: return 2;
            case RARE: return 4;
            case VERY_RARE: return 8;
            default: return 1;
        }
    }
}
