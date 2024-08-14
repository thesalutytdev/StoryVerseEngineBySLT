package org.thesalutyt.storyverse.api.environment.action;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

public class ActPlayer implements EnvResource {
    private ServerPlayerEntity player;
    EventManager eventManager;
    ActionPacket actionPacket = new ActionPacket(player);

    public ActPlayer(ServerPlayerEntity player, EventManager eventManager) {
        this.player = player;
        this.eventManager = eventManager;
    }

    @Override
    public String getResourceId() {
        return "ActPlayer";
    }
}
