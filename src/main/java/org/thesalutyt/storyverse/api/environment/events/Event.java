package org.thesalutyt.storyverse.api.environment.events;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.thesalutyt.storyverse.common.events.EventType;
import org.thesalutyt.storyverse.common.keybinds.DefaultBinds;

import java.util.UUID;

public class Event {
    EventType eventType;
    private ServerPlayerEntity player;
    public Event(EventType eventType, ServerPlayerEntity player) {
        this.eventType = eventType;
        this.player = player;
    }
    public Event setMessageEvent(String message, Runnable code) {
        EventManager.onMessage.put(message, code);

        return this;
    }
    public Event setInteractEvent(UUID mobId, Runnable code) {
        EventManager.onInteract.put(mobId, code);

        return this;
    }
    public Event setOnSleepEvent(UUID playerUUUD, Runnable code) {
        EventManager.onPlayerSleep.put(playerUUUD, code);

        return this;
    }
    public Event setOnNextButtonPress(KeyBinding keyBinding, Runnable code) {
        EventManager.onButtonPress.put(keyBinding, code);

        return this;
    }
    public Event setOnNextButtonPress(Runnable code) {
        EventManager.onButtonPress.put(DefaultBinds.keyStory, code);

        return this;
    }
}
