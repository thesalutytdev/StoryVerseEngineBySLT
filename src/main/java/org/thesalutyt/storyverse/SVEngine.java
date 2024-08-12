package org.thesalutyt.storyverse;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Logger;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Interpreter;
import org.thesalutyt.storyverse.api.environment.js.mod.ModInterpreter;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.gui.FadeScreenGui;
import org.thesalutyt.storyverse.common.config.SVConfig;
import org.thesalutyt.storyverse.loader.AssetsLoader;
import org.thesalutyt.storyverse.logger.SVELogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SVEngine {
    public static final Codec<SVEngine> CODEC = Codec.unit(SVEngine::new);
    public static final String MOD_ID = "storyverse";
    public static final String MODID = "storyverse";
    public static final String DOC_LINK = "404:NoDocsAtMoment";
    public static final String PREFIX = "[StoryVerseEngine]";
    public static final String CHAT_PREFIX = "§7[§b§lStory§a§lVerse§7]§r ";
    public static final String MOD_VERSION = "5.6-Beta";
    public static final String BORDER = " =========================== ";
    public static final String ON_LINE_BORDER = " || ";
    public static final String DESCRIPTION = "Mod what allows and helps you create story-based maps and series!";
    public static final Logger LOGGER = StoryVerse.LOGGER;
    public static final Path GAME_DIR_PATH = FMLPaths.GAMEDIR.get().toAbsolutePath();
    public static final String GAME_DIR = GAME_DIR_PATH.toString();
    public static final String SCRIPTS_PATH = SVConfig.ROOT_DIR.get().replace("%GAME_DIR%", GAME_DIR) + "/";
    public static final File SCRIPTS_PATH_FILE = new File(SCRIPTS_PATH);
    public static final String LOGS_PATH = SCRIPTS_PATH + "logs/";
    public static final String DOCS_PATH = SCRIPTS_PATH + "docs/";
    public static final String ASSETS_DIR = SCRIPTS_PATH + "assets/storyverse/";
    public static final String ANIMATIONS_PATH = ASSETS_DIR + "/animations/";
    public static final String MODELS_PATH = ASSETS_DIR + "/models/";
    public static final String TEXTURES_PATH = ASSETS_DIR + "/textures/";
    public static final String CONFIG_PATH = GAME_DIR + "/config/";
    public static final String MOB_CONTROLLER_PREFIX = "prefix.storyverse.mob_controller";
    public static final String WORLD_WRAPPER_PREFIX = "prefix.storyverse.world_actions";
    public static final String SOUNDS_ENGINE_PREFIX = "prefix.storyverse.sound";
    public static final String PLAYER_ACTIONS_PREFIX = "prefix.storyverse.player";
    public static final String DAMAGE_SOURCE_PREFIX = "damage.storyverse.script";
    public static final String FILE_FORMAT = ".js";
    public static final String LOG_FILE_FORMAT = ".sv_log";
    public static String LOG_FILE_PATH;
    public static File LOG_FILE;
    public static final String GUI_CONTAINER_NAME = "gui_container";
    public static final Integer KEY_CONTINUE_CODE = 72;
    public static final Integer KEY_START_CODE = 71;
    public static SVColors CHARACTER_COLOR = SVColors.GREEN;
    public static String CHARACTER_COLOR_STR = "§3";
    public static String DEFAULT_CHARACTER_NAME = "prefix.storyverse.default.npc";
    public static final String HERO_ID = "#7f3bc";
    public static final String OP_PLAYER = "TheSALUTYT";
    public static boolean IS_DEBUG = SVConfig.DEBUG_MODE.get();
    public static Interpreter interpreter;
    public static ModInterpreter modInterpreter = new ModInterpreter(SCRIPTS_PATH);
    public static final String MOD_SCRIPT_FILE = SVConfig.MOD_SCRIPT_FILE.get();
    public static AssetsLoader assetsLoader = new AssetsLoader();

    public static FadeScreenGui fadeScreen = new FadeScreenGui();
    public SVEngine(){}
    public static enum SVError {
        TYPE_ERROR("TypeError"),
        GENERAL("UnexpectedError"),
        RHINO_ERROR("RhinoError"),
        JAVA_ERROR("JavaError"),
        EVENT_ERROR("EventError"),
        ENTITY_ERROR("EntityError"),
        CAMERA_FAILED("FailedCameraAction");
        public final String errorName;
        private SVError(String error) {
            this.errorName = error;
        }
        public static String createErrorMessage(SVError type, String msg) {
            String message = String.format("§4[ERROR] §e%s:§r\n %s", type, msg);
            Chat.sendMessage(message);
            SVELogger.write(SVEngine.LOG_FILE_PATH, message);
            return message;
        }
    }
    public static enum SVColors {
        RED("§4"),
        LIGHT_RED("§c"),
        YELLOW("§e"),
        AQUA("§b"),
        DARK_AQUA("§3"),
        WHITE("§f"),
        BLACK("§0"),
        BLUE("§1"),
        LIGHT_BLUE("§9"),
        GREEN("§2"),
        LIME("§a"),
        ORANGE("§6"),
        PINK("§d"),
        PURPLE("§5"),
        GRAY("§7"),
        DARK_GRAY("§8"),
        BOLD("§l"),
        UNDERLINE("§n"),
        ITALIC("§o"),
        STRIKE("§m"),
        MAGIC("§k"),
        RESET("§r");
        private final java.lang.String color;
        private SVColors(String color) {this.color = color;}
        public String createColoredMessage(SVColors type, String msg) {
            return java.lang.String.format("%s%s", type.color, msg);
        }
        public SVColors getColors() {
            return this;
        }
    }
    public static void sendInfoMessage(){
        String firstLine = BORDER + PREFIX + BORDER;
        String infoLine = ON_LINE_BORDER + "StoryVerseEngine - " + MOD_VERSION + ON_LINE_BORDER;
        String descLine = ON_LINE_BORDER + DESCRIPTION + ON_LINE_BORDER;
        String docsLine = ON_LINE_BORDER + "Docs: " + DOC_LINK + ON_LINE_BORDER;
        String modId = ON_LINE_BORDER + "Mod ID: " + MOD_ID + ON_LINE_BORDER;
        String finalLine = BORDER + PREFIX + BORDER;
        StoryVerse.LOGGER.info(firstLine);
        StoryVerse.LOGGER.info(infoLine);
        StoryVerse.LOGGER.info(descLine);
        StoryVerse.LOGGER.info(docsLine);
        StoryVerse.LOGGER.info(modId);
        StoryVerse.LOGGER.info(finalLine);
    }
    public static void createEngineDirectory() {
        if (new File(SCRIPTS_PATH).exists()) {
            return;
        }
        SVELogger.create_dir(SCRIPTS_PATH);
        System.out.println("[SVEngine::DirectoryCreating] Created main directory");
        SVELogger.create_dir(LOGS_PATH);
        System.out.println("[SVEngine::DirectoryCreating] Created logs directory");
        SVELogger.create_dir(ASSETS_DIR);
        System.out.println("[SVEngine::DirectoryCreating] Created assets directory");
        SVELogger.create_dir(ANIMATIONS_PATH);
        System.out.println("[SVEngine::DirectoryCreating] Created animations directory");
        SVELogger.create_dir(MODELS_PATH);
        System.out.println("[SVEngine::DirectoryCreating] Created models directory");
        SVELogger.create_dir(TEXTURES_PATH);
        System.out.println("[SVEngine::DirectoryCreating] Created textures directory");
    }
    public static void specialDocumentation() {
        System.out.println(Item.getId(new ItemStack(
                Items.DIAMOND_SWORD
        ).getItem()));
        SVELogger.create_dir(DOCS_PATH);
        SVELogger.create_file(DOCS_PATH, "item_id.txt");
        SVELogger.write(DOCS_PATH + "item_id.txt", "ITEM INTENSIFICATION:\n" + Item.getId(new ItemStack(
                Items.DIAMOND_SWORD
        ).getItem()));
    }
    public static void initAssets() {
        assetsLoader = new AssetsLoader();
        try {
            assetsLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
