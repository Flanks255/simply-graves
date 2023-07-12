package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SGBlocks;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), SimplyGraves.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        SGBlocks.GRAVES.forEach(g -> simpleBlock(g.get(), models().getExistingFile(modLoc(g.get().graveType.modelName))));
    }
}
