package org.thesalutyt.storyverse.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SVConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DEBUG_MODE;

    static {
        BUILDER.push("StoryVerse Configuration");
        DEBUG_MODE = BUILDER.define("Debug Mode", false);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
