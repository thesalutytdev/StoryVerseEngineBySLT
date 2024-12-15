package org.thesalutyt.storyverse.api.features;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.command.impl.SummonCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.camera.cutscene.CutsceneType;
import org.thesalutyt.storyverse.api.camera.entityCamera.CameraType;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.js.minecraft.block.JSBlock;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.common.entities.Entities;
import org.thesalutyt.storyverse.common.events.EventType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WorldWrapper extends ScriptableObject implements EnvResource {
    private static final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    private static final Minecraft mc = Minecraft.getInstance();
    private final World world;
    private static HashMap<String, BaseFunction> onTimeChange = new HashMap<>();
    @Documentate(
            desc = "Returns world"
    )
    public WorldWrapper(World world){
        this.world = world;
    }

    public WorldWrapper(String world){
        this.world = getWorld(world);
    }

    public static World getWorld(String type) {
        if (Objects.equals(type, "overworld")){
            return server.getLevel(World.OVERWORLD);
        }
        if (Objects.equals(type, "nether")){
            return server.getLevel(World.NETHER);
        }
        if (Objects.equals(type, "end")){
            return server.getLevel(World.END);
        }
        return server.overworld();
    }

    public static World getWorld(){
        return server.overworld();
    }

    public World getMCWorld() {
        return this.world;
    }

    public WorldWrapper(){
        this.world = getWorld();
    }


    @Documentate(
            desc = "Sets block"
    )
    public void setBlock(BlockPos pos, Block block){
        this.world.setBlockAndUpdate(pos, block.defaultBlockState());
    }
    public WorldWrapper setBlock(Double x, Double y, Double z, Block block){
        this.world.setBlockAndUpdate(new BlockPos(x, y, z), block.defaultBlockState());
        return this;
    }

    public WorldWrapper setBlock(BlockPos pos, BlockState state){
        this.world.setBlockAndUpdate(pos, state);
        return this;
    }

    @Documentate(
            desc = "Uses block (not working at the moment)"
    )
    public WorldWrapper useBlock(Double x, Double y, Double z, PlayerEntity player) {
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockState state = this.world.getBlockState(blockPos);
        state.use(getWorld(), player, Hand.MAIN_HAND, new BlockRayTraceResult(new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                Direction.NORTH, blockPos, true));
        return this;
    }

    @Documentate(
            desc = "Returns block state"
    )
    public BlockState checkBlock(Double x, Double y, Double z) {
        return this.world.getBlockState(new BlockPos(x, y, z));
    }
    public BlockState getState(Double x, Double y, Double z){
        BlockPos pos = new BlockPos(x, y, z);
        return this.world.getBlockState(pos);
    }

    @Documentate(
            desc = "Spawns an entity"
    )
    public Entity spawnEntity(Double x, Double y, Double z, String type){
        BlockPos pos = new BlockPos(x, y, z);
        Entity entity = toEntityType(type).create(this.world);
        assert entity != null;
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.world.addFreshEntity(entity);
        return entity;
    }
    public Entity spawnEntity(BlockPos pos, EntityType<?> type) {
        Entity entity = type.create(this.world);
        assert entity != null;
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.world.addFreshEntity(entity);
        return entity;
    }
    public static BlockPos pos(Double x, Double y, Double z) {
        return new BlockPos(x, y, z);
    }
    public static EntityType<?> toEntityType(String entityType) {
        return (EntityType<?>) GameRegistry.findRegistry(EntityType.class).getValue(ResourceLocation.tryParse(entityType));
    }

    public static EntityType<?> toEntityType(String mod, String id) {
        return (EntityType<?>) GameRegistry.findRegistry(EntityType.class).getValue(new ResourceLocation(mod, id));
    }

    public static Integer armorSlot(String slot) {
        switch (slot) {
            case "HEAD":
            case "HELMET": {
                return 0;
            }
            case "CHEST":
            case "CHESTPLATE": {
                return 1;
            }
            case "LEGS":
            case "LEGGINGS":{
                return 2;
            }
            case "FEET":
            case "BOOTS": {
                return 3;
            }
            default: {
                return null;
            }
        }
    }

    public static CameraType toCameraType(String type) {
        switch (type) {
            case "MOVE":
            case "MOVING": {
                return CameraType.MOVING;
            }
            case "ROTATION_ONLY": {
                return CameraType.ROT_ONLY;
            }
            case "POSITION_ONLY": {
                return CameraType.POS_ONLY;
            }
            case "FULL":
            case "STATIC":
            default: {
                return CameraType.FULL;
            }
        }
    }
    public static Effect toEffect(Integer id) {
        return Effect.byId(id);
    }
    public static Hand selectHand(Integer hand) {
        switch (hand){
            case 0: {
                return Hand.MAIN_HAND;
            }
            case 1: {
                return Hand.OFF_HAND;
            }
        }
        return Hand.MAIN_HAND;
    }
    public static Item item(Integer id) {
        return Item.byId(id);
    }
    public static String key(String key) {
        switch (key) {
            case "DEFAULT": {
                return "DEFAULT";
            }
        }
        return "null";
    }
    public static EventType toEventType(String type) {
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

    public static void use(String world, Double x, Double y, Double z) {
        WorldWrapper worldWrapper = new WorldWrapper(getWorld(world));
        BlockState state = worldWrapper.getMCWorld().getBlockState(new BlockPos(x, y, z));
        Block block = state.getBlock();
        if (block instanceof AbstractButtonBlock) {
            AbstractButtonBlock self = (AbstractButtonBlock) block;
            self.press(state, worldWrapper.getMCWorld(), new BlockPos(x, y, z));
        } else if (block instanceof LeverBlock) {
            LeverBlock self = (LeverBlock) block;
            self.pull(state, worldWrapper.getMCWorld(), new BlockPos(x, y, z));
        } else if (block instanceof TrapDoorBlock) {
            TrapDoorBlock self = (TrapDoorBlock) block;
            self.use(state, worldWrapper.getMCWorld(), new BlockPos(x, y, z), Server.getFirstPlayer(), Hand.MAIN_HAND, new BlockRayTraceResult(new Vector3d(x, y, z), Direction.DOWN, new BlockPos(x, y, z), false));
        } else {
            System.out.println("Cannot use block " + block);
        }
    }

    public static void useDoor(String world, Double x, Double y, Double z, Boolean open) {
        WorldWrapper worldWrapper = new WorldWrapper(getWorld(world));
        BlockState state = worldWrapper.getMCWorld().getBlockState(new BlockPos(x, y, z));
        Block block = state.getBlock();
        if (block instanceof DoorBlock) {
            DoorBlock self = (DoorBlock) block;
            self.setOpen(worldWrapper.getMCWorld(), state, new BlockPos(x, y, z), open);
        }

    }

    public static boolean isDay() {
        return getWorld().isDay();
    }

    public static Double getCurrentTime() {
        return (double) getWorld().getDayTime();
    }

    public static void setOnTimeChange(BaseFunction f, String time) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            switch (time.toUpperCase()) {
                case "DAY": {
                    onTimeChange.put("day", f);
                    break;
                }
                case "NIGHT": {
                    onTimeChange.put("night", f);
                    break;
                }
            }
        });
    }

    public static void removeOnTimeChange(String time) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            onTimeChange.remove(time);
        });
    }

    public static void runOnTimeChange(String time) {
        EventLoop.getLoopInstance().runImmediate(() -> {
            switch (time) {
                case "DAY": {
                    onTimeChange.get("day").call(Context.getCurrentContext(), SVEngine.interpreter.getScope(),
                            SVEngine.interpreter.getScope(), new Object[]{getCurrentTime()});
                    break;
                }
                case "NIGHT": {
                    onTimeChange.get("night").call(Context.getCurrentContext(), SVEngine.interpreter.getScope(),
                            SVEngine.interpreter.getScope(), new Object[]{getCurrentTime()});
                    break;
                }
            }
        });
    }

    public static CutsceneType toCutsceneType(String type) {
        switch (type.toUpperCase()) {
            case "FULL": {
                return CutsceneType.FULL;
            }
            case "POS_ONLY": {
                return CutsceneType.POS_ONLY;
            }
            case "ROT_ONLY": {
                return CutsceneType.ROT_ONLY;
            }
            case "NULL": {
                return CutsceneType.NULL;
            }
        }
        return null;
    }

    public Boolean setBlock(Double x, Double y, Double z, String block){
        return this.world.setBlockAndUpdate(new BlockPos(x, y, z), JSBlock.blocks.get(block).defaultBlockState());
    }

    public void fill(NativeArray pos0, NativeArray pos1, String block) {
        Double x0 = (Double) pos0.get(0);
        Double y0 = (Double) pos0.get(1);
        Double z0 = (Double) pos0.get(2);
        Double x1 = (Double) pos1.get(0);
        Double y1 = (Double) pos1.get(1);
        Double z1 = (Double) pos1.get(2);
        for (Double x : MathScript.getRange(x0, x1)) {
            boolean b = this.setBlock(x, y0, z0, block);
        }
        for (Double y : MathScript.getRange(y0, y1)) {
            boolean b = this.setBlock(x1, y, z0, block);
        }
        for (Double z : MathScript.getRange(z0, z1)) {
            boolean b = this.setBlock(x1, y1, z, block);
        }
    }

    public void breakBlock(Double x, Double y, Double z) {
        BlockState state = this.world.getBlockState(new BlockPos(x, y, z));
        Block block = state.getBlock();

        block.destroy(this.world, new BlockPos(x, y, z), state);
    }

    public static void clearOnTimeChange() {
        onTimeChange.clear();
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope) {
        WorldWrapper ef = new WorldWrapper();
        ef.setParentScope(scope);

        try {
            Method getWorld = WorldWrapper.class.getMethod("getWorld");
            methodsToAdd.add(getWorld);
            Method getWorldByStr = WorldWrapper.class.getMethod("getWorld", String.class);
            methodsToAdd.add(getWorldByStr);
            Method item = WorldWrapper.class.getMethod("item", Integer.class);
            methodsToAdd.add(item);
            Method setState = WorldWrapper.class.getMethod("getState", Double.class, Double.class, Double.class);
            methodsToAdd.add(setState);
            Method pos = WorldWrapper.class.getMethod("pos", Double.class, Double.class, Double.class);
            methodsToAdd.add(pos);
            Method toEntityType = WorldWrapper.class.getMethod("toEntityType", String.class);
            methodsToAdd.add(toEntityType);
            Method getEntity = WorldWrapper.class.getMethod("toEntityType", String.class, String.class);
            methodsToAdd.add(getEntity);
            Method checkState = WorldWrapper.class.getMethod("getState", Double.class, Double.class, Double.class);
            methodsToAdd.add(checkState);
            Method checkBlock = WorldWrapper.class.getMethod("checkBlock", Double.class, Double.class, Double.class);
            methodsToAdd.add(checkBlock);
            Method spawnEntity = WorldWrapper.class.getMethod("spawnEntity", Double.class, Double.class, Double.class, String.class);
            methodsToAdd.add(spawnEntity);
            Method selectHand = WorldWrapper.class.getMethod("selectHand", Integer.class);
            methodsToAdd.add(selectHand);
            Method getMCWorld = WorldWrapper.class.getMethod("getMCWorld");
            methodsToAdd.add(getMCWorld);
            Method toCameraType = WorldWrapper.class.getMethod("toCameraType", String.class);
            methodsToAdd.add(toCameraType);
            Method toEffect = WorldWrapper.class.getMethod("toEffect", Integer.class);
            methodsToAdd.add(toEffect);
            Method useBlock = WorldWrapper.class.getMethod("use", String.class, Double.class, Double.class, Double.class);
            methodsToAdd.add(useBlock);
            Method key = WorldWrapper.class.getMethod("key", String.class);
            methodsToAdd.add(key);
            Method toEventType = WorldWrapper.class.getMethod("toEventType", String.class);
            methodsToAdd.add(toEventType);
            Method useDoor = WorldWrapper.class.getMethod("useDoor", String.class, Double.class, Double.class, Double.class, Boolean.class);
            methodsToAdd.add(useDoor);
            Method armorSlot = WorldWrapper.class.getMethod("armorSlot", String.class);
            methodsToAdd.add(armorSlot);
            Method isDay = WorldWrapper.class.getMethod("isDay");
            methodsToAdd.add(isDay);
            Method getCurrentTime = WorldWrapper.class.getMethod("getCurrentTime");
            methodsToAdd.add(getCurrentTime);
            Method toCutsceneType = WorldWrapper.class.getMethod("toCutsceneType", String.class);
            methodsToAdd.add(toCutsceneType);
//            Method setOnTimeChange = WorldWrapper.class.getMethod("setOnTimeChange", BaseFunction.class, String.class);
//            methodsToAdd.add(setOnTimeChange);
//            Method removeOnTimeChange = WorldWrapper.class.getMethod("removeOnTimeChange", String.class);
//            methodsToAdd.add(removeOnTimeChange);
//            Method runOnTimeChange = WorldWrapper.class.getMethod("runOnTimeChange", String.class);
//            methodsToAdd.add(runOnTimeChange);
//            Method clearOnTimeChange = WorldWrapper.class.getMethod("clearOnTimeChange");
//            methodsToAdd.add(clearOnTimeChange);
            Method setBlock = WorldWrapper.class.getMethod("setBlock", Double.class, Double.class, Double.class, String.class);
            methodsToAdd.add(setBlock);
            Method fill = WorldWrapper.class.getMethod("fill", NativeArray.class, NativeArray.class, String.class);
            methodsToAdd.add(fill);
            Method breakBlock = WorldWrapper.class.getMethod("breakBlock", Double.class, Double.class, Double.class);
            methodsToAdd.add(breakBlock);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("world", scope, ef);
    }

    @Override
    public String getResourceId() {
        return "WorldWrapper";
    }

    @Override
    public String getClassName() {
        return "WorldWrapper";
    }
}
