package com.flanks255.simplygraves;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GraveData implements Comparable<GraveData>{
        public final UUID playerUUID;
        public final String playerName;
        public final UUID graveUUID;
        public BlockPos blockPos;
        public final ResourceKey<Level> dim;
        public final long deathTime;
        public final ItemStackHandler inventory;
        public boolean failed;

    public GraveData(UUID playerUUID, String playerName, UUID graveUUID, BlockPos blockPos, ResourceKey<Level> dim, long deathTime, ItemStackHandler inventory, boolean failed) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.graveUUID = graveUUID;
        this.blockPos = blockPos;
        this.dim = dim;
        this.deathTime = deathTime;
        this.inventory = inventory;
        this.failed = failed;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putUUID("PlayerUUID", this.playerUUID);
        tag.putString("PlayerName", this.playerName);
        tag.putUUID("GraveUUID", this.graveUUID);

        tag.putInt("X", this.blockPos.getX());
        tag.putInt("Y", this.blockPos.getY());
        tag.putInt("Z", this.blockPos.getZ());
        tag.putString("Dim", this.dim.location().toString());

        tag.putLong("DeathTime", this.deathTime);
        tag.put("Inventory", this.inventory.serializeNBT());
        tag.putBoolean("Failed", this.failed);

        return tag;
    }

    public static GraveData deserializeNBT(CompoundTag nbt) {
        UUID playerUUID = nbt.getUUID("PlayerUUID");
        String playerName = nbt.getString("PlayerName");
        UUID graveUUID = nbt.getUUID("GraveUUID");

        int x = nbt.getInt("X");
        int y = nbt.getInt("Y");
        int z = nbt.getInt("Z");
        ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("Dim")));

        BlockPos blockPos = new BlockPos(x, y, z);

        long deathTime = nbt.getLong("DeathTime");

        ItemStackHandler inventory = new ItemStackHandler();
        inventory.deserializeNBT(nbt.getCompound("Inventory"));

        boolean failed = false;
        if (nbt.contains("Failed"))
            failed = nbt.getBoolean("Failed");


        return new GraveData(playerUUID, playerName, graveUUID, blockPos, dim, deathTime, inventory, failed);
    }

    @Override
    public int compareTo(@NotNull GraveData o) {
        return Long.compare(o.deathTime, deathTime);
    }
}