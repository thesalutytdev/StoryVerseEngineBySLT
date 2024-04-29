package org.thesalutyt.storyverse.api.features;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.util.List;
import java.util.Objects;

public class Server implements EnvResource {
    private static final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    private static final Minecraft mc = Minecraft.getInstance();

    @Documentate(
            desc = "Returns world"
    )

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

    @Documentate(
            desc = "Returns players list"
    )
    public static List<ServerPlayerEntity> getPlayers() {
        return server.getPlayerList().getPlayers();
    }

    @Documentate(
            desc = "Toggles PvP"
    )
    public static void allowPvP(boolean method) {
        server.setPvpAllowed(method);
    }

    @Documentate(
            desc = "Sets max build height"
    )
    public static void setMaxBuildHeight(int maxBuildHeight) {
        server.setMaxBuildHeight(maxBuildHeight);
    }

    @Documentate(
            desc = "Closes server"
    )
    public static void close() {
        server.close();
    }

    @Documentate(
            desc = "Toggles flight"
    )
    public static void setFlightAllowed(boolean method){
        server.setFlightAllowed(method);
    }

    @Documentate(
            desc = "Executes command"
    )
    public static int execute(PlayerEntity player, String command) {
        if (server == null) {
            return 0;
        } else {
            assert mc.player != null;
            mc.player.chat(command);
            return 1;
        }
    }

    @Documentate(
            desc = "Returns player entity"
    )
    public static PlayerEntity getPlayer() {
        return mc.player;
    }

    @Override
    public String getResourceId() {
        return "Server";
    }
}
