package org.thesalutyt.storyverse;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thesalutyt.storyverse.api.effekseer.loader.EffekseerLoader;
import org.thesalutyt.storyverse.api.environment.js.mod.Analyze;
import org.thesalutyt.storyverse.api.features.Chat;
import org.thesalutyt.storyverse.api.gui.FadeScreenGui;
import org.thesalutyt.storyverse.common.block.ModBlocks;
import org.thesalutyt.storyverse.common.block.adder.CustomBlock;
import org.thesalutyt.storyverse.common.config.SVConfig;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.items.ModItems;
import org.thesalutyt.storyverse.common.items.adder.CustomItem;
import org.thesalutyt.storyverse.common.tabs.ModCreativeTabs;

@Mod(StoryVerse.MOD_ID)
public class StoryVerse {
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
        SVEngine.createEngineDirectory();
        Analyze a = new Analyze();
        a.analyze();
        SVEngine.specialDocumentation();
        SVEngine.sendInfoMessage();
        MinecraftForge.EVENT_BUS.register(FadeScreenGui.class);
    }
}
