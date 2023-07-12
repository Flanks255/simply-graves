package com.flanks255.simplygraves.commands;

import com.flanks255.simplygraves.SimplyGraves;
import com.flanks255.simplygraves.WSD.GraveStorage;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.UUID;

import static com.flanks255.simplygraves.commands.List.getPlayerSuggestions;

public class Recover {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("recover")
                .requires(cs -> cs.hasPermission(1))
                .then(Commands.argument("UUID", UuidArgument.uuid()).suggests(((context, builder) -> SharedSuggestionProvider.suggest(SimplyGraves.getUUIDSuggestions(), builder)))
                        .then(Commands.argument("Player", EntityArgument.player()).suggests((cs, builder) -> SharedSuggestionProvider.suggest(getPlayerSuggestions(cs), builder))
                        .executes(cs -> recover(cs, UuidArgument.getUuid(cs, "UUID"), EntityArgument.getPlayer(cs, "Player")))));
    }
    public static int recover(CommandContext<CommandSourceStack> ctx, UUID uuid, Player target) throws CommandSyntaxException {
        var storage = GraveStorage.get();

        storage.getGrave(uuid).ifPresent(grave -> {
            ItemStackHandler inv = grave.inventory;
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemHandlerHelper.giveItemToPlayer(target, inv.getStackInSlot(i));
            }
            storage.removeGrave(uuid);
            ctx.getSource().sendSuccess(() -> Component.literal("Grave recovered to ").append(target.getDisplayName()), false);
        });



        return 0;
    }
}
