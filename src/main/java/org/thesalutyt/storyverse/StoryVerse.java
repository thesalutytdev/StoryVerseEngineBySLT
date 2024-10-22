package org.thesalutyt.storyverse;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thesalutyt.storyverse.api.environment.js.mod.Analyze;
import org.thesalutyt.storyverse.api.gui.FadeScreenGui;
import org.thesalutyt.storyverse.common.block.ModBlocks;
import org.thesalutyt.storyverse.common.config.SVConfig;
import org.thesalutyt.storyverse.common.dimension.Dimensions;
import org.thesalutyt.storyverse.common.effects.ModEffects;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.items.ModItems;

@Mod(StoryVerse.MOD_ID)
public class StoryVerse {
    public static final String MOD_ID = "storyverse";
    public static final Logger LOGGER = LogManager.getLogger();

    public StoryVerse() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        ModItems.register(bus);
        ModBlocks.register(bus);
        ModEffects.register(bus);
        Entities.register(bus);
        Dimensions.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SVConfig.SPEC, MOD_ID + ".toml");
        SVEngine.createEngineDirectory();
        Analyze a = new Analyze();
        a.analyze();
        SVEngine.specialDocumentation();
        SVEngine.sendInfoMessage();
        MinecraftForge.EVENT_BUS.register(FadeScreenGui.class);
    }
}
