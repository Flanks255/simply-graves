//package com.flanks255.simplygraves;
//
//import net.minecraft.tags.TagKey;
//import net.neoforged.neoforge.common.util.Lazy;
//import net.neoforged.neoforge.registries.IForgeRegistry;
//import net.neoforged.neoforge.registries.tags.ITag;
//
//public class TagLookup<T> {
//    private final TagKey<T> tagKey;
//    private final Lazy<ITag<T>> lazy;
//
//    public TagLookup(IForgeRegistry<T> registry, TagKey<T> key) {
//        this.tagKey = key;
//        this.lazy = Lazy.of(() -> registry.tags().getTag(key));
//    }
//
//    public ITag<T> get() {
//        return this.lazy.get();
//    }
//
//    public TagKey<T> getKey() {
//        return this.tagKey;
//    }
//
//    public boolean contains(T entry) {
//        return this.get().contains(entry);
//    }
//
//    public boolean isEmpty() {
//        return this.get().isEmpty();
//    }
//}
