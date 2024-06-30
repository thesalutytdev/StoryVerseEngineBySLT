package org.thesalutyt.storyverse.api.environment.js.interpreter;

import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Asynchronous extends ScriptableObject {
    @Override
    public String getClassName() {
        return getClass().getName();
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        Asynchronous as = new Asynchronous();
        as.setParentScope(scope);

        try {
            Method setTimeout = Asynchronous.class.getMethod("setTimeout", BaseFunction.class, Integer.class);
            methodsToAdd.add(setTimeout);
            Method clearTimeout = Asynchronous.class.getMethod("clearTimeout", Integer.class);
            methodsToAdd.add(clearTimeout);
            Method setInterval = Asynchronous.class.getMethod("setInterval", BaseFunction.class, Integer.class);
            methodsToAdd.add(setInterval);
            Method clearInterval = Asynchronous.class.getMethod("clearInterval", Integer.class);
            methodsToAdd.add(clearInterval);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, as);
            as.put(m.getName(), as, methodInstance);
        }

        scope.put("Async", scope, as);
    }

    public Integer setTimeout(BaseFunction fn, Integer delay) {
        Runnable callback = () -> {
            Context ctx = Context.getCurrentContext();
            fn.call(ctx, this, this, new Object[0]);
        };

        return EventLoop.getLoopInstance().runTimeout(callback, delay);
    }

    public void clearTimeout(Integer id) {
        EventLoop.getLoopInstance().resetTimeout(id);
    }

    public Integer setInterval(BaseFunction fn, Integer delay) {
        Runnable callback = () -> {
            Context ctx = Context.getCurrentContext();
            fn.call(ctx, this, this, new Object[0]);
        };

        return EventLoop.getLoopInstance().runInterval(callback, delay);
    }

    public void clearInterval(Integer id) {
        EventLoop.getLoopInstance().resetInterval(id);
    }
}