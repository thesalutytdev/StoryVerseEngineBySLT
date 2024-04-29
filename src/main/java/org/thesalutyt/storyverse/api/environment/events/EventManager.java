package org.thesalutyt.storyverse.api.environment.events;

import net.minecraft.client.settings.KeyBinding;
import org.thesalutyt.storyverse.api.ActResult;

import java.util.HashMap;
import java.util.UUID;

public class EventManager {
    public static HashMap<UUID, Runnable> onInteract = new HashMap<>();
    public static HashMap<UUID, Runnable> onPlayerSleep = new HashMap<>();
    public static HashMap<KeyBinding, Runnable> onButtonPress = new HashMap<>();
    public static HashMap<String, Runnable> onMessage = new HashMap<>();
    public static ActResult runInteract(UUID mobID) {
        try {
            onInteract.get(mobID).run();
            System.out.println("Ran interact event");
            return ActResult.SUCCESS;
        } catch (NullPointerException ex) {
            System.out.println("Interacted with some unregistered entity");
            return ActResult.NULL_POINTER_EXCEPTION;
        }
    }
    public static ActResult runMessage(String message) {
        try {
            onMessage.get(message).run();
            System.out.println("Ran message event");
            return ActResult.SUCCESS;
        } catch (NullPointerException ex) {
            System.out.println("This message(" + message + ") is unregistered");
            return ActResult.NULL_POINTER_EXCEPTION;
        }
    }
    public static ActResult runOnButtonPress(KeyBinding keyBinding) {
        try {
            onButtonPress.get(keyBinding).run();
            System.out.println("Ran event on button press event");
            return ActResult.SUCCESS;
        } catch (NullPointerException ex) {
            System.out.println("Action for pressing story key is unregistered");
            return ActResult.NULL_POINTER_EXCEPTION;
        }
    }
    public static ActResult runOnPlayerSleep(UUID playerUUID) {
        try {
            onPlayerSleep.get(playerUUID).run();
            System.out.println("Ran interact event");
            return ActResult.SUCCESS;
        } catch (NullPointerException ex) {
            System.out.println("Player slept but events didn't happened");
            return ActResult.NULL_POINTER_EXCEPTION;
        }
    }
}
