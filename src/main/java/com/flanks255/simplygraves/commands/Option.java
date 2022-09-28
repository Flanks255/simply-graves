package com.flanks255.simplygraves.commands;

import com.flanks255.simplygraves.WSD.PreferenceStorage;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class Option {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("option")
                .executes(Option::getStatus)
                .then(Commands.literal("enable").executes(cs -> setStatus(cs, true)))
                .then(Commands.literal("disable").executes(cs -> setStatus(cs, false)));
    }

    private static int setStatus(CommandContext<CommandSourceStack> ctx, boolean status) {
        var preferenceStorage = PreferenceStorage.get();
        var prefs = preferenceStorage.getPrefs(ctx.getSource().getPlayer().getUUID());

        prefs.setGraveOption(status);
        preferenceStorage.setDirty();
        ctx.getSource().sendSuccess(Component.literal("Grave Option: " + (status?"Enabled":"Disabled")), false);

        return 0;
    }
    private static int getStatus(CommandContext<CommandSourceStack> ctx) {
        var preferenceStorage = PreferenceStorage.get();
        var prefs = preferenceStorage.getPrefs(ctx.getSource().getPlayer().getUUID());

        prefs.getGraveOption().ifPresentOrElse(
                b -> ctx.getSource().sendSuccess(Component.literal("Grave Option: " + (b?"Enabled":"Disabled")), false),
                () -> ctx.getSource().sendSuccess(Component.literal("Grave Option: Unset"), false));
        return 0;
    }
}
