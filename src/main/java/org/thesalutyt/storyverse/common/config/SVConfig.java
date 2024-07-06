package org.thesalutyt.storyverse.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SVConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DEBUG_MODE;
    public static final ForgeConfigSpec.ConfigValue<String> ROOT_DIR;
    public static final ForgeConfigSpec.ConfigValue<String> MOD_SCRIPT_FILE;

    static {
        BUILDER.push("StoryVerse Engine Configuration");
        DEBUG_MODE = BUILDER.comment(" Is engine into debug mode. Default is true").define("is_debug", true);
        ROOT_DIR = BUILDER.comment(" Root directory for scripts. Default is '%GAME_DIR%/sve_scripts'.").
                define("root_dir", "%GAME_DIR%/sve_scripts");
        MOD_SCRIPT_FILE = BUILDER.comment(" File name of the script that contains the mod script")
                .define("mod_script_file", "mod.js");
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
