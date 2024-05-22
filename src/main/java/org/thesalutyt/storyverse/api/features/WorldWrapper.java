package org.thesalutyt.storyverse.api.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class WorldWrapper extends ScriptableObject implements EnvResource {
    private static final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    private static final Minecraft mc = Minecraft.getInstance();
    private final World world;

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
    public Entity spawnEntity(BlockPos pos, EntityType<?> type){
        Entity entity = type.create(this.world);
        assert entity != null;
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.world.addFreshEntity(entity);
        return entity;
    }
    public BlockPos pos(Double x, Double y, Double z) {
        return new BlockPos(x, y, z);
    }
    public EntityType<?> toEntityType(String entityType) {
        switch (entityType) {
            case "BAT": {
                return EntityType.BAT;
            }
            case "SHEEP": {
                return EntityType.SHEEP;
            }
            case "WITHER_SKELETON": {
                return EntityType.WITHER_SKELETON;
            }
            case "PIG": {
                return EntityType.PIG;
            }
            case "FOX": {
                return EntityType.FOX;
            }
            case "COW": {
                return EntityType.COW;
            }
            case "ARMOR_STAND":
            case "NPC": {
                return EntityType.ARMOR_STAND;
            }
            case "SQUID": {
                return EntityType.SQUID;
            }
        }
        return EntityType.COW;
    }
    public Effect toEffect(Integer id) {
        return Effect.byId(id);
    }
    public Hand selectHand(Integer hand) {
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
    public Item item(Integer id) {
        return Item.byId(id);
    }

    public static void putIntoScope (Scriptable scope) {
        WorldWrapper ef = new WorldWrapper();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();

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
