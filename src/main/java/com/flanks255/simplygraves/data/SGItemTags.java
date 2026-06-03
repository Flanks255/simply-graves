package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class SGItemTags extends ItemTagsProvider {
    public SGItemTags(DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> something, BlockTagsProvider blockTagProvider) {
        super(dataGenerator.getPackOutput(), something, SimplyGraves.MODID);
    }
    @Override
    protected void addTags(@Nonnull HolderLookup.Provider pointless) {
        getOrCreateRawBuilder(SimplyGraves.NO_GRAVE);
    }
}
