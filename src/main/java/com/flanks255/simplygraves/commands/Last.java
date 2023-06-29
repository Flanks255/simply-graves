package com.flanks255.simplygraves.commands;

import com.flanks255.simplygraves.WSD.GraveStorage;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Last {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("latest")
                .executes(Last::last);
    }
    public static int last(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var player = ctx.getSource().getPlayer();
        if (player == null)
            return 0;

        var lastGrave = GraveStorage.get().getLastGrave(player);

        lastGrave.ifPresentOrElse(g -> {
            player.sendSystemMessage(Component.translatable("simplygraves.latest_grave"));
            player.sendSystemMessage(Component.literal(g.dim.location().getPath() + " @ " + g.blockPos.toShortString()));
            player.sendSystemMessage(Component.literal(SDF.format(new Date(g.deathTime))));
        }, () -> player.sendSystemMessage(Component.translatable("simplygraves.no_graves")));

        return 0;
    }
}
