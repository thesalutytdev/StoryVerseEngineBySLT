package org.thesalutyt.storyverse.api.environment.file;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.thesalutyt.storyverse.api.environment.action.ActPlayer;
import org.thesalutyt.storyverse.api.environment.action.ActionPacket;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.features.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileManager implements EnvResource {
    private static ServerPlayerEntity player = (ServerPlayerEntity) Server.getPlayer();
    private static ActionPacket actionPacket = new ActionPacket(player);
    private static ActPlayer actPlayer = new ActPlayer(player, new EventManager());
    public static void scriptWork(String filePathFull) throws FileNotFoundException {
        File script = new File(filePathFull);
        Scanner scanner = new Scanner(script);
        StringBuilder starter_file_code = new StringBuilder();
        if (filePathFull.startsWith("starter")) {
            while (scanner.hasNextLine()) {
                starter_file_code.append(scanner.nextLine()).append("\n");
            }
            actionPacket.registerOnWorldStart(starter_file_code.toString());
        } else {
            StringBuilder file_code = new StringBuilder();
            while (scanner.hasNextLine()) {
                file_code.append(scanner.nextLine()).append("\n");
            }
            actionPacket.register(file_code.toString());
        }
    }
    public static String readFile(String fileFullPath) throws FileNotFoundException {
        File file = new File(fileFullPath);
        Scanner scanner = new Scanner(file);
        StringBuilder fileContains = new StringBuilder();
        while (scanner.hasNextLine()) {
            fileContains.append(scanner.nextLine());
        }

        return fileContains.toString();
    }

    @Override
    public String getResourceId() {
        return "FileManager";
    }
}
