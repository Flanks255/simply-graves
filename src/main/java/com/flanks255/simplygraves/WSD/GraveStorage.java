package com.flanks255.simplygraves.WSD;

import com.flanks255.simplygraves.GraveData;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GraveStorage extends SavedData {
    private static final String NAME = SimplyGraves.MODID + "_data";
    private static final HashMap<UUID, GraveData> data = new HashMap<>();
    private static GraveStorage INSTANCE = null;

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        ListTag list = new ListTag();
        data.forEach((uuid, grave) -> list.add(grave.serializeNBT()));

        pCompoundTag.put("Graves", list);
        return pCompoundTag;
    }

    public static GraveStorage load(CompoundTag pCompoundTag) {
        if (pCompoundTag.contains("Graves")) {
            ListTag list = pCompoundTag.getList("Graves", Tag.TAG_COMPOUND);

            list.forEach(tag -> {
                CompoundTag graveTag = (CompoundTag) tag;
                data.put(graveTag.getUUID("GraveUUID"), GraveData.deserializeNBT(graveTag));
            });
        }

        return new GraveStorage();
    }

    public static GraveStorage get() {
        if (INSTANCE == null) {
            INSTANCE = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(new Factory<>(GraveStorage::new, GraveStorage::load), NAME);
        }
        return INSTANCE;
    }

    public HashMap<UUID, GraveData> getData() {
        return data;
    }

    public void addGrave(UUID uuid, GraveData grave) {
        data.put(uuid, grave);
        setDirty();
    }

    public void removeGrave(UUID uuid) {
        data.remove(uuid);
        setDirty();
    }

    public Optional<GraveData> getGrave(UUID uuid) {
        if (data.containsKey(uuid))
            return Optional.of(data.get(uuid));
        return Optional.empty();
    }

    public Optional<GraveData> getLastGrave(ServerPlayer player) {
        return data.values().stream().filter(g -> g.playerUUID.compareTo(player.getUUID()) == 0).sorted().findFirst();
    }

    public Optional<GraveData> getFailedGrave(ServerPlayer player) {
        for (Map.Entry<UUID, GraveData> entry : data.entrySet()) {
            if (entry.getValue().failed && entry.getValue().playerUUID.compareTo(player.getUUID()) == 0)
                return Optional.of(entry.getValue());
        }
        return Optional.empty();
    }

    public void setFailed(UUID uuid) {
        data.get(uuid).failed = true;
        setDirty();
    }
}
