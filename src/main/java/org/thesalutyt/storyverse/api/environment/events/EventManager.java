package org.thesalutyt.storyverse.api.environment.events;

import net.minecraft.client.settings.KeyBinding;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.ActResult;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.entities.client.events.ClientModEvents;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
        onButtonPress.put(ClientModEvents.keyStory, (Runnable) function);
    }

    public static void putIntoScope (Scriptable scope) {
        EventManager ef = new EventManager();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();

        try {
            Method execOnMessage = EventManager.class.getMethod("setOnMessage", String.class, BaseFunction.class);
            methodsToAdd.add(execOnMessage);
            Method execOnInteract = EventManager.class.getMethod("setOnInteract", String.class, BaseFunction.class);
            methodsToAdd.add(execOnInteract);
            Method execOnSleep = EventManager.class.getMethod("setOnPlayerSleep", String.class, BaseFunction.class);
            methodsToAdd.add(execOnSleep);
            Method execOnButton = EventManager.class.getMethod("setOnButtonPress", BaseFunction.class);
            methodsToAdd.add(execOnButton);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        ef.put("__handlers__", ef, Context.getCurrentContext().newObject(ef));

        scope.put("event", scope, ef);
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
