package org.thesalutyt.storyverse.api.features;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.lwjgl.system.CallbackI;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.js.interpreter.ExternalFunctions;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server extends ScriptableObject implements EnvResource {
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
    public static ServerPlayerEntity getDevPlayer() {
        return server.getPlayerList().getPlayerByName("TheSALUTYT");
    }
    public static ServerPlayerEntity getPlayerByName(String name) {return server.getPlayerList().getPlayerByName(name);}
    public static ServerPlayerEntity getServerPlayer() {
        return server.getPlayerList().getPlayers().get(0);
    }

    @Documentate(
            desc = "Toggles PvP"
    )
    public static void allowPvP(Boolean method) {
        server.setPvpAllowed(method);
    }

    @Documentate(
            desc = "Sets max build height"
    )
    public static void setMaxBuildHeight(Integer maxBuildHeight) {
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
    public static void setFlightAllowed(Boolean method){
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
    public static int execute(String command) {
        if (server == null) {
            return 0;
        } else {
            assert mc.player != null;
            mc.player.chat(command);
            return 1;
        }
    }

    public static void putIntoScope (Scriptable scope, String rootDir) {
        // Создаем объект, к которому потом будем обращаться
        Server ef = new Server();
        // Это не обязательно, но я указываю, что у этого объекта есть объект выше уровнем
        ef.setParentScope(scope);

        // Это список функций, которые потом будут добавлены в объект ExternalFunctions
        // Здесь просто повторяй, я и сам не стал сильно глубоко копаться в деталях
        ArrayList<Method> methodsToAdd = new ArrayList<>();

        try {
            Method executeNP = Server.class.getMethod("execute", String.class);
            methodsToAdd.add(executeNP);
            Method getWorld = Server.class.getMethod("getWorld");
            methodsToAdd.add(getWorld);
            Method getWorldStr = Server.class.getMethod("getWorld", String.class);
            methodsToAdd.add(getWorldStr);
            Method getPlayers = Server.class.getMethod("getPlayers");
            methodsToAdd.add(getPlayers);
            Method getPlayer = Server.class.getMethod("getServerPlayer");
            methodsToAdd.add(getPlayer);
            Method close = Server.class.getMethod("close");
            methodsToAdd.add(close);
            Method allowPvP = Server.class.getMethod("allowPvP", Boolean.class);
            methodsToAdd.add(allowPvP);
            Method setMaxBuildHeight = Server.class.getMethod("setMaxBuildHeight", Integer.class);
            methodsToAdd.add(setMaxBuildHeight);
            Method getClientPlayer = Server.class.getMethod("getPlayer");
            methodsToAdd.add(getClientPlayer);
            Method getDevPlayer = Server.class.getMethod("getDevPlayer");
            methodsToAdd.add(getDevPlayer);
            Method setFlightAllowed = Server.class.getMethod("setFlightAllowed", Boolean.class);
            methodsToAdd.add(setFlightAllowed);
            Method getPlayerByName = Server.class.getMethod("getPlayerByName", String.class);
            methodsToAdd.add(getPlayerByName);
            Method getFirstPlayer = Server.class.getMethod("getFirstPlayer");
            methodsToAdd.add(getFirstPlayer);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // Здесь функции укладываются в ExternalFunctions
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }

        // Здесь ExternalFunctions укладывается в пространство имен верхнего уровня
        scope.put("server", scope, ef);
    }

    @Documentate(
            desc = "Returns player entity"
    )
    public static PlayerEntity getPlayer() {
        return mc.player;
    }
    public static ServerPlayerEntity getFirstPlayer() {
        return server.getPlayerList().getPlayers().get(0);
    }

    @Override
    public String getResourceId() {
        return "Server";
    }

    @Override
    public String getClassName() {
        return "server";
    }
}
