package com.flanks255.simplygraves;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GraveItemStorage {
    private List<GraveItem> items = new ArrayList<>();
    public static final Codec<GraveItemStorage> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            GraveItem.CODEC.listOf().fieldOf("items").forGetter(GraveItemStorage::getItems))
            .apply(inst, GraveItemStorage::of));

    public List<GraveItem> getItems() {
        return items;
    }

    public static GraveItemStorage of(List<GraveItem> items) {
        var storage = new GraveItemStorage();
        storage.items.addAll(items);
        return storage;
    }

    public void addItem(GraveItem item) {
        items.add(item);
    }
    public void addItem(ItemStack item, GraveItem.InventoryType inventoryType, int slot) {
        items.add(new GraveItem(item, inventoryType, slot));
    }

    public int getCount() {
        return items.size();
    }

    public GraveItem getItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Grave Storage getItem, Index: " + index + ", Size: " + items.size());
        }
        return items.get(index);
    }

    public record GraveItem(ItemStack item, InventoryType inventoryType, int slot) {
        public enum InventoryType {
            MAIN, CURIOS
        }
        public static final Codec<GraveItem> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ItemStack.CODEC.fieldOf("item").forGetter(GraveItem::item),
                Codec.STRING.xmap(InventoryType::valueOf, InventoryType::name).fieldOf("inventoryType").forGetter(GraveItem::inventoryType),
                Codec.INT.fieldOf("slot").forGetter(GraveItem::slot))
                .apply(inst, GraveItem::new));
    }
}
