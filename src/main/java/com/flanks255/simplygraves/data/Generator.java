package com.flanks255.simplygraves.data;

import net.minecraftforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();

        generator.addProvider(true, new BlockStates(generator, event.getExistingFileHelper()));
        generator.addProvider(true, new LangGen(generator));
        BlockTagGen blocks = new BlockTagGen(generator, event.getExistingFileHelper());
        generator.addProvider(true, blocks);
        generator.addProvider(true, new SGItemTags(generator, blocks, event.getExistingFileHelper()));
    }
}
