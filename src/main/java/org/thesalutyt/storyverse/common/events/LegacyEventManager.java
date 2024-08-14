package org.thesalutyt.storyverse.common.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.features.Chat;

import java.util.HashMap;
import java.util.UUID;

public class LegacyEventManager {
    private LegacyEventManager eventManager;
    public HashMap<UUID, Boolean> managers = new HashMap<>();
    public HashMap<UUID, EventType> eventWaiting = new HashMap<>();
    public static HashMap<String, Runnable> onMessage = new HashMap<>();
    public static HashMap<String, Runnable> onInteract = new HashMap<>();
    public static HashMap<String, Runnable> onPlayerSleep = new HashMap<>();
    public static HashMap<KeyBinding, Runnable> onNextButtonPress = new HashMap<>();
    public static String waitedMessage;
    public static String waitedMessageNoPlayerTag;
    public static Boolean onJoinActive = false;

    @Documentate(
            desc = "Toggles event manager and returns manager's UUID"
    )
    public UUID toggle(boolean method) {
        UUID managerUUID = UUID.randomUUID();
        managers.put(managerUUID, method);
        return managerUUID;
    }

    @Documentate(
            desc = "Add waiting event for your manager"
    )
    public LegacyEventManager addWaitingEvent(UUID managerUUID, EventType eventType) {
        eventWaiting.put(managerUUID, eventType);
        return this;
    }

    @Documentate(
            desc = "Sets message, what mod will wait for execute actions"
    )
    public LegacyEventManager setWaitedMessage(String message) {
        assert Minecraft.getInstance().player != null;
        waitedMessage = "<" + Minecraft.getInstance().player.getName().getContents() + "> " + message;
        return this;
    }

    @Documentate(
            desc = ""
    )
    public static void onInteractEvent(String actionID) {
        try {
            System.out.println("Interacted with entity: ");
            System.out.println("HashMap size: " + onInteract.size());
            for ( String key : onInteract.keySet() ) {
                System.out.println( key );
            }
            onInteract.get(actionID).run();
            onInteract.remove(actionID);
        } catch (NullPointerException e) {
            System.out.println("Could not execute actions after interact with this error:");
            System.out.println(e.getMessage());
            Chat.sendMessage("error_code.storyverse.scriptFailed");
        }

    }

    @Documentate(
            desc = "Executes actions after receiving packet"
    )
    public static void onMessageEvent(String message) {
        try {
            System.out.println("Ran message event");
            System.out.println("Message -> " + message);
            onMessage.get(message).run();
        } catch (NullPointerException ex) {
            System.out.println("HashMap size: " + onMessage.size());
            for ( String key : onMessage.keySet() ) {
                System.out.println( key );
            }
            System.out.println("[EventManager] Message haven't been register");
        }
    }

    @Documentate(
            desc = "Executes actions after receiving packet"
    )
    public static void onPlayerSleepEvent(String playerName) {
        try {
            onPlayerSleep.get(playerName).run();
        } catch (NullPointerException e) {
            System.out.println("Event haven't register but happened!");
        }

    }

    @Documentate(
            desc = "Executes actions after receiving packet"
    )
    public static void onNextButtonPressEvent(KeyBinding key) {
        try {
            onNextButtonPress.get(key).run();
        } catch (NullPointerException e) {
            System.out.println("Event haven't register but happened!");
        }
    }
}
