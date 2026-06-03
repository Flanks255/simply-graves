package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SGBlocks;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), SimplyGraves.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        SGBlocks.GRAVES.forEach(g -> simpleBlock(g.get(), models().getExistingFile(modLoc(g.get().graveType.modelName))));
    }
}
