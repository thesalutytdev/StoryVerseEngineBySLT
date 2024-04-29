package org.thesalutyt.storyverse.api.environment.resource.script;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.action.ActPlayer;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.file.FileManager;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.features.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Scripts implements EnvResource {
    public static HashMap<Path, String> scripts = new HashMap<>();
    private static final ServerPlayerEntity player = (ServerPlayerEntity) Server.getPlayer();
    private static final ActPlayer actPlayer = new ActPlayer(player, new EventManager());
    @Documentate(
            desc = "Returns HashMap with all registered scripts"
    )
    public static HashMap<Path, String> getScripts() {
        return scripts;
    }

    @Override
    public String getResourceId() {
        return "ScriptsBackground";
    }
}
