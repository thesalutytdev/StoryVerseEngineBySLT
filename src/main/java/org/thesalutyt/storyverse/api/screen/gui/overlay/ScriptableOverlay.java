package org.thesalutyt.storyverse.api.screen.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.screen.gui.elements.*;
import org.thesalutyt.storyverse.common.events.ModEvents;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class ScriptableOverlay extends ScriptableObject implements EnvResource, JSResource {
    public OverlayGui gui;
    public String id;
    public ArrayList<BaseFunction> onTick = new ArrayList<>();
    public ArrayList<BaseFunction> onRender = new ArrayList<>();

    public ScriptableOverlay create(String name, Integer mode) {
        gui = new OverlayGui(Minecraft.getInstance());
        this.id = name;

        gui.setRenderMode(mode);

        gui.setOnRender(() -> {
            if (onRender.isEmpty()) return;
            EventLoop.getLoopInstance().runImmediate(() -> {
                for (BaseFunction f : onRender) {
                    Context ctx = Context.getCurrentContext();
                    Object[] args = new Object[]{gui.getTicks()};
                    if (ModEvents.inWorld) {
                        f.call(ctx, SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(), args);
                    } else {
                        f.call(ctx, SVEngine.modInterpreter.getScope(), SVEngine.modInterpreter.getScope(), args);
                    }
                }
            });
        });

        gui.setOnTick(() -> {
            if (onTick.isEmpty()) return;
            EventLoop.getLoopInstance().runImmediate(() -> {
                for (BaseFunction f : onTick) {
                    Context ctx = Context.getCurrentContext();
                    Object[] args = new Object[]{gui.getTicks()};
                    if (ModEvents.inWorld) {
                        f.call(ctx, SVEngine.interpreter.getScope(), SVEngine.interpreter.getScope(), args);
                    } else {
                        f.call(ctx, SVEngine.modInterpreter.getScope(), SVEngine.modInterpreter.getScope(), args);
                    }
                }
            });
        });

        return this;
    }

    public ScriptableOverlay addMob(String mobId, Double x, Double y, Double size) {
        gui.getEntities().add(new GuiDisplayEntity(mobId, x, y, size));
        return this;
    }

    public ScriptableOverlay addLabel(String labelId) {
        gui.getLabels().add(GuiLabel.labels.get(labelId));
        return this;
    }

    public ScriptableOverlay addCircleRect(Double x, Double y, Double x1, Double y1, Double radius, Integer color) {
        gui.getCircles().add(new CircleRect(x.floatValue(),
                y.floatValue(),
                x1.floatValue(),
                y1.floatValue(),
                radius.intValue(),
                color));
        return this;
    }

    public Integer getWidth() {
        return gui.getWidth();
    }

    public Integer getHeight() {
        return gui.getHeight();
    }

    public Long toDouble(Double long_) {
        return long_.longValue();
    }

    public Integer getMouseX() {
        return gui.getMouseX();
    }

    public Integer getMouseY() {
        return gui.getMouseY();
    }

    public String createLabel(Double x, Double y, Double width, Double height, String text, Integer size,
                              Boolean centered) {
        return new GuiLabel((Integer) x.intValue(), (Integer) y.intValue(), width, height, text, size, centered).message;
    }

    public static void remove(String type, String id) {
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
    }

    public GuiImage addImage(String path, Integer x, Integer y, Integer width, Integer height, Boolean centered) {
        GuiImage img = new GuiImage(path, x, y, width, height, centered);
        gui.getImages().add(img);
        return img;
    }

    public ScriptableOverlay onGuiTick(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onTick.add(function);
        });

        return this;
    }

    public ScriptableOverlay setOnRender(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onRender.add(function);
        });
        return this;
    }

    public ScriptableOverlay setOnTick(BaseFunction function) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onTick.add(function);
        });
        return this;
    }

    public void addItem(String stack, Double x, Double y) {
        gui.getItems().add(new GuiItem(JSItem.getStack(stack), x, y));
    }

    public void render() {
        OverlayHandler.registerOverlay(id, gui);
    }

    public void close() {
        OverlayHandler.removeOverlay(id);
    }

    public void remove() {
        OverlayHandler.removeOverlay(id);
    }

    public void disable() {
        OverlayHandler.disableOverlay(id);
    }

    public void enable() {
        OverlayHandler.enableOverlay(id);
    }

    public void tick() {
        gui.tick();
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        ScriptableOverlay overlay = new ScriptableOverlay();
        overlay.setParentScope(scope);

        try {
            Method create = ScriptableOverlay.class.getMethod("create", String.class, Integer.class);
            methodsToAdd.add(create);
            Method addLabel = ScriptableOverlay.class.getMethod("addLabel", String.class);
            methodsToAdd.add(addLabel);
            Method addCircleRect = ScriptableOverlay.class.getMethod("addCircleRect", Double.class, Double.class, Double.class, Double.class, Double.class, Integer.class);
            methodsToAdd.add(addCircleRect);
            Method addMob = ScriptableOverlay.class.getMethod("addMob", String.class, Double.class, Double.class, Double.class);
            methodsToAdd.add(addMob);
            Method addImage = ScriptableOverlay.class.getMethod("addImage", String.class, Integer.class, Integer.class, Integer.class, Integer.class, Boolean.class);
            methodsToAdd.add(addImage);
            Method createLabel = ScriptableOverlay.class.getMethod("createLabel", Double.class, Double.class, Double.class, Double.class, String.class, Integer.class, Boolean.class);
            methodsToAdd.add(createLabel);
            Method close = ScriptableOverlay.class.getMethod("close");
            methodsToAdd.add(close);
            Method render = ScriptableOverlay.class.getMethod("render");
            methodsToAdd.add(render);
            Method tick = ScriptableOverlay.class.getMethod("tick");
            methodsToAdd.add(tick);
            Method onGuiTick = ScriptableOverlay.class.getMethod("onGuiTick", BaseFunction.class);
            methodsToAdd.add(onGuiTick);
            Method setOnRender = ScriptableOverlay.class.getMethod("setOnRender", BaseFunction.class);
            methodsToAdd.add(setOnRender);
            Method setOnTick = ScriptableOverlay.class.getMethod("setOnTick", BaseFunction.class);
            methodsToAdd.add(setOnTick);
            Method getWidth = ScriptableOverlay.class.getMethod("getWidth");
            methodsToAdd.add(getWidth);
            Method getHeight = ScriptableOverlay.class.getMethod("getHeight");
            methodsToAdd.add(getHeight);
            Method getMouseX = ScriptableOverlay.class.getMethod("getMouseX");
            methodsToAdd.add(getMouseX);
            Method getMouseY = ScriptableOverlay.class.getMethod("getMouseY");
            methodsToAdd.add(getMouseY);
            Method enable = ScriptableOverlay.class.getMethod("enable");
            methodsToAdd.add(enable);
            Method disable = ScriptableOverlay.class.getMethod("disable");
            methodsToAdd.add(disable);
            Method addItem = ScriptableOverlay.class.getMethod("addItem", String.class, Double.class, Double.class);
            methodsToAdd.add(addItem);
            Method remove = ScriptableOverlay.class.getMethod("remove");
            methodsToAdd.add(remove);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, overlay);
            overlay.put(m.getName(), overlay, methodInstance);
        }

        scope.put("overlay", scope, overlay);
    }


    @Override
    public String getClassName() {
        return "ScriptableOverlay";
    }

    @Override
    public String getResourceId() {
        return "ScriptableOverlay";
    }

    @Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID,
    value = Dist.CLIENT)
    public static class OverlayHandler {
        public static HashMap<String, OverlayGui> overlayMap = new HashMap<>();
        public static HashMap<OverlayGui, Boolean> overlays = new HashMap<>();
        public static ArrayList<OverlayGui> toRender = new ArrayList<>();

        public static void registerOverlay(String overlayId, OverlayGui gui) {
            if (overlayMap.containsKey(overlayId)
                    || overlayMap.containsValue(gui)) {
                overlayMap.remove(overlayId);
                overlays.remove(gui);
            }

            overlayMap.put(overlayId, gui);
            overlays.put(gui, true);
        }

        public static OverlayGui getOverlay(String overlayId) {
            if (!overlayMap.containsKey(overlayId))
                throw new IllegalArgumentException("Overlay not registered: " + overlayId);
            return overlayMap.get(overlayId);
        }

        public static boolean isActive(String overlayId) {
            if (!overlayMap.containsKey(overlayId))
                throw new IllegalArgumentException("Overlay not registered: " + overlayId);
            return overlays.get(overlayMap.get(overlayId));
        }

        public static boolean isActive(OverlayGui gui) {
            return overlays.get(gui);
        }

        public static void disableOverlay(String overlayId) {
            if (!overlayMap.containsKey(overlayId))
                throw new IllegalArgumentException("Overlay not registered: " + overlayId);
            overlays.remove(overlayMap.get(overlayId));
            overlays.put(overlayMap.get(overlayId), false);

            toRender.remove(overlayMap.get(overlayId));
        }

        public static void enableOverlay(String overlayId) {
            if (!overlayMap.containsKey(overlayId))
                throw new IllegalArgumentException("Overlay not registered: " + overlayId);
            overlays.remove(overlayMap.get(overlayId));
            overlays.put(overlayMap.get(overlayId), true);

            toRender.add(overlayMap.get(overlayId));
        }

        public static void removeOverlay(String overlayId) {
            if (!overlayMap.containsKey(overlayId))
                throw new IllegalArgumentException("Overlay not registered: " + overlayId);
            overlays.remove(overlayMap.get(overlayId));
            overlayMap.remove(overlayId);

            toRender.remove(overlayMap.get(overlayId));
        }

        @SubscribeEvent
        public static void onGameOverlayRender(RenderGameOverlayEvent.Post event) {
            if (overlayMap.isEmpty() || overlays.isEmpty()) return;

            Iterator<OverlayGui> iterator = toRender.iterator();
            while (iterator.hasNext()) {
                try {
                    OverlayGui gui = iterator.next();
                    if (!overlays.get(gui)) continue;
                    gui.renderItems(event.getMatrixStack());
                } catch (ConcurrentModificationException ignored) {
                }
            }
        }

        @SubscribeEvent
        public static void onTick(TickEvent.ClientTickEvent event) {
            if (overlayMap.isEmpty() || overlays.isEmpty() || event.phase == TickEvent.Phase.END) return;

            overlays.forEach((gui, state) -> {
                if (!state) return;
                gui.tick();
            });
        }
    }

}
