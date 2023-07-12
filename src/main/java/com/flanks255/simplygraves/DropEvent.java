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
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.UUID;

public class DropEvent {
    public static void Event(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player && !event.getEntity().level().isClientSide) {
            var pref = PreferenceStorage.get().getPrefs(player.getUUID()).getGraveOption();
            if(!pref.orElse(CommonConfig.DEFAULT_GRAVE_OPTION.get()))
                return;

            if (pref.isEmpty()) {
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
            drops.forEach(drop -> ItemHandlerHelper.insertItem(inventory, drop.getItem(), false));
            UUID uuid = UUID.randomUUID();
            BlockPos pos = player.getOnPos().above();
            long time = System.currentTimeMillis();
            String name = player.getName().getString();

            GraveStorage.get().addGrave(uuid, new GraveData(
                    player.getUUID(),
                    name,
                    uuid,
                    pos,
                    player.level().dimension(),
                    time,
                    inventory,
                    false
            ));

            Level level = player.level();
            BlockPos gravePos = search(level, pos);
            boolean replaceable = valid(level, gravePos);
            if (replaceable) {
                RegistryObject<GraveBlock> block = SGBlocks.GRAVES.get(event.getEntity().level().random.nextInt(SGBlocks.GRAVES.size()));
                BlockState state = block.get().defaultBlockState();
                if (level.setBlock(gravePos, state, 3)) {
                    if(level.getBlockState(gravePos).hasBlockEntity() && level.getBlockEntity(gravePos) instanceof GraveEntity entity) {
                        SimplyGraves.LOGGER.info("Grave placed @ " + gravePos);
                        successGrave(player, uuid, gravePos, level.dimension().location().getPath());
                        entity.setGrave(uuid, player.getUUID(), name, time);
                    }
                    else {
                        failedGrave(player, uuid);
                    }
                }
                else
                    failedGrave(player, uuid);
            }
            else
                failedGrave(player, uuid);

            event.setCanceled(true);
        }
    }

    private static void successGrave(Player player, UUID graveUUID, BlockPos pos, String dim) {
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
        return level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));

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
