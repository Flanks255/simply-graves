package com.flanks255.simplygraves;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GraveData implements Comparable<GraveData>{
    public final UUID playerUUID;
    public final String playerName;
    public final UUID graveUUID;
    public BlockPos blockPos;
    public final ResourceKey<Level> dim;
    public final long deathTime;
    public final GraveItemStorage inventory;
    public boolean failed;

    public static final Codec<GraveData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("PlayerUUID").forGetter(g -> g.playerUUID),
            Codec.STRING.fieldOf("PlayerName").forGetter(g -> g.playerName),
            UUIDUtil.CODEC.fieldOf("GraveUUID").forGetter(g -> g.graveUUID),
            BlockPos.CODEC.fieldOf("BlockPos").forGetter(g -> g.blockPos),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("Dim").forGetter(g -> g.dim),
            Codec.LONG.fieldOf("DeathTime").forGetter(g -> g.deathTime),
            GraveItemStorage.CODEC.fieldOf("Inventory").forGetter(g -> g.inventory),
            Codec.BOOL.fieldOf("Failed").orElse(false).forGetter(g -> g.failed)
    ).apply(instance, GraveData::new));

    public GraveData(UUID playerUUID, String playerName, UUID graveUUID, BlockPos blockPos, ResourceKey<Level> dim, long deathTime, GraveItemStorage inventory, boolean failed) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.graveUUID = graveUUID;
        this.blockPos = blockPos;
        this.dim = dim;
        this.deathTime = deathTime;
        this.inventory = inventory;
        this.failed = failed;
    }

/*    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putUUID("PlayerUUID", this.playerUUID);
        tag.putString("PlayerName", this.playerName);
        tag.putUUID("GraveUUID", this.graveUUID);

        tag.putInt("X", this.blockPos.getX());
        tag.putInt("Y", this.blockPos.getY());
        tag.putInt("Z", this.blockPos.getZ());
        tag.putString("Dim", this.dim.identifier().toString());

        tag.putLong("DeathTime", this.deathTime);
        tag.put("Inventory", this.inventory.serializeNBT(provider));
        tag.putBoolean("Failed", this.failed);

        return tag;
    }

    public static GraveData deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        UUID playerUUID = nbt.getUUID("PlayerUUID");
        String playerName = nbt.getString("PlayerName");
        UUID graveUUID = nbt.getUUID("GraveUUID");

        int x = nbt.getInt("X");
        int y = nbt.getInt("Y");
        int z = nbt.getInt("Z");
        ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, Identifier.parse(nbt.getString("Dim")));

        BlockPos blockPos = new BlockPos(x, y, z);

        long deathTime = nbt.getLong("DeathTime");

        ItemStackHandler inventory = new ItemStackHandler();
        inventory.deserializeNBT(provider, nbt.getCompound("Inventory"));

        boolean failed = false;
        if (nbt.contains("Failed"))
            failed = nbt.getBoolean("Failed");


        return new GraveData(playerUUID, playerName, graveUUID, blockPos, dim, deathTime, inventory, failed);
    }*/

    @Override
    public int compareTo(@NotNull GraveData o) {
        return Long.compare(o.deathTime, deathTime);
    }
}