package org.thesalutyt.storyverse.common.commands.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import java.util.Set;

public class ClearHome {
    public ClearHome(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("home").then(Commands.literal("clear").executes((command) -> {
            return clearHome(command.getSource());
        })));
    }

    private int clearHome(CommandSource source) throws CommandSyntaxException{
        SetHome.homePosX = 0;
        SetHome.homePosY = -1000;
        SetHome.homePosZ = 0;
        SetHome.homePositionCords = "(0, 0, 0)";

        source.sendSuccess(new StringTextComponent("Cleared your home position!"), true);

        return 1;
    }
}
