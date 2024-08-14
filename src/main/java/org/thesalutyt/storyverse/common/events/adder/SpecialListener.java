package org.thesalutyt.storyverse.common.events.adder;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.EventListenerHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.common.elements.ICustomElement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = StoryVerse.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpecialListener extends ScriptableObject implements ICustomElement, EnvResource, JSResource {
    public static HashMap<String, ArrayList<BaseFunction>> listeners = new HashMap<>();
    public static ArrayList<String> availableEvents = new ArrayList<>();
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public SpecialListener() {
        availableEvents.add("onMessage");
        availableEvents.add("blockBreak");
        availableEvents.add("key");
        availableEvents.add("onPlayerJoin");
        availableEvents.add("onPlayerLeave");
        availableEvents.add("onPickup");
        availableEvents.add("onSleep");
        availableEvents.add("onRespawn");
        availableEvents.add("onItemDrop");
        availableEvents.add("onItemPickup");
        availableEvents.add("onItemCraft");
    }
    public static void putIntoScope(Scriptable scope) {
        SpecialListener ef = new SpecialListener();
        ef.setParentScope(scope);
        try {
            Method addEventListener = SpecialListener.class.getMethod("addEventListener", String.class, BaseFunction.class);
            methodsToAdd.add(addEventListener);
            Method getAvailableEvents = SpecialListener.class.getMethod("getAvailableEvents");
            methodsToAdd.add(getAvailableEvents);
            Method clear = SpecialListener.class.getMethod("clear");
            methodsToAdd.add(clear);
            Method removeEventListener = SpecialListener.class.getMethod("removeEventListener", String.class);
            methodsToAdd.add(removeEventListener);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("forgeEvent", scope, ef);
    }

    public static void addEventListener(String name, BaseFunction function) {
        new SpecialListener();
        if (availableEvents.contains(name) && !listeners.containsKey(name)) {
            EventLoop.getLoopInstance().runImmediate(() -> {
                ArrayList<BaseFunction> functions = new ArrayList<>();
                functions.add(function);
                listeners.put(name, functions);
            });
        } else {
            return;
        }
    }

    public static NativeArray getAvailableEvents() {
        new SpecialListener();
        return new NativeArray(availableEvents.toArray());
    }

    public static void runEvent(String name, Object[] args) {
        if (availableEvents.contains(name) && listeners.containsKey(name)) {
            EventLoop.getLoopInstance().runImmediate(() -> {
                ArrayList<BaseFunction> arr = listeners.get(name);
                Context ctx = Context.getCurrentContext();
                for (BaseFunction baseFunction : arr) {
                    baseFunction.call(ctx, SVEngine.interpreter.getScope(),
                            SVEngine.interpreter.getScope(), Objects.requireNonNull(args));
                }
            });
        } else {
            return;
        }
    }

    public static void removeEventListener(String name) {
        if (availableEvents.contains(name) && listeners.containsKey(name)) {
            EventLoop.getLoopInstance().runImmediate(() -> {
                listeners.remove(name);
            });
        } else {
            return;
        }
    }

    public static void clear() {
        EventLoop.getLoopInstance().runImmediate(() -> {
            listeners.clear();
        });
    }

    @SubscribeEvent
    public static void onMessage(ClientChatReceivedEvent event) {
        runEvent("onMessage", new Object[]{event.getMessage().getContents()});
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        runEvent("onPlayerJoin", new Object[]{event.getPlayer()});
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        runEvent("onPlayerLeave", new Object[]{event.getPlayer()});
    }

    @SubscribeEvent
    public static void onPickup(PlayerEvent.ItemPickupEvent event) {
        runEvent("onPickup", new Object[]{event.getPlayer().getName().getContents(),
                event.getStack(), event.getStack().getItem()});
    }

    @SubscribeEvent
    public static void onSleep(PlayerSleepInBedEvent event) {
        runEvent("onSleep", new Object[]{event.getPlayer()});
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        runEvent("onRespawn", new Object[]{event.getPlayer()});
    }

    @SubscribeEvent
    public static void onItemDrop(ItemTossEvent event) {
        runEvent("onItemDrop", new Object[]{event.getPlayer(), event.getEntityItem().getItem()});
    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemCraftedEvent event) {
        runEvent("onItemCraft", new Object[]{event.getPlayer(), event.getCrafting(), event.getCrafting().getItem()});
    }

    @SubscribeEvent
    public static void keyPressed(InputEvent.KeyInputEvent event) {
        if (event.isCanceled()) {
            return;
        }
        runEvent("keyPressed", new Object[]{event.getKey()});
    }

    @Override
    public Object getDefaultElement() {
        return EventListenerHelper.class;
    }

    @Override
    public String getClassName() {
        return "SpecialListener";
    }

    @Override
    public boolean userReachable() {
        return true;
    }

    @Override
    public String getResourceId() {
        return "SpecialListener";
    }
}
