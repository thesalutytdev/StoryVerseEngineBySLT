package org.thesalutyt.storyverse.api.environment.js.thread;

import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Delayed extends ScriptableObject implements EnvResource, JSResource {
    private final Script sc = new Script();

    public Delayed() {}

    public Delayed(BaseFunction func, Double delay) {
        new Thread(() -> {
           sc.waitTime(delay.intValue());
           EventLoop.getLoopInstance().runImmediate(() -> {
               Context ctx = Context.getCurrentContext();
               func.call(ctx, SVEngine.interpreter.getScope(),
                       SVEngine.interpreter.getScope(), new Object[]{sc});
           });
        }).start();
    }

    public Delayed add(BaseFunction func, Double delay) {
        return new Delayed(func, delay);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        Delayed ef = new Delayed();
        ef.setParentScope(scope);
        try {
            Method add = Delayed.class.getMethod("add", BaseFunction.class, Double.class);
            methodsToAdd.add(add);
        } catch (Exception e) {
            new ErrorPrinter(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("delay", scope, ef);
    }

    @Override
    public String getClassName() {
        return "Delayed";
    }

    @Override
    public String getResourceId() {
        return "Delayed";
    }
}
