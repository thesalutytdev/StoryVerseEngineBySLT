package org.thesalutyt.storyverse.forge.api.features.world;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.forge.SVEngine;
import org.thesalutyt.storyverse.forge.annotation.Documentate;
import org.thesalutyt.storyverse.forge.api.environment.js.resource.EnvResource;
import org.thesalutyt.storyverse.forge.api.features.managment.Server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Chat extends ScriptableObject implements EnvResource {
    private static final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    public static HashMap<String, ArrayList<BaseFunction>> events = new HashMap<>();
    public static String last_message;
    @Documentate(
            desc = "Sends chat message"
    )
    public static void sendMessage(String text) {
        LiteralContents message = new LiteralContents(String.format("%s", text));
        sendEveryone(message);
    }

    @Documentate(
            desc = "Sends chat message with sender's name"
    )
    public static void sendNamed(String name, String text) {
        LiteralContents message = new LiteralContents(String.format("%s[%s] §r %s", SVEngine.CHARACTER_COLOR_STR, name, text));

        sendEveryone(message);
    }

    @Documentate(
            desc = "Sends message to local players"
    )
    private static void sendLocal(LiteralContents message, Player player) {
        player.sendSystemMessage(Component.literal(message.toString()));
    }

    @Documentate(
            desc = "Sends message to everyone"
    )
    public static void sendEveryone(LiteralContents text) {
        server.getPlayerList().getPlayers().forEach(serverPlayerEntity -> sendLocal(text, serverPlayerEntity));
    }

    public static void sendAsEngine(String text) {
        LiteralContents message = new LiteralContents("%s[Движок]§r %s".formatted(SVEngine.CHARACTER_COLOR_STR, text));
        sendEveryone(message);
    }
    public static void sendError(String text) {
        LiteralContents message = new LiteralContents(String.format("§4[ERROR] §r%s", text));
        sendEveryone(message);
    }
    public static void sendTranslatable(String text) {
        TranslatableContents message = new TranslatableContents(text);
        server.getPlayerList().getPlayers().forEach(serverPlayerEntity ->
                serverPlayerEntity.sendSystemMessage((Component) message));
    }
    public static void sendCopyable(String text) {
        Server.execute(String.format(
                "/tellraw @a {\"text\":\"%s\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"%s\"}}",
                text,
                text
        ));

    }
    public static void setCharacterColor(String color) {
        if (Objects.equals(color, "reset")) {
            SVEngine.CHARACTER_COLOR_STR = "§3";
            return;
        }
        SVEngine.CHARACTER_COLOR_STR = color;
    }

    public static void sendAsStoryVerse(String text) {
        LiteralContents message = new LiteralContents(String.format("%s%s", SVEngine.CHAT_PREFIX, text));
        sendEveryone(message);
    }

    @Documentate(
            desc = "Returns all players"
    )
    public static List<ServerPlayer> getPlayers() {
        return server.getPlayerList().getPlayers();
    }
    public static ArrayList<Method> methodsToAdd = new ArrayList<>();
    public static void putIntoScope (Scriptable scope) {
        Chat ef = new Chat();
        ef.setParentScope(scope);

        try {
            Method sendMessage = Chat.class.getMethod("sendMessage", String.class);
            methodsToAdd.add(sendMessage);
            Method sendNamed = Chat.class.getMethod("sendNamed", String.class, String.class);
            methodsToAdd.add(sendNamed);
            Method sendAsEngine = Chat.class.getMethod("sendAsEngine", String.class);
            methodsToAdd.add(sendAsEngine);
            Method setCharacterColor = Chat.class.getMethod("setCharacterColor", String.class);
            methodsToAdd.add(setCharacterColor);
            Method sendError = Chat.class.getMethod("sendError", String.class);
            methodsToAdd.add(sendError);
            Method sendTranslatable = Chat.class.getMethod("sendTranslatable", String.class);
            methodsToAdd.add(sendTranslatable);
            Method sendCopyable = Chat.class.getMethod("sendCopyable", String.class);
            methodsToAdd.add(sendCopyable);
            Method sendAsStoryVerse = Chat.class.getMethod("sendAsStoryVerse", String.class);
            methodsToAdd.add(sendAsStoryVerse);
            Method getPlayers = Chat.class.getMethod("getPlayers");
            methodsToAdd.add(getPlayers);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("chat", scope, ef);
    }

    @Override
    public String getResourceId() {
        return "Chat";
    }

    @Override
    public String getClassName() {
        return "Chat";
    }
}
