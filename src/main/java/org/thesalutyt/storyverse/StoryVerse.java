package org.thesalutyt.storyverse;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thesalutyt.storyverse.logger.SVELogger;
import java.nio.file.Path;
import java.util.stream.Collectors;
// import software.bernie.geckolib3.GeckoLib;


@Mod(StoryVerse.MOD_ID)
public class StoryVerse {
    public static final String MOD_ID = "storyverse";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String HEROID = "#7f3bc";

    public StoryVerse() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        final Path GAME_DIR_PATH = FMLPaths.GAMEDIR.get().toAbsolutePath();
        final String GAME_DIR = GAME_DIR_PATH.toString();
        final String SCRIPTS_PATH = GAME_DIR + "/sve_scripts/";
        SVELogger.create_dir(SCRIPTS_PATH);
    }
}
