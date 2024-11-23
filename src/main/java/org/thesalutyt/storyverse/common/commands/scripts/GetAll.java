package org.thesalutyt.storyverse.common.commands.scripts;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.thesalutyt.storyverse.SVEngine;

import java.io.File;
import java.util.ArrayList;

public class GetAll {
    public GetAll(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("scripts")
                .then(Commands.literal("get-all")
                        .executes((command) -> {
                            return getAll(command.getSource());
                        })));
    }

    public int getAll(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        File folder = new File(SVEngine.SCRIPTS_PATH);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> dirs = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();

        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    files.add(listOfFile.getName());
                } else if (listOfFile.isDirectory()) {
                    dirs.add(listOfFile.getName());
                }
            }
        }

        player.sendMessage(new TranslationTextComponent("text.storyverse.directories"), player.getUUID());
        dirs.forEach(dir -> {
            player.sendMessage(new StringTextComponent(String.format(" | - %s", dir)), player.getUUID());
        });

        player.sendMessage(new TranslationTextComponent("text.storyverse.files"), player.getUUID());
        files.forEach(file -> {
            player.sendMessage(new StringTextComponent(String.format(" | - %s", file)), player.getUUID());
        });


        return 1;
    }

}
