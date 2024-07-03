package org.thesalutyt.storyverse.api.special;

import net.minecraftforge.common.MinecraftForge;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.gui.FadeScreenGui;
import org.thesalutyt.storyverse.utils.TimeHelper;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class FadeScreen extends ScriptableObject implements EnvResource, JSResource {

    private static final FadeScreenGui fadeScreenGui = new FadeScreenGui();
    private static final TimeHelper timer = new TimeHelper();
    public static void fade(String text, Integer color, Integer time) {
        FadeScreenGui.text = text;
        FadeScreenGui.color = color;
        FadeScreenGui.time = time;
        FadeScreenGui.elapsedTicks = 0;
        MinecraftForge.EVENT_BUS.register(FadeScreenGui.class);
    }

    public static void fade(Integer color, Integer time) {
        FadeScreenGui.color = color;
        FadeScreenGui.time = time;
        FadeScreenGui.elapsedTicks = 0;
        MinecraftForge.EVENT_BUS.register(FadeScreenGui.class);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        FadeScreen ef = new FadeScreen();
        ef.setParentScope(scope);
        try {
            Method fade = FadeScreen.class.getMethod("fade", String.class, Integer.class, Integer.class);
            methodsToAdd.add(fade);
            Method fade2 = FadeScreen.class.getMethod("fade", Integer.class, Integer.class);
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
