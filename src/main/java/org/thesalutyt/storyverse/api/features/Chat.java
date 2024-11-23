package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

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
        if (Objects.equals(color, "reset")) {
            SVEngine.CHARACTER_COLOR_STR = "§3";
            return;
        }
        SVEngine.CHARACTER_COLOR_STR = color;
    }

    public static void sendAsStoryVerse(String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("%s%s", SVEngine.CHAT_PREFIX, text));
        sendEveryone(message);
    }

    public static void sendAs(String name, String text) {
        sendNamed(name, text);
    }

    public static void sendAsPlayer(String name, String text) {
        String message = String.format("<%s> %s", name, text);

        sendEveryone(new StringTextComponent(message));
    }

    public static void sendEveryone(String text) {
        sendEveryone(new StringTextComponent(text));
    }

    @Documentate(
            desc = "Returns all players"
    )
    public static List<ServerPlayerEntity> getPlayers() {
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
            Method sendAs = Chat.class.getMethod("sendAs", String.class, String.class);
            methodsToAdd.add(sendAs);
            Method sendAsPlayer = Chat.class.getMethod("sendAsPlayer", String.class, String.class);
            methodsToAdd.add(sendAsPlayer);
            Method sendEveryone = Chat.class.getMethod("sendEveryone", String.class);
            methodsToAdd.add(sendEveryone);
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
