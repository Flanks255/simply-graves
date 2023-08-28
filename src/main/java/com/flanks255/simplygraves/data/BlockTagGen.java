package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SGBlocks;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagGen extends BlockTagsProvider {
    public BlockTagGen(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, SimplyGraves.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        SGBlocks.BLOCKS.getEntries().forEach(b -> {
            tag(BlockTags.WITHER_IMMUNE).add(b.get());
            tag(BlockTags.DRAGON_IMMUNE).add(b.get());

            tag(SimplyGraves.FTBCHUNKS).add(b.get());
            tag(SimplyGraves.GAIA_BLOCK).add(b.get());
            tag(SimplyGraves.GRAVES).add(b.get());
        });
    }
}
