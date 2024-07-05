package org.thesalutyt.storyverse.api.effekseer.loader;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.thesalutyt.storyverse.api.effekseer.EffekseerEffect;
import org.thesalutyt.storyverse.api.effekseer.EffekseerManager;
import org.thesalutyt.storyverse.api.effekseer.ParticleEmitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//НЕ РАБОТАЕТ
public class EffekseerLoader {
    private static EffekseerManager effekseerManager;
    private static final Map<String, EffekseerEffect> loadedEffects = new HashMap<>();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    private static class EventHandlers {
        @SubscribeEvent
        public void onClientSetup(FMLClientSetupEvent event) {
            effekseerManager = new EffekseerManager();
            effekseerManager.init(1000);
        }

        @SubscribeEvent
        public void onLoadComplete(FMLLoadCompleteEvent event) {
            loadEffect("example_effect", new ResourceLocation("storyverse", "effects/lightning.efkefc"));
        }

        @SubscribeEvent
        public void onServerStopping(FMLServerStoppingEvent event) {
            effekseerManager.close();
        }
    }

    public static void loadEffect(String effectName, ResourceLocation resourceLocation) {
        try (InputStream stream = Minecraft.getInstance().getResourceManager().getResource(resourceLocation).getInputStream()) {
            EffekseerEffect effect = new EffekseerEffect();
            if (effect.load(stream, 1.0f)) {
                loadedEffects.put(effectName, effect);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EffekseerEffect getEffect(String effectName) {
        return loadedEffects.get(effectName);
    }

    public static void playEffect(String effectName, float x, float y, float z) {
        EffekseerEffect effect = getEffect(effectName);
        if (effect != null && effekseerManager != null) {
            ParticleEmitter emitter = effekseerManager.createParticle(effect);
            emitter.setPosition(x, y, z);
        }
    }
}