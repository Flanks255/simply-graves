package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SGBlocks;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BlockTagGen extends BlockTagsProvider {
    public BlockTagGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> thingIDontUse, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), thingIDontUse, SimplyGraves.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider $) {
        SGBlocks.BLOCKS.getEntries().forEach(b -> {
            tag(BlockTags.WITHER_IMMUNE).add(b.get());
            tag(BlockTags.DRAGON_IMMUNE).add(b.get());

            tag(SimplyGraves.FTBCHUNKS).add(b.get());
            tag(SimplyGraves.GAIA_BLOCK).add(b.get());
            tag(SimplyGraves.GRAVES).add(b.get());
        });
    }
}
