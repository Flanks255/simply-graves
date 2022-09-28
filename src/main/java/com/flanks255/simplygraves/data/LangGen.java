package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SGBlocks;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LangGen extends LanguageProvider {
    public LangGen(DataGenerator gen) {
        super(gen, SimplyGraves.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        SGBlocks.GRAVES.forEach(g -> add(g.get(), g.get().graveType.lang));

    add("simplygraves.server_enabled", "The default for graves is currently enabled, and a grave has been created for this death.");
    add("simplygraves.server_disabled", "The default for graves is currently disabled, and a grave has not been created for this death.");
    add("simplygraves.server_choice", "Would you like to use graves in the future?");
    add("simplygraves.failed", "Sorry, your grave failed to place.");
    add("simplygraves.success", "Your grave is located at: %s, in: %s, GraveID: %s");
    }
}
