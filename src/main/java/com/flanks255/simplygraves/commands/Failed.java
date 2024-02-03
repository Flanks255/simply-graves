package com.flanks255.simplygraves.commands;

import com.flanks255.simplygraves.WSD.GraveStorage;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;


public class Failed {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("failed")
                .executes(Failed::failed);
    }

    public static int failed(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var storage = GraveStorage.get();
        var player = ctx.getSource().getPlayerOrException();

        storage.getFailedGrave(player).ifPresent(grave -> {
            ItemStackHandler inv = grave.inventory;
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemHandlerHelper.giveItemToPlayer(player, inv.getStackInSlot(i));
            }
            storage.removeGrave(grave.graveUUID);
        });



        return 0;
    }
}
