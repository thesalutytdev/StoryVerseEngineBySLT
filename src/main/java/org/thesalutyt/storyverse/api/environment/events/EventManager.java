package org.thesalutyt.storyverse.api.environment.events;

import net.minecraft.client.settings.KeyBinding;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.ActResult;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.keybinds.DefaultBinds;

import java.util.HashMap;
import java.util.UUID;

public class EventManager extends ScriptableObject implements EnvResource, JSResource {
    public static HashMap<UUID, Runnable> onInteract = new HashMap<>();
    public static HashMap<UUID, Runnable> onPlayerSleep = new HashMap<>();
    public static HashMap<KeyBinding, Runnable> onButtonPress = new HashMap<>();
    public static HashMap<String, Runnable> onMessage = new HashMap<>();
    public ActResult runInteract(UUID mobID) {
        try {
            onInteract.get(mobID).run();
            System.out.println("Ran interact event");
            return ActResult.SUCCESS;
        } catch (NullPointerException ex) {
            System.out.println("Interacted with some unregistered entity");
            return ActResult.NULL_POINTER_EXCEPTION;
        }
    }
    public ActResult runMessage(String message) {
        // try {
        //     onMessage.get(message).run();
        //     System.out.println("Ran message event");
        //     return ActResult.SUCCESS;
        // } catch (NullPointerException ex) {
        //     System.out.println("This message(" + message + ") is unregistered");
        //     return ActResult.NULL_POINTER_EXCEPTION;
        // }

        NativeObject handlers = (NativeObject) this.get("__handlers__");
        handlers.get("onMessage");

        return ActResult.SUCCESS;
    }
    public ActResult runOnButtonPress(KeyBinding keyBinding) {
        try {
            onButtonPress.get(keyBinding).run();
            System.out.println("Ran event on button press event");
            return ActResult.SUCCESS;
        } catch (NullPointerException ex) {
            System.out.println("Action for pressing story key is unregistered");
            return ActResult.NULL_POINTER_EXCEPTION;
        }
    }
    public ActResult runOnPlayerSleep(UUID playerUUID) {
        try {
            onPlayerSleep.get(playerUUID).run();
            System.out.println("Ran interact event");
            return ActResult.SUCCESS;
        } catch (NullPointerException ex) {
            System.out.println("Player slept but events didn't happened");
            return ActResult.NULL_POINTER_EXCEPTION;
        }
    }
    public static void setOnMessage(String message, BaseFunction function) {
        onMessage.put(message, (Runnable) function);
    }
    public static void setOnPlayerSleep(String playerUUID, BaseFunction function) {
        onPlayerSleep.put(UUID.fromString(playerUUID), (Runnable) function);
    }
    public static void setOnInteract(String mobID, BaseFunction function) {
        onInteract.put(UUID.fromString(mobID), (Runnable) function);
    }
    public static void setOnButtonPress(BaseFunction function) {
        onButtonPress.put(DefaultBinds.keyStory, (Runnable) function);
    }
    @Override
    public String getClassName() {
        return "EventManager";
    }

    @Override
    public String getResourceId() {
        return "EventManager";
    }
}
