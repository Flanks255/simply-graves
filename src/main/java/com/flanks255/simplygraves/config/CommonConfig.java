package com.flanks255.simplygraves.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    public static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec COMMON_CONFIG;

    public static ModConfigSpec.BooleanValue DEFAULT_GRAVE_OPTION;
    public static ModConfigSpec.BooleanValue OPERATOR_ONLY;
    public static ModConfigSpec.IntValue DELAY_TO_PUBLIC;
    public static ModConfigSpec.IntValue GRAVE_COOLDOWN;

    static {
        DEFAULT_GRAVE_OPTION = COMMON_BUILDER.comment("Default grave option for players.")
                .define("defaultOption", true);
        OPERATOR_ONLY = COMMON_BUILDER.comment("Makes grave configuration op only.")
                .define("operatorOnly", false);
        DELAY_TO_PUBLIC = COMMON_BUILDER.comment("Time delay until a grave becomes public, in seconds.")
                .defineInRange("publicDelay", 3600, 0 , Integer.MAX_VALUE);
        GRAVE_COOLDOWN = COMMON_BUILDER.comment("Delay before you can spawn another grave, in seconds.")
                .defineInRange("graveCooldown", 0, 0, Integer.MAX_VALUE);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
