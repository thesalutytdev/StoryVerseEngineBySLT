package org.thesalutyt.storyverse.forge.api.features.managment;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.forge.SVEngine;
import org.thesalutyt.storyverse.forge.annotation.Documentate;
import org.thesalutyt.storyverse.forge.api.environment.js.resource.EnvResource;

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

    public static Level getWorld(String type) {
        if (Objects.equals(type, "overworld")){
            return server.getLevel(Level.OVERWORLD);
        }
        if (Objects.equals(type, "nether")){
            return server.getLevel(Level.NETHER);
        }
        if (Objects.equals(type, "end")){
            return server.getLevel(Level.END);
        }
        return server.overworld();
    }

    public static Level getWorld(){
        return server.overworld();
    }

    @Documentate(
            desc = "Returns players list"
    )
    public static List<ServerPlayer> getPlayers() {
        return server.getPlayerList().getPlayers();
    }
    public static ServerPlayer getDevPlayer() {
        return server.getPlayerList().getPlayerByName("TheSALUTYT");
    }
    public static ServerPlayer getPlayerByName(String name) {return server.getPlayerList().getPlayerByName(name);}
    public static ServerPlayer getServerPlayer() {
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
    public static void setDemo(Boolean is) {
        server.setDemo(is);
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
    public static int execute(ServerPlayer player, String command) {
        if (server == null) {
            return 0;
        } else {
            assert mc.player != null;
            CommandSourceStack source = server.createCommandSourceStack()
                    .withEntity(player)
                    .withPermission(4);
            return server.getCommands().performPrefixedCommand(source, command);
        }
    }
    public static int execute(String command) {
        assert mc.player != null;
        CommandSourceStack source = server.createCommandSourceStack()
                .withEntity(mc.player)
                .withPermission(4);
        return server.getCommands().performPrefixedCommand(source, command);
    }

    public static String getOpPlayer() {
        return SVEngine.OP_PLAYER;
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope, String rootDir) {
        // Создаем объект, к которому потом будем обращаться
        Server ef = new Server();
        // Это не обязательно, но я указываю, что у этого объекта есть объект выше уровнем
        ef.setParentScope(scope);

        // Это список функций, которые потом будут добавлены в объект ExternalFunctions
        // Здесь просто повторяй, я и сам не стал сильно глубоко копаться в деталях

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
            Method getDevPlayer = Server.class.getMethod("getDevPlayer");
            methodsToAdd.add(getDevPlayer);
            Method setFlightAllowed = Server.class.getMethod("setFlightAllowed", Boolean.class);
            methodsToAdd.add(setFlightAllowed);
            Method getPlayerByName = Server.class.getMethod("getPlayerByName", String.class);
            methodsToAdd.add(getPlayerByName);
            Method getFirstPlayer = Server.class.getMethod("getFirstPlayer");
            methodsToAdd.add(getFirstPlayer);
            Method getOp = Server.class.getMethod("getOpPlayer");
            methodsToAdd.add(getOp);
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
    public static ServerPlayer getFirstPlayer() {
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

