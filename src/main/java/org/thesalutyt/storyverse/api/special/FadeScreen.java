package org.thesalutyt.storyverse.api.special;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.gui.FadeScreenGui;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FadeScreen extends ScriptableObject implements EnvResource, JSResource {

    public static void text(String text, Integer color, Integer time, Integer input, Integer output) {
        FadeScreenGui.fade(text, time, color, input, output);
    }

    public static void fade(Integer color, Integer time, Integer input, Integer output) {
        FadeScreenGui.fade(" ", time, color, input, output);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        FadeScreen ef = new FadeScreen();
        ef.setParentScope(scope);
        try {
            Method fade = FadeScreen.class.getMethod("text", String.class, Integer.class, Integer.class, Integer.class, Integer.class);
            methodsToAdd.add(fade);
            Method fade2 = FadeScreen.class.getMethod("fade", Integer.class, Integer.class, Integer.class, Integer.class);
            methodsToAdd.add(fade2);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("fade", scope, ef);
    }

    @Override
    public String getClassName() {
        return "FadeScreen";
    }

    @Override
    public String getResourceId() {
        return "FadeScreen";
    }
}
