package com.flanks255.simplygraves;

import com.flanks255.simplygraves.WSD.GraveStorage;
import com.flanks255.simplygraves.config.CommonConfig;
import com.mojang.math.OctahedralGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class GraveBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public final Grave graveType;
    public final VoxelShape SHAPE_NORTH;
    public final VoxelShape SHAPE_EAST;
    public final VoxelShape SHAPE_SOUTH;
    public final VoxelShape SHAPE_WEST;

    public GraveBlock(Grave graveIn, Identifier id) {
        super(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, id))
                .forceSolidOn()
                .strength(200.0F, 3600000.0F)
                .pushReaction(PushReaction.BLOCK));
        graveType = graveIn;
        registerDefaultState(getStateDefinition().any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(BlockStateProperties.WATERLOGGED, false));
        SHAPE_NORTH = graveIn.shape;
        SHAPE_EAST = Shapes.rotate(graveIn.shape, OctahedralGroup.BLOCK_ROT_Y_90);
        SHAPE_SOUTH = Shapes.rotate(graveIn.shape, OctahedralGroup.BLOCK_ROT_Y_180);
        SHAPE_WEST = Shapes.rotate(graveIn.shape, OctahedralGroup.BLOCK_ROT_Y_270);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState) {
        return new GraveEntity(pPos, pState);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState pState, @Nonnull BlockGetter pLevel, @Nonnull BlockPos pPos, @Nonnull CollisionContext pContext) {
        Direction facing = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return switch (facing) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@Nonnull BlockState pState, Level pLevel, @Nonnull BlockPos pPos, @Nonnull Player pPlayer, @Nonnull BlockHitResult pHit) {
        if (!pLevel.isClientSide() && !pPlayer.isCrouching() && pPlayer.getUsedItemHand() == InteractionHand.MAIN_HAND && pLevel.getBlockState(pPos).hasBlockEntity() && pLevel.getBlockEntity(pPos) instanceof GraveEntity entity) {
            UUID playerUUID = pPlayer.getUUID();
            entity.getUUID().ifPresentOrElse(uuid -> {
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
                    for (int i = 0; i < inv.getCount(); i++){
                        var stack = inv.getItem(i);
                        if (!stack.item().isEmpty()) {
                            pPlayer.getInventory().placeItemBackInInventory(stack.item().copy());
                        }
                    }
                    storage.removeGrave(uuid);
                    pLevel.removeBlockEntity(pPos);
                    pLevel.removeBlock(pPos, false);
                    pLevel.levelEvent(2001, pPos, Block.getId(pState));
                });
            }, () -> {
                pLevel.removeBlockEntity(pPos);
                pLevel.removeBlock(pPos, false);
                pLevel.levelEvent(2001, pPos, Block.getId(pState));
            });
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHit);
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        //super.wasExploded(level, pos, explosion);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.WATERLOGGED);

    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED)? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
