package com.flanks255.simplygraves;

import com.flanks255.simplygraves.WSD.GraveStorage;
import com.flanks255.simplygraves.WSD.PreferenceStorage;
import com.flanks255.simplygraves.config.CommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DropEvent {
    public static void Event(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player && !event.getEntity().level().isClientSide) {
            var prefs = PreferenceStorage.get().getPrefs(player.getUUID());
            long time = System.currentTimeMillis();

            if(!prefs.getGraveOption().orElse(CommonConfig.DEFAULT_GRAVE_OPTION.get()))
                return;

            if (time - prefs.getLastGrave() < (CommonConfig.GRAVE_COOLDOWN.get() * 1000)) {
                var timeLeft = Duration.ofSeconds(CommonConfig.GRAVE_COOLDOWN.get() - ((time - prefs.getLastGrave()) / 1000));
                player.sendSystemMessage(Component.translatable("simplygraves.cooldown",
                                timeLeft.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase())
                        .withStyle(style -> style.withColor(ChatFormatting.RED)));
                return;
            }

            if (prefs.getGraveOption().isEmpty()) {
                player.sendSystemMessage(Component.translatable(CommonConfig.DEFAULT_GRAVE_OPTION.get()?"simplygraves.server_enabled":"simplygraves.server_disabled"));
                player.sendSystemMessage(Component.translatable("simplygraves.server_choice"));
                var optInLink = Component.literal("Opt-in");
                optInLink.withStyle(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sg option enable"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Enable Graves")))
                        .withColor(ChatFormatting.GREEN)
                        .withUnderlined(true));
                var optOutLink = Component.literal("Opt-out");
                optOutLink.withStyle(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sg option disable"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Disable Graves")))
                        .withColor(ChatFormatting.GREEN)
                        .withUnderlined(true));
                player.sendSystemMessage(Component.literal("[").append(optInLink).append("] - [").append(optOutLink).append("]"));
            }

            Collection<ItemEntity> drops = event.getDrops();
            if (drops.isEmpty()) //No items, don't waste a grave.
                return;

            ItemStackHandler inventory = new ItemStackHandler(drops.size());

            List<ItemEntity> toRemove = new ArrayList<>();
            drops.forEach(drop -> {
                if (!drop.getItem().is(SimplyGraves.NO_GRAVE)) {
                    ItemHandlerHelper.insertItem(inventory, drop.getItem(), false);
                    toRemove.add(drop);
                }
            });

            toRemove.forEach(drops::remove);

            UUID uuid = UUID.randomUUID();
            BlockPos pos = player.getOnPos().above();
            String name = player.getName().getString();

            GraveStorage.get().addGrave(uuid, new GraveData(
                    player.getUUID(),
                    name,
                    uuid,
                    pos,
                    player.level().dimension(),
                    time,
                    inventory,
                    true
            ));

            Level level = player.level();
            BlockPos gravePos = search(level, pos);
            boolean replaceable = valid(level, gravePos);

            boolean failedFlag = true;

            if (replaceable) {
                DeferredBlock<GraveBlock> block = SGBlocks.GRAVES.get(event.getEntity().level().random.nextInt(SGBlocks.GRAVES.size()));
                BlockState state = block.get().defaultBlockState();
                if (level.setBlock(gravePos, state, 3)) {
                    if(level.getBlockState(gravePos).hasBlockEntity() && level.getBlockEntity(gravePos) instanceof GraveEntity entity) {
                        SimplyGraves.LOGGER.info("Grave placed @ " + gravePos);
                        entity.setGrave(uuid, player.getUUID(), name, time);
                        failedFlag = false;
                        successGrave(player, uuid, gravePos, level.dimension().location().getPath());
                    }
                }
            }
            if (failedFlag)
                failedGrave(player, uuid);

            prefs.setLastGrave(time);
        }
    }

    private static void successGrave(Player player, UUID graveUUID, BlockPos pos, String dim) {
        GraveStorage.get().setSuccess(graveUUID);
        player.sendSystemMessage(Component.translatable("simplygraves.success",
                Component.literal(pos.toShortString()).withStyle(style -> style.withColor(ChatFormatting.GREEN)),
                Component.literal(dim).withStyle(style -> style.withColor(ChatFormatting.GREEN)),
                Component.literal(graveUUID.toString().substring(0,8)).withStyle(style -> style.withColor(ChatFormatting.BLUE))
        ));
    }

    private static void failedGrave(Player player, UUID graveUUID) {
        SimplyGraves.LOGGER.info("Grave for " + player.getName().getString() + " failed to place.");
        GraveStorage.get().setFailed(graveUUID);
        var recover = Component.literal("Recover");
        recover.withStyle(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sg failed"))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Recover failed grave")))
                .withColor(ChatFormatting.GREEN)
                .withUnderlined(true));
        player.sendSystemMessage(Component.translatable("simplygraves.failed"));
        player.sendSystemMessage(Component.literal("    [").append(recover).append("]"));
    }
    private static boolean valid(Level level, BlockPos pos) {
        return level.getBlockState(pos).canBeReplaced();
    }

    private static BlockHitResult checkColumn(Level level, BlockPos pos) {
        Vec3 start = Vec3.atCenterOf(pos.above(4));
        Vec3 end = Vec3.atCenterOf(pos);
        return level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, CollisionContext.empty()));

    }

    private static BlockPos search(Level level, BlockPos deathPos) {
        if (valid(level, deathPos))
            return deathPos;
        if (valid(level, deathPos.above()))
            return deathPos.above();

        var res1 = checkColumn(level, deathPos);
        if (res1.getType() == HitResult.Type.BLOCK && valid(level, res1.getBlockPos().above()))
            return res1.getBlockPos().above();

        var res2 = checkColumn(level, deathPos.north());
        if (res2.getType() == HitResult.Type.BLOCK && valid(level, res2.getBlockPos().above()))
            return res2.getBlockPos().above();

        var res3 = checkColumn(level, deathPos.south());
        if (res3.getType() == HitResult.Type.BLOCK && valid(level, res3.getBlockPos().above()))
            return res3.getBlockPos().above();

        var res4 = checkColumn(level, deathPos.east());
        if (res4.getType() == HitResult.Type.BLOCK && valid(level, res4.getBlockPos().above()))
            return res4.getBlockPos().above();

        var res5 = checkColumn(level, deathPos.west());
        if (res5.getType() == HitResult.Type.BLOCK && valid(level, res5.getBlockPos().above()))
            return res5.getBlockPos().above();

        return deathPos;
    }
}
