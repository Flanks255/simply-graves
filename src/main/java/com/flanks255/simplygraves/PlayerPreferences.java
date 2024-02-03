package com.flanks255.simplygraves;

import net.minecraft.nbt.CompoundTag;

import java.util.Optional;
import java.util.UUID;

public class PlayerPreferences {
    private Optional<Boolean> graveOption;
    private Optional<Boolean> autoEquip;
    private final UUID player;

    private PlayerPreferences(CompoundTag nbt) {
        this.player = nbt.getUUID("uuid");

        if(nbt.contains("graveoption"))
            graveOption = Optional.of(nbt.getBoolean("graveoption"));
        else
            graveOption = Optional.empty();

        if (nbt.contains("autoequip"))
            autoEquip = Optional.of(nbt.getBoolean("autoequip"));
        else
            autoEquip = Optional.empty();
    }

    public PlayerPreferences(UUID player) {
        this.player = player;
        this.graveOption = Optional.empty();
    }

    public static PlayerPreferences of( CompoundTag nbt) {
        return new PlayerPreferences(nbt);
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();

        nbt.putUUID("uuid", player);

        graveOption.ifPresent($ -> nbt.putBoolean("graveoption", $));
        autoEquip.ifPresent($ -> nbt.putBoolean("autoequip", $));

        return nbt;
    }

    public UUID getPlayer() {
        return player;
    }
    public Optional<Boolean> getGraveOption() {
        return graveOption;
    }
    public void setGraveOption(boolean option) {
        graveOption = Optional.of(option);
    }

    public Optional<Boolean> getEquipOption() {
        return autoEquip;
    }
    public void setEquipOption(boolean option) {
        autoEquip = Optional.of(option);
    }
}
