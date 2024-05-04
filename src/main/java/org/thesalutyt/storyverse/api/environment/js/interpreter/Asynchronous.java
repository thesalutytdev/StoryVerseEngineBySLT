package org.thesalutyt.storyverse.api.environment.js.interpreter;

import org.mozilla.javascript.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Asynchronous extends ScriptableObject {
    private final Scriptable scope;
    private final EventLoop loop;
    private int timeoutCounter = 0;
    private final HashMap<Integer, Integer> timeoutOnLoop = new HashMap<>();
    private int intervalCounter = 0;
    private final HashMap<Integer, Integer> intervalOnLoop = new HashMap<>();

    public Asynchronous (Scriptable scope, EventLoop loop) {
        this.scope = scope;
        this.loop = loop;
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    public static void putIntoScope(Scriptable scope, EventLoop loop) {
        Asynchronous as = new Asynchronous(scope, loop);
        as.setParentScope(scope);

        ArrayList<Method> methodsToAdd = new ArrayList<>();

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

        as.put("__timeouts__", as, Context.getCurrentContext().newObject(as));
        as.put("__intervals__", as, Context.getCurrentContext().newObject(as));

        scope.put("Async", scope, as);
    }

    public Integer setTimeout(BaseFunction fn, Integer delay) {
        Integer localId = this.timeoutCounter;
        this.timeoutCounter++;
        if (this.timeoutCounter == Integer.MAX_VALUE) {
            this.timeoutCounter = 0;
        }
        Scriptable tObj = (Scriptable) this.get("__timeouts__");
        tObj.put(localId, tObj, fn);
        Integer loopId = loop.runTimeout(() -> {
            Context ctx = Context.getCurrentContext();
            ctx.evaluateString(
                    scope,
                    "Async.__timeouts__[" + localId + "]();",
                    "Async",
                    1,
                    null
            );
            timeoutOnLoop.remove(localId);
            tObj.delete(localId);
        }, delay);
        timeoutOnLoop.put(localId, loopId);

        return localId;
    }

    public Void clearTimeout(Integer id) {
        Integer loopId = timeoutOnLoop.get(id);
        if (loopId != null) {
            loop.resetTimeout(loopId);
            Scriptable tObj = (Scriptable) this.get("__timeouts__");
            tObj.delete(id);
        }
        return null;
    }

    public Integer setInterval(BaseFunction fn, Integer delay) {
        Integer localId = this.intervalCounter;
        this.intervalCounter++;
        if (this.intervalCounter == Integer.MAX_VALUE) {
            this.intervalCounter = 0;
        }
        Scriptable tObj = (Scriptable) this.get("__intervals__");
        tObj.put(localId, tObj, fn);
        Integer loopId = loop.runInterval(() -> {
            Context ctx = Context.getCurrentContext();
            ctx.evaluateString(
                    scope,
                    "Async.__intervals__[" + localId + "]();",
                    "Async",
                    1,
                    null
            );
        }, delay);
        intervalOnLoop.put(localId, loopId);

        return localId;
    }

    public Void clearInterval(Integer id) {
        Integer loopId = intervalOnLoop.get(id);
        if (loopId != null) {
            loop.resetInterval(loopId);
            Scriptable tObj = (Scriptable) this.get("__timeouts__");
            tObj.delete(id);
        }
        return null;
    }
}