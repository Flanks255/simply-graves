package com.flanks255.simplygraves;

import com.flanks255.simplygraves.WSD.GraveStorage;
import com.flanks255.simplygraves.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class GraveBlock extends Block implements EntityBlock {
    public final Grave graveType;
    public GraveBlock(Grave graveIn) {
        super(BlockBehaviour.Properties.of()
                .forceSolidOn()
                .strength(200.0F, 3600000.0F)
                .pushReaction(PushReaction.BLOCK));
        graveType = graveIn;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState) {
        return new GraveEntity(pPos, pState);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState pState, @Nonnull BlockGetter pLevel, @Nonnull BlockPos pPos, @Nonnull CollisionContext pContext) {
        return graveType.shape;
    }

    @Override
    public @NotNull InteractionResult use(@Nonnull BlockState pState, Level pLevel, @Nonnull BlockPos pPos, @Nonnull Player pPlayer, @Nonnull InteractionHand pHand, @Nonnull BlockHitResult pHit) {
        if (!pLevel.isClientSide() && !pPlayer.isCrouching() && pHand == InteractionHand.MAIN_HAND && pLevel.getBlockState(pPos).hasBlockEntity() && pLevel.getBlockEntity(pPos) instanceof GraveEntity entity) {
            UUID playerUUID = pPlayer.getUUID();
            entity.getUUID().ifPresent(uuid -> {
                var storage = GraveStorage.get();
                    if (storage.getGrave(uuid).isEmpty()) {
                        storage.removeGrave(uuid);
                        pLevel.removeBlockEntity(pPos);
                        pLevel.removeBlock(pPos, false);
                        pLevel.levelEvent(2001, pPos, Block.getId(pState));
                    }
                    storage.getGrave(uuid).ifPresent(graveData -> {
                        if (!playerUUID.equals(graveData.playerUUID) && (graveData.deathTime + (CommonConfig.DELAY_TO_PUBLIC.get()*1000)) > System.currentTimeMillis()) {//Not yo grave
                            pPlayer.sendSystemMessage(Component.translatable("simplygraves.not_yours", graveData.playerName));
                            return;
                        }
                    var inv = graveData.inventory;
                    for (int i = 0; i < inv.getSlots(); i++){
                        var stack = inv.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            ItemHandlerHelper.giveItemToPlayer(pPlayer, stack);
                        }
                    }
                    storage.removeGrave(uuid);
                    pLevel.removeBlockEntity(pPos);
                    pLevel.removeBlock(pPos, false);
                    pLevel.levelEvent(2001, pPos, Block.getId(pState));
                });
            });
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        //super.onBlockExploded(state, level, pos, explosion);
    }
}
