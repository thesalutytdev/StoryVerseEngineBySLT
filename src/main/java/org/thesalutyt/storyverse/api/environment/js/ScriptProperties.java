package org.thesalutyt.storyverse.api.environment.js;

import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.features.Time;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class ScriptProperties extends ScriptableObject implements EnvResource, JSResource {
    public static String worldStarterScript;
    public static Boolean ran = false;
    public static Time.ITime delay = new Time.ITime(1000.0);

    static {
        if (!Objects.equals(SVEngine.WORLD_STARTER_SCRIPT, "%NULL%")) {
            worldStarterScript = SVEngine.WORLD_STARTER_SCRIPT;
        }
    }

    public static void onWorldStart(String script_name, Boolean is) {
        if (!is) {
            return;
        } else {
            worldStarterScript = script_name;
        }
    }

    public static void setDelay(Object delay) {
        if (!(delay instanceof Time.ITime)) throw new RuntimeException("Delay must be an instance of Time.ITime");

        ScriptProperties.delay = (Time.ITime) delay;
    }

    public static void run() {
        if (ran) return;
        EventLoop.getLoopInstance().runTimeout(() -> {
            Script.runScript(worldStarterScript);
            ran = true;
        }, (int) delay.milliSeconds);
    }

    public static void resetWorldStart() {
        worldStarterScript = null;
    }

    public static void putIntoScope(Scriptable scope) {
        ScriptProperties ef = new ScriptProperties();
        ArrayList<Method> methodsToAdd = new ArrayList<>();
        ef.setParentScope(scope);
        try {
            Method onWorldJoined = ScriptProperties.class.getMethod("onWorldStart", String.class, Boolean.class);
            methodsToAdd.add(onWorldJoined);
            Method resetWorldStart = ScriptProperties.class.getMethod("resetWorldStart");
            methodsToAdd.add(resetWorldStart);
            Method run = ScriptProperties.class.getMethod("run");
            methodsToAdd.add(run);
            Method setDelay = ScriptProperties.class.getMethod("setDelay", Object.class);
            methodsToAdd.add(setDelay);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("properties", scope, ef);
    }
    @Override
    public String getClassName() {
        return "ScriptProperties";
    }

    @Override
    public String getResourceId() {
        return "ScriptProperties";
    }
}
