package org.thesalutyt.storyverse.common.commands.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import org.thesalutyt.storyverse.StoryVerse;

public class SetHome {
    public SetHome(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("home").then(Commands.literal("set").executes((command) -> {
            return setHome(command.getSource());
        })));
    }
    public static String homePositionCords = "";
    public static Integer homePosX = 0;
    public static Integer homePosY = -1000;
    public static Integer homePosZ = 0;
    private int setHome(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        BlockPos playerPos = player.blockPosition();
        String position = String.format("(%s, %s, %s)", playerPos.getX(), playerPos.getY(), playerPos.getZ());
        homePositionCords = position;
        homePosX = playerPos.getX();
        homePosY = playerPos.getY();
        homePosZ = playerPos.getZ();

        player.getPersistentData().putIntArray(StoryVerse.MOD_ID + "homepos",
                new int[]{ playerPos.getX(), playerPos.getY(), playerPos.getZ() });
        // player.setRespawnPosition(World.OVERWORLD, playerPos.getX(), playerPos.getY(), playerPos.getZ());

        source.sendSuccess(new StringTextComponent("Set home at " + position), true);
        return 1;
    }
}
