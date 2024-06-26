package org.thesalutyt.storyverse.api.features;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.utils.RenderUtils;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Gui extends ScriptableObject implements EnvResource {

    public void drawRect() {
        RenderUtils.drawCircleRect(0, 0, 500, 500, 4, new Color(110, 120, 130).getRGB());
    }

    public static void putIntoScope (Scriptable scope) {
        Gui ef = new Gui();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();
        try {
            Method drawRect = Gui.class.getMethod("drawRect");
            methodsToAdd.add(drawRect);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("Gui", scope, ef);
    }

    @Override
    public String getResourceId() {
        return "Gui";
    }

    @Override
    public String getClassName() {
        return "Gui";
    }
}