package com.flanks255.simplygraves;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class GraveEntity extends BlockEntity {
    public GraveEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SGBlocks.GRAVE_ENTITY.get(), pWorldPosition, pBlockState);
    }
    private UUID uuid = Util.NIL_UUID;
    private UUID player = Util.NIL_UUID;
    private String playerName = "";
    private long deathTime = 0;

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayer() {
        return player;
    }

    public long getDeathTime() {
        return deathTime;
    }

    public void setGrave(UUID pUUID, UUID playerUUID, String pPlayerName, long pDeathTime) {
        uuid = pUUID;
        playerName = pPlayerName;
        deathTime = pDeathTime;
        player = playerUUID;
    }

    public Optional<UUID> getUUID() {
        if (uuid == Util.NIL_UUID || uuid == null) {
            return Optional.empty();
        }
        return Optional.of(uuid);
    }

    // bulk sync
    @Override
    public CompoundTag getUpdateTag() {
        var tag = super.getUpdateTag();

        tag.putUUID("UUID", uuid);
        tag.putUUID("playerUUID", player);
        tag.putString("PlayerName", playerName);
        tag.putLong("DeathTime", deathTime);

        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);

        uuid = tag.getUUID("UUID");
        player = tag.getUUID("playerUUID");
        playerName = tag.getString("PlayerName");
        deathTime = tag.getLong("DeathTime");
    }

    //save/load
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        uuid = pTag.getUUID("UUID");
        player = pTag.getUUID("playerUUID");
        playerName = pTag.getString("PlayerName");
        deathTime = pTag.getLong("DeathTime");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.putUUID("UUID", uuid);
        pTag.putUUID("playerUUID", player);
        pTag.putString("PlayerName", playerName);
        pTag.putLong("DeathTime", deathTime);
    }
}
