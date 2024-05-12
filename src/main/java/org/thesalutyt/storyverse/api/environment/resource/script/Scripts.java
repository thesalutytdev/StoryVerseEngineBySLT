package org.thesalutyt.storyverse.api.environment.resource.script;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.ActResult;
import org.thesalutyt.storyverse.api.environment.action.ActPlayer;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.features.Server;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class Scripts implements EnvResource {
    public static ArrayList<String> scripts = new ArrayList<>();
    private static final ServerPlayerEntity player = (ServerPlayerEntity) Server.getPlayer();
    private static final ActPlayer actPlayer = new ActPlayer(player, new EventManager());
    @Documentate(
            desc = "Returns HashMap with all registered scripts"
    )
    public static ArrayList<String> getScripts() {
        return scripts;
    }
    @Documentate(
            desc = "Registers script"
    )
    public static void registerScript(String fileName) {
        if (fileName.endsWith(SVEngine.FILE_FORMAT)) {
            scripts.add(fileName);
        } else {
            String newFileName = fileName + SVEngine.FILE_FORMAT;
            scripts.add(newFileName);
        }
    }
    @Documentate(
            desc = "Runs script"
    )
    public static ActResult runScript(String fileName) {
        if (fileName.endsWith(SVEngine.FILE_FORMAT)) {
            if (scripts.contains(fileName)) {
                SVEngine.interpreter.executeString(String.format("ExternalFunctions.import_file(\"%s\")", fileName));
                return ActResult.SUCCESS;
            }   else {
                return ActResult.NULL_POINTER_EXCEPTION;
            }
        } else {
            return ActResult.WRONG_FILE_FORMAT;
        }
    }
    @Override
    public String getResourceId() {
        return "ScriptsBackend";
    }
}
