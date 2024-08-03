package org.thesalutyt.storyverse.api.environment.js.waiter;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class WaitConditionJS extends ScriptableObject implements EnvResource, JSResource {
    public WaitCondition waiter;

    public WaitConditionJS wait(Boolean condition, BaseFunction action) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            ArrayList<BaseFunction> actions = new ArrayList<>();
            actions.add(action);
            this.waiter = new WaitCondition(condition, actions);
        });

        return this;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        WaitConditionJS ef = new WaitConditionJS();
        ef.setParentScope(scope);
        try {
            Method block = WaitConditionJS.class.getMethod("wait", Boolean.class, BaseFunction.class);
            methodsToAdd.add(block);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("condition", scope, ef);
    }

    @Override
    public String getClassName() {
        return "WaitConditionJS";
    }

    @Override
    public String getResourceId() {
        return "WaitConditionJS";
    }
}
