package org.thesalutyt.storyverse.api.features;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;


public class BackgroundScript implements EnvResource {
    public final Logger LOGGER = LogManager.getLogger("StoryVerse::BackgroundScript");
    public final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    public BackgroundScript() {}

    @Override
    public String getResourceId() {
        return "BackgroundScript";
    }
}