package org.thesalutyt.storyverse.api.environment.js.event;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.special.FadeScreen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Mod.EventBusSubscriber(
        modid = StoryVerse.MOD_ID
)
public class EventManagerJS extends ScriptableObject implements EnvResource, JSResource {
    private static String msg = "";
    private static String player_slept = "";
    private static String block_broken = "";
    private static BlockPos blockPos = new BlockPos(0, 0, 0);
    private static HashMap<String, ArrayList<BaseFunction>> events = new HashMap<>();
    @SubscribeEvent
    public static void onMessage(ClientChatReceivedEvent event) {
        msg = event.getMessage().getContents();
        runEvent("message");
    }
    @SubscribeEvent
    public static void onSleep(PlayerSleepInBedEvent event) {
        player_slept = event.getPlayer().getName().getContents();
        runEvent("sleep");
    }
    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        block_broken = event.getWorld().getBlockState(event.getPos()).getBlock().toString();
        blockPos = event.getPos();
        runEvent("block_break");
    }
    public static void addEventListener(String event_name, BaseFunction function) {
        if (!events.containsKey(event_name) && !Objects.equals(event_name, "sleep")
                || !Objects.equals(event_name, "message")
                || !Objects.equals(event_name, "block_break")) {
            EventLoop.getLoopInstance().runImmediate(() -> {
                ArrayList<BaseFunction> functions = new ArrayList<>();
                functions.add(function);
                events.put(event_name, functions);
            });
        } else {
            return;
        }
    }
    public static void runEvent(String event_name) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            if (events.containsKey(event_name)) {
                ArrayList<BaseFunction> arr = events.get(event_name);
                Context ctx = Context.getCurrentContext();
                Object[] args = new Object[]{};
                switch (event_name) {
                    case "sleep":
                        args[0] = player_slept;
                        break;
                    case "message":
                        args[0] = msg;
                        break;
                    case "block_break":
                        args[0] = block_broken;
                        args[1] = blockPos.getX();
                        args[2] = blockPos.getY();
                        args[3] = blockPos.getZ();
                        break;
                    default:
                        break;
                }
                for (int i=0;i<arr.size(); i++) {
                    arr.get(i).call(ctx, SVEngine.interpreter.getScope(),
                            SVEngine.interpreter.getScope(), args);
                }
            } else {
                return;
            }
        });
    }
    public static void removeEventListener(String event_name) {
        if (!Objects.equals(event_name, "sleep") && !Objects.equals(event_name, "message")) {
            return;
        } else {
            events.remove(event_name);
        }
    }
    public static String getLastMessage() {
        return msg;
    }
    public static String getLastPlayerSlept() {
        return player_slept;
    }
    public static String getLastBlockBroken() {
        return block_broken;
    }
    public static void putIntoScope(Scriptable scope) {
        EventManagerJS ef = new EventManagerJS();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();
        try {
            Method addEventListener = EventManagerJS.class.getMethod("addEventListener", String.class, BaseFunction.class);
            methodsToAdd.add(addEventListener);
            Method msg = EventManagerJS.class.getMethod("getLastMessage");
            methodsToAdd.add(msg);
            Method player_slept = EventManagerJS.class.getMethod("getLastPlayerSlept");
            methodsToAdd.add(player_slept);
            Method block_broken = EventManagerJS.class.getMethod("getLastBlockBroken");
            methodsToAdd.add(block_broken);
            Method removeEventListener = EventManagerJS.class.getMethod("removeEventListener", String.class);
            methodsToAdd.add(removeEventListener);
            Method runEvent = EventManagerJS.class.getMethod("runEvent", String.class);
            methodsToAdd.add(runEvent);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("event", scope, ef);
    }
    @Override
    public String getResourceId() {
        return "EventManagerJS";
    }

    @Override
    public String getClassName() {
        return "EventManagerJS";
    }
}
