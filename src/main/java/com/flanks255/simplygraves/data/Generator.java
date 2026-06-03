package com.flanks255.simplygraves.data;

import net.neoforged.neoforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent.Server event) {
        var generator = event.getGenerator();

        //generator.addProvider(true, new Models(generator));
        generator.addProvider(true, new LangGen(generator));
        BlockTagGen blocks = new BlockTagGen(generator, event.getLookupProvider());
        generator.addProvider(true, blocks);
        generator.addProvider(true, new SGItemTags(generator, event.getLookupProvider(), blocks));
    }
    public static void gatherData2(GatherDataEvent.Client event) {
        var generator = event.getGenerator();

        //generator.addProvider(true, new Models(generator));
        generator.addProvider(true, new LangGen(generator));
        BlockTagGen blocks = new BlockTagGen(generator, event.getLookupProvider());
        generator.addProvider(true, blocks);
        generator.addProvider(true, new SGItemTags(generator, event.getLookupProvider(), blocks));
    }
}
