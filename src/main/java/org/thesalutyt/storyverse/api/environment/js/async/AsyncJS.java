package org.thesalutyt.storyverse.api.environment.js.async;

import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class AsyncJS extends ScriptableObject implements EnvResource {
    public static void async(String action) {new Thread(() -> {SVEngine.interpreter.executeString(action);}).start();}
    public static void async(BaseFunction function) {new Thread(() -> {
        EventLoop.getLoopInstance().runImmediate(() -> {
            Context ctx = Context.getCurrentContext();
            function.call(ctx, SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(), new Object[]{});
        });
    });}
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope (Scriptable scope) {
        // Создаем объект, к которому потом будем обращаться
        AsyncJS ef = new AsyncJS();
        try {
            Method factorial = AsyncJS.class.getMethod("async", String.class);
            methodsToAdd.add(factorial);
            Method async = AsyncJS.class.getMethod("async", BaseFunction.class);
            methodsToAdd.add(async);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // Здесь функции укладываются в ExternalFunctions
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        // Здесь ExternalFunctions укладывается в пространство имен верхнего уровня
        scope.put("script", scope, ef);
    }

    @Override
    public String getResourceId() {
        return "AsyncJS";
    }

    @Override
    public String getClassName() {
        return "AsyncJS";
    }
}
