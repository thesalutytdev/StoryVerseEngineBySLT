package org.thesalutyt.storyverse.api.environment.js.action;

import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Action extends ScriptableObject implements EnvResource, JSResource {
    public static ArrayList<BaseFunction> onEveryTick = new ArrayList<>();

    public Action() {}

    public static void onEveryTick(BaseFunction f) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onEveryTick.add(f);
        });
    }

    public static void runOnTick(Integer tick) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction f : onEveryTick) {
                f.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{tick});
            }
        });
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        Action ac = new Action();
        ac.setParentScope(scope);

        try {
            Method onTick = Action.class.getMethod("onEveryTick", BaseFunction.class);
            methodsToAdd.add(onTick);
            Method runOnTick = Action.class.getMethod("runOnTick", Integer.class);
            methodsToAdd.add(runOnTick);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ac);
            ac.put(m.getName(), ac, methodInstance);
        }
        scope.put("action", scope, ac);
    }

    @Override
    public String getClassName() {
        return "Action";
    }

    @Override
    public String getResourceId() {
        return "Action";
    }
}
