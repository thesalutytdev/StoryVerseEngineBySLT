package org.thesalutyt.storyverse.api.features;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Timer extends ScriptableObject implements EnvResource {

    private Long lastMS = System.currentTimeMillis();

    public Integer convertToMS(Integer d) {
        return 1000 / d;
    }
    public Long getCurrentMS()  {
        return System.currentTimeMillis();
    }
    public Boolean hasReached(Double milliseconds ) {
        return ((System.currentTimeMillis() - lastMS) > milliseconds);
    }
    public Boolean hasTimeReached(double delay) {
        return System.currentTimeMillis() - lastMS >= delay;
    }
    public Long getDelay() {
        return System.currentTimeMillis() - lastMS;
    }
    public void reset() {
        lastMS = System.currentTimeMillis();
    }
    public void reset(Long def) {
        this.lastMS = System.currentTimeMillis() - def;
    }
    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }
    public void setLastMS(Long lastMS) {
        this.lastMS = lastMS;
    }
    public Boolean isDelayComplete(Double d) {
        return this.hasReached(d);
    }
    public Boolean hasPassed(Double milliseconds) {
        return ((System.currentTimeMillis() - this.lastMS) > milliseconds);
    }
    public Long getTime() {
        return getCurrentMS() - this.lastMS;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope) {
        Server ef = new Server();
        ef.setParentScope(scope);

        try {
            Method getCurrentMS = Timer.class.getMethod("getCurrentMS");
            methodsToAdd.add(getCurrentMS);
            Method reset = Timer.class.getMethod("reset");
            methodsToAdd.add(reset);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("timer", scope, ef);
    }
    @Override
    public String getResourceId() {
        return "Timer";
    }

    @Override
    public String getClassName() {
        return "timer";
    }
}
