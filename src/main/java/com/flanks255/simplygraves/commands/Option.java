package com.flanks255.simplygraves.commands;

import com.flanks255.simplygraves.WSD.PreferenceStorage;
import com.flanks255.simplygraves.config.CommonConfig;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static com.flanks255.simplygraves.commands.List.getPlayerSuggestions;

public class Option {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("option")
                .executes(Option::getStatus)

                    .then(Commands.literal("enable").requires(cs -> !CommonConfig.OPERATOR_ONLY.get() || cs.hasPermission(1))
                            .executes(cs -> setStatus(cs, cs.getSource().getPlayer(), true)))

                    .then(Commands.literal("disable").requires(cs -> !CommonConfig.OPERATOR_ONLY.get() || cs.hasPermission(1))
                            .executes(cs -> setStatus(cs, cs.getSource().getPlayer(), false)))

                    .then(Commands.literal("set").requires(cs -> cs.hasPermission(1))
                            .then(Commands.argument("Player", EntityArgument.player()).suggests((cs, builder) -> SharedSuggestionProvider.suggest(getPlayerSuggestions(cs), builder))
                            .then(Commands.argument("Option", BoolArgumentType.bool())
                                .executes(cs -> setStatus(cs, EntityArgument.getPlayer(cs, "Player"), BoolArgumentType.getBool(cs, "Option"))))));
    }
    private static int setStatus(CommandContext<CommandSourceStack> ctx, Player target, boolean status) {
        var preferenceStorage = PreferenceStorage.get();
        var prefs = preferenceStorage.getPrefs(target.getUUID());

        prefs.setGraveOption(status);
        preferenceStorage.setDirty();

        target.sendSystemMessage(Component.literal("Grave Option: " + (status?"Enabled":"Disabled")));

        return 0;
    }
    private static int getStatus(CommandContext<CommandSourceStack> ctx) {
        var preferenceStorage = PreferenceStorage.get();
        var prefs = preferenceStorage.getPrefs(ctx.getSource().getPlayer().getUUID());

        prefs.getGraveOption().ifPresentOrElse(
                b -> ctx.getSource().sendSuccess(() -> Component.literal("Grave Option: " + (b?"Enabled":"Disabled")), false),
                () -> ctx.getSource().sendSuccess(() -> Component.literal("Grave Option: Unset"), false));
        return 0;
    }
}
