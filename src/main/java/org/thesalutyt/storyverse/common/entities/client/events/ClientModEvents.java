package org.thesalutyt.storyverse.common.entities.client.events;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.entities.npc.NPCRender;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class ClientModEvents {
    public static KeyBinding keyStory;
    public static KeyBinding startStoryButton;
    public static NPCRender renderManager;

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        keyStory = new KeyBinding("key.storyverse.next_act",
                InputMappings.Type.KEYSYM,
                SVEngine.KEY_CONTINUE_CODE,
                "keylist.storyverse"
        );
        startStoryButton = new KeyBinding("key.storyverse.start_script",
                InputMappings.Type.KEYSYM,
                SVEngine.KEY_START_CODE,
                "keylist.storyverse"
        );
        ClientRegistry.registerKeyBinding(startStoryButton);
        ClientRegistry.registerKeyBinding(keyStory);
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(Entities.NPC.get(), NPCRender::new);
    }
}
