package com.flanks255.simplygraves;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class GraveEntity extends BlockEntity {
    public GraveEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SGBlocks.GRAVE_ENTITY.get(), pWorldPosition, pBlockState);
    }
    private UUID uuid;
    private UUID player;
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
        setChanged();
    }

    public Optional<UUID> getUUID() {
        if (uuid == null) {
            return Optional.empty();
        }
        return Optional.of(uuid);
    }

    // bulk sync
    @Nonnull
    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
        var tag = super.getUpdateTag(registries);

        tag.putLong("UUIDM", uuid.getMostSignificantBits());
        tag.putLong("UUIDL", uuid.getLeastSignificantBits());
        tag.putLong("playerUUIDM", player.getMostSignificantBits());
        tag.putLong("playerUUIDL", player.getLeastSignificantBits());
        tag.putString("PlayerName", playerName);
        tag.putLong("DeathTime", deathTime);

        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput tag) {
        super.handleUpdateTag(tag);

        uuid = new UUID(tag.getLongOr("UUIDM",0), tag.getLongOr("UUIDL",0));
        player = new UUID(tag.getLongOr("playerUUIDM",0), tag.getLongOr("playerUUIDL",0));
        playerName = tag.getStringOr("PlayerName","");
        deathTime = tag.getLongOr("DeathTime",0);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        uuid = new UUID(input.getLongOr("UUIDM", 0), input.getLongOr("UUIDL", 0));
        player = new UUID(input.getLongOr("playerUUIDM", 0), input.getLongOr("playerUUIDL", 0));
        playerName = input.getStringOr("PlayerName", "");
        deathTime = input.getLongOr("DeathTime", 0);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putLong("UUIDM", uuid.getMostSignificantBits());
        output.putLong("UUIDL", uuid.getLeastSignificantBits());
        output.putLong("playerUUIDM", player.getMostSignificantBits());
        output.putLong("playerUUIDL", player.getLeastSignificantBits());
        output.putString("PlayerName", playerName);
        output.putLong("DeathTime", deathTime);
    }
}
