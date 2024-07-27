package org.thesalutyt.storyverse.api.features;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.SVEnvironment;
import org.thesalutyt.storyverse.api.environment.js.interpreter.Interpreter;
import org.thesalutyt.storyverse.api.environment.js.mod.ModInterpreter;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class RootJS extends ScriptableObject implements EnvResource, JSResource {
    public static String getVersion() {
        return SVEngine.MOD_VERSION;
    }

    public static String getAssetsDir() {
        return SVEngine.ASSETS_DIR;
    }

    public static String getRootDir() {
        return SVEngine.SCRIPTS_PATH;
    }

    public static String getGameDir() {
        return SVEngine.GAME_DIR;
    }

    public static String getCharacterColor() {
        return SVEngine.CHARACTER_COLOR_STR;
    }

    public static void setCharacterColor(String color) {
        if (Objects.equals(color, "reset")) {
            SVEngine.CHARACTER_COLOR_STR = "§3";
            return;
        }
        SVEngine.CHARACTER_COLOR_STR = color;
    }

    public static String getCharacterColorStr() {
        return SVEngine.CHARACTER_COLOR_STR;
    }

    public static Interpreter getInterpreter() {
        return SVEngine.interpreter;
    }

    public static ModInterpreter getModInterpreter() {
        return SVEngine.modInterpreter;
    }

    public static Integer getCurrentTick() {
        return SVEnvironment.Root.ticks;
    }

    public static void resetRootTick() {
        SVEnvironment.Root.resetTick();
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        RootJS ef = new RootJS();
        ef.setParentScope(scope);

        try {
            Method getVersion = RootJS.class.getMethod("getVersion");
            methodsToAdd.add(getVersion);
            Method getAssetsDir = RootJS.class.getMethod("getAssetsDir");
            methodsToAdd.add(getAssetsDir);
            Method getRootDir = RootJS.class.getMethod("getRootDir");
            methodsToAdd.add(getRootDir);
            Method getGameDir = RootJS.class.getMethod("getGameDir");
            methodsToAdd.add(getGameDir);
            Method getCharacterColor = RootJS.class.getMethod("getCharacterColor");
            methodsToAdd.add(getCharacterColor);
            Method setCharacterColor = RootJS.class.getMethod("setCharacterColor", String.class);
            methodsToAdd.add(setCharacterColor);
            Method getCharacterColorStr = RootJS.class.getMethod("getCharacterColorStr");
            methodsToAdd.add(getCharacterColorStr);
            Method getInterpreter = RootJS.class.getMethod("getInterpreter");
            methodsToAdd.add(getInterpreter);
            Method getModInterpreter = RootJS.class.getMethod("getModInterpreter");
            methodsToAdd.add(getModInterpreter);
            Method getCurrentTick = RootJS.class.getMethod("getCurrentTick");
            methodsToAdd.add(getCurrentTick);
            Method resetRootTick = RootJS.class.getMethod("resetRootTick");
            methodsToAdd.add(resetRootTick);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        scope.put("root", scope, ef);
    }

    @Override
    public String getClassName() {
        return "RootJS";
    }

    @Override
    public String getResourceId() {
        return "RootJS";
    }
}
