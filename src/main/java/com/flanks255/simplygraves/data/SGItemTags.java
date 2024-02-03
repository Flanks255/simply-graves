package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class SGItemTags extends ItemTagsProvider {
    public SGItemTags(DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> something, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator.getPackOutput(), something, blockTagProvider.contentsGetter(), SimplyGraves.MODID, existingFileHelper);
    }
    @Override
    protected void addTags(@Nonnull HolderLookup.Provider pointless) {
        getOrCreateRawBuilder(SimplyGraves.NO_GRAVE);
    }
}
