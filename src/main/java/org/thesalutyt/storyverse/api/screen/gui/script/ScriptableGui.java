package org.thesalutyt.storyverse.api.screen.gui.script;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Server;
import org.thesalutyt.storyverse.api.screen.CustomizableGui;
import org.thesalutyt.storyverse.api.screen.gui.elements.*;
import org.thesalutyt.storyverse.api.screen.gui.resource.GuiType;
import org.thesalutyt.storyverse.common.specific.networking.Networking;
import org.thesalutyt.storyverse.common.specific.networking.packets.custom.SVGuiPacket;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class ScriptableGui extends ScriptableObject implements EnvResource, JSResource, Serializable {
    public String id;
    public CustomizableGui gui;
    public static HashMap<String, CustomizableGui> guis = new HashMap<>();
    public ArrayList<BaseFunction> onGuiTick = new ArrayList<>();
    public ArrayList<BaseFunction> onGuiClose = new ArrayList<>();
    public Integer ticks = 0;
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
    public ScriptableGui create(String name, Integer width, Integer height) {
        gui = new CustomizableGui(name);
        gui.gWidth = width;
        gui.gHeight = height;
        gui.init();
        gui.setScriptableReference(this);
        guis.put(name, gui);
        this.id = name;
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

    public static ScriptableGui remove(String type, String id) {
        switch (type) {
            case "button":
                GuiButton.btns.remove(id);
                break;
            case "label":
                GuiLabel.labels.remove(id);
                break;
            default:
                break;
        }
        return null;
    }

    public static void render(String name) {
        Minecraft.getInstance().setScreen(guis.get(name));
    }

    public static void render(String player, String name) {
        if (player.equals("$every")) {
            for (ServerPlayerEntity p : Server.getPlayers()) {
                Networking.sendToPlayer(new SVGuiPacket(guis.get(name)), p);
            }
            return;
        }
        Networking.sendToPlayer(new SVGuiPacket(guis.get(name)), Server.getPlayerByName(player));
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

    public ScriptableGui onGuiTick(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onGuiTick.add(function);
        });

        return this;
    }

    public ScriptableGui tick() {
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction function : onGuiTick) {
                ticks++;
                function.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(),
                        new Object[]{ticks});
            }
        });

        return this;
    }

    public ScriptableGui setOnClose(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onGuiClose.add(function);
        });
        return this;
    }

    public ScriptableGui onClose(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onGuiClose.add(function);
        });
        return this;
    }

    public ScriptableGui setOnTick(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onGuiTick.add(function);
        });
        return this;
    }

    public ScriptableGui onTick(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onGuiTick.add(function);
        });
        return this;
    }

    public void runOnClose() {
        EventLoop.getLoopInstance().runImmediate(() -> {
            for (BaseFunction function : onGuiClose) {
                function.call(Context.getCurrentContext(), SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(),
                        new Object[]{});
            }
        });
        guis.remove(this.id);
    }

    public void addItem(String stack, Double x, Double y) {
        gui.items.add(new GuiItem(JSItem.getStack(stack), x, y));
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        ScriptableGui sc = new ScriptableGui();
        sc.setParentScope(scope);

        try {
            Method create = ScriptableGui.class.getMethod("create", String.class, Integer.class, Integer.class);
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
            Method close = ScriptableGui.class.getMethod("close");
            methodsToAdd.add(close);
            Method render = ScriptableGui.class.getMethod("render", String.class);
            methodsToAdd.add(render);
            Method renderToPlayer = ScriptableGui.class.getMethod("render", String.class, String.class);
            methodsToAdd.add(renderToPlayer);
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
            Method onGuiTick = ScriptableGui.class.getMethod("onGuiTick", BaseFunction.class);
            methodsToAdd.add(onGuiTick);
            Method tick = ScriptableGui.class.getMethod("tick");
            methodsToAdd.add(tick);
            Method setOnClose = ScriptableGui.class.getMethod("setOnClose", BaseFunction.class);
            methodsToAdd.add(setOnClose);
            Method onClose = ScriptableGui.class.getMethod("onClose", BaseFunction.class);
            methodsToAdd.add(onClose);
            Method setOnTick = ScriptableGui.class.getMethod("setOnTick", BaseFunction.class);
            methodsToAdd.add(setOnTick);
            Method onTick = ScriptableGui.class.getMethod("onTick", BaseFunction.class);
            methodsToAdd.add(onTick);
            Method runOnClose = ScriptableGui.class.getMethod("runOnClose");
            methodsToAdd.add(runOnClose);
            Method addItem = ScriptableGui.class.getMethod("addItem", String.class, Double.class, Double.class);
            methodsToAdd.add(addItem);
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
