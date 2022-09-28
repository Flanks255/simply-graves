package com.flanks255.simplygraves.commands;

import com.flanks255.simplygraves.GraveData;
import com.flanks255.simplygraves.WSD.GraveStorage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class List {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("list")
            .executes(cs -> list(cs, ""))
                .then(Commands.argument("PlayerName", StringArgumentType.string())
                .suggests((cs, builder) -> SharedSuggestionProvider.suggest(getPlayerSuggestions(cs), builder))
                .executes(cs -> list(cs, StringArgumentType.getString(cs, "PlayerName"))));
    }

    public static Set<String> getPlayerSuggestions(CommandContext<CommandSourceStack> commandSource) {
        Set<String> list = new HashSet<>();
        commandSource.getSource().getServer().getPlayerList().getPlayers().forEach( serverPlayerEntity -> list.add(serverPlayerEntity.getName().getString()));

        return list;
    }
    public static int list(CommandContext<CommandSourceStack> ctx, String playername) {
        var storage = GraveStorage.get();
        var data = storage.getData();

        if (data.size() == 0) {
            ctx.getSource().sendSuccess(Component.literal("[ ]"), false);
            return 0;
        }

        if (Objects.equals(playername, ""))
            data.forEach((uuid, grave) -> sendGrave(ctx.getSource().getPlayer(), grave));
        else
            data.forEach((uuid, grave) -> {
                if (grave.playerName.equalsIgnoreCase(playername))
                    sendGrave(ctx.getSource().getPlayer(), grave);
            });

        return 0;
    }

    public static void sendGrave(Player player, GraveData graveData) {
        player.sendSystemMessage(Component.literal("===========================").withStyle(ChatFormatting.DARK_GRAY));
        player.sendSystemMessage(Component.literal(
                graveData.graveUUID.toString().substring(0,8) + "...\n" + graveData.playerName + "\n" + SDF.format(new Date(graveData.deathTime))
        ));
    }
}
