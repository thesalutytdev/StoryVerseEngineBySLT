package org.thesalutyt.storyverse.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.text.StringTextComponent;

import java.sql.Time;

public class CrashMyGame {
    public CrashMyGame(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("crash").executes((command) -> {
            return crashGame(command.getSource());
        }));
    }
    private static int crashGame(CommandSource source) throws CommandSyntaxException{
        source.sendSuccess(new StringTextComponent("Crashing your game"), true);
        Minecraft.crash(new CrashReport("You asked for this!", new Throwable("Idk, what's this")));

        return 1;
    }
}
