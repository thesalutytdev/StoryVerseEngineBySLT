package org.thesalutyt.storyverse;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thesalutyt.storyverse.common.entities.Entities;


@Mod(StoryVerse.MOD_ID)
public class StoryVerse {
    public static final String MOD_ID = "storyverse";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String HEROID = "#7f3bc";

    public StoryVerse() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        Entities.register(bus);
        SVEngine.createEngineDirectory();
        SVEngine.sendInfoMessage();
    }
}
