package org.thesalutyt.storyverse.common.commands.scripts;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class PlayerFuncsDebug {
    private PlayerEntity player;

    public PlayerFuncsDebug(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("player_debug")/*.then(Commands.literal("clear")*/.executes((command) -> {
            return playerDebug(command.getSource());
        })/*)*/);
    }
    private int playerDebug(CommandSource source) throws CommandSyntaxException {
        /* CODE LOWER IS FROM CHATGPT */
        this.player.sendMessage(new StringTextComponent("test"), player.getUUID());
        //this.player.sendMessage(new StringTextComponent("Test message::Тестовое сообщение"), Util.NIL_UUID);
        //this.player.displayClientMessage(new StringTextComponent("Test"), true);
        //source.sendSuccess(new StringTextComponent("§a§lSuccess"), true);

        return 1;
    }
}
