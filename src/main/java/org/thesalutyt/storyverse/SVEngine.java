package org.thesalutyt.storyverse;

import com.mojang.serialization.Codec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Path;

public class SVEngine {
    public static final Codec<SVEngine> CODEC = Codec.unit(SVEngine::new);
    public static String docLink = "404:NoDocsAtMoment";
    public static String prefix = "[StoryVerseEngine]";
    public static String chatPrefix = "§7[§b§lStory§a§lVerse§7]§r ";
    public static String modVersion = "0.0.0.1-Pre-alpha";
    public static String border = " ======================= ";
    public static String onLineBorder = " || ";
    public static String description = "Mod what allows and helps you create story-based maps and series!";
    public static final Path GAME_DIR_PATH = FMLPaths.GAMEDIR.get().toAbsolutePath();
    public static final String GAME_DIR = GAME_DIR_PATH.toString();
    public static final String SCRIPTS_PATH = GAME_DIR + "/storyverse/";
    public static final File SCRIPTS_PATH_FILE = new File(SCRIPTS_PATH);
    public static final String MOB_CONTROLLER_PREFIX = "prefix.storyverse.mob_controller";
    public static final String WORLD_WRAPPER_PREFIX = "prefix.storyverse.world_actions";
    public static final String SOUNDS_ENGINE_PREFIX = "prefix.storyverse.sound";
    public static final String PLAYER_ACTIONS_PREFIX = "prefix.storyverse.player";
    public static final String DAMAGE_SOURCE_PREFIX = "damage.storyverse.script";
    public static final String FILE_FORMAT = ".svsc";
    public static final String GUI_CONTAINER_NAME = "gui_container";
    public static final Integer KEY_START_CODE = 72;
    public static boolean IS_DEBUG = true;
    public SVEngine(){}
    public static enum SVError {
        TYPE_ERROR,
        EVENT_ERROR,
        ENTITY_ERROR
    }
    public static void sendInfoMessage(){
        String firstLine = border + prefix + border;
        String infoLine = onLineBorder + "StoryVerseEngine - " + modVersion + onLineBorder;
        String descLine = onLineBorder + description + onLineBorder;
        String docsLine = onLineBorder + "Docs: " + docLink + onLineBorder;
        String finalLine = border + prefix + border;
    }
}
