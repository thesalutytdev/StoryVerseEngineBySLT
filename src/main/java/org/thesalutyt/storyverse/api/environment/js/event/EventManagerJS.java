package org.thesalutyt.storyverse.api.environment.js.event;

import com.google.common.base.Ascii;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.model.b3d.B3DModel;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.StoryVerse;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.js.MobJS;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.special.FadeScreen;
import org.thesalutyt.storyverse.common.events.ModEvents;
import org.thesalutyt.storyverse.api.features.Player;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
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
    private static String block_interacted = "";
    private static String block_interacted_name = "";
    private static String block_placed = "";
    private static String block_placed_name = "";
    private static String dimension_new = "";
    private static String dimension_old = "";
    private static String item_dropped = "";
    private static String player_respawned = "";
    private static String item_pickup = "";
    private static String item_crafted = "";
    private static String item_smelted = "";
    private static String item_used = "";
    private static String player = "";
    private static Integer key_pressed = 0;
    private static BlockPos blockPos = new BlockPos(0, 0, 0);
    public static HashMap<String, ArrayList<BaseFunction>> events = new HashMap<>();
    @SubscribeEvent
    public static void onMessage(ClientChatReceivedEvent event) {
        msg = event.getMessage().getString();
        System.out.println("Message: " + msg);
        System.out.println("Full: " + event.getMessage().toString());
        runEvent("message");
    }
    @SubscribeEvent
    public static void onSleep(PlayerSleepInBedEvent event) {
        player_slept = event.getPlayer().getName().getContents();
        runEvent("sleep");
    }
    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        block_broken = event.getWorld().getBlockState(event.getPos()).getBlock().getName().toString();
        blockPos = event.getPos();
        player = event.getPlayer().getName().getContents();
        runEvent("block_break");
    }
    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        block_interacted = event.getUseBlock().toString();
        block_interacted_name = event.getWorld().getBlockState(event.getPos()).getBlock().getName().toString();
        player = event.getPlayer().getName().getContents();
        item_used = event.getItemStack().toString();
        runEvent("block_interact");
    }
    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        dimension_new = event.getTo().getRegistryName().toString();
        dimension_old = event.getFrom().getRegistryName().toString();
        player = event.getPlayer().getName().getContents();
        runEvent("dimension_change");
    }
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        block_placed = event.getPlacedBlock().toString();
        block_placed_name = event.getWorld().getBlockState(event.getPos()).getBlock().getName().toString();
        runEvent("block_placed");
    }
    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        item_dropped = event.getEntityItem().getItem().getDisplayName().getContents();
        player = event.getPlayer().getName().getContents();
        runEvent("item_dropped");
    }
    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        player_respawned = event.getPlayer().getName().getContents();
        runEvent("player_respawned");
    }
    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent event) {
        key_pressed = event.getKey();
        runEvent("key_pressed");
    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        item_pickup = event.getStack().getItem().getRegistryName().getPath();
        player = event.getPlayer().getName().getContents();
        runEvent("item_pickup");
    }

    @SubscribeEvent
    public static void onItemCraft(PlayerEvent.ItemCraftedEvent event) {
        item_crafted = event.getCrafting().toString();
        player = event.getPlayer().getName().getContents();
        runEvent("item_crafted");
    }

    @SubscribeEvent
    public static void onItemSmelt(PlayerEvent.ItemSmeltedEvent event) {
        item_smelted = event.getSmelting().toString();
        player = event.getPlayer().getName().getContents();
        runEvent("item_smelted");
    }

    public static void addEventListener(String event_name, BaseFunction function) {
        if (!ModEvents.inWorld) {
            return;
        }
        if (!events.containsKey(event_name)
                && !Objects.equals(event_name, "sleep")
                || !Objects.equals(event_name, "message")
                || !Objects.equals(event_name, "block_break")
                || !Objects.equals(event_name, "block_interact")
                || !Objects.equals(event_name, "dimension_change")
                || !Objects.equals(event_name, "block_placed")
                || !Objects.equals(event_name, "item_dropped")
                || !Objects.equals(event_name, "player_respawned")
                || !Objects.equals(event_name, "key_pressed")
                || !Objects.equals(event_name, "item_pickup")
                || !Objects.equals(event_name, "item_crafted")
                || !Objects.equals(event_name, "item_smelted")) {
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
        if (!ModEvents.inWorld) {
            return;
        }
        EventLoop.getLoopInstance().runImmediate(() -> {
            if (events.containsKey(event_name)) {
                ArrayList<BaseFunction> arr = events.get(event_name);
                Context ctx = Context.getCurrentContext();
                Object[] args;
                switch (event_name) {
                        case "sleep":
                                args = new Object[1];
                                args[0] = player_slept;
                                break;
                        case "message":
                                args = new Object[1];
                                args[0] = msg;
                                break;
                        case "block_break":
                                args = new Object[4];
                                args[0] = block_broken;
                                args[1] = new Double((double) blockPos.getX());
                                args[2] = new Double((double) blockPos.getY());
                                args[3] = new Double((double) blockPos.getZ());
                                break;
                        default:
                                args = new Object[0];
                                return;
                    }
                for (int i=0;i<arr.size(); i++) {
                    arr.get(i).call(ctx, SVEngine.interpreter.getScope(),
                            SVEngine.interpreter.getScope(), Objects.requireNonNull(getArgs()).toArray());
                }
            } else {
                return;
            }
        });
    }
    public static void removeEventListener(String event_name) {
        if (!ModEvents.inWorld) {
            return;
        }
        if (!Objects.equals(event_name, "sleep") && !Objects.equals(event_name, "message")
        && !Objects.equals(event_name, "block_break") && !Objects.equals(event_name, "block_interact")
        && Objects.equals(event_name, "dimension_change") && !Objects.equals(event_name, "block_placed")    ) {
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
    public static String getLastBlockInteracted() {
        return block_interacted;
    }
    public static String getLastBlockInteractedName() {
        return block_interacted_name;
    }
    public static String getLastBlockPos() {
        return blockPos.toString();
    }
    public static String getLastBlockPlaced() {
        return block_placed;
    }
    public static String getLastBlockPlacedName() {
        return block_placed_name;
    }
    public static String getLastDimensionNew() {
        return dimension_new;
    }
    public static String getLastDimensionOld() {
        return dimension_old;
    }
    public static String getLastItemDropped() {
        return item_dropped;
    }
    public static String getLastPlayerRespawned() {
        return player_respawned;
    }
    public static Integer getLastKeyPressed() {
        return key_pressed;
    }
    public static String getLastItemPickup() {
        return item_pickup;
    }
    public static String getLastItemCrafted() {
        return item_crafted;
    }
    public static String getLastItemSmelted() {
        return item_smelted;
    }
    public static String getLastItemUsed() {
        return item_used;
    }
    public static String getPlayerName() {
        return player;
    }
    public static void clear() {
        events.clear();
        MobJS.events.clear();
    }
    public static NativeArray getArgs() {
        if (!ModEvents.inWorld) {
            return null;
        }
        Object[] args = new Object[15];
        args[0] = new Double((double) blockPos.getX());
        args[1] = new Double((double) blockPos.getY());
        args[2] = new Double((double) blockPos.getZ());
        args[3] = block_interacted;
        args[4] = block_broken;
        args[5] = player_slept;
        args[6] = msg;
        args[7] = block_interacted_name;
        args[8] = block_placed;
        args[9] = block_placed_name;
        args[10] = dimension_new;
        args[11] = dimension_old;
        args[12] = item_dropped;
        args[13] = player_respawned;
        args[14] = key_pressed;
        return new NativeArray(args);
    }
    public static String getMsg() {
        System.out.println(msg);
        return msg;
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope(Scriptable scope) {
        EventManagerJS ef = new EventManagerJS();
        ef.setParentScope(scope);
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
            Method getArgs = EventManagerJS.class.getMethod("getArgs");
            methodsToAdd.add(getArgs);
            Method blockPos = EventManagerJS.class.getMethod("getLastBlockPos");
            methodsToAdd.add(blockPos);
            Method block_interacted = EventManagerJS.class.getMethod("getLastBlockInteracted");
            methodsToAdd.add(block_interacted);
            Method block_interacted_name = EventManagerJS.class.getMethod("getLastBlockInteractedName");
            methodsToAdd.add(block_interacted_name);
            Method block_placed = EventManagerJS.class.getMethod("getLastBlockPlaced");
            methodsToAdd.add(block_placed);
            Method block_placed_name = EventManagerJS.class.getMethod("getLastBlockPlacedName");
            methodsToAdd.add(block_placed_name);
            Method dimension_new = EventManagerJS.class.getMethod("getLastDimensionNew");
            methodsToAdd.add(dimension_new);
            Method dimension_old = EventManagerJS.class.getMethod("getLastDimensionOld");
            methodsToAdd.add(dimension_old);
            Method item_dropped = EventManagerJS.class.getMethod("getLastItemDropped");
            methodsToAdd.add(item_dropped);
            Method player_respawned = EventManagerJS.class.getMethod("getLastPlayerRespawned");
            methodsToAdd.add(player_respawned);
            Method key_pressed = EventManagerJS.class.getMethod("getLastKeyPressed");
            methodsToAdd.add(key_pressed);
            Method clear = EventManagerJS.class.getMethod("clear");
            methodsToAdd.add(clear);
            Method getMsg = EventManagerJS.class.getMethod("getMsg");
            methodsToAdd.add(getMsg);
            Method item_pickup = EventManagerJS.class.getMethod("getLastItemPickup");
            methodsToAdd.add(item_pickup);
            Method item_crafted = EventManagerJS.class.getMethod("getLastItemCrafted");
            methodsToAdd.add(item_crafted);
            Method item_smelted = EventManagerJS.class.getMethod("getLastItemSmelted");
            methodsToAdd.add(item_smelted);
            Method item_used = EventManagerJS.class.getMethod("getLastItemUsed");
            methodsToAdd.add(item_used);
            Method getPlayerName = EventManagerJS.class.getMethod("getPlayerName");
            methodsToAdd.add(getPlayerName);
            Method getClassName = EventManagerJS.class.getMethod("getClassName");
            methodsToAdd.add(getClassName);
            Method getResourceId = EventManagerJS.class.getMethod("getResourceId");
            methodsToAdd.add(getResourceId);
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
