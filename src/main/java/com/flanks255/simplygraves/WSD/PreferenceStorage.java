package com.flanks255.simplygraves.WSD;

import com.flanks255.simplygraves.PlayerPreferences;
import com.flanks255.simplygraves.SimplyGraves;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PreferenceStorage extends SavedData {
    private final HashMap<UUID, PlayerPreferences> data = new HashMap<>();
    private static PreferenceStorage INSTANCE = null;
    public static final Codec<PreferenceStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, PlayerPreferences.CODEC).fieldOf("data").forGetter($ -> $.data)
    ).apply(instance, PreferenceStorage::new));

    public static final SavedDataType<PreferenceStorage> TYPE = new SavedDataType<>(
            SimplyGraves.rl("prefs"),
            PreferenceStorage::new,
            CODEC
    );

/*    @Nonnull
    @Override
    public CompoundTag save(CompoundTag pCompoundTag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        data.forEach((uuid, pref) -> list.add(pref.save()));

        pCompoundTag.put("Prefs", list);
        return pCompoundTag;
    }*/

    public PreferenceStorage(Map<UUID, PlayerPreferences> data) {
        this.data.putAll(data);
    }
    public PreferenceStorage() {}

    public HashMap<UUID, PlayerPreferences> getData() {
        return data;
    }

/*    public static PreferenceStorage load(CompoundTag nbt, HolderLookup.Provider provider){
        if (nbt.contains("Prefs")) {
            ListTag list = nbt.getList("Prefs", Tag.TAG_COMPOUND);

            list.forEach(tag -> {
                CompoundTag prefTag = (CompoundTag) tag;
                data.put(prefTag.getUUID("uuid"), PlayerPreferences.of(prefTag));
            });
        }

        return new PreferenceStorage();
    }*/

    public static PreferenceStorage get() {
        if (INSTANCE == null) {
            INSTANCE = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(PreferenceStorage.TYPE);
        }
        return INSTANCE;
    }

    public PlayerPreferences getPrefs(UUID player) {
        return data.computeIfAbsent(player, uuid -> {
            setDirty();
            return new PlayerPreferences(uuid);});
    }

}
