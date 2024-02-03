package com.flanks255.simplygraves;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.items.ItemHandlerHelper;

public class MiscUtils {

    public static boolean isEquipable(ItemStack stack) {
        return LivingEntity.getEquipmentSlotForItem(stack).isArmor();
    }
    public static boolean isCursed(ItemStack stack) {
        if (!stack.isEnchanted())
            return false;
        return stack.getAllEnchantments().keySet().stream().anyMatch(Enchantment::isCurse);
    }

    public static void giveItem(Player player,ItemStack stack) {
        if (isEquipable(stack) && !isCursed(stack)) {


            return;
        }
        ItemHandlerHelper.giveItemToPlayer(player, stack);
    }

    public static void equipItem(Player player, ItemStack stack) {
        var slot = Player.getEquipmentSlotForItem(stack);
        player.setItemSlot(slot, stack);
    }
}
