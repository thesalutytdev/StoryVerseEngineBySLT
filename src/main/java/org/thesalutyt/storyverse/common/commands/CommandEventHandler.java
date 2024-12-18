package org.thesalutyt.storyverse.common.commands;// Import necessary classes

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Your mod's main class or a dedicated event handler class
@Mod.EventBusSubscriber
public class CommandEventHandler {

    // Subscribe to the RegisterCommandsEvent
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        // Register the "opengui" command
        dispatcher.register(Commands.literal("opengui")
                .executes(context -> {
                    // Get the player who issued the command and open the GUI
                    // context.getSource().getPlayerOrException().openMenu((INamedContainerProvider) new CustomGuiScreen());
                    return 1; // Return a successful result
                })
        );
    }
}