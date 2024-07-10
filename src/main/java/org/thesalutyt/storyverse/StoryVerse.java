package org.thesalutyt.storyverse;

import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thesalutyt.storyverse.api.effekseer.Effekseer;
import org.thesalutyt.storyverse.api.effekseer.loader.EffekseerLoader;
import org.thesalutyt.storyverse.api.environment.js.mod.Analyze;
import org.thesalutyt.storyverse.api.gui.FadeScreenGui;
import org.thesalutyt.storyverse.common.block.ModBlocks;
import org.thesalutyt.storyverse.common.config.SVConfig;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.items.ModItems;
import org.thesalutyt.storyverse.loader.AssetsLoader;
import org.thesalutyt.storyverse.loader.ILoader;
import org.thesalutyt.storyverse.loader.LoaderType;

@Mod(StoryVerse.MOD_ID)
public class StoryVerse implements ILoader {
    public static final String MOD_ID = "storyverse";
    public static final String MODID = "storyverse";
    public static final Logger LOGGER = LogManager.getLogger();

    public StoryVerse() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        ModItems.register(bus);
        ModBlocks.register(bus);
        Entities.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SVConfig.SPEC, MOD_ID + ".toml");
        init();
    }

    @Override
    public void init() {
        SVEngine.createEngineDirectory();
        Analyze a = new Analyze();
        a.analyze();
        SVEngine.specialDocumentation();
        SVEngine.sendInfoMessage();
        MinecraftForge.EVENT_BUS.register(FadeScreenGui.class);
        EffekseerLoader.init();
        SVEngine.initAssets();
    }

    @Override
    public LoaderType getType() {
        return LoaderType.MOD;
    }

    @Override
    public void load() {
        init();
    }
}
