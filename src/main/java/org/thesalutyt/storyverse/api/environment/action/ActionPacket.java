package org.thesalutyt.storyverse.api.environment.action;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.thesalutyt.storyverse.api.environment.events.Event;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.common.events.EventType;
import org.thesalutyt.storyverse.common.keybinds.DefaultBinds;

import java.util.HashMap;
import java.util.UUID;

public class ActionPacket implements EnvResource {
    private ServerPlayerEntity player;
    private EventManager eventManager = new EventManager();
    public HashMap<UUID, String> actions = new HashMap<>();
    public String onWorldStart;
    private String messageWaiting;

    public ActionPacket(ServerPlayerEntity player) {
        this.player = player;
    }
    public ActionPacket register(String code) {
        UUID thisActUUID = UUID.randomUUID();
        this.actions.put(thisActUUID, code);

        return this;
    }
    public ActionPacket registerOnWorldStart(String code) {
        onWorldStart += code;
        return this;
    }
    public ActionPacket registerWithEvent(EventType eventType, Runnable code, Object eventArgument) {
        switch (eventType){
            case MESSAGE:
                EventManager.onMessage.put(eventArgument.toString(), code);
            case INTERACT:
                EventManager.onInteract.put(UUID.fromString(eventArgument.toString()), code);
            case ON_PLAYER_SLEEP:
                EventManager.onPlayerSleep.put(player.getUUID(), code);
            case ON_NEXT_BUTTON_PRESS:
                if (eventArgument != DefaultBinds.keyStory){
                    return this;
                }
                else {
                    EventManager.onButtonPress.put(DefaultBinds.keyStory, code);
                }
        }
        return this;
    }
    public ActionPacket WAIT_MESSAGE(String message) {
        this.messageWaiting = message;

        return this;
    }
    public ActionPacket MESSAGE(){
        try {
            eventManager.runMessage(messageWaiting);
        } catch (NullPointerException ex) {
            return this;
        }

        return this;
    }
    public ActionPacket MESSAGE(String messageWaiting){
        try {
            eventManager.runMessage(messageWaiting);
        } catch (NullPointerException ex) {
            return this;
        }

        return this;
    }
    public ActionPacket PLAYER_SLEEP() {
        try {
            eventManager.runOnPlayerSleep(this.player.getUUID());
        } catch (NullPointerException ex) {
            return this;
        }

        return this;
    }
    public ActionPacket KEY() {
        try {
            eventManager.runOnButtonPress(DefaultBinds.keyStory);
        } catch (NullPointerException ex) {
            return this;
        }

        return this;
    }
    public Event getEvent(EventType eventType) {
        return new Event(eventType, player);
    }
    public ActionPacket ON_WORLD_START(String code) {
        this.onWorldStart = code;

        return this;
    }

    @Override
    public String getResourceId() {
        return "ActionPacket";
    }
}
