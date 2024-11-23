package org.thesalutyt.storyverse.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SVConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.ConfigValue<Boolean> DEBUG_MODE;
    public static ForgeConfigSpec.ConfigValue<String> ROOT_DIR;
    public static ForgeConfigSpec.ConfigValue<String> MOD_SCRIPT_FILE;
    public static ForgeConfigSpec.ConfigValue<String> WORLD_STARTER_SCRIPT;
    public static ForgeConfigSpec.ConfigValue<Boolean> ALLOW_PLAYER_DELETING;
    public static ForgeConfigSpec.ConfigValue<Boolean> SHOUT_PLAYER_DELETING;
    public static ForgeConfigSpec.ConfigValue<Boolean> SHOUT_SCRIPT_STARTING;

    static {
        BUILDER.push("StoryVerse Engine Configuration");
        DEBUG_MODE = BUILDER.comment(" Is engine into debug mode. Default is true").define("is_debug", true);
        ROOT_DIR = BUILDER.comment(" Root directory for scripts. Default is '%GAME_DIR%/sve_scripts/'. DO NOT FORGER THE TRAILING SLASH").
                define("root_dir", "%GAME_DIR%/sve_scripts");
        MOD_SCRIPT_FILE = BUILDER.comment(" File name of the script that contains the mod script")
                .define("mod_script_file", "mod.js");
        ALLOW_PLAYER_DELETING = BUILDER.comment(" Allow players to delete other players with 'Entity Deleter'")
                .define("allow_player_deleting", false);
        WORLD_STARTER_SCRIPT = BUILDER.comment(" File name of the script that will immidiately start, when the world is loaded")
                        .define("world_starter_script", "%NULL%");
        SHOUT_PLAYER_DELETING = BUILDER.comment(" Should shout when a player is deleted")
                        .define("shout_player_deleting", true);
        SHOUT_SCRIPT_STARTING = BUILDER.comment(" Should shout when a script starts")
                .define("shout_script_starting", false);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
