package org.thesalutyt.storyverse.api.environment.js.event;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.special.FadeScreen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EventManagerJS extends ScriptableObject implements EnvResource, JSResource {
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
        scope.put("event", scope, ef);
    }
    @Override
    public String getResourceId() {
        return "EventManagerJS";
    }

    @Override
    public String getClassName() {
        return "EventManagerJS";
    }
}
