package org.thesalutyt.storyverse.forge.common.events;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.forge.common.command.MainCommand;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new MainCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

}
