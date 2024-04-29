package org.thesalutyt.storyverse.fs_environment;

import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.ActResult;
import org.thesalutyt.storyverse.fs_environment.instances.ScriptInstance;
import org.thesalutyt.storyverse.logger.SVELogger;

public class Environment {
    public static String environmentId = "basic";
    public static String version = "1";
    public Environment environment = this;

    public Environment() {}

    private ActResult onScenePlay(String sceneName, ScriptInstance scriptInstance) {
        String logPath = SVEngine.GAME_DIR + "/storyverse/logs";
        SVELogger.create_dir(logPath);
        SVELogger.init(String.format("%s_%s", sceneName, "latest"));
        SVELogger.write(logPath, "Queued scene to " + scriptInstance.player.getName().getContents());
        return ActResult.SUCCESS;
    }
}
