package org.thesalutyt.storyverse.api.screen.gui.script;

import net.minecraft.client.Minecraft;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Gui;
import org.thesalutyt.storyverse.api.screen.color.Color;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiButton;
import org.thesalutyt.storyverse.api.screen.gui.elements.GuiLabel;
import org.thesalutyt.storyverse.api.screen.gui.resource.GuiType;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ScriptableGui extends ScriptableObject implements EnvResource, JSResource {
    public static GuiType toGuiType(String type) {
        switch (type) {
            case "default":
                return GuiType.DEFAULT;
            case "test":
                return GuiType.TEST;
            case "dialogue":
                return GuiType.DIALOGUE;
            case "custom":
                return GuiType.CUSTOM;
            default:
                return null;
        }
    }
    public ScriptableGui create(String name, String type, Integer width, Integer height) {
        Gui.create_gui(name, width, height);
        Gui.type = toGuiType(type);
        return this;
    }
    public static void setType(String type) {
        Gui.type = toGuiType(type);
    }
    public static void setPause(Boolean pause) {
        Gui.setPause(pause);
    }
    public static void setBackGround(String background) {
        Gui.setBackGround(background);
    }
    public static void addMob(String mobId) {
        Gui.addMob(mobId);
    }
    public static void addButton(String buttonId) {
        Gui.addButton(buttonId);
    }
    public static void addLabel(String labelId) {
        Gui.addLabel(labelId);
    }
    public static void addCircleRect(Double x, Double y, Double x1, Double y1, Double radius, Integer color) {
        Gui.addCircle(x, y, x1, y1, radius, color);
    }
    public static void setEntityPos(Integer x, Integer y) {
        Gui.setEntityPos(x, y);
    }
    public static void getWidth() {
        Gui.getWidth();
    }
    public static void getHeight() {
        Gui.getHeight();
    }
    public static String createButton(Double x, Double y, Double width, Double height, String text, BaseFunction onClick) {
        return new GuiButton(x, y, width, height, text, onClick).message;
    }
    public static String createLabel(Double x, Double y, Double width, Double height, String text, Integer size,
                                     Boolean centered) {
        return new GuiLabel((Integer) x.intValue(), (Integer) y.intValue(), width, height, text, size, centered).message;
    }
    public static void render(String guiId) {
        Minecraft.getInstance().setScreen(new Gui());
    }
    public static void close() {
        Minecraft.getInstance().setScreen(null);
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        ScriptableGui sc = new ScriptableGui();
        sc.setParentScope(scope);

        try {
            Method create = ScriptableGui.class.getMethod("create", String.class, String.class, Integer.class, Integer.class);
            methodsToAdd.add(create);
            Method setPause = ScriptableGui.class.getMethod("setPause", Boolean.class);
            methodsToAdd.add(setPause);
            Method setBackGround = ScriptableGui.class.getMethod("setBackGround", String.class);
            methodsToAdd.add(setBackGround);
            Method addMob = ScriptableGui.class.getMethod("addMob", String.class);
            methodsToAdd.add(addMob);
            Method addButton = ScriptableGui.class.getMethod("addButton", String.class);
            methodsToAdd.add(addButton);
            Method addLabel = ScriptableGui.class.getMethod("addLabel", String.class);
            methodsToAdd.add(addLabel);
            Method addCircleRect = ScriptableGui.class.getMethod("addCircleRect", Double.class, Double.class, Double.class, Double.class, Double.class, Integer.class);
            methodsToAdd.add(addCircleRect);
            Method setEntityPos = ScriptableGui.class.getMethod("setEntityPos", Integer.class, Integer.class);
            methodsToAdd.add(setEntityPos);
            Method getWidth = ScriptableGui.class.getMethod("getWidth");
            methodsToAdd.add(getWidth);
            Method getHeight = ScriptableGui.class.getMethod("getHeight");
            methodsToAdd.add(getHeight);
            Method createButton = ScriptableGui.class.getMethod("createButton", Double.class, Double.class, Double.class, Double.class, String.class,
                    BaseFunction.class);
            methodsToAdd.add(createButton);
            Method createLabel = ScriptableGui.class.getMethod("createLabel", Double.class, Double.class, Double.class, Double.class, String.class,
                    Integer.class, Boolean.class);
            methodsToAdd.add(createLabel);
            Method render = ScriptableGui.class.getMethod("render", String.class);
            methodsToAdd.add(render);
            Method close = ScriptableGui.class.getMethod("close");
            methodsToAdd.add(close);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(), m, sc);
            sc.put(m.getName(), sc, methodInstance);
        }
        sc.put("gui", scope, sc);
    }
    @Override
    public String getClassName() {
        return ScriptableGui.class.getName();
    }

    @Override
    public String getResourceId() {
        return "GuiScript";
    }
}
