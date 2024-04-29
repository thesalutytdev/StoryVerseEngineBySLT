package org.thesalutyt.storyverse.api.features;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.thesalutyt.storyverse.annotations.Documentate;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

import java.util.List;

public class Chat implements EnvResource {
    private static final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    @Documentate(
            desc = "Sends chat message"
    )
    public static void sendMessage(PlayerEntity player, String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("%s", text));
        sendEveryone(message);
    }

    @Documentate(
            desc = "Sends chat message with sender's name"
    )
    public static void sendNamed(PlayerEntity player, String name, String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("§3[%s]", name));
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

    public static void sendAsEngine(PlayerEntity player, String text) {
        IFormattableTextComponent message = new StringTextComponent("");
        message.append(String.format("§3[Движок]§r %s", text));
        sendEveryone(message);
    }
    @Documentate(
            desc = "Returns all players"
    )
    public static List<ServerPlayerEntity> getPlayers() {
        return server.getPlayerList().getPlayers();
    }

    @Override
    public String getResourceId() {
        return "Chat";
    }
}
