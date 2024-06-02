package org.thesalutyt.storyverse.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SVConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DEBUG_MODE;

    static {
        BUILDER.push("StoryVerse Engine Configuration");
        DEBUG_MODE = BUILDER.comment("Is engine into debug mode. Default is true").define("is debug", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
