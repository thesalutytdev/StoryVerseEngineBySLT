package org.thesalutyt.storyverse.api.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.util.Objects;

public class WorldWrapper implements EnvResource {
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
    public WorldWrapper setBlock(BlockPos pos, Block block){
        this.world.setBlockAndUpdate(pos, block.defaultBlockState());
        return this;
    }

    public WorldWrapper setBlock(BlockPos pos, BlockState state){
        this.world.setBlockAndUpdate(pos, state);
        return this;
    }

    @Documentate(
            desc = "Uses block (not working at the moment)"
    )
    public WorldWrapper useBlock(BlockPos pos, PlayerEntity player) {
        BlockState state = this.world.getBlockState(pos);
        state.use(getWorld(), player, Hand.MAIN_HAND, new BlockRayTraceResult(new Vector3d(pos.getX(), pos.getY(), pos.getZ()),
                Direction.NORTH, pos, true));
        return this;
    }

    @Documentate(
            desc = "Returns block state"
    )
    public BlockState checkBlock(BlockPos pos) {
        return this.world.getBlockState(pos);
    }
    public BlockState getState(BlockPos pos){
        return this.world.getBlockState(pos);
    }

    @Documentate(
            desc = "Spawns an entity"
    )
    public Entity spawnEntity(BlockPos pos, EntityType<?> type){
        Entity entity = type.create(this.world);
        assert entity != null;
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.world.addFreshEntity(entity);
        return entity;
    }

    @Override
    public String getResourceId() {
        return "WorldWrapper";
    }
}
