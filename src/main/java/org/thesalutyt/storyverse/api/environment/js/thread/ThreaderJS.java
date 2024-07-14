package org.thesalutyt.storyverse.api.environment.js.thread;

import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.utils.ErrorPrinter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ThreaderJS extends ScriptableObject implements EnvResource, JSResource {
    public Thread self;
    public HashMap<Double, ThreaderJS> threads = new HashMap<>();

    public ThreaderJS(BaseFunction func) {
        this.self = new Thread(() -> {
            EventLoop.getLoopInstance().runImmediate(() -> {
                Context ctx = Context.getCurrentContext();
                func.call(ctx, SVEngine.interpreter.getScope(),
                        SVEngine.interpreter.getScope(), new Object[]{self});
            });
        });
    }

    public ThreaderJS() {}

    public ThreaderJS thread(Double id, BaseFunction func) {
        ThreaderJS th = new ThreaderJS(func);
        threads.put(id, new ThreaderJS(func));
        return th;
    }

    public ThreaderJS start(Double id) {
        threads.get(id).self.start();
        return this;
    }

    public String getName(Double id) {
        return threads.get(id).self.getName();
    }

    public ThreaderJS setName(Double id, String name) {
        threads.get(id).self.setName(name);
        return threads.get(id);
    }

    public ThreaderJS setDaemon(Double id, Boolean daemon) {
        threads.get(id).self.setDaemon(daemon);
        return threads.get(id);
    }

    public ThreaderJS setPriority(Double id, Integer priority) {
        threads.get(id).self.setPriority(priority);
        return threads.get(id);
    }

    public Double getId(Double id) {
        return (double) threads.get(id).self.getId();
    }

    public ThreaderJS interrupt(Double id) {
        try {
            threads.get(id).self.interrupt();
        } catch (Exception e) {
            new ErrorPrinter(e);
        }
        return this;
    }

    public ThreaderJS stop(Double id) {
        try {
            threads.get(id).self.stop();
        } catch (Exception e) {
            new ErrorPrinter(e);
        }
        return this;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        ThreaderJS ef = new ThreaderJS();
        ef.setParentScope(scope);
        try {
            Method thread = ThreaderJS.class.getMethod("thread", Double.class, BaseFunction.class);
            methodsToAdd.add(thread);
            Method start = ThreaderJS.class.getMethod("start", Double.class);
            methodsToAdd.add(start);
            Method getName = ThreaderJS.class.getMethod("getName", Double.class);
            methodsToAdd.add(getName);
            Method setName = ThreaderJS.class.getMethod("setName", Double.class, String.class);
            methodsToAdd.add(setName);
            Method setDaemon = ThreaderJS.class.getMethod("setDaemon", Double.class, Boolean.class);
            methodsToAdd.add(setDaemon);
            Method setPriority = ThreaderJS.class.getMethod("setPriority", Double.class, Integer.class);
            methodsToAdd.add(setPriority);
            Method getId = ThreaderJS.class.getMethod("getId", Double.class);
            methodsToAdd.add(getId);
            Method interrupt = ThreaderJS.class.getMethod("interrupt", Double.class);
            methodsToAdd.add(interrupt);
            Method stop = ThreaderJS.class.getMethod("stop", Double.class);
            methodsToAdd.add(stop);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("thread", scope, ef);
    }

    @Override
    public String getClassName() {
        return "ThreaderJS";
    }

    @Override
    public String getResourceId() {
        return "ThreaderJS";
    }
}
