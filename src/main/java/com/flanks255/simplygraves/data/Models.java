/*
package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SGBlocks;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.DataGenerator;

import javax.annotation.Nonnull;
import java.util.Optional;

public class Models extends ModelProvider {
    public Models(DataGenerator gen) {
        super(gen.getPackOutput(), SimplyGraves.MODID);
    }

    @Override
    protected void registerModels(@Nonnull BlockModelGenerators blockModels, @Nonnull ItemModelGenerators itemModels) {
        ModelTemplate grave = new ModelTemplate(Optional.of(SimplyGraves.rl("block/grave")), Optional.empty(), TextureSlot.ALL);
    }

    @Override
    protected void registerStatesAndModels() {
        SGBlocks.GRAVES.forEach(g -> simpleBlock(g.get(), models().getExistingFile(modLoc(g.get().graveType.modelName))));
    }
}
*/
