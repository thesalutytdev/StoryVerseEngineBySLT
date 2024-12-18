package org.thesalutyt.storyverse.common.commands.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;

public class ReturnHome {
    public ReturnHome(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("home").then(Commands.literal("return").executes((command) -> {
            return returnHome(command.getSource());
        })));
    }
    private int returnHome(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        boolean hasHomePos = player.getPersistentData().getIntArray(
                StoryVerse.MOD_ID + "homepos"
        ).length != 0;
        if(hasHomePos && SetHome.homePosY != -1000){
            int [] playerPos = player.getPersistentData().getIntArray(StoryVerse.MOD_ID + "homepos");
            player.moveTo(SetHome.homePosX, SetHome.homePosY, SetHome.homePosZ);
            source.sendSuccess(new StringTextComponent("Player returned home!"), true);
            return 1;
        } else{
            source.sendSuccess(new StringTextComponent("You don't have any home!"), true);
            return -1;
        }
    }
}
