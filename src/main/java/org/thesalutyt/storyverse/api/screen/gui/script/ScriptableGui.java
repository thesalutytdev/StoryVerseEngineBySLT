package org.thesalutyt.storyverse.api.screen.gui.script;

import net.minecraft.client.Minecraft;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.screen.CustomizableGui;
import org.thesalutyt.storyverse.api.screen.gui.elements.*;
import org.thesalutyt.storyverse.api.screen.gui.resource.GuiType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ScriptableGui extends ScriptableObject implements EnvResource, JSResource {
    public CustomizableGui gui;
    public static HashMap<String, CustomizableGui> guis = new HashMap<>();
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
    public ScriptableGui create(String name, String title, Integer width, Integer height) {
        gui = new CustomizableGui(title);
        gui.gWidth = width;
        gui.gHeight = height;
        gui.init();
        guis.put(name, gui);
        return this;
    }
    public ScriptableGui setPause(Boolean pause) {
        gui.isPause = pause;
        return this;
    }
    public ScriptableGui setBackGround(String background) {
        gui.background = background;
        return this;
    }
    public ScriptableGui addMob(String mobId, Double x, Double y, Double size) {
        gui.entities.add(new GuiDisplayEntity(mobId, x, y, size));
        return this;
    }
    public ScriptableGui addButton(String buttonId) {
        gui.buttons.add(GuiButton.btns.get(buttonId));
        return this;
    }
    public ScriptableGui addLabel(String labelId) {
        gui.labels.add(GuiLabel.labels.get(labelId));
        return this;
    }
    public ScriptableGui addCircleRect(Double x, Double y, Double x1, Double y1, Double radius, Integer color) {
        gui.circleRect.add(new CircleRect(x.floatValue(),
                y.floatValue(),
                x1.floatValue(),
                y1.floatValue(),
                radius.intValue(),
                color));
        return this;
    }
    public Integer getWidth() {
        return gui.width;
    }
    public Integer getHeight() {
        return gui.height;
    }
    public Long toDouble(Double long_) {
        return long_.longValue();
    }
        public Integer getMouseX() {
        return gui.gMouseX;
    }
    public Integer getMouseY() {
        return gui.gMouseY;
    }
    public String createButton(String id, String texture, Double x, Double y, Double width, Double height, String text, BaseFunction onClick) {
        return new GuiButton(id, texture, x, y, width, height, text, onClick).id;
    }
    public String createLabel(Double x, Double y, Double width, Double height, String text, Integer size,
                                     Boolean centered) {
        return new GuiLabel((Integer) x.intValue(), (Integer) y.intValue(), width, height, text, size, centered).message;
    }
    public void render() {
        Minecraft.getInstance().setScreen(gui);
    }
    public void close() {
        Minecraft.getInstance().setScreen(null);
    }
    public ScriptableGui setCloseOnEsc(Boolean closeOnEsc) {
        gui.closeOnEsc = closeOnEsc;
        return this;
    }
    public GuiImage addImage(String path, Integer x, Integer y, Integer width, Integer height, Boolean centered) {
        GuiImage img = new GuiImage(path, x, y, width, height, centered);
        gui.images.add(img);
        return img;
    }
    public ScriptableGui renderBackground(Boolean method) {
        gui.renderBG = method;
        return this;
    }
    public ScriptableGui setCursorPos(Integer x, Integer y) {
        gui.cursorX = x;
        gui.cursorY = y;
        return this;
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
            Method addMob = ScriptableGui.class.getMethod("addMob", String.class, Double.class, Double.class, Double.class);
            methodsToAdd.add(addMob);
            Method addButton = ScriptableGui.class.getMethod("addButton", String.class);
            methodsToAdd.add(addButton);
            Method addLabel = ScriptableGui.class.getMethod("addLabel", String.class);
            methodsToAdd.add(addLabel);
            Method addCircleRect = ScriptableGui.class.getMethod("addCircleRect", Double.class, Double.class, Double.class, Double.class, Double.class, Integer.class);
            methodsToAdd.add(addCircleRect);
            Method getWidth = ScriptableGui.class.getMethod("getWidth");
            methodsToAdd.add(getWidth);
            Method getHeight = ScriptableGui.class.getMethod("getHeight");
            methodsToAdd.add(getHeight);
            Method getMouseX = ScriptableGui.class.getMethod("getMouseX");
            methodsToAdd.add(getMouseX);
            Method getMouseY = ScriptableGui.class.getMethod("getMouseY");
            methodsToAdd.add(getMouseY);
            Method createButton = ScriptableGui.class.getMethod("createButton",
                    String.class, String.class, Double.class, Double.class,
                    Double.class, Double.class, String.class,
                    BaseFunction.class);
            methodsToAdd.add(createButton);
            Method createLabel = ScriptableGui.class.getMethod("createLabel", Double.class, Double.class, Double.class, Double.class, String.class,
                    Integer.class, Boolean.class);
            methodsToAdd.add(createLabel);
            Method render = ScriptableGui.class.getMethod("render");
            methodsToAdd.add(render);
            Method close = ScriptableGui.class.getMethod("close");
            methodsToAdd.add(close);
            Method setCloseOnEsc = ScriptableGui.class.getMethod("setCloseOnEsc", Boolean.class);
            methodsToAdd.add(setCloseOnEsc);
            Method addImage = ScriptableGui.class.getMethod("addImage",
                    String.class, Integer.class, Integer.class,
                    Integer.class, Integer.class, Boolean.class);
            methodsToAdd.add(addImage);
            Method renderBackground = ScriptableGui.class.getMethod("renderBackground", Boolean.class);
            methodsToAdd.add(renderBackground);
            Method setCursorPos = ScriptableGui.class.getMethod("setCursorPos", Integer.class, Integer.class);
            methodsToAdd.add(setCursorPos);
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
