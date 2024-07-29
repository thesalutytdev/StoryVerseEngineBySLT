package org.thesalutyt.storyverse.forge.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.thesalutyt.storyverse.forge.api.features.managment.Script;

public class MainCommand {
    public MainCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("storyverse")
                .then(Commands.literal("run")
                        .then(Commands.argument("script_name", StringArgumentType.string())
                                .executes((command) -> {
                                    return runScript(command.getSource(), StringArgumentType.getString(command, "script_name"));
                                })
                        ))
        );
    }

    public static int runScript(CommandSourceStack source, String scriptName) throws CommandSyntaxException {
        Script.runScript(scriptName);
        return 1;
    }

}

