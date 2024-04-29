package org.thesalutyt.storyverse.common.entities.client.events;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.thesalutyt.storyverse.StoryVerse;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    public static KeyBinding keyStory;
    public static KeyBinding startStoryButton;

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        keyStory = new KeyBinding("key.storyverse.next_act",
                InputMappings.Type.KEYSYM,
                72,
                "keylist.storyverse"
        );
        startStoryButton = new KeyBinding("key.storyverse.start_script",
                InputMappings.Type.KEYSYM,
                71,
                "keylist.storyverse"
        );
        ClientRegistry.registerKeyBinding(startStoryButton);
        ClientRegistry.registerKeyBinding(keyStory);
    }

}
