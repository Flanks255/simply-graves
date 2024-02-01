package com.flanks255.simplygraves.data;

import com.flanks255.simplygraves.SGBlocks;
import com.flanks255.simplygraves.SimplyGraves;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LangGen extends LanguageProvider {
    public LangGen(DataGenerator gen) {
        super(gen.getPackOutput(), SimplyGraves.MODID, "en_us");
    }

    protected void addMy(String key, String value){
        add(SimplyGraves.MODID + "." + key, value);
    }
    @Override
    protected void addTranslations() {
        SGBlocks.GRAVES.forEach(g -> add(g.get(), g.get().graveType.lang));

    addMy("server_enabled", "The default for graves is currently enabled, and a grave has been created for this death.");
    addMy("server_disabled", "The default for graves is currently disabled, and a grave has not been created for this death.");
    addMy("server_choice", "Would you like to use graves in the future?");
    addMy("failed", "Sorry, your grave failed to place.");
    addMy("success", "Your grave is located at: %s, in: %s, GraveID: %s");
    addMy("not_yours", "Sorry, this grave belongs to %s, please try again later...");
    addMy("latest_grave", "Latest Grave");
    addMy("no_graves", "No graves found.");
    }
}
