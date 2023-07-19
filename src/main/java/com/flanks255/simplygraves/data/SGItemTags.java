package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class SGItemTags extends ItemTagsProvider {
    public SGItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, SimplyGraves.MODID, existingFileHelper);
    }
    @Override
    protected void addTags() {
        getOrCreateRawBuilder(SimplyGraves.NO_GRAVE);
    }
}
