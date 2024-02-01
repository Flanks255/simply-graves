package com.flanks255.simplygraves.WSD;

import com.flanks255.simplygraves.PlayerPreferences;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.UUID;

public class PreferenceStorage extends SavedData {
    private static final String NAME = SimplyGraves.MODID + "_prefs";
    private static final HashMap<UUID, PlayerPreferences> data = new HashMap<>();
    private static PreferenceStorage INSTANCE = null;

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        ListTag list = new ListTag();
        data.forEach((uuid, pref) -> list.add(pref.save()));

        pCompoundTag.put("Prefs", list);
        return pCompoundTag;
    }

    public static PreferenceStorage load(CompoundTag nbt){
        if (nbt.contains("Prefs")) {
            ListTag list = nbt.getList("Prefs", Tag.TAG_COMPOUND);

            list.forEach(tag -> {
                CompoundTag prefTag = (CompoundTag) tag;
                data.put(prefTag.getUUID("uuid"), PlayerPreferences.of(prefTag));
            });
        }

        return new PreferenceStorage();
    }

    public static PreferenceStorage get() {
        if (INSTANCE == null) {
            INSTANCE = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(new Factory<>(PreferenceStorage::new, PreferenceStorage::load), NAME);
        }
        return INSTANCE;
    }

    public PlayerPreferences getPrefs(UUID player) {
        return data.computeIfAbsent(player, uuid -> {
            setDirty();
            return new PlayerPreferences(uuid);});
    }

}
