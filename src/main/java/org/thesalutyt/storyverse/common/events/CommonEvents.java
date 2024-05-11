package org.thesalutyt.storyverse.common.events;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.SVEnvironment;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class CommonEvents {
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {

    }
    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        SVEnvironment.Root.tick();
    }
    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public static void playerJoinedWorld(PlayerEvent.PlayerLoggedInEvent event) {
        SVEnvironment.Root.playerJoined(event);
    }
    @SubscribeEvent
    public static void serverStarted(FMLServerStartedEvent event) {
        SVEngine.sendInfoMessage();
    }

}
