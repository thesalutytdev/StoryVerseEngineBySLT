package org.thesalutyt.storyverse.common.commands.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;

public class GetHomePos {
    public GetHomePos(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("home").then(Commands.literal("get").executes((command) -> {
            return getHomePos(command.getSource());
        })));
    }
    private int getHomePos(CommandSource source) throws CommandSyntaxException{
        ServerPlayerEntity player = source.getPlayerOrException();
        boolean hasHomePos = player.getPersistentData().getIntArray(
                StoryVerse.MOD_ID + "homepos"
        ).length != 0;
        if(hasHomePos && SetHome.homePosY != -1000){
            int [] playerPos = player.getPersistentData().getIntArray(StoryVerse.MOD_ID + "homepos");
            player.setPosAndOldPos(playerPos[0], playerPos[1], playerPos[2]);
            String homePos = SetHome.homePositionCords;

            source.sendSuccess(new StringTextComponent("Your home position is: " + homePos), true);
            return 1;
        } else{
            source.sendSuccess(new StringTextComponent("You don't have any home!"), true);
            return -1;
        }
    }
}
