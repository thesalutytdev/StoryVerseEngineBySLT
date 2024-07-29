package org.thesalutyt.storyverse.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.thesalutyt.storyverse.forge.common.config.SVConfig;
import org.thesalutyt.storyverse.forge.common.entity.Entities;
import software.bernie.geckolib3.GeckoLib;

import static org.thesalutyt.storyverse.StoryVerse.MOD_ID;

@Mod(MOD_ID)
public final class StoryVerse {
    public StoryVerse() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SVConfig.SPEC, MOD_ID + ".toml");

        // Run our common setup.
        org.thesalutyt.storyverse.StoryVerse.init();
        GeckoLib.initialize();

        Entities.register(bus);

        SVEngine.createEngineDirectory();
        SVEngine.specialDocumentation();
        SVEngine.sendInfoMessage();
    }
}
