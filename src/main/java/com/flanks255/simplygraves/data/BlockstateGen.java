package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SGBlocks;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nonnull;

public class BlockstateGen extends ModelProvider {
    public BlockstateGen(DataGenerator gen) {
        super(gen.getPackOutput(), SimplyGraves.MODID);
    }

    @Override
    protected void registerModels(@Nonnull BlockModelGenerators blockModels, @Nonnull ItemModelGenerators itemModels) {
        SGBlocks.GRAVES.forEach(grave -> {
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(grave.get())
                    .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING)
                            .generate(dir -> existingModel(grave.get().graveType.modelName)
                                    .with(switch(dir){
                                        case EAST -> BlockModelGenerators.Y_ROT_90;
                                        case SOUTH -> BlockModelGenerators.Y_ROT_180;
                                        case WEST -> BlockModelGenerators.Y_ROT_270;
                                        default -> BlockModelGenerators.NOP;
                                    }))));
        });
    }

    private static MultiVariant existingModel(String modelName) {
        return BlockModelGenerators.plainVariant(SimplyGraves.rl("block/" + modelName));
    }
}
