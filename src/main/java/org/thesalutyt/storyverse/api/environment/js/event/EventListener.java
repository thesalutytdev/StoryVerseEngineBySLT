package org.thesalutyt.storyverse.api.environment.js.event;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.features.Player;
import org.thesalutyt.storyverse.common.entities.client.events.ClientModEvents;
import org.thesalutyt.storyverse.common.events.EventType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class EventListener extends ScriptableObject implements EnvResource {
    public static HashMap<EventType, HashMap<String, ArrayList<BaseFunction>>> events = new HashMap<>();
    // public static WorldWrapper worldWrapper = new WorldWrapper();
    public static HashMap<String, BaseFunction> messageEvent = new HashMap<>();
    public static HashMap<String, BaseFunction> interactEvent = new HashMap<>();
    public static HashMap<String, BaseFunction> playerSleep = new HashMap<>();
    public static HashMap<KeyBinding, BaseFunction> buttonPressEvent = new HashMap<>();
    private static EventLoop loop;
    public EventListener(EventLoop eventLoop) {
        loop = eventLoop;
    }
    public EventType toEventType(String type) {
        switch (type) {
            case "message":
                return EventType.MESSAGE;
            case "interact":
                return EventType.INTERACT;
            case "button":
                return EventType.ON_NEXT_BUTTON_PRESS;
            case "sleep":
                return EventType.ON_PLAYER_SLEEP;
        }
        return null;
    }
    public void newListener(String event, BaseFunction function, String eventArgs) {
        EventType eventType = this.toEventType(event);
        ArrayList<BaseFunction> arrayList = new ArrayList<>();
        HashMap<String, ArrayList<BaseFunction>> evArg = new HashMap<>();
        arrayList.add(function);
        evArg.put(eventArgs, arrayList);
        switch (eventType) {
            // events.put(EventType.MESSAGE, evArg);
            case MESSAGE: {
                if (eventArgs.startsWith(String.format("<%s> ", Player.getPlayerName()))) {
                    messageEvent.put(eventArgs, function);
                } else {
                    messageEvent.put(String.format("<%s> %s", Player.getPlayerName(), eventArgs), function);
                }
            } case INTERACT: {
                // events.put(EventType.INTERACT, eventArgs);
                interactEvent.put(eventArgs, function);
            } case ON_PLAYER_SLEEP: {
                // events.put(EventType.ON_PLAYER_SLEEP, eventArgs);
                playerSleep.put(eventArgs, function);
            } case ON_NEXT_BUTTON_PRESS: {
                switch (eventArgs) {
                    case "DEFAULT": {
                        // events.put(EventType.ON_NEXT_BUTTON_PRESS, eventArgs);
                        buttonPressEvent.put(ClientModEvents.keyStory, function);
                    } case "START_STORY": {
                        // events.put(EventType.ON_NEXT_BUTTON_PRESS, eventArgs);
                        buttonPressEvent.put(ClientModEvents.startStoryButton, function);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent event) {
        this.runEvent(EventType.MESSAGE, event.getMessage().getContents());
        EventManagerJS.last_message = event.getMessage().getContents();
        System.out.println("Got message: " + event.getMessage().getContents());
    }
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.EntityInteract event) {
        this.runEvent(EventType.INTERACT, event.getTarget().getUUID().toString());
        System.out.println("Player interacted: " + event.getTarget() + "\n" + event.getTarget().getUUID());
    }
    @SubscribeEvent
    public void playerSleep(PlayerSleepInBedEvent event) {
        this.runEvent(EventType.ON_PLAYER_SLEEP, event.getPlayer().getUUID().toString());
        System.out.println("Player " + event.getPlayer() + " (" + event.getPlayer().getUUID() + ") slept");
    }
    public void runEvent(EventType eventType, String eventArg) {
        loop.runImmediate(() -> {
            NativeArray eventArgs = new NativeArray(new Object[]{eventArg});
            Object[] objects = new Object[]{eventArg};
            Context ctx = Context.getCurrentContext();
            switch (eventType) {
                case MESSAGE:
                    messageEvent.get(eventArg).call(ctx, this, this, objects);
                    messageEvent.remove(eventArg);
                    break;
                case INTERACT:
                    interactEvent.get(eventArg).call(ctx, this, this, objects);
                    interactEvent.remove(eventArg);
                    break;
                case ON_PLAYER_SLEEP:
                    playerSleep.get(eventArg).call(ctx, this, this, objects);
                    playerSleep.remove(eventArg);
                    break;
                case ON_NEXT_BUTTON_PRESS:
                    switch (eventArg) {
                        case "DEFAULT":
                            buttonPressEvent.get(ClientModEvents.keyStory).call(ctx, this, this, objects);
                            buttonPressEvent.remove(ClientModEvents.keyStory);
                            break;
                        case "START_STORY":
                            buttonPressEvent.get(ClientModEvents.startStoryButton).call(ctx, this, this, objects);
                            buttonPressEvent.remove(ClientModEvents.keyStory);
                            break;
                    }
            }
        });
    }
    public void runEvent(String eventT, String eventArg) {
        EventType eventType = toEventType(eventT);
        loop.runImmediate(() -> {
            NativeArray eventArgs = new NativeArray(new Object[]{eventArg});
            Object[] objects = new Object[]{eventArg};
            Context ctx = Context.getCurrentContext();
            switch (eventType) {
                case MESSAGE:
                    messageEvent.get(eventArg).call(ctx, this, this, objects);
                    messageEvent.remove(eventArg);
                    break;
                case INTERACT:
                    interactEvent.get(eventArg).call(ctx, this, this, objects);
                    interactEvent.remove(eventArg);
                    break;
                case ON_PLAYER_SLEEP:
                    playerSleep.get(eventArg).call(ctx, this, this, objects);
                    playerSleep.remove(eventArg);
                    break;
                case ON_NEXT_BUTTON_PRESS:
                    switch (eventArg) {
                        case "DEFAULT":
                            buttonPressEvent.get(ClientModEvents.keyStory).call(ctx, this, this, objects);
                            buttonPressEvent.remove(ClientModEvents.keyStory);
                            break;
                        case "START_STORY":
                            buttonPressEvent.get(ClientModEvents.startStoryButton).call(ctx, this, this, objects);
                            buttonPressEvent.remove(ClientModEvents.keyStory);
                            break;
                    }
            }
        });
    }
    public static void putIntoScope(Scriptable scope, EventLoop loop) {
        EventListener eventListener = new EventListener(loop);

        ArrayList<Method> methodsToAdd = new ArrayList<>();
        try {
            Method addEventListener = EventListener.class.getMethod("newListener", String.class, BaseFunction.class, String.class);
            methodsToAdd.add(addEventListener);
            Method runEvent = EventListener.class.getMethod("runEvent", String.class, String.class);
            methodsToAdd.add(runEvent);
            Method toEventType = EventListener.class.getMethod("toEventType", String.class);
            methodsToAdd.add(toEventType);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, eventListener);
            eventListener.put(m.getName(), eventListener, methodInstance);
        }

        scope.put("eventManager", scope, eventListener);
    }
    @Override
    public String getResourceId() {
        return "EventListener";
    }

    @Override
    public String getClassName() {
        return "EventListener";
    }
}
