package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Chat extends ScriptableObject implements EnvResource {
    private static final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    @Documentate(
            desc = "Sends chat message"
    )
    public static void sendMessage(String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("%s", text));
        sendEveryone(message);
    }

    @Documentate(
            desc = "Sends chat message with sender's name"
    )
    public static void sendNamed(String name, String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("%s[%s]", SVEngine.CHARACTER_COLOR_STR, name));
        message.append(String.format("§r %s", text));

        sendEveryone(message);
    }

    @Documentate(
            desc = "Sends message to local players"
    )
    private static void sendLocal(ITextComponent message, PlayerEntity player) {
        player.sendMessage(message, player.getUUID());
    }

    @Documentate(
            desc = "Sends message to everyone"
    )
    public static void sendEveryone(ITextComponent text) {
        server.getPlayerList().getPlayers().forEach(serverPlayerEntity -> sendLocal(text, serverPlayerEntity));
    }

    public static void sendAsEngine(String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("%s[Движок]§r %s", SVEngine.CHARACTER_COLOR_STR, text));
        sendEveryone(message);
    }
    public static void sendError(String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("§4[ERROR] §r%s", text));
        sendEveryone(message);
    }
    public static void sendTranslatable(String text) {
        TranslationTextComponent message = new TranslationTextComponent(text);
        sendEveryone(message);
    }
    public static void sendCopyable(String text) {
        Server.execute(String.format(
                "/tellraw @a {\"text\":\"%s\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"%s\"}}",
                text,
                text
        ));
    }
    public static void setCharacterColor(String color) {
        SVEngine.CHARACTER_COLOR_STR = color;
    }
    @Documentate(
            desc = "Returns all players"
    )
    public static List<ServerPlayerEntity> getPlayers() {
        return server.getPlayerList().getPlayers();
    }
    public static void putIntoScope (Scriptable scope) {
        Chat ef = new Chat();
        ef.setParentScope(scope);
        ArrayList<Method> methodsToAdd = new ArrayList<>();

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
