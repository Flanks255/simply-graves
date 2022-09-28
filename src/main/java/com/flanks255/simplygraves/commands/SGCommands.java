package com.flanks255.simplygraves.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class SGCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var commands = dispatcher.register(
            Commands.literal("simplygraves")
                .then(Recover.register())
                .then(List.register())
                .then(Option.register())
                .then(Failed.register())
        );

        dispatcher.register(Commands.literal("sg").redirect(commands));
    }
}
