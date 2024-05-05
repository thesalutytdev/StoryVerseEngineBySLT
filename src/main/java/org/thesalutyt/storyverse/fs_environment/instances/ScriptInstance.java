package org.thesalutyt.storyverse.fs_environment.instances;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.thesalutyt.storyverse.api.SVEnvironment;
import org.thesalutyt.storyverse.common.events.LegacyEventManager;
import org.thesalutyt.storyverse.fs_environment.ScriptScene;
import org.thesalutyt.storyverse.fs_environment.action.Action;

import java.util.ArrayList;

public class ScriptInstance {
    public final ServerPlayerEntity player;
    public final LegacyEventManager eventManager = new LegacyEventManager();
    private final SVEnvironment env;
    public final ArrayList<Action> actsJs = new ArrayList<>();
    public int curActIndex = 0;
    public boolean inCutscene = false;
    public int ticks;
    int ticksTimer = 0;

    public ScriptInstance(ScriptScene scene, ServerPlayerEntity player) {
        this.player = player;
        this.env = new SVEnvironment(player, this);
    }

    public ScriptInstance(ScriptInstance instance, ServerPlayerEntity player, SVEnvironment env) {
        this.player = player;
        this.env = env;
    }
}
