package com.flanks255.simplygraves.WSD;

import com.flanks255.simplygraves.GraveData;
import com.flanks255.simplygraves.SimplyGraves;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GraveStorage extends SavedData {
    private static final Identifier NAME = SimplyGraves.rl("graves");
    private final HashMap<UUID, GraveData> data = new HashMap<>();
    private static GraveStorage INSTANCE = null;
    public static final Codec<GraveStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, GraveData.CODEC).fieldOf("data").forGetter($ -> $.data))
            .apply(instance, GraveStorage::new));

    public static final SavedDataType<GraveStorage> TYPE = new SavedDataType<>(NAME, GraveStorage::new, CODEC);

    public GraveStorage() {
    }

    public GraveStorage(Map<UUID, GraveData> data) {
        this.data.putAll(data);
    }

    public static GraveStorage get() {
        if (INSTANCE == null) {
            INSTANCE = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(TYPE);
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
    public void setSuccess(UUID uuid) {
        data.get(uuid).failed = false;
        setDirty();
    }
}
