package com.flanks255.simplygraves.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue DEFAULT_GRAVE_OPTION;
    public static ForgeConfigSpec.BooleanValue FORCE_GRAVE_OPTION;
    public static ForgeConfigSpec.IntValue DELAY_TO_PUBLIC;

    static {
        DEFAULT_GRAVE_OPTION = COMMON_BUILDER.comment("Default grave option for players.")
                .define("defaultOption", true);
        FORCE_GRAVE_OPTION = COMMON_BUILDER.comment("Force grave option regardless of player preference")
                .define("forceOption", false);
        DELAY_TO_PUBLIC = COMMON_BUILDER.comment("Time delay until a grave becomes public, in seconds.")
                .defineInRange("publicDelay", 3600, 0 , Integer.MAX_VALUE);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
